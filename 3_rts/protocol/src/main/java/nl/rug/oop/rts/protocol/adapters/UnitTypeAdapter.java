package nl.rug.oop.rts.protocol.adapters;

import java.util.List;

import nl.rug.oop.rts.protocol.objects.model.units.Unit;
import nl.rug.oop.rugson.converters.structure.ObjectTreeSerializer;
import nl.rug.oop.rugson.converters.structure.TypeAdapter;
import nl.rug.oop.rugson.objects.JsonElement;
import nl.rug.oop.rugson.objects.JsonObject;

public class UnitTypeAdapter extends TypeAdapter<Unit> {

    public UnitTypeAdapter(ObjectTreeSerializer serializer){
        super(serializer);
    }

    @Override
    public JsonElement serialize(Unit object) {
        JsonObject result = new JsonObject();
        result.put("type", object.getType().toString());
        result.put("name", object.getName());
        result.put("damage", object.getDamage());
        result.put("health", object.getHealth()); 
    }

    @Override
    public Unit deserialize(JsonElement consumer, Class<Unit> clazz, List<Class<?>> genericTypes) {
        
    }
    
}
