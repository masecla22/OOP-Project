package nl.rug.oop.rts.protocol.objects.model.units.rohan;

import nl.rug.oop.rts.protocol.objects.model.units.Unit;
import nl.rug.oop.rts.protocol.objects.model.units.UnitType;

/**
 * This class represents the Rohan Spearman unit.
 */
public class RohanSpearman extends Unit {
    /**
     * Constructor for the Rohan Spearman unit.
     * 
     * @param name   - name
     * @param damage - damage
     * @param health - health
     */
    public RohanSpearman(String name, double damage, double health) {
        super(UnitType.ROHAN_SPEARMAN, name, damage, health);
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
        this.setHealth(this.getHealth() - damage);
    }
}
