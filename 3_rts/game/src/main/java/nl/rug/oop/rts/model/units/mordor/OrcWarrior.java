package nl.rug.oop.rts.model.units.mordor;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class OrcWarrior extends Unit {
    public OrcWarrior(double damage, double health) {
        super(UnitType.ORC_WARRIOR, "Orc Warrior", damage, health);
    }
}
