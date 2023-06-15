package nl.rug.oop.rts.model.units.mordor;

import nl.rug.oop.rts.model.armies.Faction;
import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class HaradrimArcher extends Unit {
    public HaradrimArcher(String name, double damage, double health) {
        super(UnitType.HARADRIM_ARCHER, name, damage, health);
    }

    @Override
    public double dealDamage(Unit unit) {
        if (unit.getType().getFaction().equals(Faction.ELVES)) {
            this.setDamage(this.getDamage() * 1.2);
        }
        return this.getDamage();
    }

    @Override
    public void takeDamage(Unit unit, double damage) {
        this.setHealth(this.getHealth() - damage);
    }
}
