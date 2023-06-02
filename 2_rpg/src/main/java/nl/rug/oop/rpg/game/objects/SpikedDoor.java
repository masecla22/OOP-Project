package nl.rug.oop.rpg.game.objects;

import lombok.NonNull;
import nl.rug.oop.rpg.game.player.Player;

/**
 * A spiked door. You will take damage if you walk through it.
 */
public class SpikedDoor extends Door {
    /**
     * Create a new spiked door.
     * 
     * @param description the description of the door
     * @param source      the room the door is in
     * @param target      the room the door leads to
     */
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
