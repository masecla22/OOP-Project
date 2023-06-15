package nl.rug.oop.rts.model.units.elves;

import nl.rug.oop.rts.model.armies.Faction;
import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitType;

public class MirkwoodArcher extends Unit {
    public MirkwoodArcher(String name, double damage, double health) {
        super(UnitType.MIRKWOOD_ARCHER, name, damage, health);
    }

    @Override
    public double dealDamage(Unit unit) {
        if (unit.getName().equals("Ralphael")) {
            return this.getDamage() * 2.0;
        }

        if (unit.getType().getFaction().equals(Faction.MORDOR)){
            return this.getDamage()*1.2;
        }
        return this.getDamage();
    }

    @Override
    public void takeDamage(Unit unit, double damage) {
        if (unit.getName().equals("Patricia")) {
            this.setHealth(50.0);
        }
    }
}
