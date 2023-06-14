package nl.rug.oop.rts.model.units.elves;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class RivendellLancer extends Unit {
    public RivendellLancer(String name, double damage, double health) {
        super(UnitType.RIVENDELL_LANCER, name, damage, health);
    }
}
