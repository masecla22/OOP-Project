package nl.rug.oop.rpg.interaction;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.rug.oop.rpg.Game;
import nl.rug.oop.rpg.game.behaviours.Fightable;

/**
 * Represents a combat interaction menu.
 */
@RequiredArgsConstructor
public class CombatInteraction {
    /** The game of this interaction. */
    @NonNull
    private Game game;

    /** The first opponent. */
    @NonNull
    private Fightable opponentOne;

    /** The second opponent. */
    @NonNull
    private Fightable opponentTwo;

    /** The runnable to run when the player wins. */
    private Runnable onWin;

    /** The runnable to run when the player loses. */
    private Runnable onLoss = () -> {
        System.out.println("You died!");
        System.exit(0);
    };

    /**
     * Sets the onWin runnable.
     * 
     * @param onWin - the runnable to run when the player wins
     * @return - this
     */
    public CombatInteraction onWin(Runnable onWin) {
        this.onWin = onWin;
        return this;
    }

    /**
     * Sets the onLoss runnable.
     * 
     * @param onLoss - the runnable to run when the player loses
     * @return - this
     */
    public CombatInteraction onLoss(Runnable onLoss) {
        this.onLoss = onLoss;
        return this;
    }

    /**
     * Runs the interaction.
     */
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
