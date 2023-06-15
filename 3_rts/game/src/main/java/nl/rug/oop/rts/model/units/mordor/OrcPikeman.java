package nl.rug.oop.rts.model.units.mordor;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class OrcPikeman extends Unit {
    public OrcPikeman(String name, double damage, double health) {
        super(UnitType.ORC_PIKEMAN, name, damage, health);
    }

    @Override
    public double dealDamage(Unit unit) {
        if (unit.getDamage() > this.getDamage()) {
            this.setDamage(unit.getDamage());
        }
        return this.getDamage();
    }

    @Override
    public void takeDamage(Unit unit, double damage) {
        if (damage > this.getDamage()) {
            this.setHealth(this.getHealth() * 0.9);
        }
    }
}
