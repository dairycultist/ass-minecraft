import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;

public abstract class TileMeshProvider {

    public abstract void appendToMeshBuilder(MeshPartBuilder builder, int tiledata);

}
