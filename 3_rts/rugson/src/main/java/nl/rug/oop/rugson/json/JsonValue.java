package nl.rug.oop.rugson.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.rug.oop.rugson.objects.JsonArray;
import nl.rug.oop.rugson.objects.JsonElement;
import nl.rug.oop.rugson.objects.JsonObject;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class JsonValue extends JsonElement {
    @NonNull
    private JsonToken type;
    private Object value;

    public JsonObject asObject() {
        return (JsonObject) value;
    }

    public JsonArray asArray() {
        return (JsonArray) value;
    }

    public JsonNumber asNumber() {
        return new JsonNumber((String) value);
    }

    @Override
    public String toString() {
        if (value == null) {
            return "[" + type.toString() + "]";
        } else {
            return "[" + type.toString() + ": " + value + "]";
        }
    }
}
