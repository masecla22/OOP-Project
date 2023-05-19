package nl.rug.oop.rpg.game.items.modifiers;

import nl.rug.oop.rpg.game.items.ModifierItem;

public class WarriorTalisman extends ModifierItem {

    public WarriorTalisman() {
        super("Warrior Talisman", "A small pendant with a sword engraved on it. x2 damage and 0.5x incoming damage");
    }

    @Override
    public double modifyIncomingDamage(double damage) {
        return damage * 0.5;
    }

    @Override
    public double modifyOutgoingDamage(double damage) {
        return damage * 2;
    }
}
