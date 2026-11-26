import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;

import java.util.Random;

public class BlockTileMeshProvider extends TileMeshProvider {

    public void appendToBuilder(MeshPartBuilder builder, int localX, int localY, int localZ, Random random) {

        MeshPartBuilder.VertexInfo c00 = new MeshPartBuilder.VertexInfo();
        MeshPartBuilder.VertexInfo c01 = new MeshPartBuilder.VertexInfo();
        MeshPartBuilder.VertexInfo c11 = new MeshPartBuilder.VertexInfo();
        MeshPartBuilder.VertexInfo c10 = new MeshPartBuilder.VertexInfo();

        c00.setUV(0, 0);
        c01.setUV(0, 1);
        c11.setUV(1, 1);
        c10.setUV(1, 0);

        // +y
        c00.setPos(localX + 0, localY + 1, localZ + 0);
        c01.setPos(localX + 0, localY + 1, localZ + 1);
        c11.setPos(localX + 1, localY + 1, localZ + 1);
        c10.setPos(localX + 1, localY + 1, localZ + 0);

        c00.setNor(0, 1, 0);
        c01.setNor(0, 1, 0);
        c11.setNor(0, 1, 0);
        c10.setNor(0, 1, 0);

        builder.rect(c00, c01, c11, c10);

        // -y
        c00.setPos(localX + 0, localY, localZ + 0);
        c01.setPos(localX + 1, localY, localZ + 0);
        c11.setPos(localX + 1, localY, localZ + 1);
        c10.setPos(localX + 0, localY, localZ + 1);

        c00.setNor(0, -1, 0);
        c01.setNor(0, -1, 0);
        c11.setNor(0, -1, 0);
        c10.setNor(0, -1, 0);

        builder.rect(c00, c01, c11, c10);

        // +x
        c00.setPos(localX + 1, localY + 1, localZ + 1);
        c01.setPos(localX + 1, localY + 0, localZ + 1);
        c11.setPos(localX + 1, localY + 0, localZ + 0);
        c10.setPos(localX + 1, localY + 1, localZ + 0);

        c00.setNor(1, 0, 0);
        c01.setNor(1, 0, 0);
        c11.setNor(1, 0, 0);
        c10.setNor(1, 0, 0);

        builder.rect(c00, c01, c11, c10);

        // -x
        c00.setPos(localX, localY + 1, localZ + 0);
        c01.setPos(localX, localY + 0, localZ + 0);
        c11.setPos(localX, localY + 0, localZ + 1);
        c10.setPos(localX, localY + 1, localZ + 1);

        c00.setNor(-1, 0, 0);
        c01.setNor(-1, 0, 0);
        c11.setNor(-1, 0, 0);
        c10.setNor(-1, 0, 0);

        builder.rect(c00, c01, c11, c10);

        // +z
        c00.setPos(localX + 0, localY + 1, localZ + 1);
        c01.setPos(localX + 0, localY + 0, localZ + 1);
        c11.setPos(localX + 1, localY + 0, localZ + 1);
        c10.setPos(localX + 1, localY + 1, localZ + 1);

        c00.setNor(0, 0, 1);
        c01.setNor(0, 0, 1);
        c11.setNor(0, 0, 1);
        c10.setNor(0, 0, 1);

        builder.rect(c00, c01, c11, c10);

        // -z
        c00.setPos(localX + 1, localY + 1, localZ);
        c01.setPos(localX + 1, localY + 0, localZ);
        c11.setPos(localX + 0, localY + 0, localZ);
        c10.setPos(localX + 0, localY + 1, localZ);

        c00.setNor(0, 0, -1);
        c01.setNor(0, 0, -1);
        c11.setNor(0, 0, -1);
        c10.setNor(0, 0, -1);

        builder.rect(c00, c01, c11, c10);
    }

}

