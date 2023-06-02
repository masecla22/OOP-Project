package nl.rug.oop.rpg.game.items.modifiers;

import nl.rug.oop.rpg.game.items.ModifierItem;

/**
 * A talisman that doubles your damage and halves incoming damage.
 */
public class WarriorTalisman extends ModifierItem {

    /**
     * Create a new warrior talisman.
     */
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
