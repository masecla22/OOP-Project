package nl.rug.oop.rts.protocol.objects.model.units.rohan;

import nl.rug.oop.rts.protocol.objects.model.units.Unit;
import nl.rug.oop.rts.protocol.objects.model.units.UnitType;

/**
 * This class represents the Rohan Swordsman unit.
 */
public class RohanSwordsman extends Unit {
    /**
     * Constructor for the Rohan Swordsman unit.
     * 
     * @param name   - name
     * @param damage - damage
     * @param health - health
     */
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
        this.setHealth(this.getHealth() - damage);
    }
}
