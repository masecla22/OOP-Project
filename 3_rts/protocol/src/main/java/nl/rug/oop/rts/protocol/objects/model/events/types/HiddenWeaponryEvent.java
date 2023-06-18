package nl.rug.oop.rts.protocol.objects.model.events.types;

import nl.rug.oop.rts.protocol.objects.model.armies.Army;
import nl.rug.oop.rts.protocol.objects.model.events.Event;
import nl.rug.oop.rts.protocol.objects.model.events.EventType;

/**
 * Hidden weaponry event.
 */
public class HiddenWeaponryEvent extends Event {
    /**
     * Constructor for HiddenWeaponryEvent.
     */
    public HiddenWeaponryEvent() {
        super(EventType.HIDDEN_WEAPONRY);
    }

    @Override
    public void execute(Army army) {
        army.getUnits().forEach(unit -> unit.setDamage(unit.getDamage() + 1));
    }
}
