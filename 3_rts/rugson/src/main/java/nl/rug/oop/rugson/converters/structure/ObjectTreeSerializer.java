package nl.rug.oop.rugson.converters.structure;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import nl.rug.oop.rugson.converters.structure.serializers.EnumTypeAdapter;
import nl.rug.oop.rugson.converters.structure.serializers.ReflectiveTypeAdapter;
import nl.rug.oop.rugson.converters.structure.serializers.collections.ListTypeAdapter;
import nl.rug.oop.rugson.converters.structure.serializers.collections.MapTypeAdapter;
import nl.rug.oop.rugson.converters.structure.serializers.collections.SetTypeAdapter;
import nl.rug.oop.rugson.converters.structure.serializers.primitives.BooleanTypeAdapter;
import nl.rug.oop.rugson.converters.structure.serializers.primitives.NumberTypeAdapter;
import nl.rug.oop.rugson.converters.structure.serializers.primitives.StringTypeAdapter;
import nl.rug.oop.rugson.converters.structure.serializers.primitives.UUIDTypeAdapter;
import nl.rug.oop.rugson.objects.JsonElement;

/**
 * This class is responsible for serializing and deserializing objects.
 * It will use the TypeAdapter's to do so.
 */
public class ObjectTreeSerializer {
    private Map<Class<?>, TypeAdapter<?>> converters = new HashMap<>();

    private TypeAdapter<Object> defaultConverter;

    /**
     * Creates a new ObjectTreeSerializer with the default converters.
     * 
     * @param converters - A map of converters to register.
     *                   (on top of the default converters)
     */
    public ObjectTreeSerializer(Map<Class<?>, TypeAdapter<?>> converters) {
        registerDefaultConverters();
        if (converters != null) {
            for (Class<?> clazz : converters.keySet()) {
                this.register(clazz, converters.get(clazz));
            }
        }
    }

    private void registerDefaultConverters() {
        this.defaultConverter = new ReflectiveTypeAdapter();
        this.defaultConverter.setTreeSerializer(this);

        this.register(List.class, new ListTypeAdapter());
        this.register(Map.class, new MapTypeAdapter());
        this.register(Set.class, new SetTypeAdapter());

        this.register(Number.class, new NumberTypeAdapter());
        this.register(String.class, new StringTypeAdapter());
        this.register(UUID.class, new UUIDTypeAdapter());

        this.register(Enum.class, new EnumTypeAdapter());

        this.register(Boolean.class, new BooleanTypeAdapter());
        this.register(boolean.class, new BooleanTypeAdapter());

        Set<Class<?>> coveredByNumber = Set.of(byte.class, short.class, int.class, long.class, float.class,
                double.class);

        for (Class<?> clazz : coveredByNumber) {
            this.register(clazz, new NumberTypeAdapter());
        }
    }

    /**
     * Registers a new converter for a class.
     * 
     * @param clazz     - The class to register the converter for.
     * @param converter - The converter to register.
     * @param <K>       - The type of the class.
     */
    public <K> void register(Class<K> clazz, TypeAdapter<?> converter) {
        converter.setTreeSerializer(this);
        converters.put(clazz, converter);
    }

    private TypeAdapter<?> getConverter(Class<?> clazz) {
        if (clazz.equals(Class.class)) {
            throw new RuntimeException("SAFETY MEASURE: Class.class is not supported");
        }

        TypeAdapter<?> converter = converters.get(clazz);
        if (converter != null) {
            return converter;
        }

        // Look for a converter for a superclass
        for (Class<?> available : converters.keySet()) {
            if (available.isAssignableFrom(clazz)) {
                TypeAdapter<?> availableConverter = converters.get(available);
                return availableConverter;
            }
        }

        return defaultConverter;
    }

    /**
     * Serializes an object to a JsonElement.
     * 
     * @param object - The object to serialize.
     * @param <K>    - The type of the object.
     * @return - The serialized JsonElement.
     */
    @SuppressWarnings("unchecked")
    public <K> JsonElement toJson(K object) {
        Class<K> clazz = (Class<K>) object.getClass();
        TypeAdapter<K> converter = (TypeAdapter<K>) getConverter(clazz);

        return converter.serialize(object);
    }

    /**
     * Serializes an object to a JsonElement.
     * 
     * @param object - The object to serialize.
     * @param clazz  - The class of the object.
     * @param <K>    - The type of the object.
     * @return - The serialized JsonElement.
     */
    @SuppressWarnings("unchecked")
    public <K> JsonElement toJson(K object, Class<?> clazz) {
        if (object != null) {
            clazz = (Class<K>) object.getClass();
        }

        TypeAdapter<K> converter = (TypeAdapter<K>) getConverter(clazz);

        return converter.serialize(object);
    }

    /**
     * Deserializes a JsonElement to an object.
     * 
     * @param element - The JsonElement to deserialize.
     * @param clazz   - The class of the object.
     * @param <K>     - The type of the object.
     * @return - The deserialized object.
     */
    @SuppressWarnings("unchecked")
    public <K> K fromJson(JsonElement element, Class<?> clazz) {
        TypeAdapter<K> converter = (TypeAdapter<K>) getConverter(clazz);
        return converter.deserialize(element, (Class<K>) clazz, new ArrayList<>());
    }

    /**
     * Deserializes a JsonElement to an object. It will also use the generic types
     * of
     * the field.
     * 
     * @param element - The JsonElement to deserialize.
     * @param field   - The field of the object.
     * @param <K>     - The type of the object.
     * @return - The deserialized object.
     */
    @SuppressWarnings("unchecked")
    public <K> K fromJson(JsonElement element, Field field) {
        Type type = field.getGenericType();
        TypeAdapter<K> converter = (TypeAdapter<K>) getConverter(field.getType());
        Class<K> clazz = (Class<K>) field.getType();
        if (type instanceof ParameterizedType generics) {
            List<Class<?>> genericTypes = new ArrayList<>();
            for (Type generic : generics.getActualTypeArguments()) {
                genericTypes.add((Class<?>) generic);
            }

            return converter.deserialize(element, clazz, genericTypes);
        } else {
            return converter.deserialize(element, clazz, new ArrayList<>());
        }
    }

    /**
     * Deserializes a JsonElement to an object.
     * 
     * @param <K>          - The type of the object.
     * @param element      - The JsonElement to deserialize.
     * @param clazz        - The class of the object.
     * @param genericTypes - The generic types of the object.
     * @return - The deserialized object.
     */
    @SuppressWarnings("unchecked")
    public <K> K fromJson(JsonElement element, Class<?> clazz, List<Class<?>> genericTypes) {
        TypeAdapter<K> converter = (TypeAdapter<K>) getConverter(clazz);
        return converter.deserialize(element, (Class<K>) clazz, genericTypes);
    }

    /**
     * Deserializes a JsonElement to an object.
     * 
     * @param <K>          - The type of the object.
     * @param element      - The JsonElement to deserialize.
     * @param clazz        - The class of the object.
     * @param genericTypes - The generic types of the object.
     * @return - The deserialized object.
     */
    @SuppressWarnings("unchecked")
    public <K> K fromJson(JsonElement element, Class<?> clazz, Class<?>... genericTypes) {
        return fromJson(element, (Class<K>) clazz, Arrays.asList(genericTypes));
    }

}
