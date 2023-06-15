package nl.rug.oop.rts.model.units;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import nl.rug.oop.rts.model.armies.Faction;
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
import nl.rug.oop.rts.model.units.rohan.RohanArcher;
import nl.rug.oop.rts.model.units.rohan.RohanSpearman;
import nl.rug.oop.rts.model.units.rohan.RohanSwordsman;

@Getter
@AllArgsConstructor
public enum UnitType {
    AXE_THROWER(AxeThrower.class, 10.0, 30.0, 60.0, 80.0,
            List.of("Ralph", "Mickey", "Bob")),
    GUARDIAN(Guardian.class, 10.0, 20.0, 40.0, 60.0,
            List.of("Adolph", "Goofy", "Mario")),
    PHALANX(Phalanx.class, 5.0, 10.0, 50.0, 60.0,
            List.of("Lorenzo", "Greg", "Gabby")),

    LORIEN_WARRIOR(LorienWarrior.class, 15.0, 30.0, 60.0, 70.0,
            List.of("Fabiola", "Fernus", "Fabrizio")),
    MIRKWOOD_ARCHER(MirkwoodArcher.class, 10.0, 30.0, 70.0, 80.0,
            List.of("Odobasian", "Roberta", "Paulo")),
    RIVENDELL_LANCER(RivendellLancer.class, 10.0, 40.0, 70.0, 90.0,
            List.of("Maya", "Karina", "Sebastian")),

    URUK_CROSSBOW_MAN(UrukCrossbowman.class, 5.0, 40.0, 60.0, 90.0,
            List.of("Angelus", "Pablo", "Petrus")),
    URUK_HAI(UrukHai.class, 10.0, 20.0, 40.0, 80.0,
            List.of("Lucretiu", "Feriga", "Don")),
    WARG_RIDER(WargRider.class, 7.0, 25.0, 60.0, 90.0,
            List.of("Cici", "Souvlaki", "Couscous")),

    GONDOR_SOLDIER(GondorSoldier.class, 10.0, 30.0, 80.0, 100.0,
            List.of("Cerasela", "Cayus", "Gordon")),
    ITHILIEN_RANGER(IthilienRanger.class, 5.0, 40.0, 60.0, 75.0,
            List.of("Mathew", "Andrei", "Paprika")),
    TOWER_GUARD(TowerGuard.class, 10.0, 35.0, 80.0, 90.0,
            List.of("Hobo", "Horatiu", "Lawrence")),

    HARADRIM_ARCHER(HaradrimArcher.class, 30.0, 40.0, 50.0, 60.0,
            List.of("Hercules", "Ignatiu", "Chilli")),
    ORC_PIKEMAN(OrcPikeman.class, 10.0, 20.0, 80.0, 110.0,
            List.of("Dove", "Kyle", "Karen")),
    ORC_WARRIOR(OrcWarrior.class, 20.0, 30.0, 70.0, 90.0,
            List.of("Patricia", "Romin", "Goyle")),

    ROHAN_ARCHER(RohanArcher.class, 10.0, 30.0, 60.0, 80.0,
            List.of("Ralphael", "MickeyD's", "Boaba")),
    ROHAN_SPEARMAN(RohanSpearman.class, 10.0, 20.0, 40.0, 60.0,
            List.of("Adolphung", "Goofymus", "MarioKart")),
    ROHAN_SWORDSMAN(RohanSwordsman.class, 5.0, 10.0, 50.0, 60.0,
            List.of("Lorenzoios", "Gregozio", "Gabbyriela Coxanus"));

    private Class<? extends Unit> unitClass;

    private double minDamage;
    private double maxDamage;

    private double minHealth;
    private double maxHealth;

    private List<String> availableNames;

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

        Constructor<? extends Unit> constructor = this.unitClass.getConstructor(String.class, Double.TYPE,
                Double.TYPE);
        cachedConstructors.put(this, constructor);
        return constructor;
    }

    public Faction getFaction() {
        switch (this) {
            case AXE_THROWER:
            case GUARDIAN:
            case PHALANX:
                return Faction.DWARVES;
            case LORIEN_WARRIOR:
            case MIRKWOOD_ARCHER:
            case RIVENDELL_LANCER:
                return Faction.ELVES;
            case URUK_CROSSBOW_MAN:
            case URUK_HAI:
            case WARG_RIDER:
                return Faction.ISENGARD;
            case GONDOR_SOLDIER:
            case ITHILIEN_RANGER:
            case TOWER_GUARD:
                return Faction.MEN;
            case HARADRIM_ARCHER:
            case ORC_PIKEMAN:
            case ORC_WARRIOR:
                return Faction.MORDOR;
            case ROHAN_ARCHER:
            case ROHAN_SPEARMAN:
            case ROHAN_SWORDSMAN:
                return Faction.ROHAN;
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }
}
