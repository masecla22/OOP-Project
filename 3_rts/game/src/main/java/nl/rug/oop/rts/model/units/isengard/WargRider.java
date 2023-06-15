package nl.rug.oop.rts.model.units.isengard;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class WargRider extends Unit {
    public WargRider(String name, double damage, double health) {
        super(UnitType.WARG_RIDER, name, damage, health);
    }

    @Override
    public double dealDamage(Unit unit) {
        if (unit.getType().equals(UnitType.MIRKWOOD_ARCHER)) {
            this.setDamage(this.getDamage() * 1.1);
        }
        return this.getDamage();
    }

    @Override
    public void takeDamage(Unit unit, double damage) {
        this.setHealth(this.getHealth() - damage);
    }
}
