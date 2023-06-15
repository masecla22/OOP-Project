package nl.rug.oop.rts.model.units;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Unit {
    private UnitType type;

    private String name;

    private double damage;
    private double health;

    public abstract double dealDamage(Unit unit);
    public abstract void takeDamage(Unit unit, double damage);
}
