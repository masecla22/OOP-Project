package nl.rug.oop.rts.model.units;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import nl.rug.oop.rts.model.units.dwarves.AxeThrower;
import nl.rug.oop.rts.model.units.dwarves.Guardian;
import nl.rug.oop.rts.model.units.dwarves.Phalanx;
import nl.rug.oop.rts.model.units.elves.LorienWarrior;
import nl.rug.oop.rts.model.units.elves.MirkwoodArcher;
import nl.rug.oop.rts.model.units.elves.RivendellLancer;
import nl.rug.oop.rts.model.units.isengard.UrukCrossbowman;
import nl.rug.oop.rts.model.units.isengard.UrukHai;
import nl.rug.oop.rts.model.units.isengard.WargRider;
import nl.rug.oop.rts.model.units.men.GondorSoldier;
import nl.rug.oop.rts.model.units.men.IthilienRanger;
import nl.rug.oop.rts.model.units.men.TowerGuard;
import nl.rug.oop.rts.model.units.mordor.HaradrimArcher;
import nl.rug.oop.rts.model.units.mordor.OrcPikeman;
import nl.rug.oop.rts.model.units.mordor.OrcWarrior;

@Getter
@AllArgsConstructor
public enum UnitType {
    AXE_THROWER(AxeThrower.class),
    GUARDIAN(Guardian.class),
    PHALANX(Phalanx.class),

    LORIEN_WARRIOR(LorienWarrior.class),
    MIRKWOOD_ARCHER(MirkwoodArcher.class),
    RIVENDELL_LANCER(RivendellLancer.class),

    URUK_CROSSBOW_MAN(UrukCrossbowman.class),
    URUK_HAI(UrukHai.class),
    WARG_RIDER(WargRider.class),

    GONDOR_SOLDIER(GondorSoldier.class),
    ITHILIEN_RANGER(IthilienRanger.class),
    TOWER_GUARD(TowerGuard.class),

    HARADRIM_ARCHER(HaradrimArcher.class),
    ORC_PIKEMAN(OrcPikeman.class),
    ORC_WARRIOR(OrcWarrior.class);

    private Class<? extends Unit> unitClass;

    private static Map<UnitType, Constructor<? extends Unit>> cachedConstructors = new HashMap<>();

    @SneakyThrows
    public Unit buildUnit(String name, double damage, double health) {
        return getConstructor().newInstance(name, damage, health);
    }

    @SneakyThrows
    private Constructor<? extends Unit> getConstructor() {
        if (cachedConstructors.containsKey(this)) {
            return cachedConstructors.get(this);
        }

        Constructor<? extends Unit> constructor = this.unitClass.getConstructor(String.class, Double.TYPE, Double.TYPE);
        cachedConstructors.put(this, constructor);
        return constructor;
    }
}
