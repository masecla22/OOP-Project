package nl.rug.oop.rpg.game.entities;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.NonNull;
import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.items.Item;
import nl.rug.oop.rpg.game.objects.Room;
import nl.rug.oop.rpg.game.player.Player;
import nl.rug.oop.rpg.interaction.DialogInteraction;

public class Merchant extends NPC {

    /**
     * These are all the items the merchant sells. The value is how much the item
     * costs.
     * 
     * We use an array to allow for duplicated items being sold by the same NPC
     */
    private List<Map.Entry<Item, Integer>> sellableItems = new ArrayList<>();

    public Merchant(@NonNull Game game, @NonNull Room room,
            @NonNull String description, @NonNull String name) {
        super(game, room, description, name);
    }

    protected void addToInventory(Item item, int cost) {
        sellableItems.add(new SimpleEntry<>(item, cost));
    }

    @Override
    public void interact(Player player) {
        buildShopInteraction(player).interact();
    }

    @Override
    public void inspect() {
        System.out.println("Seems to be a merchant with a couple things on him!");
    }

    private DialogInteraction buildShopInteraction(Player player) {
        DialogInteraction interaction = this.getGame().newInteraction()
                .prompt("Welcome to my shop! I am " + this.getName() + "\n" +
                        this.getDescription() + " What would you like to buy?")
                .cursor("Type option (-1: exit shop)");

        for (int i = 0; i < sellableItems.size(); i++) {
            Map.Entry<Item, Integer> shopEntry = sellableItems.get(i);
            Item item = shopEntry.getKey();
            int price = shopEntry.getValue();

            // This is required due to how lambdas in Java work
            final int j = i;
            interaction.option(item.getName() + " - " + price + " gold",
                    () -> handleItemPurchase(player, item, price, j));
        }

        return interaction;
    }

    private void handleItemPurchase(Player player, Item item, int cost, int index) {
        if (player.getGold() < cost) {
            System.out.println("You don't have enough gold to be able to purchase this!");
            interact(player);
            return;
        }

        this.getGame().newInteraction().prompt("Are you sure you want to buy this?")
                .option("Confirm purchase", () -> {
                    player.setGold(player.getGold() - cost);
                    player.getInventory().addItem(item);
                    this.sellableItems.remove(index);
                    interact(player);
                })
                .option("Nevermind", () -> interact(player))
                .interact();
    }
}
