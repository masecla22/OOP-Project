package nl.rug.oop.rugson.converters.structure.serializers.primitives;

import java.util.List;

import nl.rug.oop.rugson.converters.structure.TypeAdapter;
import nl.rug.oop.rugson.json.JsonToken;
import nl.rug.oop.rugson.json.JsonValue;
import nl.rug.oop.rugson.objects.JsonElement;

/**
 * This class is responsible for serializing and deserializing booleans.
 */
public class BooleanTypeAdapter extends TypeAdapter<Boolean> {

    @Override
    public Boolean deserialize(JsonElement consumer, Class<Boolean> clazz, List<Class<?>> genericTypes) {
        if (consumer == null) {
            return false;
        }

        if (!consumer.isJsonValue()) {
            throw new IllegalArgumentException("Expected JsonValue, got " + consumer.getClass().getSimpleName());
        }

        if (consumer.asJsonValue().getType().equals(JsonToken.NULL)) {
            return null;
        }

        if (!consumer.asJsonValue().getType().equals(JsonToken.BOOLEAN)) {
            throw new IllegalArgumentException("Expected JsonBoolean, got " + consumer.getClass().getSimpleName());
        }

        return (boolean) consumer.asJsonValue().getValue();
    }

    @Override
    public JsonElement serialize(Boolean object) {
        if (object == null) {
            return new JsonValue(JsonToken.NULL, null);
        }

        return new JsonValue(JsonToken.BOOLEAN, object);
    }

}
