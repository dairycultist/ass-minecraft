import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;

public class Player extends MovingEntity {

    public Player(float radius, float height) {
        super(radius, height);
    }

    @Override
    public void process(float delta, TileWorld tileWorld) {

        // direction
        pitch += -Gdx.input.getDeltaY() * 0.25f * delta;
        yaw   += -Gdx.input.getDeltaX() * 0.25f * delta;

        pitch = Math.min(Math.max(pitch, (float) -Math.PI / 2), (float) Math.PI / 2);

        // movement
        float inputVertical = 0, inputHorizontal = 0;
        
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            inputVertical += 1.0;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            inputVertical -= 1.0;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            inputHorizontal += 1.0;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            inputHorizontal -= 1.0;
        }

        dx = MathUtils.lerp(dx, (float) (-inputVertical * Math.sin(yaw) +  inputHorizontal * Math.cos(yaw)) * 6.0f, 10.0f * delta);
        dz = MathUtils.lerp(dz, (float) (-inputVertical * Math.cos(yaw) + -inputHorizontal * Math.sin(yaw)) * 6.0f, 10.0f * delta);

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && isGrounded) {
            dy = 10.0f;
        }
        
        super.process(delta, tileWorld);
    }
}
