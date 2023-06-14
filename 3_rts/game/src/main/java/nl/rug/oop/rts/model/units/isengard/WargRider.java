package nl.rug.oop.rts.model.units.isengard;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class WargRider extends Unit {
    public WargRider(String name, double damage, double health) {
        super(UnitType.WARG_RIDER, name, damage, health);
    }
}
