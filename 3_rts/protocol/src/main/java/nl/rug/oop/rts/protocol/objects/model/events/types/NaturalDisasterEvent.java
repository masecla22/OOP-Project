package nl.rug.oop.rts.protocol.objects.model.events.types;

import nl.rug.oop.rts.protocol.objects.model.armies.Army;
import nl.rug.oop.rts.protocol.objects.model.events.Event;
import nl.rug.oop.rts.protocol.objects.model.events.EventType;

/**
 * Natural disaster event.
 */
public class NaturalDisasterEvent extends Event {

    /**
     * Constructor for NaturalDisasterEvent.
     */
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
