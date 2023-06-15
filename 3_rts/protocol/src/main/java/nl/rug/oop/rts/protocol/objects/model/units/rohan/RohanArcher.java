package nl.rug.oop.rts.protocol.objects.model.units.rohan;

import nl.rug.oop.rts.protocol.objects.model.units.Unit;
import nl.rug.oop.rts.protocol.objects.model.units.UnitType;

public class RohanArcher extends Unit {
    public RohanArcher(String name, double damage, double health) {
        super(UnitType.ROHAN_ARCHER, name, damage, health);
    }

    @Override
    public double dealDamage(Unit unit) {
        if (unit.getName().equals("Petrus")) {
            this.setDamage(this.getDamage() * 1.7);
        }
        return this.getDamage();
    }

    @Override
    public void takeDamage(Unit unit, double damage) {
        this.setHealth(this.getHealth() - damage);
    }
}
