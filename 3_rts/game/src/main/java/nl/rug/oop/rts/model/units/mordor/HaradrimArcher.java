package nl.rug.oop.rts.model.units.mordor;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class HaradrimArcher extends Unit {
    public HaradrimArcher(double damage, double health) {
        super(UnitType.HARADRIM_ARCHER, "Haradrim Archer", damage, health);
    }
}
