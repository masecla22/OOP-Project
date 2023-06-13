package nl.rug.oop.rugson.objects;

import nl.rug.oop.rugson.json.JsonNumber;
import nl.rug.oop.rugson.json.JsonToken;
import nl.rug.oop.rugson.json.JsonValue;

/**
 * This class represents a JSON element.
 */
public class JsonElement {

    /**
     * Returns whether this element is a JSON array.
     *
     * @return - whether this element is a JSON array
     */
    public boolean isJsonArray() {
        return this instanceof JsonArray;
    }

    /**
     * Returns whether this element is a JSON object.
     *
     * @return - whether this element is a JSON object
     */
    public boolean isJsonObject() {
        return this instanceof JsonObject;
    }

    /**
     * Returns whether this element is a JSON number.
     *
     * @return - whether this element is a JSON number
     */
    public boolean isJsonNumber() {
        return this.isJsonValue() && this.asJsonValue().getType().equals(JsonToken.NUMBER);
    }

    /**
     * Returns whether this element is a JSON value.
     *
     * @return - whether this element is a JSON value
     */
    public boolean isJsonValue() {
        return this instanceof JsonValue;
    }

    /**
     * Returns a {@link JsonArray} representation of this element.
     * 
     * @return - a {@link JsonArray} representation of this element
     */
    public JsonArray asJsonArray() {
        return (JsonArray) this;
    }

    /**
     * Returns a {@link JsonObject} representation of this element.
     * 
     * @return - a {@link JsonObject} representation of this element
     */
    public JsonObject asJsonObject() {
        return (JsonObject) this;
    }

    /**
     * Returns a {@link JsonNumber} representation of this element.
     * 
     * @return - a {@link JsonNumber} representation of this element
     */
    public JsonNumber asJsonNumber() {
        return asJsonValue().asNumber();
    }

    /**
     * Returns a {@link JsonValue} representation of this element.
     * 
     * @return - a {@link JsonValue} representation of this element
     */
    public JsonValue asJsonValue() {
        return (JsonValue) this;
    }
}
