
public class MovingEntity extends Entity {

    public static final float GRAVITY = 20.0f;

    public float dx, dy, dz;
    public boolean isGrounded;

    public MovingEntity(float radius, float height) {
        super(radius, height);
    }

    @Override
    public void process(float delta, TileWorld tileWorld) {

        super.process(delta, tileWorld);

        float prevX = x, prevY = y, prevZ = z;

        x += dx * delta;
        if (tileWorld.collidesWithEntity(this)) {
            x = prevX;
            dx = 0;
        }

        y += dy * delta;
        if (isGrounded = tileWorld.collidesWithEntity(this)) {
            y = prevY;
            dy = 0;
        }
        
        z += dz * delta;
        if (tileWorld.collidesWithEntity(this)) {
            z = prevZ;
            dz = 0;
        }

        // gravity
        dy -= GRAVITY * delta;
    }
}
