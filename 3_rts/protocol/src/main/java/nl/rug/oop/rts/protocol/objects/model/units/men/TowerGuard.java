package nl.rug.oop.rts.protocol.objects.model.units.men;

import nl.rug.oop.rts.protocol.objects.model.armies.Faction;
import nl.rug.oop.rts.protocol.objects.model.units.Unit;
import nl.rug.oop.rts.protocol.objects.model.units.UnitType;

public class TowerGuard extends Unit {
    public TowerGuard(String name, double damage, double health) {
        super(UnitType.TOWER_GUARD, name, damage, health);
    }

    @Override
    public double dealDamage(Unit unit) {
        if (unit.getType().getFaction().equals(Faction.ISENGARD)) {
            this.setDamage(this.getDamage() * 1.3);
        }
        return this.getDamage();
    }

    @Override
    public void takeDamage(Unit unit, double damage) {
        this.setHealth(this.getHealth() - damage);
    }
}
