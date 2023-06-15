package nl.rug.oop.rts.model.events.types;

import nl.rug.oop.rts.model.armies.Army;
import nl.rug.oop.rts.model.events.Event;
import nl.rug.oop.rts.model.events.EventType;

public class HiddenWeaponryEvent extends Event {
    public HiddenWeaponryEvent() {
        super(EventType.HIDDEN_WEAPONRY);
    }

    @Override
    public void execute(Army army) {
        army.getUnits().forEach(unit -> unit.setDamage(unit.getDamage() + 1));
    }
}
