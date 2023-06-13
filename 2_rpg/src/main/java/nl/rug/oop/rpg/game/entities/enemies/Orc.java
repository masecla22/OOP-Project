package nl.rug.oop.rpg.game.entities.enemies;

import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.entities.Enemy;
import nl.rug.oop.rpg.game.objects.Room;

/**
 * An orc. Large grotesque humanoid.
 */
public class Orc extends Enemy {

    /**
     * Create a new orc.
     * 
     * @param game the game
     * @param room the room the orc is in
     */
    public Orc(Game game, Room room) {
        super(game, room, "An orc, a fearsome warrior", "Orc", 50, 10);
    }
    
}
