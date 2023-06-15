package nl.rug.oop.rts.protocol.objects.model.units.elves;

import nl.rug.oop.rts.protocol.objects.model.armies.Faction;
import nl.rug.oop.rts.protocol.objects.model.units.Unit;
import nl.rug.oop.rts.protocol.objects.model.units.UnitType;

public class LorienWarrior extends Unit {
    public LorienWarrior(String name, double damage, double health) {
        super(UnitType.LORIEN_WARRIOR, name, damage, health);
    }

    @Override
    public double dealDamage(Unit unit) {
        if (unit.getType().getFaction().equals(Faction.DWARVES)) {
            return this.getDamage() * 1.5;
        }

        return this.getDamage();
    }

    @Override
    public void takeDamage(Unit unit, double damage) {
        this.setHealth(this.getHealth() - damage);
    }
}
