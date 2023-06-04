package nl.rug.oop.rpg.game.entities.buffers;

import lombok.NonNull;
import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.entities.Buffer;
import nl.rug.oop.rpg.game.objects.Room;
import nl.rug.oop.rpg.game.player.Player;

/**
 * A wizard that can heal you.
 */
public class HealingWizard extends Buffer {

    /**
     * Create a new healing wizard.
     * 
     * @param game the game
     * @param room the room the healing wizard is in
     */
    public HealingWizard(@NonNull Game game, @NonNull Room room) {
        super(game, room, "A wizard that can heal you", "Healing Wizard");
    }

    @Override
    public void applyEffects(Player player) {
        player.heal(30);
    }

    @Override
    public int getCost(Player player) {
        return 50;
    }

    @Override
    public String getDialog() {
        return "I can heal you for 50 gold";
    }

    @Override
    public HealingWizard copy() {
        return new HealingWizard(this.getGame(), this.getRoom());
    }
}
