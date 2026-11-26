import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
// import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

// https://xoppa.github.io/blog/basic-3d-using-libgdx/
// https://github.com/libgdx/libgdx/

public class Window extends ApplicationAdapter {

    public PerspectiveCamera camera;
    public ModelBatch modelBatch;
    public Environment environment;

    public TileWorld world;

    @Override
    public void create() {

        modelBatch = new ModelBatch();

        camera = new PerspectiveCamera(90, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(20f, 20f, 20f);
        camera.lookAt(0f, 0f, 0f);
        camera.near = 1f;
        camera.far = 300f;
        camera.update();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        world = new TileWorld();
    }

    @Override
    public void render() {
        
        ScreenUtils.clear(0f, 0f, 0f, 1f, true);

        modelBatch.begin(camera);
        for (ModelInstance instance : world.modelInstances()) {
            modelBatch.render(instance, environment);
        }
        modelBatch.end();

        // chunk.getModelInstance().transform.rotate(0.0f, 1.0f, 0.0f, 0.5f);
    }
    
    @Override
    public void dispose() {

        modelBatch.dispose();
        world.dispose();
    }
    
    @Override
    public void resume() {
    }

    @Override
    public void resize(int width, int height) {
        
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void pause() {
    }
}