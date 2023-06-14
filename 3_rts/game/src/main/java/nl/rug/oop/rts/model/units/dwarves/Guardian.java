package nl.rug.oop.rts.model.units.dwarves;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class Guardian extends Unit {
    public Guardian(double damage, double health) {
        super(UnitType.GUARDIAN, "Guardian", damage, health);
    }
}
