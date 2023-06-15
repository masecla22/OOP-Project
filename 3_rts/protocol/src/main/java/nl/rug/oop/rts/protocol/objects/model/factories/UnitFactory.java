package nl.rug.oop.rts.protocol.objects.model.factories;

import nl.rug.oop.rts.protocol.objects.model.armies.Army;
import nl.rug.oop.rts.protocol.objects.model.armies.Faction;
import nl.rug.oop.rts.protocol.objects.model.units.Unit;

public abstract class UnitFactory {
    public abstract Army buildArmy(Faction faction);
    public abstract Unit buildUnit(Faction faction, int index);
}
