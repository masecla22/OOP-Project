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

/**
 * This class represents the map of the game.
 */
@Data
@RequiredArgsConstructor
public class GameMap implements Serializable {
    /** Serial version ID. */
    private static final long serialVersionUID = 2430987560976l;

    private Room initialRoom;

    @NonNull
    private Game game;

    /**
     * This is where the entire map of the game is laid out.
     */
    public void initialize() {
        initialRoom = new Room("A rather dusty room full of computers!");
        Room cheeseRoom = new Room("A room with a suspicious amount of cheese!"),
                bossRoom = new Room("The final room of the game!"),
                enemyRoom1 = new Room("A room with a couple enemies!"),
                enemyRoom2 = new Room("A room with some enemies!"),
                enemyRoom3 = new Room("A room with an amount of enemies!"),
                enemyRoom4 = new Room("A room with a handful of enemies!"),
                replenishingRoom = new Room("A room with merchants and buffers!");

        // No need to keep references to the doors, as they are stored in the rooms
        new Door("A door to the cheese room", initialRoom, cheeseRoom);
        new Door("A door to the initial room", cheeseRoom, initialRoom);
        new Door("A door to the first enemy room", cheeseRoom, enemyRoom1);
        new Door("A door to the cheese room", enemyRoom1, cheeseRoom);
        new SpikedDoor("A spiked door to the second enemy room", enemyRoom1, enemyRoom2);
        new Door("A door to the first enemy room", enemyRoom2, enemyRoom1);
        new ProtectedDoor("A door to the replenishing room", enemyRoom2, replenishingRoom);
        new Door("A door to the second enemy room", replenishingRoom, enemyRoom2);
        new Door("A door to the third enemy room", replenishingRoom, enemyRoom3);
        new Door("A door to the replenishing room", enemyRoom3, replenishingRoom);
        new ProtectedDoor("A door to the fourth enemy room", enemyRoom3, enemyRoom4);
        new Door("A door to the third enemy room", enemyRoom4, enemyRoom3);
        new SpikedDoor("A spiked door to the boss room", enemyRoom4, bossRoom);
        new Door("A door to the fourth enemy room", bossRoom, enemyRoom4);

        populateCheeseRoom(cheeseRoom);
        populateFirstEnemyRoom(enemyRoom1);
        populateSecondEnemyRoom(enemyRoom2);
        populateThirdEnemyRoom(enemyRoom3);
        populateFourthEnemyRoom(enemyRoom4);
        bossRoom.addNPC(new Boss(game, bossRoom));
        populateReplenishingRoom(replenishingRoom);
    }

    private void populateReplenishingRoom(Room replenishingRoom) {
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

    private void populateCheeseRoom(Room cheeseRoom) {
        // Cheese room
        CheeseFighter cheeseRoomFighter = new CheeseFighter(game, cheeseRoom);
        cheeseRoom.addNPC(cheeseRoomFighter);

        CheeseSeller cheeseRoomSeller = new CheeseSeller(game, cheeseRoom);
        cheeseRoom.addNPC(cheeseRoomSeller);
    }

    private void populateFirstEnemyRoom(Room enemyRoom1) {
        // First enemy room
        for (int i = 0; i < 2; i++) {
            enemyRoom1.addNPC(new Orc(game, enemyRoom1));
        }
    }

    private void populateSecondEnemyRoom(Room enemyRoom2) {
        // Second enemy room
        for (int i = 0; i < 2; i++) {
            enemyRoom2.addNPC(new Orc(game, enemyRoom2));
        }
        enemyRoom2.addNPC(new Goblin(game, enemyRoom2));
    }

    private void populateThirdEnemyRoom(Room enemyRoom3) {
        // Third enemy room
        for (int i = 0; i < 5; i++) {
            enemyRoom3.addNPC(new Orc(game, enemyRoom3));
        }
        for (int i = 0; i < 3; i++) {
            enemyRoom3.addNPC(new Goblin(game, enemyRoom3));
        }
    }

    private void populateFourthEnemyRoom(Room enemyRoom4) {
        // Fourth enemy room
        for (int i = 0; i < 10; i++) {
            enemyRoom4.addNPC(new Orc(game, enemyRoom4));
        }
        for (int i = 0; i < 5; i++) {
            enemyRoom4.addNPC(new Goblin(game, enemyRoom4));
        }
    }
}
