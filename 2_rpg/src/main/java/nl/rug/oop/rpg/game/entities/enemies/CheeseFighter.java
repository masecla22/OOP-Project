package nl.rug.oop.rpg.game.entities.enemies;

import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.entities.Enemy;
import nl.rug.oop.rpg.game.objects.Room;

public class CheeseFighter extends Enemy {

    public CheeseFighter(Game game, Room room) {
        super(game, room, "A cheese fighter who seems overly confident", "Cheese Fighter", 30, 10);
    }
    
}
