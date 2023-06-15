package nl.rug.oop.rts.model.units.rohan;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class RohanSwordsman extends Unit {
    public RohanSwordsman(String name, double damage, double health) {
        super(UnitType.ROHAN_SWORDSMAN, name, damage, health);
    }

    @Override
    public double dealDamage(Unit unit) {
        if (unit.getName().equals("Roberta")) {
            this.setDamage(this.getDamage() * 1.1);
        }
        return this.getDamage();
    }

    @Override
    public void takeDamage(Unit unit, double damage) {
        if (unit.getHealth() < this.getHealth() && unit.getType().equals(UnitType.LORIEN_WARRIOR)) {
            this.setHealth(this.getHealth() - 10.0);
        }
    }
}
