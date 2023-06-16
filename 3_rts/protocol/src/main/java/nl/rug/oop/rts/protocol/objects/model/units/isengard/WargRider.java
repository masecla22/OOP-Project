package nl.rug.oop.rts.protocol.objects.model.units.isengard;

import nl.rug.oop.rts.protocol.objects.model.units.Unit;
import nl.rug.oop.rts.protocol.objects.model.units.UnitType;

/**
 * This class represents the Warg Rider unit.
 */
public class WargRider extends Unit {
    /**
     * Constructor for the Warg Rider unit.
     * 
     * @param name   - name
     * @param damage - damage
     * @param health - health
     */
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
