package nl.rug.oop.rugson.objects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * JsonArray is a class that represents a JSON array.
 * It is a list of JsonElements.
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "values")
public class JsonArray extends JsonElement implements Iterable<JsonElement> {
    private List<JsonElement> values = new ArrayList<>();

    @Override
    public Iterator<JsonElement> iterator() {
        return values.iterator();
    }

    public JsonElement get(int index) {
        return values.get(index);
    }

    public int size() {
        return values.size();
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public void add(JsonElement value) {
        values.add(value);
    }
}
