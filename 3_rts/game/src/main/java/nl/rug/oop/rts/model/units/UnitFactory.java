package nl.rug.oop.rts.model.units;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.AllArgsConstructor;
import nl.rug.oop.rts.model.armies.Army;
import nl.rug.oop.rts.model.armies.Faction;

@AllArgsConstructor
public class UnitFactory {
    private int minArmySize;
    private int maxArmySize;

    private Random random;

    public UnitFactory(int seed) {
        random = new Random(seed);

        this.minArmySize = 10;
        this.maxArmySize = 50;
    }

    public Army buildArmy(Faction faction) {
        // We use 51
        int unitCount = random.nextInt(minArmySize, maxArmySize + 1);

        List<Unit> units = new ArrayList<>();
        for (int i = 0; i < unitCount; i++)
            units.add(buildUnit(faction));

        Army army = new Army(units, faction);
        return army;
    }

    public Unit buildUnit(Faction faction) {
        List<UnitType> types = faction.getAvailableUnits();
        UnitType type = types.get(random.nextInt(types.size()));

        List<String> names = type.getAvailableNames();
        String name = names.get(random.nextInt(names.size()));

        double health = random.nextDouble(type.getMinHealth(), type.getMaxHealth());
        double damage = random.nextDouble(type.getMinDamage(), type.getMaxDamage());

        return type.buildUnit(name, damage, health);
    }
}
