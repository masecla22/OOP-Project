package nl.rug.oop.rpg.game.objects;

import lombok.NonNull;
import nl.rug.oop.rpg.game.player.Player;

public class SpikedDoor extends Door {
    public SpikedDoor(@NonNull String description) {
        this(description, null, null);
    }

    public SpikedDoor(@NonNull String description, @NonNull Room source, @NonNull Room target) {
        super(description + ". You will take 7 damage if you walk through this door!",
                source, target);
    }

    @Override
    public void interact(Player player) {
        player.receiveDamage(null, 7);
        super.interact(player);
    }
}
