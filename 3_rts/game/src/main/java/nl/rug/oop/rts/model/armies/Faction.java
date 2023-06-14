package nl.rug.oop.rts.model.armies;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.rug.oop.rts.model.units.UnitType;

@Getter
@AllArgsConstructor
public enum Faction {
    MEN("Men", "TeamA", Set.of(UnitType.GONDOR_SOLDIER, UnitType.ITHILIEN_RANGER, UnitType.TOWER_GUARD)),
    ELVES("Elves", "TeamA", Set.of(UnitType.LORIEN_WARRIOR, UnitType.MIRKWOOD_ARCHER, UnitType.RIVENDELL_LANCER)),
    DWARVES("Dwarves", "TeamA", Set.of(UnitType.AXE_THROWER, UnitType.GUARDIAN, UnitType.PHALANX)),
    MORDOR("Mordor", "TeamB", Set.of(UnitType.HARADRIM_ARCHER, UnitType.ORC_PIKEMAN, UnitType.ORC_WARRIOR)),
    ISENGARD("Isengard", "TeamB", Set.of(UnitType.URUK_CROSSBOW_MAN, UnitType.URUK_HAI, UnitType.WARG_RIDER));

    private String niceName;
    private String team;

    private Set<UnitType> availableUnits;

    @Override
    public String toString() {
        return niceName;
    }
}
