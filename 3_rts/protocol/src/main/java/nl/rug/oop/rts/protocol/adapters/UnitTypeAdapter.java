package nl.rug.oop.rts.protocol.adapters;

import java.util.List;

import nl.rug.oop.rts.protocol.objects.model.units.Unit;
import nl.rug.oop.rts.protocol.objects.model.units.UnitType;
import nl.rug.oop.rugson.converters.structure.TypeAdapter;
import nl.rug.oop.rugson.objects.JsonElement;
import nl.rug.oop.rugson.objects.JsonObject;

/**
 * Adapter for the Unit class.
 */
public class UnitTypeAdapter extends TypeAdapter<Unit> {

    @Override
    public JsonElement serialize(Unit object) {
        JsonObject result = new JsonObject();
        result.put("type", this.getTreeSerializer().toJson(object.getType().toString()));
        result.put("name", this.getTreeSerializer().toJson(object.getName()));
        result.put("damage", this.getTreeSerializer().toJson(object.getDamage()));
        result.put("health", this.getTreeSerializer().toJson(object.getHealth()));

        return result;
    }

    @Override
    public Unit deserialize(JsonElement consumer, Class<Unit> clazz, List<Class<?>> genericTypes) {
        if (!(consumer instanceof JsonObject)) {
            throw new IllegalArgumentException("Expected JsonObject, got " + consumer.getClass().getSimpleName());
        }

        JsonObject object = consumer.asJsonObject();

        UnitType type = UnitType.valueOf((String) object.get("type").asJsonValue().getValue());
        String name = (String) object.get("name").asJsonValue().getValue();
        double damage = (double) object.get("damage").asJsonNumber().asDouble();
        double health = (double) object.get("health").asJsonNumber().asDouble();

        return type.buildUnit(name, damage, health);
    }

}
