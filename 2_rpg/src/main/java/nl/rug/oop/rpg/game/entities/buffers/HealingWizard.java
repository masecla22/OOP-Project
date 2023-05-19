package nl.rug.oop.rpg.game.entities.buffers;

import lombok.NonNull;
import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.entities.Buffer;
import nl.rug.oop.rpg.game.objects.Room;
import nl.rug.oop.rpg.game.player.Player;

public class HealingWizard extends Buffer {

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
}
