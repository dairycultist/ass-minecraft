import com.badlogic.gdx.math.collision.BoundingBox;
// import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.math.Vector3;

public class Entity {

    // TODO make an EntityWorld class which exposes helpers for entity ray intersection and other global entity logic

    // TODO GDX does raycast logic by just letting you check if a ray intersects with a bounding box, you gotta iterate yourself
    // TODO we have to figure out a good way of casting rays to detect entities and tiles both separately and together
    // Intersector.intersectRayBounds(...)

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

    public void process(float delta, TileWorld tileWorld) { // TODO pass EntityWorld
    }
}
