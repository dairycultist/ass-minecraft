import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;

public abstract class TileMeshProvider {

    public abstract void appendToBuilder(MeshPartBuilder builder, int localX, int localY, int localZ); // eventually also: int[] self, int[] neighbors
}
