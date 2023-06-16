package nl.rug.oop.rts.protocol.objects.model.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This class shows the different types of events that can occur in the game.
 */
@Getter
@AllArgsConstructor
public enum EventType {
    REINFORCEMENTS("Reinforcements (Increases army size by 50%)"),
    NATURAL_DISASTER("Natural Disaster (decimates 50% of units)"),
    HIDDEN_WEAPONRY("Hidden Weaponry (increases damage of all units by 1)");

    private String name;

    @Override
    public String toString() {
        return name;
    }
}
