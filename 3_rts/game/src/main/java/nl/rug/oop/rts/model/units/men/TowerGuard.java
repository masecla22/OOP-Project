package nl.rug.oop.rts.model.units.men;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class TowerGuard extends Unit {
    public TowerGuard(double damage, double health) {
        super(UnitType.TOWER_GUARD, "Tower Guard", damage, health);
    }
}
