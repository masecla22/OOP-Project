package nl.rug.oop.rts.model.armies;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import nl.rug.oop.rts.model.units.Unit;

@Data
@AllArgsConstructor
public class Army {
    private List<Unit> units = new ArrayList<>();
    private Faction faction;

    @Override
    public String toString() {
        return faction.toString() + " army (" + units.size() + " units)";
    }
}
