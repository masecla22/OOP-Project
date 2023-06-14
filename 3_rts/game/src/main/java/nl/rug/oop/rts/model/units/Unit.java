package nl.rug.oop.rts.model.units;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Unit {
    private UnitType type;

    private String name;

    private double damage;
    private double health;
}
