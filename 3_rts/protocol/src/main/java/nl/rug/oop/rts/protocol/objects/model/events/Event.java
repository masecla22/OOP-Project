package nl.rug.oop.rts.protocol.objects.model.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.rug.oop.rts.protocol.objects.model.armies.Army;

@Getter
@AllArgsConstructor
public abstract class Event {
    private EventType type;

    public abstract void execute(Army army);

    @Override
    public String toString() {
        return type.toString();
    }
}
