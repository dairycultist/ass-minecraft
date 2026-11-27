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

    public ModelBatch modelBatch;
    public Environment environment;

    public TileWorld tileWorld;

    public Player player;

    public InGameScreen() {

        modelBatch = new ModelBatch();

        player = new Player(0.3f, 1.7f);
        player.y = 20f;

        camera = new PerspectiveCamera(90, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.lookAt(0f, 0f, 0f);
        camera.near = 0.01f;
        camera.far = 300f;
        camera.update();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        tileWorld = new TileWorld();

        // init basic tileWorld terrain for testing
        Random rand = new Random();

        for (int y = 0; y < 8; y++) {
            for (int z = -30; z < 30; z++) {
                for (int x = -30; x < 30; x++) {

                    tileWorld.setTile(ModLoader.tiles[rand.nextInt(0, ModLoader.tiles.length)].id, x, y, z);
                }
            }
        }

        tileWorld.remeshDirtyChunks();
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

        player.process(delta, tileWorld);

        // align camera with player
        camera.direction.x = 0;
        camera.direction.y = 0;
        camera.direction.z = -1;
        camera.up.x = 0;
        camera.up.y = 1;
        camera.up.z = 0;
        
        camera.rotate(Vector3.X, player.pitch * 180.0f / (float) Math.PI);
        camera.rotate(Vector3.Y, player.yaw * 180.0f / (float) Math.PI);
        camera.position.set(player.x, player.y + 1.5f, player.z);
        camera.update();

        ScreenUtils.clear(0f, 0f, 0f, 1f, true);

        modelBatch.begin(camera);
        for (ModelInstance instance : tileWorld.modelInstances()) {
            modelBatch.render(instance, environment);
        }
        modelBatch.end();

        // modelInstance.transform.rotate(0.0f, 1.0f, 0.0f, 0.5f);
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
        tileWorld.dispose();
    }
}