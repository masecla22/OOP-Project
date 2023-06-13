package nl.rug.oop.rugson.converters.structure.serializers.collections;

import java.util.ArrayList;
import java.util.List;

import nl.rug.oop.rugson.converters.structure.ObjectTreeSerializer;
import nl.rug.oop.rugson.converters.structure.TypeAdapter;
import nl.rug.oop.rugson.objects.JsonArray;
import nl.rug.oop.rugson.objects.JsonElement;

public class ListTypeAdapter extends TypeAdapter<List<?>> {

    public ListTypeAdapter(ObjectTreeSerializer treeSerializer) {
        super(treeSerializer);
    }

    @Override
    public List<?> deserialize(JsonElement consumer, Class<List<?>> clazz, List<Class<?>> genericTypes) {
        if (!consumer.isJsonArray()) {
            throw new IllegalArgumentException("Expected JsonArray, got " + consumer.getClass().getSimpleName());
        }

        if (genericTypes.size() != 1) {
            throw new IllegalArgumentException("Expected 1 generic type, got " + genericTypes.size());
        }

        List<?> result = new ArrayList<>();
        Class<?> genericType = genericTypes.get(0);

        for (JsonElement element : consumer.asJsonArray())
            result.add(this.getTreeSerializer().fromJson(element, genericType));

        return result;
    }

    @Override
    public JsonElement serialize(List<?> object) {
        JsonArray result = new JsonArray();
        for (Object element : object)
            result.add(this.getTreeSerializer().toJson(element));

        return result;
    }

}
