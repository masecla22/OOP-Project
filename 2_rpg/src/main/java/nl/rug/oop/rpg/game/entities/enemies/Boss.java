package nl.rug.oop.rpg.game.entities.enemies;

import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.entities.Enemy;
import nl.rug.oop.rpg.game.objects.Room;

public class Boss extends Enemy {

    public Boss(Game game, Room room) {
        super(game, room, "The Final Boss of the Game.", "The Boss.", 5000, 30);
    }

}
