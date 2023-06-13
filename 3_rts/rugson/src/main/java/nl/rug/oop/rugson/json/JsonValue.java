package nl.rug.oop.rugson.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.rug.oop.rugson.objects.JsonArray;
import nl.rug.oop.rugson.objects.JsonElement;
import nl.rug.oop.rugson.objects.JsonObject;

/**
 * This class represents a JSON value.
 */
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class JsonValue extends JsonElement {
    @NonNull
    private JsonToken type;
    private Object value;

    /**
     * Returns the value as a {@link JsonObject}.
     * 
     * @return - the value as a {@link JsonObject}
     */
    public JsonObject asObject() {
        return (JsonObject) value;
    }

    /**
     * Returns the value as a {@link JsonArray}.
     * 
     * @return - the value as a {@link JsonArray}
     */
    public JsonArray asArray() {
        return (JsonArray) value;
    }

    /**
     * Returns the value as a {@link JsonNumber}.
     * 
     * @return - the value as a {@link JsonNumber}
     */
    public JsonNumber asNumber() {
        return new JsonNumber((String) value);
    }

    /**
     * Returns the value as a {@link String}.
     * This will <b>NOT</b> return the value as a JSON String,
     * and should only be used for debugging purposes.
     * 
     * @return - the value as a {@link String}
     */
    @Override
    public String toString() {
        if (value == null) {
            return "[" + type.toString() + "]";
        } else {
            return "[" + type.toString() + ": " + value + "]";
        }
    }
}
