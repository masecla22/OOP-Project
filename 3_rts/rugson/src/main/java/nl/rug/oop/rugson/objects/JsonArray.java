package nl.rug.oop.rugson.objects;

import java.util.Iterator;
import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JsonArray implements Iterable<JsonObject> {
    private List<JsonObject> values;

    @Override
    public Iterator<JsonObject> iterator() {
        return values.iterator();
    }

    public JsonObject get(int index) {
        return values.get(index);
    }

    public int size() {
        return values.size();
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public void add(JsonObject value) {
        values.add(value);
    }
}
