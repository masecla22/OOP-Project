package nl.rug.oop.rts.model.units.dwarves;

import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

import static nl.rug.oop.rts.model.armies.Faction.ISENGARD;
import static nl.rug.oop.rts.model.units.UnitType.*;

public class AxeThrower extends Unit {
    public AxeThrower(String name, double damage, double health) {
        super(UnitType.AXE_THROWER, name, damage, health);
    }

    @Override
    public double dealDamage(Unit unit) {
        // not too strong against isengard
        if (unit.getType().getFaction().equals(ISENGARD)) {
            return this.getDamage() * 0.7;
        }

        // gets stronger when health is minimal
        if (this.getHealth() <= 60.0) {
            return this.getDamage() * 1.5;
        }

        // weak against haradrim archers
        if (unit.getType().equals(UnitType.HARADRIM_ARCHER)) {
            return this.getDamage() * 0.3;
        }

        return this.getDamage();
    }

    @Override
    public void takeDamage(Unit unit, double damage) {
        // interaction with Kyle results in great damage
        if (unit.getName().equals("Kyle")) {
            this.setHealth(this.getHealth() - damage * 1.5);
        }

        // interaction with any URUK_HAI does not result in great damage
        else if (unit.getType().equals(URUK_HAI)) {
            this.setHealth(this.getHealth() - damage * 0.9);
        }

        // interaction with any other unit results in normal damage
        else {
            this.setHealth(this.getHealth() - damage);
        }
    }
}
