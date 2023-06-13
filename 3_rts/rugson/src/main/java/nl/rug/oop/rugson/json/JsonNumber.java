package nl.rug.oop.rugson.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.rug.oop.rugson.objects.JsonElement;

@AllArgsConstructor
public class JsonNumber extends JsonElement {

    /**
     * As JSON does not have a number type, we store the value as a string,
     * and parse it when needed.
     */
    @Getter
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

    public byte asByte() {
        return Byte.parseByte(value);
    }

    public short asShort() {
        return Short.parseShort(value);
    }
}
