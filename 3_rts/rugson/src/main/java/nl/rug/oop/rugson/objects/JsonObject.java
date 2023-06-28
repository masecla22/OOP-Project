package nl.rug.oop.rugson.objects;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.ToString;

/**
 * JsonObject is a class that represents a JSON object.
 * It is a map of JsonElements.
 */
@ToString(of = "values")
public class JsonObject extends JsonElement implements Map<String, JsonElement> {
    private transient Map<String, JsonElement> values = new HashMap<>();

    @Override
    public void clear() {
        values.clear();
    }

    @Override
    public boolean containsKey(Object object) {
        return values.containsKey(object);
    }

    @Override
    public boolean containsValue(Object object) {
        return values.containsValue(object);
    }

    @Override
    public Set<Entry<String, JsonElement>> entrySet() {
        return values.entrySet();
    }

    @Override
    public JsonElement get(Object object) {
        if (!(object instanceof String)) {
            throw new IllegalArgumentException("Key must be a string");
        }

        return values.get(object);
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }

    @Override
    public Set<String> keySet() {
        return values.keySet();
    }

    @Override
    public JsonElement put(String key, JsonElement value) {
        return values.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends JsonElement> map) {
        values.putAll(map);
    }

    @Override
    public JsonElement remove(Object key) {
        return values.remove(key);
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public Collection<JsonElement> values() {
        return values.values();
    }
}
