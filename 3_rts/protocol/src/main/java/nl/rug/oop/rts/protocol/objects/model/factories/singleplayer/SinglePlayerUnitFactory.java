package nl.rug.oop.rts.protocol.objects.model.factories.singleplayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nl.rug.oop.rts.protocol.objects.model.armies.Army;
import nl.rug.oop.rts.protocol.objects.model.armies.Faction;
import nl.rug.oop.rts.protocol.objects.model.factories.UnitFactory;
import nl.rug.oop.rts.protocol.objects.model.units.Unit;
import nl.rug.oop.rts.protocol.objects.model.units.UnitType;

public class SinglePlayerUnitFactory extends UnitFactory {
    private int minArmySize;
    private int maxArmySize;

    private Random random;

    public SinglePlayerUnitFactory(int seed) {
        random = new Random(seed);

        this.minArmySize = 10;
        this.maxArmySize = 50;
    }

    public Army buildArmy(Faction faction) {
        // We use 51
        int unitCount = random.nextInt(minArmySize, maxArmySize + 1);

        List<Unit> units = new ArrayList<>();
        for (int i = 0; i < unitCount; i++)
            units.add(buildUnit(faction, i));

        Army army = new Army(units, faction);
        return army;
    }

    public Unit buildUnit(Faction faction, int unit) {
        List<UnitType> types = faction.getAvailableUnits();
        UnitType type = types.get(random.nextInt(types.size()));

        List<String> names = type.getAvailableNames();
        String name = names.get(random.nextInt(names.size()));

        double health = random.nextDouble(type.getMinHealth(), type.getMaxHealth());
        double damage = random.nextDouble(type.getMinDamage(), type.getMaxDamage());

        return type.buildUnit(name, damage, health);
    }
}
