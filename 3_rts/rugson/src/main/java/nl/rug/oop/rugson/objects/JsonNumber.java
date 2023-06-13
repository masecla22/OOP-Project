package nl.rug.oop.rugson.objects;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JsonNumber extends JsonObject {

    /**
     * As JSON does not have a number type, we store the value as a string,
     * and parse it when needed.
     */
    private String value;

    public int asInt() {
        return Integer.parseInt(value);
    }

    public double asDouble() {
        return Double.parseDouble(value);
    }

    public float asFloat() {
        return Float.parseFloat(value);
    }

    public long asLong() {
        return Long.parseLong(value);
    }
}
