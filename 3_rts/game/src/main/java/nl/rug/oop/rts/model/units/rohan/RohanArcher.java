package nl.rug.oop.rts.model.units.rohan;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class RohanArcher extends Unit {
    public RohanArcher(String name, double damage, double health) {
        super(UnitType.ROHAN_ARCHER, name, damage, health);
    }
}
