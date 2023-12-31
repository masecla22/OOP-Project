package nl.rug.oop.rts.protocol.adapters;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import nl.rug.oop.rts.protocol.objects.model.events.Event;
import nl.rug.oop.rts.protocol.objects.model.events.EventFactory;
import nl.rug.oop.rts.protocol.objects.model.events.EventType;
import nl.rug.oop.rugson.converters.structure.TypeAdapter;
import nl.rug.oop.rugson.objects.JsonElement;
import nl.rug.oop.rugson.objects.JsonObject;

/**
 * Adapter for serializing and deserializing Event objects.
 */
@AllArgsConstructor
public class EventTypeAdapter extends TypeAdapter<Event> {

    @NonNull
    private EventFactory eventFactory;

    @Override
    public JsonElement serialize(Event object) {
        JsonObject result = new JsonObject();
        result.put("type", this.getTreeSerializer().toJson(object.getType().name()));
        return result;
    }

    @Override
    public Event deserialize(JsonElement consumer, Class<Event> clazz, List<Class<?>> genericTypes) {
        if (!(consumer instanceof JsonObject)) {
            throw new IllegalArgumentException("Expected JsonObject, got " + consumer.getClass().getSimpleName());
        }

        JsonObject object = consumer.asJsonObject();

        String type = (String) object.get("type").asJsonValue().getValue();
        EventType parsedType = EventType.valueOf(type);

        return eventFactory.build(parsedType);
    }

}
