package nl.rug.oop.rts.model.units.dwarves;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class Guardian extends Unit {
    public Guardian(String name, double damage, double health) {
        super(UnitType.GUARDIAN, name, damage, health);
    }

    @Override
    public double dealDamage(Unit unit) {
        // Mario is a strong Guardian
        if (this.getName().equals("Mario")) {
            return this.getDamage() * 1.2;
        }

        // Warg rider sets the lowers the level of damage
        if (unit.getType().equals(UnitType.WARG_RIDER)) {
            return this.getDamage() * 0.8;
        }

        return this.getDamage();
    }

    @Override
    public void takeDamage(Unit unit, double damage) {
        // if he deals with a stronger opponent, he takes less damage
        if (damage > this.getDamage()) {
            this.setHealth(this.getHealth() - damage * 0.8);
        }

        // has a weakness for orc warriors
        else if (unit.getType().equals(UnitType.ORC_WARRIOR)) {
            this.setHealth(this.getHealth() - damage * 1.2);
        }

        // interaction with any other unit results in normal damage
        else {
            this.setHealth(this.getHealth() - damage);
        }
    }
}
