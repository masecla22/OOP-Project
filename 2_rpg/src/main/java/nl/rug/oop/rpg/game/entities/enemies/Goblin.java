package nl.rug.oop.rpg.game.entities.enemies;

import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.entities.Enemy;
import nl.rug.oop.rpg.game.objects.Room;

public class Goblin extends Enemy {
    
    public Goblin(Game game, Room room) {
        super(game, room, "A goblin, small but deals a lot of damage", "Orc", 30, 50);
    }

}
