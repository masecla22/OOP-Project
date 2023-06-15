package nl.rug.oop.rts.model.events.types;

import nl.rug.oop.rts.model.armies.Army;
import nl.rug.oop.rts.model.events.Event;
import nl.rug.oop.rts.model.events.EventType;
import nl.rug.oop.rts.model.units.Unit;
import nl.rug.oop.rts.model.units.UnitFactory;

public class ReinforcementsEvent extends Event {
    private UnitFactory unitFactory;

    public ReinforcementsEvent(UnitFactory unitFactory) {
        super(EventType.REINFORCEMENTS);
        this.unitFactory = unitFactory;
    }

    @Override
    public void execute(Army army) {
        int amount = army.getUnits().size() / 2;

        for (int i = 0; i < amount; i++) {
            Unit unit = unitFactory.buildUnit(army.getFaction());
            army.getUnits().add(unit);
        }
    }
}
