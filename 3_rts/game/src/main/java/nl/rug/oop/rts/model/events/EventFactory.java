package nl.rug.oop.rts.model.events;

import lombok.AllArgsConstructor;
import nl.rug.oop.rts.model.events.types.HiddenWeaponryEvent;
import nl.rug.oop.rts.model.events.types.NaturalDisasterEvent;
import nl.rug.oop.rts.model.events.types.ReinforcementsEvent;
import nl.rug.oop.rts.model.units.UnitFactory;

@AllArgsConstructor
public class EventFactory {
    private UnitFactory unitFactory;

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
