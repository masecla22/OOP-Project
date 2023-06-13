package nl.rug.oop.rugson.converters.structure;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import nl.rug.oop.rugson.json.JsonToken;
import nl.rug.oop.rugson.json.JsonValue;
import nl.rug.oop.rugson.objects.JsonArray;
import nl.rug.oop.rugson.objects.JsonElement;
import nl.rug.oop.rugson.objects.JsonObject;

/**
 * This class converts a list of {@link JsonValue}s to a {@link JsonElement}.
 * (and vice versa)
 */
public class JsonTreeConverter {
    /**
     * Converts a list of {@link JsonValue}s to a {@link JsonElement}.
     * 
     * @param value - the list of {@link JsonValue}s
     * @return - the {@link JsonElement}
     */
    public JsonElement convertToJsonTree(List<JsonValue> value) {
        return convertToJsonTree(new ArrayDeque<>(value));
    }

    /**
     * Converts a queue of {@link JsonValue}s to a {@link JsonElement}.
     * 
     * @param value - the list of {@link JsonValue}s
     * @return - the {@link JsonElement}
     */
    public JsonElement convertToJsonTree(Queue<JsonValue> value) {
        // If string, boolean, number or null, return value
        JsonValue firstValue = value.poll();
        JsonToken firstType = firstValue.getType();

        if (firstType.equals(JsonToken.STRING) ||
                firstType.equals(JsonToken.BOOLEAN) ||
                firstType.equals(JsonToken.NUMBER) ||
                firstType.equals(JsonToken.NULL)) {
            return firstValue;
        }

        if (firstType.equals(JsonToken.START_ARRAY)) {
            return handleStartArray(value);
        }

        if (firstType.equals(JsonToken.START_OBJECT)) {
            return handleStartObject(value);
        }

        throw new RuntimeException("Unexpected token " + firstType);
    }

    private JsonElement handleStartObject(Queue<JsonValue> value) {
        JsonObject object = new JsonObject();
        while (!value.peek().getType().equals(JsonToken.END_OBJECT)) {
            JsonValue name = value.poll();
            if (!name.getType().equals(JsonToken.NAME)) {
                throw new RuntimeException("Expected name, got " + name.getType());
            }

            String nameValue = (String) name.getValue();
            JsonElement objectValue = convertToJsonTree(value);
            object.put(nameValue, objectValue);
        }

        // Consume the end object
        value.poll();

        return object;
    }

    private JsonElement handleStartArray(Queue<JsonValue> value) {
        JsonArray array = new JsonArray();
        while (!value.peek().getType().equals(JsonToken.END_ARRAY)) {
            array.add(convertToJsonTree(value));
        }

        // Consume the end array
        value.poll();

        return array;
    }

    /**
     * Converts a {@link JsonElement} to a list of {@link JsonValue}s.
     *
     * @param element    - the element to convert
     * @param endWithEof - whether to end the list with an EOF token
     * 
     * @return - the list of {@link JsonValue}s
     */
    public List<JsonValue> convertFromJsonTree(JsonElement element, boolean endWithEof) {
        List<JsonValue> result = recursivelyBuildJsonList(element);
        if (endWithEof) {
            result.add(new JsonValue(JsonToken.EOF));
        }
        return result;
    }

    private List<JsonValue> recursivelyBuildJsonList(JsonElement element) {
        List<JsonValue> result = new ArrayList<>();
        if (element.isJsonValue()) {
            result.add(element.asJsonValue());
            return result;
        }

        if (element.isJsonNumber()) {
            result.add(new JsonValue(JsonToken.NUMBER, element.asJsonNumber().getValue()));
            return result;
        }

        if (element.isJsonArray()) {
            JsonArray array = element.asJsonArray();
            result.add(new JsonValue(JsonToken.START_ARRAY));
            for (JsonElement arrayElement : array) {
                result.addAll(convertFromJsonTree(arrayElement, false));
            }
            result.add(new JsonValue(JsonToken.END_ARRAY));
            return result;
        }

        if (element.isJsonObject()) {
            JsonObject object = element.asJsonObject();
            result.add(new JsonValue(JsonToken.START_OBJECT));
            for (String key : object.keySet()) {
                result.add(new JsonValue(JsonToken.NAME, key));
                result.addAll(convertFromJsonTree(object.get(key), false));
            }

            result.add(new JsonValue(JsonToken.END_OBJECT));
            return result;
        }

        throw new RuntimeException("Unexpected element " + element);
    }
}
