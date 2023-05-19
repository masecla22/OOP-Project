package nl.rug.oop.rpg.game.items;

public abstract class ModifierItem extends Item {

    public ModifierItem(String name, String description) {
        super(name, description);
    }

    public double modifyOutgoingDamage(double damage) {
        return damage;
    }

    public double modifyIncomingDamage(double damage){
        return damage;
    }

    public double modifyIncomingHealing(double healing){
        return healing;
    }
}
