package nl.rug.oop.rts.model.units.men;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class IthilienRanger extends Unit {
    public IthilienRanger(double damage, double health) {
        super(UnitType.ITHILIEN_RANGER, "Ithilien Ranger", damage, health);
    }
}
