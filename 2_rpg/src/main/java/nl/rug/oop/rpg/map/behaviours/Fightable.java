package nl.rug.oop.rpg.map.behaviours;

public interface Fightable {
    public void receiveDamage(Fightable from, double damage);
    
    public default void attack(Fightable enemy, double damage) {
        enemy.receiveDamage(this, damage);
    }

    public double applyNextAttack(Fightable enemy);

    public double getHealth();

    public default boolean isAlive() {
        return getHealth() > 0;
    }
}
