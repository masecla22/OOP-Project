package nl.rug.oop.rugson.converters.structure.serializers.primitives;

import java.util.List;
import java.util.UUID;

import nl.rug.oop.rugson.converters.structure.ObjectTreeSerializer;
import nl.rug.oop.rugson.converters.structure.TypeAdapter;
import nl.rug.oop.rugson.json.JsonToken;
import nl.rug.oop.rugson.json.JsonValue;
import nl.rug.oop.rugson.objects.JsonElement;

/**
 * Type adapter for UUID's.
 * It will use the UUID.fromString() method to parse the string, so it will
 * throw an exception if the string is not a valid UUID. It will use the
 * UUID.toString() method to serialize the UUID.
 */
public class UUIDTypeAdapter extends TypeAdapter<UUID> {

    public UUIDTypeAdapter(ObjectTreeSerializer treeSerializer) {
        super(treeSerializer);
    }

    @Override
    public UUID deserialize(JsonElement consumer, Class<UUID> clazz, List<Class<?>> genericTypes) {
        if (!consumer.isJsonValue()) {
            throw new IllegalArgumentException("Expected JsonValue, got " + consumer.getClass().getSimpleName());
        }

        if (!consumer.asJsonValue().getType().equals(JsonToken.STRING)) {
            throw new IllegalArgumentException("Expected JsonString, got " + consumer.getClass().getSimpleName());
        }

        return UUID.fromString((String) consumer.asJsonValue().getValue());
    }

    @Override
    public JsonElement serialize(UUID object) {
        return new JsonValue(JsonToken.STRING, object.toString());
    }

}
