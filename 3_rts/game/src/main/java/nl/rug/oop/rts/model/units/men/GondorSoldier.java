package nl.rug.oop.rts.model.units.men;

import nl.rug.oop.rts.model.armies.Faction;
import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class GondorSoldier extends Unit {
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
        if (this.getHealth() < unit.getHealth()) {
            this.setHealth(unit.getHealth());
        }
    }
}
