package nl.rug.oop.rts.protocol.objects.model.units.dwarves;

import nl.rug.oop.rts.protocol.objects.model.units.Unit;
import nl.rug.oop.rts.protocol.objects.model.units.UnitType;

/**
 * This class represents the Guardian unit.
 */
public class Guardian extends Unit {
    /**
     * Constructor for the Guardian unit.
     * 
     * @param name   - name
     * @param damage - damage
     * @param health - health
     */
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
        } else if (unit.getType().equals(UnitType.ORC_WARRIOR)) {
            this.setHealth(this.getHealth() - damage * 1.2);
        } else {
            this.setHealth(this.getHealth() - damage);
        }
    }
}
