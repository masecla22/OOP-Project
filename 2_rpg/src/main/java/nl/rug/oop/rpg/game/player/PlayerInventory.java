package nl.rug.oop.rpg.game.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.items.ConsumableItem;
import nl.rug.oop.rpg.game.items.Item;
import nl.rug.oop.rpg.interaction.DialogInteraction;

/**
 * The player's inventory.
 */
@NoArgsConstructor
@AllArgsConstructor
public class PlayerInventory implements Serializable {
    /** Serial version ID. */
    private static final long serialVersionUID = 32087650123865l;

    @NonNull
    private Game game;

    @Getter
    private List<Item> items = new ArrayList<>();

    /**
     * Adds an item to the inventory.
     * 
     * @param item - the item to add
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Removes an item from the inventory.
     * 
     * @param item - the item to remove
     */
    public void removeItem(Item item) {
        items.remove(item);
    }

    /**
     * Converts the inventory to a string so
     * it can be printed.
     */
    @Override
    public String toString() {
        StringBuilder inventory = new StringBuilder();

        inventory.append("Inventory:\n");
        for (Item item : items) {
            inventory.append("- " + item.getName() + "\n");
        }
        if (items.isEmpty()) {
            inventory.append("- Empty\n");
        }

        return inventory.toString();
    }

    /**
     * Handles the use of an item.
     * 
     * @param player - the player using the item
     */
    public void handleItemUse(Player player) {
        if (items.isEmpty()) {
            System.out.println("You have no items to use");
            return;
        }

        DialogInteraction interaction = this.game.newInteraction().prompt("Which item do you want to use?");

        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            interaction.option(item.getName(), () -> {
                if (item instanceof ConsumableItem consumable) {
                    consumable.conusme(player);
                } else {
                    System.out.println("You can't use this item");
                }
            });
        }

        interaction.interact();
    }
}
