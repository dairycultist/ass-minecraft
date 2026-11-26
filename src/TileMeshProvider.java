import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;

public abstract class TileMeshProvider {

    // each Block class will have-a static TileMeshProvider instance
    // so the method doesn't even have to take in what blockID is being appended, cuz it can be baked-in during construction

    public abstract void appendToBuilder(MeshPartBuilder builder, int localX, int localY, int localZ); // eventually also: Block[] neighbors

}
