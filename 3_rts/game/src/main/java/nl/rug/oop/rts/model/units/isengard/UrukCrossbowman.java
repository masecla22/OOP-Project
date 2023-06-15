package nl.rug.oop.rts.model.units.isengard;

import nl.rug.oop.rts.model.armies.Faction;
import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class UrukCrossbowman extends Unit {
    public UrukCrossbowman(String name, double damage, double health) {
        super(UnitType.URUK_CROSSBOW_MAN, name, damage, health);
    }

    @Override
    public double dealDamage(Unit unit) {
        if (unit.getType().getFaction().equals(Faction.ELVES)) {
            return this.getDamage() * 2.0;
        }
        return this.getDamage();
    }

    @Override
    public void takeDamage(Unit unit, double damage) {
        if (damage >= this.getDamage() / 2.0) {
            this.setHealth(this.getHealth() / 2.0);
        }

        else {
            unit.setHealth(unit.getHealth() / 2.0);
        }
    }
}
