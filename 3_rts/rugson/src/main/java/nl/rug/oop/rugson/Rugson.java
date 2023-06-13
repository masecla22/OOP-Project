package nl.rug.oop.rugson;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.rug.oop.rugson.converters.structure.JsonTreeConverter;
import nl.rug.oop.rugson.converters.structure.ObjectTreeSerializer;
import nl.rug.oop.rugson.converters.structure.TypeAdapter;
import nl.rug.oop.rugson.json.JsonReader;
import nl.rug.oop.rugson.json.JsonValue;
import nl.rug.oop.rugson.json.JsonWriter;
import nl.rug.oop.rugson.objects.JsonElement;

/**
 * The main class of Rugson. This class is responsible for converting objects to
 * JSON and vice versa.
 */
public class Rugson {

    private ObjectTreeSerializer treeSerializer;
    private JsonTreeConverter treeConverter;

    private JsonWriter jsonWriter;

    /**
     * Creates a new Rugson instance with default settings. (No pretty printing, no
     * custom type adapters)
     * 
     * @see RugsonBuilder
     * @see RugsonBuilder#setPrettyPrint(boolean)
     */
    public Rugson() {
        this(false, new HashMap<>());
    }

    /**
     * Creates a new Rugson instance with the specified settings.
     * 
     * @param prettyPrinting - whether to pretty print or not
     * @param typeAdapters   - the type adapters to use
     */
    protected Rugson(boolean prettyPrinting, Map<Class<?>, TypeAdapter<?>> typeAdapters) {
        this.treeSerializer = new ObjectTreeSerializer(typeAdapters);
        this.treeConverter = new JsonTreeConverter();

        this.jsonWriter = new JsonWriter(prettyPrinting);
    }

    /**
     * Converts an object to JSON.
     * 
     * @param object - the object to convert
     * @return - the JSON string
     */
    public String toJson(Object object) {
        JsonElement jsonElement = treeSerializer.toJson(object);
        List<JsonValue> jsonValues = treeConverter.convertFromJsonTree(jsonElement, true);
        return jsonWriter.print(jsonValues);
    }

    /**
     * Converts an object from JSON.
     * 
     * @param json  - the JSON string to convert
     * @param clazz - the class of the object to convert to
     * @param <T>   - the type of the object to convert to
     * 
     * @return - the converted object
     */
    public <T> T fromJson(String json, Class<T> clazz) {
        return fromJson(new ByteArrayInputStream(json.getBytes()), clazz);
    }

    /**
     * Converts an object from JSON.
     * 
     * @param stream - the input stream to read the JSON from
     * @param clazz  - the class of the object to convert to
     * @param <T>    - the type of the object to convert to
     * 
     * @return - the converted object
     */
    public <T> T fromJson(InputStream stream, Class<T> clazz) {
        JsonReader jsonReader = new JsonReader(stream);
        List<JsonValue> jsonValues = jsonReader.ingestEverything();
        JsonElement jsonElement = treeConverter.convertToJsonTree(jsonValues);

        return treeSerializer.fromJson(jsonElement, clazz);
    }
}
