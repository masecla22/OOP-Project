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

public class Rugson {

    private ObjectTreeSerializer treeSerializer;
    private JsonTreeConverter treeConverter;

    private JsonWriter jsonWriter;

    public Rugson() {
        this(false, new HashMap<>());
    }

    protected Rugson(boolean prettyPrinting, Map<Class<?>, TypeAdapter<?>> typeAdapters) {
        this.treeSerializer = new ObjectTreeSerializer(typeAdapters);
        this.treeConverter = new JsonTreeConverter();

        this.jsonWriter = new JsonWriter(prettyPrinting);
    }

    public String toJson(Object object) {
        JsonElement jsonElement = treeSerializer.toJson(object);
        List<JsonValue> jsonValues = treeConverter.convertFromJsonTree(jsonElement, true);
        return jsonWriter.print(jsonValues);
    }

    public <T> T fromJson(String json, Class<T> clazz) {
        return fromJson(new ByteArrayInputStream(json.getBytes()), clazz);
    }

    public <T> T fromJson(InputStream stream, Class<T> clazz) {
        JsonReader jsonReader = new JsonReader(stream);
        List<JsonValue> jsonValues = jsonReader.ingestEverything();
        JsonElement jsonElement = treeConverter.convertToJsonTree(jsonValues);

        return treeSerializer.fromJson(jsonElement, clazz);
    }
}
