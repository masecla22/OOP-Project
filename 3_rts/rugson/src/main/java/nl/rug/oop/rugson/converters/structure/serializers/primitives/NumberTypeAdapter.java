package nl.rug.oop.rugson.converters.structure.serializers.primitives;

import java.util.List;

import nl.rug.oop.rugson.converters.structure.TypeAdapter;
import nl.rug.oop.rugson.json.JsonNumber;
import nl.rug.oop.rugson.json.JsonToken;
import nl.rug.oop.rugson.json.JsonValue;
import nl.rug.oop.rugson.objects.JsonElement;

/**
 * Type adapter for numbers. Will handle lazy parsing of numbers.
 */
public class NumberTypeAdapter extends TypeAdapter<Number> {

    @Override
    public Number deserialize(JsonElement consumer, Class<Number> clazz, List<Class<?>> genericTypes) {
        if (consumer == null) {
            return 0;
        }

        if (!consumer.isJsonValue()) {
            throw new IllegalArgumentException("Expected JsonValue, got " + consumer.getClass().getSimpleName());
        }

        if (consumer.asJsonValue().getType().equals(JsonToken.NULL)) {
            return 0;
        }

        if (!consumer.asJsonValue().getType().equals(JsonToken.NUMBER)) {
            throw new IllegalArgumentException("Expected JsonNumber, got " + consumer.getClass().getSimpleName());
        }

        JsonNumber number = consumer.asJsonValue().asJsonNumber();
        if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
            return number.asInt();
        } else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
            return number.asLong();
        } else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
            return number.asDouble();
        } else if (clazz.equals(Float.class) || clazz.equals(float.class)) {
            return number.asFloat();
        } else if (clazz.equals(Byte.class) || clazz.equals(byte.class)) {
            return number.asByte();
        } else if (clazz.equals(Short.class) || clazz.equals(short.class)) {
            return number.asShort();
        } else {
            throw new IllegalArgumentException("Unsupported number type " + clazz.getSimpleName());
        }
    }

    @Override
    public JsonElement serialize(Number object) {
        if (object == null) {
            return new JsonValue(JsonToken.NULL, null);
        }

        return new JsonValue(JsonToken.NUMBER, object.toString());
    }
}
