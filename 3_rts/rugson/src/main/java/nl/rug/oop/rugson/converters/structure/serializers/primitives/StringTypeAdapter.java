package nl.rug.oop.rugson.converters.structure.serializers.primitives;

import java.util.List;

import nl.rug.oop.rugson.converters.structure.TypeAdapter;
import nl.rug.oop.rugson.json.JsonToken;
import nl.rug.oop.rugson.json.JsonValue;
import nl.rug.oop.rugson.objects.JsonElement;

/**
 * Type adapter for strings. Very simple, just spits out the string.
 */
public class StringTypeAdapter extends TypeAdapter<String> {

    @Override
    public String deserialize(JsonElement consumer, Class<String> clazz, List<Class<?>> genericTypes) {
        if (consumer == null) {
            return null;
        }

        if (!consumer.isJsonValue()) {
            throw new IllegalArgumentException("Expected JsonValue, got " + consumer.getClass().getSimpleName());
        }

        if (consumer.asJsonValue().getType().equals(JsonToken.NULL)) {
            return null;
        }

        if (!consumer.asJsonValue().getType().equals(JsonToken.STRING)) {
            throw new IllegalArgumentException("Expected JsonString, got " + consumer.getClass().getSimpleName());
        }

        return (String) consumer.asJsonValue().getValue();
    }

    @Override
    public JsonElement serialize(String object) {
        return new JsonValue(JsonToken.STRING, object);
    }
}
