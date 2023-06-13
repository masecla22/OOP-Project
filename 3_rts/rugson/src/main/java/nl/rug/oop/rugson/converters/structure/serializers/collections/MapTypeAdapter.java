package nl.rug.oop.rugson.converters.structure.serializers.collections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.rug.oop.rugson.converters.structure.ObjectTreeSerializer;
import nl.rug.oop.rugson.converters.structure.TypeAdapter;
import nl.rug.oop.rugson.objects.JsonElement;
import nl.rug.oop.rugson.objects.JsonObject;

public class MapTypeAdapter extends TypeAdapter<Map<?, ?>> {

    public MapTypeAdapter(ObjectTreeSerializer treeSerializer) {
        super(treeSerializer);
    }

    @Override
    public Map<?, ?> deserialize(JsonElement consumer, Class<Map<?, ?>> clazz, List<Class<?>> genericTypes) {
        if (!consumer.isJsonObject()) {
            throw new IllegalArgumentException("Expected JsonObject, got " + consumer.getClass().getSimpleName());
        }

        if (genericTypes.size() != 2) {
            throw new IllegalArgumentException("Expected 2 generic types, got " + genericTypes.size());
        }

        if (!genericTypes.get(0).equals(String.class)) {
            throw new IllegalArgumentException(
                    "Expected String as key type, got " + genericTypes.get(0).getSimpleName());
        }

        Class<?> valueType = genericTypes.get(1);

        Map<String, Object> result = new HashMap<>();
        Set<Map.Entry<String, JsonElement>> entries = consumer.asJsonObject().entrySet();

        for (Map.Entry<String, JsonElement> entry : entries) {
            String key = entry.getKey();
            Object value = this.getTreeSerializer().fromJson(entry.getValue(), valueType);
            result.put(key, value);
        }

        return result;
    }

    @Override
    public JsonElement serialize(Map<?, ?> object) {
        JsonObject result = new JsonObject();

        for (Map.Entry<?, ?> entry : object.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();
            result.put(key, this.getTreeSerializer().toJson(value));
        }

        return result;
    }
}
