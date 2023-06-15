package nl.rug.oop.rts.model.units.elves;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class RivendellLancer extends Unit {
    public RivendellLancer(String name, double damage, double health) {
        super(UnitType.RIVENDELL_LANCER, name, damage, health);
    }

    @Override
    public double dealDamage(Unit unit) {
        if (unit.getType().equals(UnitType.WARG_RIDER)) {
            this.setDamage(this.getDamage() * 0.5);
        }
        return this.getDamage();
    }

    @Override
    public void takeDamage(Unit unit, double damage) {
        if (unit.getHealth()>this.getHealth()) {
            this.setHealth(this.getHealth() * 0.5);
        }
    }
}
