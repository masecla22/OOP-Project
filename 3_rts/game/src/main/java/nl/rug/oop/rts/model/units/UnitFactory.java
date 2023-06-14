package nl.rug.oop.rts.model.units;

import java.util.Random;

import nl.rug.oop.rts.model.armies.Army;
import nl.rug.oop.rts.model.armies.Faction;

public class UnitFactory {
    private Random random;

    public UnitFactory(int seed) {
        random = new Random(seed);
    }

    public Army buildArmy(Faction faction) {
        int unitCount = random.nextInt(40) + 10;
    }

    public Unit buildUnit(Faction faction) {
        
    }
}
