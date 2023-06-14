package nl.rug.oop.rts.model.units.dwarves;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class AxeThrower extends Unit {
    public AxeThrower(String name, double damage, double health) {
        super(UnitType.AXE_THROWER, name, damage, health);
    }
}
