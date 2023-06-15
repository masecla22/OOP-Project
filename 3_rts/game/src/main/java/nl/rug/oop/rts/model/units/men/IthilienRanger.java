package nl.rug.oop.rts.model.units.men;

import nl.rug.oop.rts.model.armies.Faction;
import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class IthilienRanger extends Unit {
    public IthilienRanger(String name, double damage, double health) {
        super(UnitType.ITHILIEN_RANGER, name, damage, health);
    }

    @Override
    public double dealDamage(Unit unit) {
        if (unit.getName().equals("MarioKart")) {
            this.setDamage(this.getDamage() * 1.2);
        }
        if (unit.getType().getFaction().equals(Faction.ROHAN)) {
            this.setDamage(this.getDamage() * 1.1);
        }
        return this.getDamage();
    }

    @Override
    public void takeDamage(Unit unit, double damage) {
        this.setHealth(this.getHealth() - damage);
    }
}
