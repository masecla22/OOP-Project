package nl.rug.oop.rts.model.units.rohan;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class RohanArcher extends Unit {
    public RohanArcher(String name, double damage, double health) {
        super(UnitType.ROHAN_ARCHER, name, damage, health);
    }

    @Override
    public double dealDamage(Unit unit) {
        if (unit.getName().equals("Petrus")) {
            this.setDamage(this.getDamage() * 1.7);
        }
        return this.getDamage();
    }

    @Override
    public void takeDamage(Unit unit, double damage) {
        if (unit.getType().equals(UnitType.GONDOR_SOLDIER)) {
            this.setHealth(this.getHealth() * 0.6);
        }
    }
}
