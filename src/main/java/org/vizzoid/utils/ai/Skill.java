package org.vizzoid.utils.ai;

import java.util.concurrent.ThreadLocalRandom;

public class Skill {

    private double value;

    public Skill() {
        this(ThreadLocalRandom.current().nextGaussian(0.25, 0.16666666666666666));
    }

    public Skill(double value) {
        if (value < 0) value = 0;
        else if (value > 1) value = 1;
        this.value = value;
    }

    public void cycle() {
        double value0 = ThreadLocalRandom.current().nextGaussian(0.0001, 3.3333333333333335E-5);
        if (value0 < 0.00001) value0 = 0.00001;
        else if (value0 > 0.001) value0 = 0.001;
        value -= value0;
    }

    public void hone() {
        hone(false);
    }

    public void hone(boolean validEnvironment) {
        hone(validEnvironment, false);
    }

    public void hone(boolean validEnvironment, boolean manyRepetitions) {
        hone(validEnvironment, manyRepetitions, false);
    }

    public void hone(boolean validEnvironment, boolean manyRepetitions, boolean practice) {
        hone(validEnvironment, manyRepetitions, practice, false);
    }

    public void hone(boolean validEnvironment, boolean manyRepetitions, boolean practice, boolean feedback) {
        double value0 = ThreadLocalRandom.current().nextGaussian(0.0001 +
            (validEnvironment ? 0.00025 : 0.00009) +
            (manyRepetitions ? 0.00025 : 0.00009) +
            (practice ? 0.00025 : 0.00009) +
            (feedback ? 0.00025 : 0.00009), 3.3333333333333335E-5);
        if (value0 < 0.00001) value0 = 0.00001;
        else if (value0 > 0.001) value0 = 0.001;
        value += value0;
    }

    public double getValue() {
        return value;
    }

    public double getAndHone() {
        hone();
        return value;
    }

}
