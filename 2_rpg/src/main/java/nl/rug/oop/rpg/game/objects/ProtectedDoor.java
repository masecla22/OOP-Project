package nl.rug.oop.rpg.game.objects;

import java.util.List;

import lombok.NonNull;
import nl.rug.oop.rpg.game.entities.Enemy;
import nl.rug.oop.rpg.game.entities.NPC;
import nl.rug.oop.rpg.game.player.Player;

public class ProtectedDoor extends Door {

    public ProtectedDoor(@NonNull String description) {
        this(description, null, null);
    }

    public ProtectedDoor(@NonNull String description, @NonNull Room source, @NonNull Room target) {
        super(description + ". It will not let you through unless all the enemies are dead!",
                source, target);
    }

    @Override
    public void interact(Player player) {
        List<NPC> enemies = this.getSource().getNpcs();
        if (enemies.stream().anyMatch(c -> c instanceof Enemy)) {
            System.out.println("You cannot go through this door until all the enemies are dead!");
        } else {
            super.interact(player);
        }
    }

}
