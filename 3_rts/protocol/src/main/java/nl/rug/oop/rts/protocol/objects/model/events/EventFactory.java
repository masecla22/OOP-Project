package nl.rug.oop.rts.protocol.objects.model.events;

import lombok.AllArgsConstructor;
import nl.rug.oop.rts.protocol.objects.model.events.types.HiddenWeaponryEvent;
import nl.rug.oop.rts.protocol.objects.model.events.types.NaturalDisasterEvent;
import nl.rug.oop.rts.protocol.objects.model.events.types.ReinforcementsEvent;
import nl.rug.oop.rts.protocol.objects.model.factories.UnitFactory;

/**
 * Factory for creating events.
 */
@AllArgsConstructor
public class EventFactory {
    private UnitFactory unitFactory;

    /**
     * Build an event of the given type.
     *
     * @param type The type of the event.
     * @return The event.
     */
    public Event build(EventType type) {
        switch (type) {
            case REINFORCEMENTS:
                return new ReinforcementsEvent(unitFactory);
            case NATURAL_DISASTER:
                return new NaturalDisasterEvent();
            case HIDDEN_WEAPONRY:
                return new HiddenWeaponryEvent();
            default:
                return null;
        }
    }
}
