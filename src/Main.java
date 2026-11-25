import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.ApplicationAdapter;

import com.badlogic.gdx.utils.ScreenUtils;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class Main {

    public static void main(String[] args) {

        System.out.println("Hello world!");

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("My libGDX Window");
        config.setWindowedMode(800, 600); // Width and height in pixels
        config.useVsync(true);
        config.setForegroundFPS(60);

        // Creates and opens the window
        new Lwjgl3Application(new ApplicationAdapter() {

            @Override
            public void create() {
                // Initialize textures, cameras, and game objects here
            }

            @Override
            public void render() {
                // Clear the screen with a dark blue color every frame
                ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
                
                // Draw your game elements here
            }

            @Override
            public void dispose() {
                // Clean up heavy resources (textures, batches, audio) on exit
            }

        }, config);
    }
}