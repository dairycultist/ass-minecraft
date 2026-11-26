import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Player extends Entity {

    public Player(float radius, float height) {

        super(radius, height);
    }

    @Override
    public void process(float delta) {

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            x -= 6.0 * Math.sin(yaw) * delta;
            z -= 6.0 * Math.cos(yaw) * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            x += 6.0 * Math.sin(yaw) * delta;
            z += 6.0 * Math.cos(yaw) * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            x -= 6.0 * Math.cos(yaw) * delta;
            z += 6.0 * Math.sin(yaw) * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            x += 6.0 * Math.cos(yaw) * delta;
            z -= 6.0 * Math.sin(yaw) * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            y += 6.0 * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            y -= 6.0 * delta;
        }

        pitch += -Gdx.input.getDeltaY() * 0.25f * delta;
        yaw   += -Gdx.input.getDeltaX() * 0.25f * delta;

        pitch = Math.min(Math.max(pitch, (float) -Math.PI / 2), (float) Math.PI / 2);
    }
}
