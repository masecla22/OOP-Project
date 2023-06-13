package nl.rug.oop.rugson.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.rug.oop.rugson.objects.JsonArray;
import nl.rug.oop.rugson.objects.JsonObject;

@Getter
@AllArgsConstructor
public class JsonValue {
    private JsonToken type;
    private Object value;

    public JsonObject asObject() {
        return (JsonObject) value;
    }

    public JsonArray asArray() {
        return (JsonArray) value;
    }

    @Override
    public String toString() {
        if (value == null) {
            return "[" + type.toString() + "], ";
        } else {
            return "[" + type.toString() + ": " + value + "], ";
        }
    }
}
