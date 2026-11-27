
public class Tile {

    // the string ID. Air is always null
    public final String id;

    // the internal representation of the Tile, for storing in chunks and stuff. use its
    // string ID for everything else. Air is always 0
    public final int index;

    // each Tile instance has-a TileMeshProvider instance that determines how
    // that Tile will be added to the chunk mesh. TileMeshProviders determine the mesh
    // shape; they can take in information about the Tile during construction (e.g.
    // texture atlas index) which lets you use the same class for multiple tiles
    public final TileMeshProvider meshProvider;

    // TODO collisionProvider (one method that takes in an entity + a TileInstance (so you get position and metadata) and returns a boolean)

    public Tile(String id, int index, TileMeshProvider meshProvider) {
        this.id = id;
        this.index = index;
        this.meshProvider = meshProvider;
    }
}
