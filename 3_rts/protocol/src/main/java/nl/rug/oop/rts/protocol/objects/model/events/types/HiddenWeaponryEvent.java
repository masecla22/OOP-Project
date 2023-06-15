package nl.rug.oop.rts.protocol.objects.model.events.types;

import nl.rug.oop.rts.protocol.objects.model.armies.Army;
import nl.rug.oop.rts.protocol.objects.model.events.Event;
import nl.rug.oop.rts.protocol.objects.model.events.EventType;

public class HiddenWeaponryEvent extends Event {
    public HiddenWeaponryEvent() {
        super(EventType.HIDDEN_WEAPONRY);
    }

    @Override
    public void execute(Army army) {
        army.getUnits().forEach(unit -> unit.setDamage(unit.getDamage() + 1));
    }
}
