import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;

import java.util.Random;

public abstract class TileMeshProvider {

    /** Appends into a chunk mesh a tile mesh at local coordinates. The random parameter is initialized with a seed
     * corresponding to the global position of the tile (e.g. it'll be the same with every remesh of the same tile
     * at the same global position).
     */
    public abstract void appendToBuilder(MeshPartBuilder builder, int localX, int localY, int localZ, Random random); // eventually also: int[] neighborhood (3x3 with itself in the center)
}
