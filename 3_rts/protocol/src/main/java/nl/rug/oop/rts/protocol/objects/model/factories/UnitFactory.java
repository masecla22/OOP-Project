package nl.rug.oop.rts.protocol.objects.model.factories;

import nl.rug.oop.rts.protocol.objects.model.armies.Army;
import nl.rug.oop.rts.protocol.objects.model.armies.Faction;
import nl.rug.oop.rts.protocol.objects.model.units.Unit;

/**
 * Abstract class for the unit factory.
 */
public abstract class UnitFactory {
    /**
     * Builds an army.
     * 
     * @param faction - faction
     * @return army
     */
    public abstract Army buildArmy(Faction faction);

    /**
     * Builds a single unit.
     * 
     * @param faction - The faction
     * @param index   - The index of the unit in the army
     * @return unit - The unit
     */
    public abstract Unit buildUnit(Faction faction, int index);
}
