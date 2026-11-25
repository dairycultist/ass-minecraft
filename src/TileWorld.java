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

// abstracts away chunks so you can just think about tile positions and not chunk boundaries and stuff
public class TileWorld implements Disposable {

    private Material material;
    private ArrayList<Chunk> chunks;
    private final Iterable<ModelInstance> iter;

    public TileWorld() {

        material = new Material(ColorAttribute.createDiffuse(Color.GREEN));

        chunks = new ArrayList<Chunk>();

        // add test chunk
        chunks.add(new Chunk(material));

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
        
        for (Chunk chunk : chunks) {

            chunk.dispose();
        }
    }

    private class Chunk implements Disposable {

        Model model;
        ModelInstance instance;

        Chunk(Material material) {

            ModelBuilder modelBuilder = new ModelBuilder();
            modelBuilder.begin();

            MeshPartBuilder meshBuilder = modelBuilder.part(
                "terrain",
                GL20.GL_TRIANGLES,
                Usage.Position | Usage.Normal,
                material
            );

            meshBuilder.rect(
                -5f, 0f, -5f,
                -5f, 0f,  5f,
                5f, 0f,  5f,
                5f, 0f, -5f,
                0f, 1f,  0f
            );

            model = modelBuilder.end();
            instance = new ModelInstance(model);
        }

        @Override
        public void dispose() {
            model.dispose();
        }
    }

}
