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
import java.util.Random;

/** abstracts away chunks so you can just think about tile positions and not chunk boundaries and stuff */
public class TileWorld implements Disposable {

    private Material material;
    private ArrayList<Chunk> chunks;
    private final Iterable<ModelInstance> iter;

    public TileWorld() {

        material = new Material(ColorAttribute.createDiffuse(Color.GREEN));

        chunks = new ArrayList<Chunk>();

        // add test chunk and test blocks in that chunk
        Chunk c = new Chunk();
        chunks.add(c);

        Random rand = new Random();

        for (int i = 0; i < c.blockIds.length; i++) {

            c.blockIds[i] = rand.nextBoolean() ? 1 : 0;
        }

        c.remesh();

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
        int[] blockIds;

        Chunk() {

            blockIds = new int[CHUNK_DIM * CHUNK_DIM * CHUNK_DIM];

            model = new Model(); // remesh disposes the existing model before remeshing so give it one
            remesh();
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

                        if (blockIds[x + z * CHUNK_DIM + y * CHUNK_DIM * CHUNK_DIM] == 1)
                            provider.appendToBuilder(meshBuilder, x, y, z);
                    }
                }
            }

            model.dispose();
            model = modelBuilder.end();
            instance = new ModelInstance(model);
        }

        @Override
        public void dispose() {
            model.dispose();
        }
    }

}
