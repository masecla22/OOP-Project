package nl.rug.oop.rts.model.units.isengard;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class UrukCrossbowman extends Unit {
    public UrukCrossbowman(double damage, double health) {
        super(UnitType.URUK_CROSSBOW_MAN, "Uruk Crossbowman", damage, health);
    }
}
