package nl.rug.oop.rpg.game.entities;

import lombok.NonNull;
import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.objects.Room;
import nl.rug.oop.rpg.game.player.Player;

public abstract class Buffer extends NPC {

    public Buffer(@NonNull Game game, @NonNull Room room,
            @NonNull String description, @NonNull String name) {
        super(game, room, description, name);
    }

    public abstract void applyEffects(Player player);

    public abstract int getCost(Player player);

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
