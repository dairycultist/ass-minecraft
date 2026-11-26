import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.Iterator;

/** abstracts away chunks so you can just think about tile positions and not chunk boundaries and stuff */
public class TileWorld implements Disposable {

    private Material material;
    private ArrayList<Chunk> chunks;
    private final Iterable<ModelInstance> iter;

    public TileWorld() {

        material = new Material(ColorAttribute.createDiffuse(Color.GREEN));

        chunks = new ArrayList<Chunk>();

        // set up chunk model instances iterable
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
        
        chunk.tileIds[globalToTileIndex(globalX, globalY, globalZ)] = tileId;
        chunk.dirty = true;

        return true;
    }

    /** returns the tile at the given position, or -1 if can't */
    public int getTile(int globalX, int globalY, int globalZ) {

        Chunk chunk = getChunkThatContains(globalX, globalY, globalZ);

        if (chunk == null)
            return -1;
        
        return chunk.tileIds[globalToTileIndex(globalX, globalY, globalZ)];
    }

    private int globalToTileIndex(int globalX, int globalY, int globalZ) {

        // localify the coordinates
        globalX = Math.floorMod(globalX, Chunk.CHUNK_DIM);
        globalY = Math.floorMod(globalY, Chunk.CHUNK_DIM);
        globalZ = Math.floorMod(globalZ, Chunk.CHUNK_DIM);

        return globalX + globalZ * Chunk.CHUNK_DIM + globalY * Chunk.CHUNK_DIM * Chunk.CHUNK_DIM;
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

    public Iterable<ModelInstance> modelInstances() {
        return iter;
    }

    @Override
    public void dispose() {
        
        for (Chunk chunk : chunks)
            chunk.dispose();
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
                Usage.Position | Usage.Normal,
                material
            );

            TileMeshProvider provider = new BlockTileMeshProvider();

            for (int y = 0; y < CHUNK_DIM; y++) {
                for (int z = 0; z < CHUNK_DIM; z++) {
                    for (int x = 0; x < CHUNK_DIM; x++) {

                        if (tileIds[x + z * CHUNK_DIM + y * CHUNK_DIM * CHUNK_DIM] == 1)
                            provider.appendToBuilder(meshBuilder, x, y, z);
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

}
