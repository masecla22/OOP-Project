package nl.rug.oop.rts.protocol.objects.model.units.isengard;

import nl.rug.oop.rts.protocol.objects.model.armies.Faction;
import nl.rug.oop.rts.protocol.objects.model.units.Unit;
import nl.rug.oop.rts.protocol.objects.model.units.UnitType;

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
        // If the damage of the opponent is more than half of the damage of the unit,
        // the unit's health is halved, it's doubled
        if (damage >= this.getDamage() / 2.0) {
            this.setHealth(this.getHealth() - damage * 2.0);
        } else {
            unit.setHealth(unit.getHealth() / 2.0);
        }
    }
}
