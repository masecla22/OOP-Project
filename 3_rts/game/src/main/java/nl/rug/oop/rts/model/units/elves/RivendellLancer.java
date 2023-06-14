package nl.rug.oop.rts.model.units.elves;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class RivendellLancer extends Unit {
    public RivendellLancer(double damage, double health) {
        super(UnitType.RIVENDELL_LANCER, "Rivendell Lancer", damage, health);
    }
}
