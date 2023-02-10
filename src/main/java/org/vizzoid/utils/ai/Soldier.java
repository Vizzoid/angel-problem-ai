package org.vizzoid.utils.ai;

// test of brain functionality
public class Soldier {

    private final Brain<Soldier> brain;

    public Soldier() {
        brain = new Brain<>(this);
    }

}
