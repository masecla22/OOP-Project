package nl.rug.oop.rts.protocol.objects.model.events.types;

import nl.rug.oop.rts.protocol.objects.model.armies.Army;
import nl.rug.oop.rts.protocol.objects.model.events.Event;
import nl.rug.oop.rts.protocol.objects.model.events.EventType;
import nl.rug.oop.rts.protocol.objects.model.factories.UnitFactory;
import nl.rug.oop.rts.protocol.objects.model.units.Unit;

/**
 * Reinforcements event.
 */
public class ReinforcementsEvent extends Event {
    private UnitFactory unitFactory;

    /**
     * Constructor for ReinforcementsEvent.
     * 
     * @param unitFactory - the unit factory
     */
    public ReinforcementsEvent(UnitFactory unitFactory) {
        super(EventType.REINFORCEMENTS);
        this.unitFactory = unitFactory;
    }

    @Override
    public void execute(Army army) {
        int amount = army.getUnits().size() / 2;

        int size = army.getUnits().size();
        for (int i = 0; i < amount; i++) {
            Unit unit = unitFactory.buildUnit(army.getFaction(), size + i);
            army.getUnits().add(unit);
        }
    }
}
