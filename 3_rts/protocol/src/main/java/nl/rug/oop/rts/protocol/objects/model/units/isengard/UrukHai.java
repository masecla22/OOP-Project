package nl.rug.oop.rts.protocol.objects.model.units.isengard;

import nl.rug.oop.rts.protocol.objects.model.units.Unit;
import nl.rug.oop.rts.protocol.objects.model.units.UnitType;

/**
 * This class represents the Uruk Hai unit.
 */
public class UrukHai extends Unit {
    /**
     * Constructor for the Uruk Hai unit.
     * 
     * @param name   - name
     * @param damage - damage
     * @param health - health
     */
    public UrukHai(String name, double damage, double health) {
        super(UnitType.URUK_HAI, name, damage, health);
    }

    @Override
    public double dealDamage(Unit unit) {
        if (unit.getName().equals("Paprika")) {
            this.setDamage(this.getDamage() + 10.0);
        }
        return this.getDamage();
    }

    @Override
    public void takeDamage(Unit unit, double damage) {
        this.setHealth(this.getHealth() - damage);
    }
}
