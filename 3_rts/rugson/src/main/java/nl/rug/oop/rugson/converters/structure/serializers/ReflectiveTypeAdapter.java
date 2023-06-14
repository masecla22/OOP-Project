package nl.rug.oop.rugson.converters.structure.serializers;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;
import nl.rug.oop.rugson.converters.structure.ObjectTreeSerializer;
import nl.rug.oop.rugson.converters.structure.TypeAdapter;
import nl.rug.oop.rugson.objects.JsonElement;
import nl.rug.oop.rugson.objects.JsonObject;

/**
 * This class is responsible for serializing and deserializing objects using
 * reflection. Specifically, this type adapter
 * is used for objects that do not have a custom type adapter, and will iterate
 * all the fields of the object and
 * serialize them. It will also create a new instance of the object and set the
 * fields to the deserialized values.
 * 
 * - A public no-args constructor is required for this type adapter to work.
 * - Transient fields will not be serialized.
 * - Static fields will not be serialized.
 * - All fields from the class and all superclasses will be serialized.
 */
public class ReflectiveTypeAdapter extends TypeAdapter<Object> {
    public ReflectiveTypeAdapter(ObjectTreeSerializer treeSerializer) {
        super(treeSerializer);
    }

    private List<Field> getFieldsFor(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        for(Field cr : clazz.getDeclaredFields())
            fields.add(cr);

        if (clazz.getSuperclass() != null) {
            fields.addAll(this.getFieldsFor(clazz.getSuperclass()));
        }

        // Remove static fields
        fields.removeIf(field -> (field.getModifiers() & Modifier.STATIC) != 0);
        // Remove transient fields
        fields.removeIf(field -> (field.getModifiers() & Modifier.TRANSIENT) != 0);

        return fields;
    }

    @Override
    @SneakyThrows
    public JsonObject serialize(Object object) {
        JsonObject jsonResult = new JsonObject();

        Class<?> clazz = object.getClass();

        List<Field> fields = this.getFieldsFor(clazz);
        for (Field field : fields) {
            field.setAccessible(true);
            jsonResult.put(field.getName(), this.getTreeSerializer().toJson(field.get(object)));
        }

        return jsonResult;
    }

    @Override
    @SneakyThrows
    public Object deserialize(JsonElement consumer, Class<Object> clazz, List<Class<?>> genericTypes) {
        if (!(consumer instanceof JsonObject)) {
            throw new IllegalArgumentException("Expected JsonObject, got " + consumer.getClass().getSimpleName());
        }

        // Setup a new instance
        Object result = clazz.getConstructor().newInstance();

        JsonObject json = (JsonObject) consumer;

        List<Field> fields = this.getFieldsFor(clazz);
        for (Field field : fields) {
            field.setAccessible(true);
            field.set(result, this.getTreeSerializer().fromJson(json.get(field.getName()), field));
        }

        return result;
    }

}
