package nl.rug.oop.rpg.game.entities.enemies;

import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.entities.Enemy;
import nl.rug.oop.rpg.game.objects.Room;

/**
 * A cheese fighter. A fighter made of cheese.
 */
public class CheeseFighter extends Enemy {

    /**
     * Create a new cheese fighter.
     * 
     * @param game the game
     * @param room the room the cheese fighter is in
     */
    public CheeseFighter(Game game, Room room) {
        super(game, room, "A cheese fighter who seems overly confident", "Cheese Fighter", 30, 10);
    }

    @Override
    public CheeseFighter copy() {
        return new CheeseFighter(this.getGame(), this.getRoom());
    }
}
