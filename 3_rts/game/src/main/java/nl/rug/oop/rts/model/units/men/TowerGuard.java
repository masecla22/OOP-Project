package nl.rug.oop.rts.model.units.men;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class TowerGuard extends Unit {
    public TowerGuard(String name, double damage, double health) {
        super(UnitType.TOWER_GUARD, name, damage, health);
    }
}
