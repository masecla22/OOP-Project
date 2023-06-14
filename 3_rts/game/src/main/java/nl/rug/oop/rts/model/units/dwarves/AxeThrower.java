package nl.rug.oop.rts.model.units.dwarves;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class AxeThrower extends Unit {
    public AxeThrower(double damage, double health) {
        super(UnitType.AXE_THROWER, "Axe Thrower", damage, health);
    }
}
