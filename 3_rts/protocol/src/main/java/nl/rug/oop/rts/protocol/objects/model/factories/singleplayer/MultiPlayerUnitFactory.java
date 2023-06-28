package nl.rug.oop.rts.protocol.objects.model.factories.singleplayer;

import java.util.ArrayList;
import java.util.List;

import nl.rug.oop.rts.protocol.objects.model.armies.Army;
import nl.rug.oop.rts.protocol.objects.model.armies.Faction;
import nl.rug.oop.rts.protocol.objects.model.factories.UnitFactory;
import nl.rug.oop.rts.protocol.objects.model.units.Unit;
import nl.rug.oop.rts.protocol.objects.model.units.UnitType;

/**
 * This class is responsible for creating armies and units for the multi player
 * mode.
 */
public class MultiPlayerUnitFactory extends UnitFactory {

    @Override
    public Army buildArmy(Faction faction) {
        int unitCount = 20;
        List<Unit> units = new ArrayList<>();
        for (int i = 0; i < unitCount; i++) {
            units.add(buildUnit(faction, i));
        }

        Army army = new Army(units, faction);
        return army;
    }

    @Override
    public Unit buildUnit(Faction faction, int index) {
        List<UnitType> types = faction.getAvailableUnits();
        UnitType type = types.get(index % types.size());

        List<String> names = type.getAvailableNames();
        String name = names.get(index % names.size());

        double health = (type.getMinHealth() + type.getMaxHealth()) / 2.0;
        double damage = (type.getMinDamage() + type.getMaxDamage()) / 2.0;

        return type.buildUnit(name, damage, health);
    }
}
