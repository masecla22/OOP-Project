package nl.rug.oop.rts.protocol.objects.model.units;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents a unit.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Unit {
    private UnitType type;

    private String name;

    private double damage;
    private double health;

    /**
     * Deal damage to a unit.
     * 
     * @param unit - the unit
     * @return - the damage dealt
     */
    public abstract double dealDamage(Unit unit);
    
    /**
     * Take damage from a unit.
     * 
     * @param unit - the unit
     * @param damage - the damage
     */
    public abstract void takeDamage(Unit unit, double damage);
}
