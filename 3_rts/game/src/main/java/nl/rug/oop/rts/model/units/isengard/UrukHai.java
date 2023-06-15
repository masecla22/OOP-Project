package nl.rug.oop.rts.model.units.isengard;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class UrukHai extends Unit {
    public UrukHai(String name, double damage, double health) {
        super(UnitType.URUK_HAI, name, damage, health);
    }

    @Override
    public double dealDamage(Unit unit) {
        if (unit.getName().equals("Paprika")) {
            this.setDamage(this.getDamage() + 10.0);
        }
        return this.getDamage();
    }

    @Override
    public void takeDamage(Unit unit, double damage) {
        if (unit.getName().equals("Hobo") && unit.getHealth() > this.getHealth()) {
            double aux = this.getHealth();
            this.setHealth(unit.getHealth());
            unit.setHealth(aux);
        }
    }
}
