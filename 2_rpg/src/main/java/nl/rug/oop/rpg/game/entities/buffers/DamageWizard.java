package nl.rug.oop.rpg.game.entities.buffers;

import lombok.NonNull;
import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.entities.Buffer;
import nl.rug.oop.rpg.game.objects.Room;
import nl.rug.oop.rpg.game.player.Player;

/**
 * A wizard that can increase your base damage.
 */
public class DamageWizard extends Buffer {

    /**
     * Create a new damage wizard.
     * 
     * @param game the game
     * @param room the room the damage wizard is in
     */
    public DamageWizard(@NonNull Game game, @NonNull Room room) {
        super(game, room, "A wizard that can increase your base damage", "Damage Wizard");
    }

    @Override
    public void applyEffects(Player player) {
        player.setDamage(player.getDamage() + 20);
    }

    @Override
    public int getCost(Player player) {
        return 20;
    }

    @Override
    public String getDialog() {
        return "I can increase your base damage for 20 gold";
    }

}
