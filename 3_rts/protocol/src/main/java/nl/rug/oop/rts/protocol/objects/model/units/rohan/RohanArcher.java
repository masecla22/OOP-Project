package nl.rug.oop.rts.protocol.objects.model.units.rohan;

import nl.rug.oop.rts.protocol.objects.model.units.Unit;
import nl.rug.oop.rts.protocol.objects.model.units.UnitType;

/**
 * This class represents the Rohan Archer unit.
 */
public class RohanArcher extends Unit {
    /**
     * Constructor for the Rohan Archer unit.
     * 
     * @param name   - name
     * @param damage - damage
     * @param health - health
     */
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
        this.setHealth(this.getHealth() - damage);
    }
}
