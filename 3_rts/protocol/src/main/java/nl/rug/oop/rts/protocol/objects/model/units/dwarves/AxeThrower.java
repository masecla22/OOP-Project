package nl.rug.oop.rts.protocol.objects.model.units.dwarves;

import nl.rug.oop.rts.protocol.objects.model.armies.Faction;
import nl.rug.oop.rts.protocol.objects.model.units.Unit;
import nl.rug.oop.rts.protocol.objects.model.units.UnitType;

/**
 * This class represents the Axe Thrower unit.
 */
public class AxeThrower extends Unit {
    /**
     * Constructor for the Axe Thrower unit.
     * 
     * @param name   - name
     * @param damage - damage
     * @param health - health
     */
    public AxeThrower(String name, double damage, double health) {
        super(UnitType.AXE_THROWER, name, damage, health);
    }

    @Override
    public double dealDamage(Unit unit) {
        // not too strong against isengard
        if (unit.getType().getFaction().equals(Faction.ISENGARD)) {
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
        } else if (unit.getType().equals(UnitType.URUK_HAI)) {
            this.setHealth(this.getHealth() - damage * 0.9);
        } else {
            this.setHealth(this.getHealth() - damage);
        }
    }
}
