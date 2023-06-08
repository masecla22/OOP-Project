package nl.rug.oop.rts.observing;

import java.util.Set;

public interface Observable {
    public void addObserver(Observer observer);

    public void removeObserver(Observer observer);

    public Set<Observer> getObservers();

    public default void update() {
        for (Observer observer : getObservers()) {
            observer.update();
        }
    }
}
