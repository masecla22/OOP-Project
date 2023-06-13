package nl.rug.oop.rugson.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.rug.oop.rugson.objects.JsonElement;

/**
 * This class represents a JSON number. We use it to store the value as a
 * string, and lazily parse it when needed.
 */
@AllArgsConstructor
public class JsonNumber extends JsonElement {

    /**
     * As JSON does not have a number type, we store the value as a string,
     * and parse it when needed.
     */
    @Getter
    private String value;

    /**
     * Returns the value as an int.
     * 
     * @return - the value as an int
     */
    public int asInt() {
        return Integer.parseInt(value);
    }

    /**
     * Returns the value as a double.
     * 
     * @return - the value as a double
     */
    public double asDouble() {
        return Double.parseDouble(value);
    }

    /**
     * Returns the value as a float.
     * 
     * @return - the value as a float
     */
    public float asFloat() {
        return Float.parseFloat(value);
    }

    /**
     * Returns the value as a long.
     * 
     * @return - the value as a long
     */
    public long asLong() {
        return Long.parseLong(value);
    }

    /**
     * Returns the value as a byte.
     * 
     * @return - the value as a byte
     */
    public byte asByte() {
        return Byte.parseByte(value);
    }

    /**
     * Returns the value as a short.
     * 
     * @return - the value as a short
     */
    public short asShort() {
        return Short.parseShort(value);
    }
}
