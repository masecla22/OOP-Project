package nl.rug.oop.rpg.game.items;

/**
 * An item that modifies damage or healing.
 */
public abstract class ModifierItem extends Item {

    /**
     * Create a new modifier item.
     * 
     * @param name        the name of the item
     * @param description the description of the item
     */
    public ModifierItem(String name, String description) {
        super(name, description);
    }

    /**
     * Modify the damage of an outgoing attack.
     * 
     * @param damage the damage to modify
     * @return the modified damage
     */
    public double modifyOutgoingDamage(double damage) {
        return damage;
    }

    /**
     * Modify the damage of incoming attack.
     * 
     * @param damage the damage to modify
     * @return the modified damage
     */
    public double modifyIncomingDamage(double damage) {
        return damage;
    }

    /**
     * Modify the incoming healing.
     * 
     * @param healing the healing to modify
     * @return the modified healing
     */
    public double modifyIncomingHealing(double healing) {
        return healing;
    }
}
