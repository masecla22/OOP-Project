package nl.rug.oop.rpg.game.behaviours;

/**
 * Interface for fightable objects.
 */
public interface Fightable {
    /**
     * Receive damage from an enemy.
     * 
     * @param from - the enemy
     * @param damage - the amount of damage
     */
    void receiveDamage(Fightable from, double damage);
    
    /**
     * Attack an enemy.
     * 
     * @param enemy - the enemy
     * @param damage - the amount of damage
     */
    default void attack(Fightable enemy, double damage) {
        enemy.receiveDamage(this, damage);
    }

    /**
     * Apply the next attack to an enemy.
     * 
     * @param enemy - the enemy
     * @return - the amount of damage
     */
    double applyNextAttack(Fightable enemy);

    /**
     * Get the health of this entity.
     * 
     * @return - the health
     */
    double getHealth();

    /**
     * Check if this entity is alive.
     * 
     * @return - true if alive
     */
    default boolean isAlive() {
        return getHealth() > 0;
    }
}
