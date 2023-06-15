package nl.rug.oop.rts.model.armies;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NonNull;
import nl.rug.oop.rts.model.Node;
import nl.rug.oop.rts.model.units.Unit;

@Data
public class Army {
    private List<Unit> units = new ArrayList<>();
    private Faction faction;

    public Army(@NonNull List<Unit> units, @NonNull Faction faction) {
        this.units = units;
        this.faction = faction;
    }

    private Node movingToNextStep;
    private boolean moved = false;

    @Override
    public String toString() {
        return faction.toString() + " army (" + units.size() + " units)";
    }
}
