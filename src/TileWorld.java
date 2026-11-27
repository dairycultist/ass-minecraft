import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/** abstracts away chunks so you can just think about tile positions and not chunk boundaries and chunk spawning and stuff */
public class TileWorld implements Disposable {

    private ArrayList<Chunk> chunks;
    private final Iterable<ModelInstance> iter;

    public TileWorld() {

        chunks = new ArrayList<Chunk>();

        iter = new Iterable<ModelInstance>() {

            @Override
            public Iterator<ModelInstance> iterator() {

                return new Iterator<ModelInstance>() {

                    int i = 0;

                    @Override
                    public boolean hasNext() {
                        return i < chunks.size();
                    }

                    @Override
                    public ModelInstance next() {
                        return chunks.get(i++).instance;
                    }
                };
            }
        };
    }

    // TODO expose helper for raycasting tiles

    public boolean collidesWithEntity(Entity entity) {

        BoundingBox aabb = entity.getBoundingBox();

        int minX = (int) Math.floor(aabb.min.x), minY = (int) Math.floor(aabb.min.y), minZ = (int) Math.floor(aabb.min.z);
        int maxX = (int) Math.floor(aabb.max.x), maxY = (int) Math.floor(aabb.max.y), maxZ = (int) Math.floor(aabb.max.z);

        for (int y = minY; y <= maxY; y++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int x = minX; x <= maxX; x++) {
            
                    if (getTile(x, y, z) > 0) // TEMP, for now any tile that isn't air or nonexistent is fully solid
                        return true;
                }
            }
        }

        return false;
    }

    /** spawns (maybe queues for spawning) in chunks that are within blockDistance
     * of the global position, and despawns chunks outside of it
     * */
    public void focusAround(int globalX, int globalY, int globalZ, int blockDistance) {

    }

    /** returns true if successfully set tile, false otherwise. but it creates a chunk if there isn't
     * one already there to accomodate that tile so it never returns false right now.
     * if it returns true, it also means there is now at least one dirty chunk that needs remeshing
     */
    public boolean setTile(int tileId, int globalX, int globalY, int globalZ) {

        Chunk chunk = getChunkThatContains(globalX, globalY, globalZ);

        if (chunk == null) {
            
            chunk = new Chunk(
                Math.floorDiv(globalX, Chunk.CHUNK_DIM),
                Math.floorDiv(globalY, Chunk.CHUNK_DIM),
                Math.floorDiv(globalZ, Chunk.CHUNK_DIM)
            );
            
            chunks.add(chunk);
        }
        
        chunk.globalSet(tileId, globalX, globalY, globalZ);
        chunk.dirty = true;

        return true;
    }

    /** returns the tile at the given position, or -1 if can't */
    public int getTile(int globalX, int globalY, int globalZ) {

        Chunk chunk = getChunkThatContains(globalX, globalY, globalZ);

        if (chunk == null)
            return -1;
        
        return chunk.globalGet(globalX, globalY, globalZ);
    }

    private Chunk getChunkThatContains(int globalX, int globalY, int globalZ) {

        for (Chunk chunk : chunks) {

            if (chunk.contains(globalX, globalY, globalZ))
                return chunk;
        }

        return null;
    }

    public void remeshDirtyChunks() {

        for (Chunk chunk : chunks) {

            if (chunk.dirty) {

                chunk.remesh();
                chunk.dirty = false;
            }
        }
    }

    /** returns an iterable you can iterate over to draw all the model instances of this tile world */
    public Iterable<ModelInstance> modelInstances() {
        return iter;
    }

    @Override
    public void dispose() {
        
        for (Chunk chunk : chunks)
            chunk.dispose();
    }

    private static int globalToTileIndex(int globalX, int globalY, int globalZ) {

        // localify the coordinates
        globalX = Math.floorMod(globalX, Chunk.CHUNK_DIM);
        globalY = Math.floorMod(globalY, Chunk.CHUNK_DIM);
        globalZ = Math.floorMod(globalZ, Chunk.CHUNK_DIM);

        return globalX + globalZ * Chunk.CHUNK_DIM + globalY * Chunk.CHUNK_DIM * Chunk.CHUNK_DIM;
    }

    /** cubic chunks, with block data ordered x,z,y
     * there is a slight problem which is that Mesh(Part)Builder only supports 2^16 vertices.
     * we might want to switch to manually constructing the mesh but for now I don't care
    */
    private class Chunk implements Disposable {

        static final int CHUNK_DIM = 8;

        Model model;
        ModelInstance instance;
        int[] tileIds; // at some point this will be more along the lines of "tile data" with, say, an int[2] corresponding to one tile, but whatever
        boolean dirty;
        final int x, y, z;

        Chunk(int chunkX, int chunkY, int chunkZ) {

            x = chunkX * CHUNK_DIM;
            y = chunkY * CHUNK_DIM;
            z = chunkZ * CHUNK_DIM;

            tileIds = new int[CHUNK_DIM * CHUNK_DIM * CHUNK_DIM];

            model = new Model(); // remesh disposes the existing model before remeshing, so give it one now so we don't NullPointerException
            instance = new ModelInstance(model); // so drawing doesn't give a NullPointerException
        }

        int globalGet(int globalX, int globalY, int globalZ) {
            return tileIds[globalToTileIndex(globalX, globalY, globalZ)];
        }

        void globalSet(int tileId, int globalX, int globalY, int globalZ) {
            tileIds[globalToTileIndex(globalX, globalY, globalZ)] = tileId;
        }

        boolean contains(int globalX, int globalY, int globalZ) {

            return globalX >= x && globalY >= y && globalZ >= globalZ
                && globalX < x + CHUNK_DIM
                && globalY < y + CHUNK_DIM
                && globalZ < z + CHUNK_DIM;
        }

        void remesh() {

            ModelBuilder modelBuilder = new ModelBuilder();
            modelBuilder.begin();

            MeshPartBuilder meshBuilder = modelBuilder.part(
                "terrain",
                GL20.GL_TRIANGLES,
                Usage.Position | Usage.Normal | Usage.TextureCoordinates,
                ModLoader.tileMaterial
            );

            Random random = new Random();

            for (int y = 0; y < CHUNK_DIM; y++) {
                for (int z = 0; z < CHUNK_DIM; z++) {
                    for (int x = 0; x < CHUNK_DIM; x++) {

                        random.setSeed(x * 3 + y * 5 + z * 7);
                        ModLoader.tiles[tileIds[x + z * CHUNK_DIM + y * CHUNK_DIM * CHUNK_DIM]].meshProvider.appendToBuilder(meshBuilder, x, y, z, random);
                    }
                }
            }

            model.dispose();
            model = modelBuilder.end();
            instance = new ModelInstance(model, x, y, z);
        }

        @Override
        public void dispose() {
            model.dispose();
        }
    }

    /** returned by stuff like TileWorld raycasting, makes manipulating the TileWorld faster */
    public class TileInstance {

        public final int x, y, z; // global

        private int tileId;
        private final Chunk source;

        private TileInstance(Chunk source, int x, int y, int z, int tileId) {

            this.source = source;
            this.x = x;
            this.y = y;
            this.z = z;
            this.tileId = tileId;
        } 

        private TileInstance(Chunk source, int x, int y, int z) {
            this(source, x, y, z, source.globalGet(x, y, z));
        }

        public int get() {
            return tileId;
        }

        public void set(int tileId) {

            this.tileId = tileId;
            source.globalSet(tileId, x, y, z);
        }
    }

}
