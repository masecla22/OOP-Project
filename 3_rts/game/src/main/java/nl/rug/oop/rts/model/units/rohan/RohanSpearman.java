package nl.rug.oop.rts.model.units.rohan;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class RohanSpearman extends Unit {
    public RohanSpearman(String name, double damage, double health) {
        super(UnitType.ROHAN_SPEARMAN, name, damage, health);
    }
}
