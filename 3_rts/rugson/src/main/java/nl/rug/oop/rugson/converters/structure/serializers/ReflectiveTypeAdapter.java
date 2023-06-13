package nl.rug.oop.rugson.converters.structure.serializers;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import lombok.SneakyThrows;
import nl.rug.oop.rugson.converters.structure.ObjectTreeSerializer;
import nl.rug.oop.rugson.converters.structure.TypeAdapter;
import nl.rug.oop.rugson.objects.JsonElement;
import nl.rug.oop.rugson.objects.JsonObject;

public class ReflectiveTypeAdapter extends TypeAdapter<Object> {
    public ReflectiveTypeAdapter(ObjectTreeSerializer treeSerializer) {
        super(treeSerializer);
    }

    @Override
    @SneakyThrows
    public JsonObject serialize(Object object) {
        JsonObject jsonResult = new JsonObject();

        Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if ((field.getModifiers() & Modifier.TRANSIENT) != 0) {
                continue;
            }

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
        for (Field field : clazz.getDeclaredFields()) {
            if ((field.getModifiers() & Modifier.TRANSIENT) != 0) {
                continue;
            }

            field.setAccessible(true);
            field.set(result, this.getTreeSerializer().fromJson(json.get(field.getName()), field));
        }

        return result;
    }

}
