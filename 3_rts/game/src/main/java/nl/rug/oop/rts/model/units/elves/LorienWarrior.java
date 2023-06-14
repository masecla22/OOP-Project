package nl.rug.oop.rts.model.units.elves;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class LorienWarrior extends Unit {
    public LorienWarrior(String name, double damage, double health) {
        super(UnitType.LORIEN_WARRIOR, name, damage, health);
    }
}
