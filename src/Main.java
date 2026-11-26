import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class Main {

    private static Game game;

    public static void main(String[] args) {

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        config.setTitle("Ass Minecraft");
        config.setWindowedMode(800, 600);
        config.useVsync(true);
        config.setForegroundFPS(60);

        game = new Game() {
            
            @Override
            public void create() {
                setScreen(new InGameScreen());
            }
        };

        new Lwjgl3Application(game, config);
    }

    public static void setScreen(Screen screen) {
        game.setScreen(screen);
    }
}