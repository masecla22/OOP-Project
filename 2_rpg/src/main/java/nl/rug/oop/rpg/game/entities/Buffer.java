package nl.rug.oop.rpg.game.entities;

import lombok.NonNull;
import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.objects.Room;
import nl.rug.oop.rpg.game.player.Player;

/**
 * A buffer is an entity that can apply effects to the player.
 */
public abstract class Buffer extends NPC {

    /**
     * Create a new buffer.
     * 
     * @param game        the game
     * @param room        the room the buffer is in
     * @param description the description of the buffer
     * @param name        the name of the buffer
     */
    public Buffer(@NonNull Game game, @NonNull Room room,
            @NonNull String description, @NonNull String name) {
        super(game, room, description, name);
    }

    /**
     * Apply the effects of the buffer to the player.
     * 
     * @param player the player
     */
    public abstract void applyEffects(Player player);

    /**
     * Get the cost of the buffer.
     * 
     * @param player the player
     * @return the cost of the buffer
     */
    public abstract int getCost(Player player);

    /**
     * Get the dialog of the buffer.
     * 
     * @return the dialog of the buffer
     */
    public abstract String getDialog();

    @Override
    public void inspect() {
        System.out.println(this.getDescription());
    }

    @Override
    public void interact(Player player) {
        this.getGame().newInteraction()
                .option("Buy the effect", () -> {
                    int cost = this.getCost(player);
                    if (player.getGold() >= cost) {
                        this.applyEffects(player);
                        player.removeGold(this.getCost(player));
                    } else {
                        System.out.println("You don't have enough gold to buy this effect.");
                    }
                })
                .prompt("I am " + this.getName() + "\n" + this.getDialog())
                .cursor("Type option (-1: exit shop)")
                .interact();
    }
}
