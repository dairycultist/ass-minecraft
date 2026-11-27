import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Player extends Entity {

    public Player(float radius, float height) {

        super(radius, height);
    }

    @Override
    public void process(float delta, TileWorld tileWorld) {

        super.process(delta, tileWorld);

        // direction
        pitch += -Gdx.input.getDeltaY() * 0.25f * delta;
        yaw   += -Gdx.input.getDeltaX() * 0.25f * delta;

        pitch = Math.min(Math.max(pitch, (float) -Math.PI / 2), (float) Math.PI / 2);

        // movement
        float prevX = x, prevY = y, prevZ = z;
        float newX = x, newY = y, newZ = z;
        
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            newX -= 6.0 * Math.sin(yaw) * delta;
            newZ -= 6.0 * Math.cos(yaw) * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            newX += 6.0 * Math.sin(yaw) * delta;
            newZ += 6.0 * Math.cos(yaw) * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            newX -= 6.0 * Math.cos(yaw) * delta;
            newZ += 6.0 * Math.sin(yaw) * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            newX += 6.0 * Math.cos(yaw) * delta;
            newZ -= 6.0 * Math.sin(yaw) * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            newY += 6.0 * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            newY -= 6.0 * delta;
        }

        x = newX;
        if (tileWorld.collidesWithEntity(this))
            x = prevX;

        y = newY;
        if (tileWorld.collidesWithEntity(this))
            y = prevY;
        
        z = newZ;
        if (tileWorld.collidesWithEntity(this))
            z = prevZ;
    }
}
