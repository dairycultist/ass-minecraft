import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.Material;

// import com.badlogic.gdx.graphics.Texture;

public class Tile {

    public static final Material material = new Material(ColorAttribute.createDiffuse(Color.GREEN));


    static {
        // load all the different types of tile, also assemble the texture atlas
    }
}
