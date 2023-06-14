package nl.rug.oop.rts.model.units.mordor;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class OrcPikeman extends Unit {
    public OrcPikeman(double damage, double health) {
        super(UnitType.ORC_PIKEMAN, "Orc Pikeman", damage, health);
    }
}
