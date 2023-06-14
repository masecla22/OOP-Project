package nl.rug.oop.rts.model.units.isengard;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class UrukHai extends Unit {
    public UrukHai(String name, double damage, double health) {
        super(UnitType.URUK_HAI, name, damage, health);
    }
}
