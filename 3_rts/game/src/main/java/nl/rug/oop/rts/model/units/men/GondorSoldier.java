package nl.rug.oop.rts.model.units.men;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class GondorSoldier extends Unit {
    public GondorSoldier(String name, double damage, double health) {
        super(UnitType.GONDOR_SOLDIER, name, damage, health);
    }
}
