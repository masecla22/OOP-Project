package nl.rug.oop.rugson.converters.structure;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import nl.rug.oop.rugson.objects.JsonElement;

/**
 * TypeAdapter is an abstract class that defines the interface for all type
 * adapters.
 * Type adapters are used to convert Java objects to JsonElements and vice
 * versa.
 * 
 * @param <K> The type of the object that this adapter can convert.
 */
public abstract class TypeAdapter<K> {
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private ObjectTreeSerializer treeSerializer;

    /**
     * Serializes an object to a JsonElement.
     * 
     * @param object - The object to serialize.
     * @return The serialized object.
     */
    public abstract JsonElement serialize(K object);

    /**
     * Deserializes a JsonElement to an object.
     * 
     * @param consumer     - The JsonElement to deserialize.
     * @param clazz        - The class of the object.
     * @param genericTypes - The generic types of the object.
     * @return The deserialized object.
     */
    public abstract K deserialize(JsonElement consumer, Class<K> clazz, List<Class<?>> genericTypes);
}