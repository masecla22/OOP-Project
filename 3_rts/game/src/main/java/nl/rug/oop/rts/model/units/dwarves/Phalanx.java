package nl.rug.oop.rts.model.units.dwarves;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class Phalanx extends Unit {
    public Phalanx(String name, double damage, double health) {
        super(UnitType.PHALANX, name, damage, health);
    }
}
