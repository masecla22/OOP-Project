package nl.rug.oop.rts.model.units.rohan;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class RohanSwordsman extends Unit {
    public RohanSwordsman(String name, double damage, double health) {
        super(UnitType.ROHAN_SWORDSMAN, name, damage, health);
    }
}
