package nl.rug.oop.rts.model.units.isengard;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class WargRider extends Unit {
    public WargRider(double damage, double health) {
        super(UnitType.WARG_RIDER, "Warg Rider", damage, health);
    }
}
