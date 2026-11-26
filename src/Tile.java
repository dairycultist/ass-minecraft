import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.Texture;

import java.io.BufferedReader;
import java.io.IOException;

public class Tile {

    public static final Material material = new Material();

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

        // eventually we're gonna move all this loading to a static class or something, which sets up Tile
        // and other stuff (like items) from the data from the mods folder

        // assemble the texture atlas and mix it into our material
        // built in texture atlas maker https://libgdx.com/wiki/tools/texture-packer
        Texture texture = new Texture(Gdx.files.local("mods/vanilla/test.png"));
        // texture.dispose();
        material.set(TextureAttribute.createDiffuse(texture));

        // load all the different types of tile
        for (FileHandle modDir : Gdx.files.local("mods/").list()) {

            if (modDir.isDirectory()) {

                FileHandle modFile = modDir.child("mod.txt");

                if (!modFile.exists())
                    continue;

                BufferedReader reader = modFile.reader(1024);

                try {
                    System.out.println(reader.readLine());
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }
}
