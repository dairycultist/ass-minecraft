import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.Game;

public class Main {

    public static void main(String[] args) {

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        config.setTitle("Ass Minecraft");
        config.setWindowedMode(800, 600);
        config.useVsync(true);
        config.setForegroundFPS(60);

        Game game = new Game() {

            @Override
            public void create() {

                setScreen(new InGameScreen());
            }
        };

        new Lwjgl3Application(game, config);
    }
}