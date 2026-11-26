import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.Material;

// import com.badlogic.gdx.graphics.Texture;

public class Tile {

    public static final Material material = new Material(ColorAttribute.createDiffuse(Color.GREEN));

    public static final Tile[] types = {
        new Tile(new EmptyTileMeshProvider()),
        new Tile(new BlockTileMeshProvider())
    };

    // each Tile instance has-a TileMeshProvider instance that determines how
    // that Tile will be added to the chunk mesh. TileMeshProviders determine the mesh
    // shape; they can take in information about the Tile during construction (e.g.
    // texture atlas index) which lets you use the same class for multiple tiles
    public final TileMeshProvider meshProvider;

    private Tile(TileMeshProvider meshProvider) {
        this.meshProvider = meshProvider;
    }

    static {
        // load all the different types of tile, also assemble the texture atlas and mix it into our material
    }
}
