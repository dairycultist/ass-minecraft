import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;

public class BlockTileMeshProvider extends TileMeshProvider {

    public void appendToBuilder(MeshPartBuilder builder, int localX, int localY, int localZ) {

        // +y
        builder.rect(
            localX + 0, localY + 1, localZ + 0,
            localX + 0, localY + 1, localZ + 1,
            localX + 1, localY + 1, localZ + 1,
            localX + 1, localY + 1, localZ + 0,
            0, 1, 0
        );

        // -y
        builder.rect(
            localX + 0, localY + 0, localZ + 0,
            localX + 1, localY + 0, localZ + 0,
            localX + 1, localY + 0, localZ + 1,
            localX + 0, localY + 0, localZ + 1,
            0, -1, 0
        );

        // +x
        builder.rect(
            localX + 1, localY + 1, localZ + 0,
            localX + 1, localY + 1, localZ + 1,
            localX + 1, localY + 0, localZ + 1,
            localX + 1, localY + 0, localZ + 0,
            1, 0, 0
        );

        // -x
        builder.rect(
            localX + 0, localY + 1, localZ + 0,
            localX + 0, localY + 0, localZ + 0,
            localX + 0, localY + 0, localZ + 1,
            localX + 0, localY + 1, localZ + 1,
            -1, 0, 0
        );

        // +z
        builder.rect(
            localX + 0, localY + 1, localZ + 1,
            localX + 0, localY + 0, localZ + 1,
            localX + 1, localY + 0, localZ + 1,
            localX + 1, localY + 1, localZ + 1,
            0, 0, 1
        );

        // -z
        builder.rect(
            localX + 0, localY + 1, localZ + 0,
            localX + 1, localY + 1, localZ + 0,
            localX + 1, localY + 0, localZ + 0,
            localX + 0, localY + 0, localZ + 0,
            0, 0, -1
        );
    }

}

