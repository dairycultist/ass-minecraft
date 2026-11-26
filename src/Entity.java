import com.badlogic.gdx.math.collision.BoundingBox;
// import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.math.Vector3;

public class Entity {

    public float x, y, z; // middle bottom of AABB
    public float pitch, yaw;

    private final float radius, height;
    private final BoundingBox aabb;

    public Entity(float radius, float height) {

        aabb = new BoundingBox();

        this.radius = radius;
        this.height = height;
    }

    public BoundingBox getBoundingBox() {

        aabb.set(
            new Vector3(x - radius, y,          z - radius),
            new Vector3(x + radius, y + height, z + radius)
        );

        return aabb;
    }

}
