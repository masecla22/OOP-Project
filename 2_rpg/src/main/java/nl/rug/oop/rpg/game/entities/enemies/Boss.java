package nl.rug.oop.rpg.game.entities.enemies;

import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.entities.Enemy;
import nl.rug.oop.rpg.game.objects.Room;

/**
 * The final boss of the game.
 */
public class Boss extends Enemy {

    /**
     * Create a new boss.
     * 
     * @param game the game
     * @param room the room the boss is in
     */
    public Boss(Game game, Room room) {
        super(game, room, "The Final Boss of the Game.", "The Boss.", 5000, 30);
    }

    @Override
    public Boss copy(){
        return new Boss(this.getGame(), this.getRoom());
    }
}
