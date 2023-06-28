package nl.rug.oop.rts.protocol.objects.interfaces.observing;

import java.util.Set;

/**
 * Interface for observable objects.
 */
public interface Observable {
    /**
     * Add an observer to the observable object.
     * 
     * @param observer - the observer to add
     */
    void addObserver(Observer observer);

    /**
     * Remove an observer from the observable object.
     * 
     * @param observer - the observer to remove
     */
    void removeObserver(Observer observer);

    /**
     * Get all observers of the observable object.
     * 
     * @return - the observers
     */
    Set<Observer> getObservers();

    /**
     * Notify all observers of the observable object.
     */
    default void update() {
        for (Observer observer : getObservers()) {
            observer.update();
        }
    }
}
