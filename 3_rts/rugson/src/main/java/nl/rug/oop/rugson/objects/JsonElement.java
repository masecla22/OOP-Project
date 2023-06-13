package nl.rug.oop.rugson.objects;

import nl.rug.oop.rugson.json.JsonNumber;
import nl.rug.oop.rugson.json.JsonToken;
import nl.rug.oop.rugson.json.JsonValue;

public class JsonElement {

    public boolean isJsonArray() {
        return this instanceof JsonArray;
    }

    public boolean isJsonObject() {
        return this instanceof JsonObject;
    }

    public boolean isJsonNumber() {
        return this.isJsonValue() && this.asJsonValue().getType().equals(JsonToken.NUMBER);
    }

    public boolean isJsonValue() {
        return this instanceof JsonValue;
    }

    public JsonArray asJsonArray() {
        return (JsonArray) this;
    }

    public JsonObject asJsonObject() {
        return (JsonObject) this;
    }

    public JsonNumber asJsonNumber() {
        return asJsonValue().asNumber();
    }

    public JsonValue asJsonValue() {
        return (JsonValue) this;
    }
}
