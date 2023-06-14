package nl.rug.oop.rts.model.units.mordor;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class OrcWarrior extends Unit {
    public OrcWarrior(String name, double damage, double health) {
        super(UnitType.ORC_WARRIOR, name, damage, health);
    }
}
