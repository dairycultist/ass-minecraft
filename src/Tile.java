import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Tile {

    public static final TextureAtlas atlas; // remember to dispose me
    public static final Material material = new Material();

    public static final Tile[] types;

    // each Tile instance has-a TileMeshProvider instance that determines how
    // that Tile will be added to the chunk mesh. TileMeshProviders determine the mesh
    // shape; they can take in information about the Tile during construction (e.g.
    // texture atlas index) which lets you use the same class for multiple tiles
    public final TileMeshProvider meshProvider;

    private Tile(TileMeshProvider meshProvider) {
        this.meshProvider = meshProvider;
    }

    // eventually we're gonna move all this loading to a static "ModLoader" class or something, which sets up Tile
    // and other stuff (like items) from the data from the mods folder
    static {

        // prepare to make a tile texture atlas https://libgdx.com/wiki/tools/texture-packer
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxWidth = 4096; // we don't want it to make multiple pages, so let pages be huge
        settings.maxHeight = 4096;
        TexturePacker packer = new TexturePacker(settings);

        int tileTextureCount = 0;

        // iterate over all the mods, getting data on TILE TEXTURES
        for (FileHandle modDir : Gdx.files.local("mods/").list()) {

            if (modDir.isDirectory()) {

                FileHandle modFile = modDir.child("mod.txt");

                if (!modFile.exists())
                    continue;

                BufferedReader reader = modFile.reader(1024);

                try {

                    String line;

                    while ((line = reader.readLine()) != null && !line.startsWith("#tileTextures")) {}
                    while ((line = reader.readLine()) != null && !line.startsWith("#end")) {

                        String[] parts = line.split("=");

                        BufferedImage image = ImageIO.read(Gdx.files.local("mods/" + modDir.name() + "/" + parts[1]).file());

                        if (image != null) {
                            packer.addImage(image, parts[0]);
                            tileTextureCount++;
                        } else {
                            throw new IOException("Could not load tile texture.");
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (tileTextureCount == 0) {

            System.out.println("No mods added any tile textures, there's nothing to play!");
            System.exit(0);
        }

        // save packed atlas to file
        FileHandle atlasDir = Gdx.files.local("atlas/");
        
        if (atlasDir.exists()) {
            atlasDir.emptyDirectory();
        } else {
            atlasDir.mkdirs();
        }
        packer.pack(atlasDir.file(), "tile");



        // SEPARATE CODE STARTS! we should probably split it into two separate functions for tangy code separation

        // load tile atlas
        atlas = new TextureAtlas(Gdx.files.local("atlas/tile.atlas"));

        material.set(TextureAttribute.createDiffuse(atlas.getRegions().get(0).getTexture()));

        // iterate over all the mods AGAIN, getting data on TILES
        for (FileHandle modDir : Gdx.files.local("mods/").list()) {

            if (modDir.isDirectory()) {

                FileHandle modFile = modDir.child("mod.txt");

                if (!modFile.exists())
                    continue;

                BufferedReader reader = modFile.reader(1024);

                try {
                    System.out.println(reader.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // // findRegion is not very fast, so the value returned should be stored rather than calling this method each frame
                // TextureAtlas.AtlasRegion region = atlas.findRegion("test");

                // System.out.println(region.getU() + " " + region.getV() + " / " + region.getU2() + " " + region.getV2());
            }
        }




        types = new Tile[] {
            new Tile(new EmptyTileMeshProvider()),
            new Tile(new BlockTileMeshProvider(0.03125f, 0.03125f, 0.53125f, 0.53125f))
        };
    }
}
