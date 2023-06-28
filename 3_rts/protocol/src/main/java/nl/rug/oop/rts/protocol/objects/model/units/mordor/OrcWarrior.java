package nl.rug.oop.rts.protocol.objects.model.units.mordor;

import nl.rug.oop.rts.protocol.objects.model.armies.Faction;
import nl.rug.oop.rts.protocol.objects.model.units.Unit;
import nl.rug.oop.rts.protocol.objects.model.units.UnitType;

/**
 * This class represents the Orc Warrior unit.
 */
public class OrcWarrior extends Unit {
    /**
     * Constructor for the Orc Warrior unit.
     * 
     * @param name   - name
     * @param damage - damage
     * @param health - health
     */
    public OrcWarrior(String name, double damage, double health) {
        super(UnitType.ORC_WARRIOR, name, damage, health);
    }

    @Override
    public double dealDamage(Unit unit) {
        if (unit.getType().getFaction().equals(Faction.DWARVES)) {
            this.setDamage(this.getDamage() * 2.0);
        }
        return this.getDamage();
    }

    @Override
    public void takeDamage(Unit unit, double damage) {
        this.setHealth(this.getHealth() - damage);
    }
}
