package nl.rug.oop.rpg.game.player;

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

@NoArgsConstructor
@AllArgsConstructor
public class PlayerInventory {
    @NonNull
    private transient Game game;

    @Getter
    private List<Item> items = new ArrayList<>();

    public void addItem(Item item) {
        items.add(item);
    }

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

    public void handleItemUse(Player player) {
        if (items.isEmpty()) {
            System.out.println("You have no items to use");
            return;
        }

        DialogInteraction interaction = this.game.newInteraction().prompt("Which item do you want to use?");

        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            interaction.option(item.getName(), () -> {
                if(item instanceof ConsumableItem consumable){
                    consumable.conusme(player);
                } else {
                    System.out.println("You can't use this item");
                }
            });
        }

        interaction.interact();
    }
}
