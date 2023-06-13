package nl.rug.oop.rugson.converters.structure.serializers.collections;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.rug.oop.rugson.converters.structure.ObjectTreeSerializer;
import nl.rug.oop.rugson.converters.structure.TypeAdapter;
import nl.rug.oop.rugson.objects.JsonArray;
import nl.rug.oop.rugson.objects.JsonElement;

/**
 * Type adapter for sets. Sets are serialized as arrays.
 */
public class SetTypeAdapter extends TypeAdapter<Set<?>> {

    /**
     * Constructor for the set type adapter.
     * 
     * @param treeSerializer - The tree serializer.
     */
    public SetTypeAdapter(ObjectTreeSerializer treeSerializer) {
        super(treeSerializer);
    }

    /**
     * Deserializes a set from a json element.
     * 
     * @param consumer     - The json element to deserialize.
     * @param clazz        - The class of the set.
     * @param genericTypes - The generic types of the set.
     * @return The deserialized set.
     */
    @Override
    public Set<?> deserialize(JsonElement consumer, Class<Set<?>> clazz, List<Class<?>> genericTypes) {
        if (!consumer.isJsonArray()) {
            throw new IllegalArgumentException("Expected JsonArray, got " + consumer.getClass().getSimpleName());
        }

        if (genericTypes.size() != 1) {
            throw new IllegalArgumentException("Expected 1 generic type, got " + genericTypes.size());
        }

        Set<?> result = new HashSet<>();
        Class<?> genericType = genericTypes.get(0);

        for (JsonElement element : consumer.asJsonArray()) {
            result.add(this.getTreeSerializer().fromJson(element, genericType));
        }

        return result;
    }

    /**
     * Serializes a set to a json element.
     * 
     * @param object - The set to serialize.
     * @return The serialized set.
     */
    @Override
    public JsonElement serialize(Set<?> object) {
        JsonArray result = new JsonArray();
        for (Object element : object) {
            result.add(this.getTreeSerializer().toJson(element));
        }

        return result;
    }

}
