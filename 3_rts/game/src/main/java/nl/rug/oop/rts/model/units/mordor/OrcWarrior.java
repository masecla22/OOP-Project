package nl.rug.oop.rts.model.units.mordor;

import nl.rug.oop.rts.model.armies.Faction;
import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class OrcWarrior extends Unit {
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
