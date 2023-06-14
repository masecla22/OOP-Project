package nl.rug.oop.rts.model.units.isengard;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class UrukCrossbowman extends Unit {
    public UrukCrossbowman(String name, double damage, double health) {
        super(UnitType.URUK_CROSSBOW_MAN, name, damage, health);
    }
}
