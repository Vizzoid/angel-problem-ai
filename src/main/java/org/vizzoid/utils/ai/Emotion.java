package org.vizzoid.utils.ai;

import org.vizzoid.utils.EmotionTrajectory;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.vizzoid.utils.EmotionTrajectory.*;

public class Emotion {

    protected double value;
    protected double additive;
    protected EmotionTrajectory trajectory = NONE;
    protected EmotionTrajectory future = NONE;
    protected int untilTrajectoryReset = -1;

    public Emotion() {
        this(true);
    }

    public Emotion(boolean randomize) {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        if (randomize) {
            value = r.nextDouble(0, 0.3333333333333333);
            additive = r.nextGaussian(0, 3.333333333333333E-4);

            if (value < -1) value = -1;
            else if (value > 1) value = 1;

            if (additive < -0.001) additive = -0.001;
            else if (additive > 0.001) additive = 0.001;
        }
    }

    public static double randInflate(Random r) {
        return r.nextGaussian(3.333333333333333E-4, 3.333333333333333E-4);
    }

    public static double randDeflate(Random r) {
        return r.nextGaussian(-3.333333333333333E-4, 3.333333333333333E-4);
    }

    public void cycle() {
        ThreadLocalRandom r = ThreadLocalRandom.current();

        if (trajectory == INFLATING) {
            additive += randInflate(r);
        }
        else if (trajectory == DEFLATING) {
            additive += randDeflate(r);
        }
        else {
            if (additive <= -0.00899) additive += randInflate(r);
            else if (additive >= 0.00899) additive += randDeflate(r);
            else additive += r.nextGaussian(0, 3.333333333333333E-4);
        }

        if (additive < -0.001) {
            additive = -0.001;
        }
        else if (additive > 0.001) {
            additive = 0.001;
        }

        double value0 = value + additive;
        if ((additive > 0 && value0 >= 1) || (additive < 0 && value0 <= -1)) value -= additive;
        else value = value0;

        double value1 = value;
        if (value1 < 0) value1 = -value1;
        if (untilTrajectoryReset == 0 && value1 < 0.001) {
            trajectory = future;
            future = NONE;
        }
        if (untilTrajectoryReset > 0) untilTrajectoryReset--;

    }

    public double getValue() {
        return value;
    }

    public double getAdditive() {
        return additive;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setAdditive(double additive) {
        this.additive = additive;
    }

    public EmotionTrajectory getTrajectory() {
        return trajectory;
    }

    public void setTrajectory(EmotionTrajectory trajectory) {
        this.trajectory = trajectory;
        untilTrajectoryReset = 50;
        future = NONE;
    }

    /**
     * Represents intense rise in emotion
     */
    public void inflate() {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        double additive0;
        if (additive <= 0) additive0 = additive + r.nextGaussian(0.0033333333333333335, 0.0033333333333333335);
        else additive0 = additive * 10;

        double value0 = value + additive0;
        if (value0 < 1) value = value0;
        setTrajectory(DEFLATING);
        future = INFLATING;
    }

    /**
     * Represents intense drop in emotion
     */
    public void deflate() {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        double additive0;
        if (additive >= 0) additive0 = additive + r.nextGaussian(-0.0033333333333333335, 0.0033333333333333335);
        else additive0 = additive * 10;

        double value0 = value + additive0;
        if (value0 > -1) value = value0;
        setTrajectory(INFLATING);
        future = DEFLATING;
    }

    /**
     * Represents intense rise in emotion
     */
    public void inflateAdditive() {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        double additive0;
        if (additive <= 0) additive0 = additive + r.nextGaussian(0.0033333333333333335, 0.0033333333333333335);
        else additive0 = additive * 10;
        additive = additive0;
        setTrajectory(DEFLATING);
    }

    /**
     * Represents intense drop in emotion
     */
    public void deflateAdditive() {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        double additive0;
        if (additive >= 0) additive0 = additive + r.nextGaussian(-0.0033333333333333335, 0.0033333333333333335);
        else additive0 = additive * 10;

        additive = additive0;
        setTrajectory(INFLATING);
    }

}
