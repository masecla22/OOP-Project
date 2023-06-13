package nl.rug.oop.rts.model.armies;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Faction {
    MEN("Men", "TeamA"),
    ELVES("Elves", "TeamA"),
    DWARVES("Dwarves", "TeamA"),
    MORDOR("Mordor", "TeamB"),
    ISENGARD("Isengard", "TeamB");

    private String niceName;

    private String team;
}
