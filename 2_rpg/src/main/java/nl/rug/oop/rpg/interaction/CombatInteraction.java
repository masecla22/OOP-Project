package nl.rug.oop.rpg.interaction;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.map.behaviours.Fightable;

@RequiredArgsConstructor
public class CombatInteraction {
    @NonNull
    private Game game;

    @NonNull
    private Fightable opponentOne;

    @NonNull
    private Fightable opponentTwo;

    private Runnable onWin;
    private Runnable onLoss = () -> {
        System.out.println("You died!");
        System.exit(0);
    };

    public CombatInteraction onWin(Runnable onWin) {
        this.onWin = onWin;
        return this;
    }

    public CombatInteraction onLoss(Runnable onLoss) {
        this.onLoss = onLoss;
        return this;
    }

    public void interact() {
        while (opponentOne.isAlive() && opponentTwo.isAlive()) {
            opponentOne.applyNextAttack(opponentTwo);
            if (opponentTwo.isAlive()) {
                opponentTwo.applyNextAttack(opponentOne);
            }
        }

        if (opponentOne.isAlive()) {
            onWin.run();
        } else {
            onLoss.run();
        }
    }
}
