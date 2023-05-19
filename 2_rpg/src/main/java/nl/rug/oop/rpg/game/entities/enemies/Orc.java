package nl.rug.oop.rpg.game.entities.enemies;

import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.entities.Enemy;
import nl.rug.oop.rpg.game.objects.Room;

public class Orc extends Enemy {

    public Orc(Game game, Room room) {
        super(game, room, "An orc, a fearsome warrior", "Orc", 50, 10);
    }
    
}
