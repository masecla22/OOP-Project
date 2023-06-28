package nl.rug.oop.rts.protocol.objects.model.units.mordor;

import nl.rug.oop.rts.protocol.objects.model.units.Unit;
import nl.rug.oop.rts.protocol.objects.model.units.UnitType;

/**
 * This class represents the Orc Pikeman unit.
 */
public class OrcPikeman extends Unit {
    /**
     * Constructor for the Orc Pikeman unit.
     * 
     * @param name   - name
     * @param damage - damage
     * @param health - health
     */
    public OrcPikeman(String name, double damage, double health) {
        super(UnitType.ORC_PIKEMAN, name, damage, health);
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
