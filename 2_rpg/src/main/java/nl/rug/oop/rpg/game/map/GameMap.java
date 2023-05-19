package nl.rug.oop.rpg.game.map;

import java.io.Serializable;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.entities.buffers.DamageWizard;
import nl.rug.oop.rpg.game.entities.buffers.HealingWizard;
import nl.rug.oop.rpg.game.entities.enemies.Boss;
import nl.rug.oop.rpg.game.entities.enemies.CheeseFighter;
import nl.rug.oop.rpg.game.entities.enemies.Goblin;
import nl.rug.oop.rpg.game.entities.enemies.Orc;
import nl.rug.oop.rpg.game.entities.merchants.CheeseSeller;
import nl.rug.oop.rpg.game.entities.merchants.FoodSeller;
import nl.rug.oop.rpg.game.entities.merchants.TalismanSeller;
import nl.rug.oop.rpg.game.objects.Door;
import nl.rug.oop.rpg.game.objects.ProtectedDoor;
import nl.rug.oop.rpg.game.objects.Room;
import nl.rug.oop.rpg.game.objects.SpikedDoor;

@Data
@RequiredArgsConstructor
public class GameMap implements Serializable {
    /** Serial version ID. */
    private static final long serialVersionUID = 2430987560976l;

    private Room initialRoom;

    @NonNull
    private transient Game game;

    /**
     * This is where the entire map of the game is laid out
     */
    public void initialize() {
        // Setup the initial room
        initialRoom = new Room("A rather dusty room full of computers!");

        // The game will have the following rooms:
        // - The cheese room
        // - The room with a boss
        // - 4 rooms with enemies
        // - The replenishing room (which will have 2 merchants and 2 buffers)

        Room cheeseRoom = new Room("A room with a suspicious amount of cheese!");

        Room bossRoom = new Room("The final room of the game!");

        Room enemyRoom1 = new Room("A room with a couple enemies!");
        Room enemyRoom2 = new Room("A room with some enemies!");
        Room enemyRoom3 = new Room("A room with an amount of enemies!");
        Room enemyRoom4 = new Room("A room with a handful of enemies!");

        Room replenishingRoom = new Room("A room with merchants and buffers!");

        // The connections are as follows
        // - The initial room has a door to the cheese room
        // - The cheese room leads into the first enemy room with a simple door
        // - The first enemy room leads into the second enemy room with a spiked door
        // - The second enemy room leads into the replenishing room with a protected
        // door
        // - The replenishing room leads into the third enemy room with a simple door
        // - The third enemy room leads into the fourth enemy room with a protected door
        // - The fourth enemy room leads into the boss room with a spiked door

        Door initialToCheese = new Door("A door to the cheese room", initialRoom, cheeseRoom);
        Door cheeseToInitial = new Door("A door to the initial room", cheeseRoom, initialRoom);

        Door cheeseToEnemy1 = new Door("A door to the first enemy room", cheeseRoom, enemyRoom1);
        Door enemy1ToCheese = new Door("A door to the cheese room", enemyRoom1, cheeseRoom);

        Door enemy1ToEnemy2 = new SpikedDoor("A spiked door to the second enemy room", enemyRoom1, enemyRoom2);
        Door enemy2ToEnemy1 = new Door("A door to the first enemy room", enemyRoom2, enemyRoom1);

        Door enemy2ToReplenishing = new ProtectedDoor("A door to the replenishing room", enemyRoom2, replenishingRoom);
        Door replenishingToEnemy2 = new Door("A door to the second enemy room", replenishingRoom, enemyRoom2);

        Door replenishingToEnemy3 = new Door("A door to the third enemy room", replenishingRoom, enemyRoom3);
        Door enemy3ToReplenishing = new Door("A door to the replenishing room", enemyRoom3, replenishingRoom);

        Door enemy3ToEnemy4 = new ProtectedDoor("A door to the fourth enemy room", enemyRoom3, enemyRoom4);
        Door enemy4ToEnemy3 = new Door("A door to the third enemy room", enemyRoom4, enemyRoom3);

        Door enemy4ToBoss = new SpikedDoor("A spiked door to the boss room", enemyRoom4, bossRoom);
        Door bossToEnemy4 = new Door("A door to the fourth enemy room", bossRoom, enemyRoom4);

        // Add the doors to the rooms
        initialRoom.addDoor(initialToCheese);
        cheeseRoom.addDoor(cheeseToInitial);
        cheeseRoom.addDoor(cheeseToEnemy1);
        enemyRoom1.addDoor(enemy1ToCheese);
        enemyRoom1.addDoor(enemy1ToEnemy2);
        enemyRoom2.addDoor(enemy2ToEnemy1);
        enemyRoom2.addDoor(enemy2ToReplenishing);
        replenishingRoom.addDoor(replenishingToEnemy2);
        replenishingRoom.addDoor(replenishingToEnemy3);
        enemyRoom3.addDoor(enemy3ToReplenishing);
        enemyRoom3.addDoor(enemy3ToEnemy4);
        enemyRoom4.addDoor(enemy4ToEnemy3);
        enemyRoom4.addDoor(enemy4ToBoss);
        bossRoom.addDoor(bossToEnemy4);

        // Populate map with entities
        // - The cheese room will have a cheese fighter and a cheese seller
        // - The first enemy room will have 2 orcs
        // - The second enemy room will have 2 orcs and a goblin
        // - The third enemy room will have 5 orcs and 3 goblins
        // - The fourth enemy room will have 10 orcs, 5 goblins and 3 cheese fighters
        // - The boss room will have the boss
        // - The replenishing room will have the FoodSeller and the TalismanSeller
        // - The replenishing room will also have DamageWizard and HealthWizard

        // Cheese room
        CheeseFighter cheeseRoomFighter = new CheeseFighter(game, cheeseRoom);
        cheeseRoom.addNPC(cheeseRoomFighter);

        CheeseSeller cheeseRoomSeller = new CheeseSeller(game, cheeseRoom);
        cheeseRoom.addNPC(cheeseRoomSeller);

        // First enemy room
        for (int i = 0; i < 2; i++)
            enemyRoom1.addNPC(new Orc(game, enemyRoom1));

        // Second enemy room
        for (int i = 0; i < 2; i++)
            enemyRoom2.addNPC(new Orc(game, enemyRoom2));
        enemyRoom2.addNPC(new Goblin(game, enemyRoom2));

        // Third enemy room
        for (int i = 0; i < 5; i++)
            enemyRoom3.addNPC(new Orc(game, enemyRoom3));
        for (int i = 0; i < 3; i++)
            enemyRoom3.addNPC(new Goblin(game, enemyRoom3));

        // Fourth enemy room
        for (int i = 0; i < 10; i++)
            enemyRoom4.addNPC(new Orc(game, enemyRoom4));
        for (int i = 0; i < 5; i++)
            enemyRoom4.addNPC(new Goblin(game, enemyRoom4));

        // Boss room
        bossRoom.addNPC(new Boss(game, bossRoom));

        // Replenishing room
        FoodSeller foodSeller = new FoodSeller(game, replenishingRoom);
        replenishingRoom.addNPC(foodSeller);

        TalismanSeller talismanSeller = new TalismanSeller(game, replenishingRoom);
        replenishingRoom.addNPC(talismanSeller);

        DamageWizard damageWizard = new DamageWizard(game, replenishingRoom);
        replenishingRoom.addNPC(damageWizard);

        HealingWizard healingWizard = new HealingWizard(game, replenishingRoom);
        replenishingRoom.addNPC(healingWizard);
    }
}
