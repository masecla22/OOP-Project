package nl.rug.oop.rts.model.units.elves;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class MirkwoodArcher extends Unit {
    public MirkwoodArcher(String name, double damage, double health) {
        super(UnitType.MIRKWOOD_ARCHER, name, damage, health);
    }
}
