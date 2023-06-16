package nl.rug.oop.rts.protocol.objects.model.units.men;

import nl.rug.oop.rts.protocol.objects.model.armies.Faction;
import nl.rug.oop.rts.protocol.objects.model.units.Unit;
import nl.rug.oop.rts.protocol.objects.model.units.UnitType;

/**
 * This class represents the Ithilien Ranger unit.
 */
public class IthilienRanger extends Unit {
    /**
     * Constructor for the Ithilien Ranger unit.
     * 
     * @param name   - name
     * @param damage - damage
     * @param health - health
     */
    public IthilienRanger(String name, double damage, double health) {
        super(UnitType.ITHILIEN_RANGER, name, damage, health);
    }

    @Override
    public double dealDamage(Unit unit) {
        if (unit.getName().equals("MarioKart")) {
            this.setDamage(this.getDamage() * 1.2);
        }
        if (unit.getType().getFaction().equals(Faction.ROHAN)) {
            this.setDamage(this.getDamage() * 1.1);
        }
        return this.getDamage();
    }

    @Override
    public void takeDamage(Unit unit, double damage) {
        this.setHealth(this.getHealth() - damage);
    }
}
