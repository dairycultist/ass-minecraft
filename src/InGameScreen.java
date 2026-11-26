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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

// https://xoppa.github.io/blog/basic-3d-using-libgdx/
// https://github.com/libgdx/libgdx/

public class InGameScreen implements Screen {

    public PerspectiveCamera camera;
    public float cameraPitch, cameraYaw;

    public ModelBatch modelBatch;
    public Environment environment;

    public TileWorld world;

    public InGameScreen() {

        modelBatch = new ModelBatch();

        camera = new PerspectiveCamera(90, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(20f, 20f, 20f);
        camera.lookAt(0f, 0f, 0f);
        camera.near = 0.01f;
        camera.far = 300f;
        camera.update();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        world = new TileWorld();

        // init basic world terrain for testing
        Random rand = new Random();

        for (int y = -15; y < 15; y++) {
            for (int z = -15; z < 15; z++) {
                for (int x = -15; x < 15; x++) {

                    world.setTile(rand.nextInt(0, ModLoader.tiles.length), x, y, z);
                }
            }
        }

        world.remeshDirtyChunks();
    }

    @Override
    public void show() {
        Gdx.input.setCursorCatched(true);
    }

    public void hide() {
        Gdx.input.setCursorCatched(false);
    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            Gdx.input.setCursorCatched(!Gdx.input.isCursorCatched());

        double angle = cameraYaw * Math.PI / 180.0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.position.x -= 6.0 * Math.sin(angle) * delta;
            camera.position.z -= 6.0 * Math.cos(angle) * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.position.x += 6.0 * Math.sin(angle) * delta;
            camera.position.z += 6.0 * Math.cos(angle) * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.position.x -= 6.0 * Math.cos(angle) * delta;
            camera.position.z += 6.0 * Math.sin(angle) * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.position.x += 6.0 * Math.cos(angle) * delta;
            camera.position.z -= 6.0 * Math.sin(angle) * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            camera.position.y += 6.0 * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            camera.position.y -= 6.0 * delta;
        }

        // camera rotation
        cameraPitch += -Gdx.input.getDeltaY() * 30f * delta;
        cameraYaw   += -Gdx.input.getDeltaX() * 30f * delta;

        cameraPitch = Math.min(Math.max(cameraPitch, -90), 90);

        camera.direction.x = 0;
        camera.direction.y = 0;
        camera.direction.z = -1;
        camera.up.x = 0;
        camera.up.y = 1;
        camera.up.z = 0;
        
        camera.rotate(Vector3.X, cameraPitch);
        camera.rotate(Vector3.Y, cameraYaw);
        camera.update();

        ScreenUtils.clear(0f, 0f, 0f, 1f, true);

        modelBatch.begin(camera);
        for (ModelInstance instance : world.modelInstances()) {
            modelBatch.render(instance, environment);
        }
        modelBatch.end();

        // chunk.getModelInstance().transform.rotate(0.0f, 1.0f, 0.0f, 0.5f);
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

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {

        modelBatch.dispose();
        world.dispose();
    }
}