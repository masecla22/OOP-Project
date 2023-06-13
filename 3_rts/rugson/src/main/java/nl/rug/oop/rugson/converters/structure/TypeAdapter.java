package nl.rug.oop.rugson.converters.structure;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.rug.oop.rugson.objects.JsonElement;

@AllArgsConstructor
public abstract class TypeAdapter<K> {
    @Getter(AccessLevel.PROTECTED)
    private ObjectTreeSerializer treeSerializer;

    public abstract JsonElement serialize(K object);

    public abstract K deserialize(JsonElement consumer, Class<K> clazz, List<Class<?>> genericTypes);
}