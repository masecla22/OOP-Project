package nl.rug.oop.rpg.game.entities.enemies;

import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.entities.Enemy;
import nl.rug.oop.rpg.game.objects.Room;

/**
 * A goblin, small but deals a lot of damage.
 */
public class Goblin extends Enemy {
   
    /**
     * Create a new goblin.
     * 
     * @param game the game
     * @param room the room the goblin is in
     */
    public Goblin(Game game, Room room) {
        super(game, room, "A goblin, small but deals a lot of damage", "Orc", 30, 50);
    }

}
