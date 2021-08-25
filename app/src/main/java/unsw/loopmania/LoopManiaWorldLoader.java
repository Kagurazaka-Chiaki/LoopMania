package unsw.loopmania;

import java.io.*;
import java.util.ArrayList;

import org.json.JSONObject;

import javafx.scene.image.Image;
import org.javatuples.Pair;
import org.json.JSONArray;
import org.json.JSONTokener;

import unsw.entity.Character;
import unsw.entity.Entity;
import unsw.entity.PathTile;
import unsw.entity.building.HeroCastleBuilding;

import java.util.List;
import java.util.Objects;

import unsw.mode.Mode;
import unsw.goal.Goal;
import unsw.worlds.Worlds;

/**
 * Loads a world from a .json file.
 * <p>
 * By extending this class, a subclass can hook into entity creation. This is
 * useful for creating UI elements with corresponding entities.
 * <p>
 * this class is used to load the world. it loads non-spawning entities from the
 * configuration files. spawning of enemies/cards must be handled by the
 * controller.
 */
public abstract class LoopManiaWorldLoader {
    private final JSONObject json;
    private final Mode mode;
    private final Goal goal;

    public LoopManiaWorldLoader(Worlds world, Mode mode) {
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("worlds/" + world.getWorldFile());
        this.json = new JSONObject(new JSONTokener(Objects.requireNonNull(input)));
        this.mode = mode;
        this.goal = world.getGoal();
    }

    /**
     * Parses the JSON to create a world.
     */
    public LoopManiaWorld load() {
        int width = json.getInt("width");
        int height = json.getInt("height");

        // path variable is collection of coordinates with directions of path taken...
        List<Pair<Integer, Integer>> orderedPath = loadPathTiles(json.getJSONObject("path"), width, height);

        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath);
        world.setMode(mode);
        world.setGoal(goal);

        JSONArray jsonEntities = json.getJSONArray("entities");

        // load non-path entities later so that they're shown on-top
        for (int i = 0; i < jsonEntities.length(); i++) {
            loadEntity(world, jsonEntities.getJSONObject(i), orderedPath);
        }

        return world;
    }

    /**
     * load an entity into the world
     *
     * @param world       backend world object
     * @param currentJson a JSON object to parse (different from the )
     * @param orderedPath list of pairs of x, y cell coordinates representing game
     *                    path
     */
    private void loadEntity(LoopManiaWorld world, JSONObject currentJson, List<Pair<Integer, Integer>> orderedPath) {
        String type = currentJson.getString("type");
        int x = currentJson.getInt("x");
        int y = currentJson.getInt("y");
        int indexInPath = orderedPath.indexOf(new Pair<>(x, y));
        assert indexInPath != -1;

        Entity entity = null;
        switch (type) {
            case "hero_castle":
                Character character = new Character(new PathPosition(indexInPath, orderedPath), world);
                HeroCastleBuilding heroCastle = new HeroCastleBuilding(Pair.with(x, y));
                world.setCharacter(character);
                world.setHeroCastle(heroCastle);
                onLoad(character);
                onLoad(heroCastle);
                entity = character;
                break;
            case "path_tile":
                throw new RuntimeException("path_tile's aren't valid entities, define the path externally.");
            default:
                break;
        }
        world.addEntity(entity);
    }

    /**
     * load path tiles
     *
     * @param path   json data loaded from file containing path information
     * @param width  width in number of cells
     * @param height height in number of cells
     * @return list of x, y cell coordinate pairs representing game path
     */
    private List<Pair<Integer, Integer>> loadPathTiles(JSONObject path, int width, int height) {
        if (!path.getString("type").equals("path_tile")) {
            // ... possible extension
            throw new RuntimeException(
                "Path object requires path_tile type.  No other path types supported at this moment.");
        }
        PathTile starting = new PathTile(Pair.with(path.getInt("x"), path.getInt("y")));
        if (starting.getY() >= height || starting.getY() < 0 || starting.getX() >= width || starting.getX() < 0) {
            throw new IllegalArgumentException("Starting point of path is out of bounds");
        }
        // load connected path tiles
        List<PathTile.Direction> connections = new ArrayList<>();
        for (Object dir : path.getJSONArray("path").toList()) {
            connections.add(Enum.valueOf(PathTile.Direction.class, dir.toString()));
        }

        if (connections.size() == 0) {
            throw new IllegalArgumentException(
                "This path just consists of a single tile, it needs to consist of multiple to form a loop.");
        }

        // load the first position into the orderedPath
        PathTile.Direction first = connections.get(0);
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(Pair.with(starting.getX(), starting.getY()));

        int x = starting.getX() + first.getXOffset();
        int y = starting.getY() + first.getYOffset();

        // add all coordinates of the path into the orderedPath
        for (int i = 1; i < connections.size(); i++) {
            orderedPath.add(Pair.with(x, y));

            if (y >= height || y < 0 || x >= width || x < 0) {
                throw new IllegalArgumentException(
                    "Path goes out of bounds at direction index " + (i - 1) + " (" + connections.get(i - 1) + ")");
            }

            PathTile.Direction dir = connections.get(i);
            PathTile tile = new PathTile(Pair.with(x, y));
            x += dir.getXOffset();
            y += dir.getYOffset();
            if (orderedPath.contains(Pair.with(x, y)) && !(x == starting.getX() && y == starting.getY())) {
                throw new IllegalArgumentException("Path crosses itself at direction index " + i + " (" + dir + ")");
            }
            onLoad(tile, connections.get(i - 1), dir);
        }
        // we should connect back to the starting point
        if (x != starting.getX() || y != starting.getY()) {
            throw new IllegalArgumentException(String.format(
                "Path must loop back around on itself, this path doesn't finish where it began, it finishes at %d, %d.",
                x, y));
        }
        onLoad(starting, connections.get(connections.size() - 1), connections.get(0));
        return orderedPath;
    }

    public abstract void onLoad(Character character);

    public abstract void onLoad(HeroCastleBuilding heroCastle);

    public abstract void onLoad(PathTile pathTile, PathTile.Direction into, PathTile.Direction out);

    public abstract Image onLoadImage(String path);
}
