package nl.rug.oop.rts.protocol.objects.model.units.men;

import nl.rug.oop.rts.protocol.objects.model.armies.Faction;
import nl.rug.oop.rts.protocol.objects.model.units.Unit;
import nl.rug.oop.rts.protocol.objects.model.units.UnitType;

/**
 * This class represents the Gondor Soldier unit.
 */
public class GondorSoldier extends Unit {
    /**
     * Constructor for the Gonodr Soldier unit.
     * 
     * @param name   - name
     * @param damage - damage
     * @param health - health
     */
    public GondorSoldier(String name, double damage, double health) {
        super(UnitType.GONDOR_SOLDIER, name, damage, health);
    }

    @Override
    public double dealDamage(Unit unit) {
        if (unit.getType().getFaction().equals(Faction.ELVES)) {
            this.setDamage(this.getDamage() * 1.5);
        }
        return this.getDamage();
    }

    @Override
    public void takeDamage(Unit unit, double damage) {
        this.setHealth(this.getHealth() - damage);
    }
}
