import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.*;
import com.badlogic.gdx.utils.ScreenUtils;

// https://xoppa.github.io/blog/basic-3d-using-libgdx/

public class Main {

    public static void main(String[] args) {

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        config.setTitle("Ass Minecraft");
        config.setWindowedMode(800, 600);
        config.useVsync(true);
        config.setForegroundFPS(60);

        new Lwjgl3Application(new ApplicationAdapter() {

            public PerspectiveCamera camera;
            public ModelBatch modelBatch;
            public Model model;
	        public ModelInstance instance;
            public Environment environment;

            @Override
            public void create() {

                modelBatch = new ModelBatch();

                camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                camera.position.set(10f, 10f, 10f);
                camera.lookAt(0,0,0);
                camera.near = 1f;
                camera.far = 300f;
                camera.update();

                environment = new Environment();
                environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
                environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

                ModelBuilder modelBuilder = new ModelBuilder();
                model = modelBuilder.createBox(5f, 5f, 5f, 
                    new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                    Usage.Position | Usage.Normal);
                instance = new ModelInstance(model);
            }

            @Override
            public void render() {
                
                ScreenUtils.clear(0f, 0f, 0f, 1f, true);

                modelBatch.begin(camera);
                modelBatch.render(instance, environment);
                modelBatch.end();

                instance.transform.rotate(0.0f, 1.0f, 0.0f, 0.5f);
            }
            
            @Override
            public void dispose() {

                modelBatch.dispose();
                model.dispose();
            }
            
            @Override
            public void resume() {
            }

            @Override
            public void resize(int width, int height) {
            }

            @Override
            public void pause() {
            }

        }, config);
    }
}