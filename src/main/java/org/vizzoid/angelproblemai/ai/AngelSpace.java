package org.vizzoid.angelproblemai.ai;

import org.deeplearning4j.rl4j.space.ObservationSpace;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.vizzoid.angelproblemai.game.Angel;

public class AngelSpace implements ObservationSpace<AngelMovement> {

    private final Angel angel;
    private final int[] shape;
    private final INDArray low;
    private final INDArray high;

    public AngelSpace(Angel angel) {
        this.angel = angel;
        this.low = AIRewards.lows(angel);
        this.high = AIRewards.highs(angel);
        this.shape = new int[]{1, angel.getAmountSpace()};
    }

    @Override
    public String getName() {
        return "Angel";
    }

    @Override
    public int[] getShape() {
        return shape;
    }

    @Override
    public INDArray getLow() {
        return low;
    }

    @Override
    public INDArray getHigh() {
        return high;
    }
}
