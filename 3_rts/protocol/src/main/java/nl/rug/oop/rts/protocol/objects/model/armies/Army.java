package nl.rug.oop.rts.protocol.objects.model.armies;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import nl.rug.oop.rts.protocol.objects.model.Node;
import nl.rug.oop.rts.protocol.objects.model.units.Unit;

/**
 * An army is a group of units.
 */
@Data
@NoArgsConstructor
public class Army {
    private List<Unit> units = new ArrayList<>();
    private Faction faction;

    private transient Node movingToNextStep;
    private transient boolean moved = false;

    /**
     * Create a new army.
     * 
     * @param units   - the units
     * @param faction - the faction
     */
    public Army(@NonNull List<Unit> units, @NonNull Faction faction) {
        this.units = units;
        this.faction = faction;
    }

    @Override
    public String toString() {
        return faction.toString() + " army (" + units.size() + " units)";
    }

    /**
     * Check if this army is empty.
     * 
     * @return - true if empty, false otherwise
     */
    public boolean isEmpty() {
        return units.isEmpty();
    }
}
