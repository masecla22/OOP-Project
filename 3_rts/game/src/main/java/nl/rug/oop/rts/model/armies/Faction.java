package nl.rug.oop.rts.model.armies;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.rug.oop.rts.model.units.UnitType;

@Getter
@AllArgsConstructor
public enum Faction {
    MEN("Men", Team.TEAM_A, "factionMen",
            List.of(UnitType.GONDOR_SOLDIER, UnitType.ITHILIEN_RANGER, UnitType.TOWER_GUARD)),
    ELVES("Elves", Team.TEAM_A, "factionElves",
            List.of(UnitType.LORIEN_WARRIOR, UnitType.MIRKWOOD_ARCHER, UnitType.RIVENDELL_LANCER)),
    DWARVES("Dwarves", Team.TEAM_A, "factionDwarves",
            List.of(UnitType.AXE_THROWER, UnitType.GUARDIAN, UnitType.PHALANX)),
    MORDOR("Mordor", Team.TEAM_B, "factionMordor",
            List.of(UnitType.HARADRIM_ARCHER, UnitType.ORC_PIKEMAN, UnitType.ORC_WARRIOR)),
    ISENGARD("Isengard", Team.TEAM_B, "factionIsengard",
            List.of(UnitType.URUK_CROSSBOW_MAN, UnitType.URUK_HAI, UnitType.WARG_RIDER)),
    ROHAN("Rohan", Team.TEAM_B, "factionRohan",
            List.of(UnitType.ROHAN_ARCHER, UnitType.ROHAN_SPEARMAN, UnitType.ROHAN_SWORDSMAN));

    private String niceName;
    private Team team;

    private String texture;

    private List<UnitType> availableUnits;

    @Override
    public String toString() {
        return niceName;
    }
}
