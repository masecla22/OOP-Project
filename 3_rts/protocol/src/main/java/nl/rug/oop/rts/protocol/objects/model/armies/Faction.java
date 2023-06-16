package nl.rug.oop.rts.protocol.objects.model.armies;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.rug.oop.rts.protocol.objects.model.units.UnitType;

@Getter
@AllArgsConstructor
public enum Faction {
    MEN("Men", Team.TEAM_A, "factionMen", 100,
            List.of(UnitType.GONDOR_SOLDIER, UnitType.ITHILIEN_RANGER, UnitType.TOWER_GUARD)),
    ELVES("Elves", Team.TEAM_A, "factionElves", 100,
            List.of(UnitType.LORIEN_WARRIOR, UnitType.MIRKWOOD_ARCHER, UnitType.RIVENDELL_LANCER)),
    DWARVES("Dwarves", Team.TEAM_A, "factionDwarves", 100,
            List.of(UnitType.AXE_THROWER, UnitType.GUARDIAN, UnitType.PHALANX)),
    MORDOR("Mordor", Team.TEAM_B, "factionMordor", 100,
            List.of(UnitType.HARADRIM_ARCHER, UnitType.ORC_PIKEMAN, UnitType.ORC_WARRIOR)),
    ISENGARD("Isengard", Team.TEAM_B, "factionIsengard", 100,
            List.of(UnitType.URUK_CROSSBOW_MAN, UnitType.URUK_HAI, UnitType.WARG_RIDER)),
    ROHAN("Rohan", Team.TEAM_B, "factionRohan", 100,
            List.of(UnitType.ROHAN_ARCHER, UnitType.ROHAN_SPEARMAN, UnitType.ROHAN_SWORDSMAN));

    private String niceName;
    private Team team;

    private String texture;

    private int cost;

    private List<UnitType> availableUnits;

    @Override
    public String toString() {
        return niceName;
    }
}
