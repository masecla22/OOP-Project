package nl.rug.oop.rugson.converters.structure.serializers;

import java.util.List;

import nl.rug.oop.rugson.converters.structure.TypeAdapter;
import nl.rug.oop.rugson.json.JsonToken;
import nl.rug.oop.rugson.json.JsonValue;
import nl.rug.oop.rugson.objects.JsonElement;

/**
 * Type adapter for enums. Enums are serialized as JsonStrings.
 */
@SuppressWarnings("rawtypes")
public class EnumTypeAdapter extends TypeAdapter<Object> {

    @Override
    @SuppressWarnings("unchecked")
    public Object deserialize(JsonElement consumer, Class<Object> clazz, List<Class<?>> genericTypes) {
        if (!consumer.isJsonValue()) {
            throw new IllegalArgumentException("Expected JsonValue, got " + consumer.getClass().getSimpleName());
        }
        if (!clazz.isEnum()) {
            throw new IllegalArgumentException("Expected Enum, got " + clazz.getSimpleName());
        }

        JsonValue value = consumer.asJsonValue();
        if (!value.getType().equals(JsonToken.STRING)) {
            throw new IllegalArgumentException("Expected JsonString, got " + consumer.getClass().getSimpleName());
        }

        String name = (String) value.getValue();
        return Enum.valueOf((Class) clazz, name);
    }

    @Override
    public JsonElement serialize(Object object) {
        if (!object.getClass().isEnum()) {
            throw new IllegalArgumentException("Expected Enum, got " + object.getClass().getSimpleName());
        }
        return new JsonValue(JsonToken.STRING, ((Enum<?>) object).name());
    }

}
