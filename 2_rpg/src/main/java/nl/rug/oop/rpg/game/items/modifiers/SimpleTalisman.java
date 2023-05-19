package nl.rug.oop.rpg.game.items.modifiers;

import nl.rug.oop.rpg.game.items.ModifierItem;

/**
 * A simple talisman. Increases damage and healing by 1.5
 */
public class SimpleTalisman extends ModifierItem {

    /**
     * Create a new simple talisman.
     */
    public SimpleTalisman() {
        super("Simple Talisman",
                "A beautiful gold chain with a little trinket in the middle. x1.5 damage and x1.5 healing effects");
    }

    @Override
    public double modifyOutgoingDamage(double damage) {
        return damage * 1.5;
    }

    @Override
    public double modifyIncomingHealing(double healing) {
        return healing * 1.5;
    }
}
