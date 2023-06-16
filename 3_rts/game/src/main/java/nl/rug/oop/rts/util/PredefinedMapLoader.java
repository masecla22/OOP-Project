package nl.rug.oop.rts.util;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nl.rug.oop.rugson.Rugson;

public class PredefinedMapLoader {
    private static final String MAP_DIR = "maps";

    private final Map<String, InputStream> textures = new HashMap<>();
    private final Map<String, nl.rug.oop.rts.protocol.objects.model.Map> cachedMaps = new HashMap<>();

    private Rugson rugson;

    public PredefinedMapLoader(Rugson rugson) {
        this.rugson = rugson;

        initMaps();
    }

    /**
     * Retrieves an input stream for a file in the "resources" folder based on the
     * given filename.
     *
     * @param first Name/path of the file within the resource folder to load.
     * @param more  Additional items of the path.
     * @return Input stream used to read the loaded file. Throws an exception if the
     *         file does not exist.
     */
    private InputStream getResourceFromPath(String first, String... more) {
        String resourceFilePath = Path.of(first, more).toString();
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(resourceFilePath);

        if (inputStream == null) {
            throw new IllegalArgumentException("File " + resourceFilePath + " not found.");
        }
        return inputStream;
    }

    private void initMaps() {
        textures.put("star", getResourceFromPath(MAP_DIR, "star.json"));
        textures.put("cubical", getResourceFromPath(MAP_DIR, "cubical.json"));
        textures.put("romania", getResourceFromPath(MAP_DIR, "romania.json"));
    }

    public nl.rug.oop.rts.protocol.objects.model.Map getMap(String mapName) {
        if (cachedMaps.containsKey(mapName)) {
            return cachedMaps.get(mapName);
        }

        InputStream mapStream = textures.get(mapName);
        if (mapStream == null) {
            throw new IllegalArgumentException("Map " + mapName + " not found.");
        }

        nl.rug.oop.rts.protocol.objects.model.Map map = rugson.fromJson(mapStream,
                nl.rug.oop.rts.protocol.objects.model.Map.class);
        cachedMaps.put(mapName, map);
        return map;
    }

    public Set<String> getPredefinedMapNames() {
        return textures.keySet();
    }
}
