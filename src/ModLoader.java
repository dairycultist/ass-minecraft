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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/** Central point for aaaaall mod assets. Sets up just tile stuff for now (but also other stuff like
 * items in the future) from the data from the mods/ folder
 */
public class ModLoader {

    public static TextureAtlas tileAtlas; // remember to dispose me
    public static Material tileMaterial;
    public static Tile[] tiles;
    public static Map<String, Integer> tileIdToIndex;

    static {

        loadTileTextureAtlas();
        loadTiles();

        tileMaterial = new Material();
        tileMaterial.set(TextureAttribute.createDiffuse(tileAtlas.getRegions().get(0).getTexture()));
    }

    public interface ModFileReader {

        void run(String namespace, String line, String modPath) throws IOException;
    }

    /** Reads over every single mod file line by line. Ignores comments. Automatically parses out the namespace. */
    private static void read(ModFileReader lambda) {

        for (FileHandle modDir : Gdx.files.local("mods/").list()) {

            if (modDir.isDirectory()) {

                FileHandle modFile = modDir.child("mod.txt");

                if (!modFile.exists()) {

                    System.out.println("Warning: The mod folder \"" + modDir.name() + "\" doesn't have a mod.txt configuration file!");
                    continue;
                }

                BufferedReader reader = modFile.reader(1024);

                try {

                    String namespace = reader.readLine().trim();

                    if (!namespace.startsWith("@") || namespace.contains(" ")) {

                        System.out.println("Warning: The mod file in the mod folder \"" + modDir.name() + "\" has an invalid namespace! (File MUST start with @<namespace> where namespace has no internal whitespace)");
                        continue;
                    }

                    namespace = namespace.substring(1);

                    String line;

                    while ((line = reader.readLine()) != null) {

                        line = line.trim();

                        if (line.startsWith("//"))
                            continue;
                        
                        if (line.isEmpty())
                            continue;
                        
                        lambda.run(namespace, line, "mods/" + modDir.name() + "/");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void loadTileTextureAtlas() {

        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxWidth = 4096; // we don't want it to make multiple pages, so let pages be huge
        settings.maxHeight = 4096;
        TexturePacker packer = new TexturePacker(settings); // https://libgdx.com/wiki/tools/texture-packer

        // iterate over all the mods for their data on tile textures
        final int[] tileTextureCount = { 0 };
        final boolean[] insideTileTextures = { false };

        ModLoader.read((String namespace, String line, String modPath) -> {

            if (insideTileTextures[0]) {

                if (line.startsWith("#end")) {

                    insideTileTextures[0] = false;
                    return;
                }

                // each line has a texturename=filepath pair
                String[] parts = line.split("=");
                parts[0] = parts[0].trim();
                parts[1] = parts[1].trim();

                BufferedImage image = ImageIO.read(Gdx.files.local(modPath + parts[1]).file());

                if (image != null) {

                    packer.addImage(image, namespace + "/" + parts[0]);
                    tileTextureCount[0]++;

                } else {
                    throw new IOException("Could not load tile texture.");
                }

            } else if (line.startsWith("#tileTextures")) {
                insideTileTextures[0] = true;
            }
        });

        if (tileTextureCount[0] == 0) {

            System.out.println("No mods added any tile textures, there's nothing to play!");
            System.exit(0);
        }

        // save tile texture atlas to file
        FileHandle atlasDir = Gdx.files.local("atlas/");
        
        if (atlasDir.exists()) {
            atlasDir.emptyDirectory();
        } else {
            atlasDir.mkdirs();
        }

        packer.pack(atlasDir.file(), "tile");
    }

    private static void loadTiles() {

        tileIdToIndex = new HashMap<String, Integer>();

        ArrayList<Tile> typesConstruct = new ArrayList<Tile>();

        // air is always the first tile type
        typesConstruct.add(new Tile(new EmptyTileMeshProvider()));
        tileIdToIndex.put(null, 0);

        // load tile atlas
        tileAtlas = new TextureAtlas(Gdx.files.local("atlas/tile.atlas"));

        // iterate over all the mods for their data on tiles
        final boolean[] insideTileType = { false };
        final String[] name = { null };
        final String[] model = { null };
        final String[] tex0 = { null };

        ModLoader.read((String namespace, String line, String modPath) -> {

            if (insideTileType[0]) {

                if (line.startsWith("#end")) {

                    // finish with this tile type
                    if (name[0] == null || model[0] == null || tex0[0] == null) {

                        System.out.println("Warning: A tile was not loaded because it was missing necessary parameters!");

                    } else {

                        TextureAtlas.AtlasRegion region = tileAtlas.findRegion(tex0[0]);

                        typesConstruct.add(new Tile(new BlockTileMeshProvider(region.getU(), region.getV(), region.getU2(), region.getV2())));
                        tileIdToIndex.put(namespace + "/" + name[0], typesConstruct.size() - 1);
                    }

                    name[0] = null;
                    model[0] = null;
                    tex0[0] = null;

                    insideTileType[0] = false;
                    return;
                }

                // parse parameter of this tile type
                String[] parts = line.split("=");
                parts[0] = parts[0].trim();
                parts[1] = parts[1].trim();
                
                if (parts[0].equals("name")) {
                    name[0] = parts[1];
                } else if (parts[0].equals("model")) {
                    model[0] = parts[1];
                } else if (parts[0].equals("tex0")) {
                    tex0[0] = parts[1];
                }

            } else if (line.startsWith("#tileType")) {
                insideTileType[0] = true;
            }
        });

        tiles = typesConstruct.toArray(new Tile[0]);
    }
}
