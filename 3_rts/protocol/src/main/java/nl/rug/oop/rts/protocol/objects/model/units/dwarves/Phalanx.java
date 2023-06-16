package nl.rug.oop.rts.protocol.objects.model.units.dwarves;

import nl.rug.oop.rts.protocol.objects.model.armies.Faction;
import nl.rug.oop.rts.protocol.objects.model.units.Unit;
import nl.rug.oop.rts.protocol.objects.model.units.UnitType;

/**
 * This class represents the Phalanx unit.
 */
public class Phalanx extends Unit {
    /**
     * Constructor for the Phalanx unit.
     * 
     * @param name   - name
     * @param damage - damage
     * @param health - health
     */
    public Phalanx(String name, double damage, double health) {
        super(UnitType.PHALANX, name, damage, health);
    }

    @Override
    public double dealDamage(Unit unit) {
        if (unit.getType().equals(UnitType.ROHAN_SPEARMAN)) {
            return this.getDamage() * 0.9;
        }

        // very strong against Lorenzoios
        if (unit.getName().equals("Lorenzoios")) {
            return this.getDamage() * 1.5;
        }

        return this.getDamage();
    }

    @Override
    public void takeDamage(Unit unit, double damage) {
        if (unit.getType().getFaction().equals(Faction.ISENGARD)) {
            this.setHealth(this.getHealth() - damage * 0.5);
        } else {
            this.setHealth(this.getHealth() - damage);
        }
    }
}
