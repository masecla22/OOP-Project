package nl.rug.oop.rts.model.events.types;

import nl.rug.oop.rts.model.armies.Army;
import nl.rug.oop.rts.model.events.Event;
import nl.rug.oop.rts.model.events.EventType;

public class NaturalDisasterEvent extends Event {

    public NaturalDisasterEvent() {
        super(EventType.NATURAL_DISASTER);
    }

    @Override
    public void execute(Army army) {
        if (army.getUnits().size() < 2) {
            army.getUnits().clear();
        }

        // Remove the second half of all units
        army.getUnits().subList(army.getUnits().size() / 2, army.getUnits().size()).clear();
    }
}
