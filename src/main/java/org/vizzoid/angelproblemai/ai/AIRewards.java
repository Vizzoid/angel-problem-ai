package org.vizzoid.angelproblemai.ai;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.vizzoid.angelproblemai.game.Angel;

public class AIRewards {
    public static final double HIGH = 1;
    public static final double LOW = -1;
    public static final double NORMAL = 0;

    public static double extreme(boolean success) {
        return success ? HIGH : LOW;
    }

    public static INDArray spaces(Angel angel, double defaultValue) {
        double[] spaces = new double[angel.getAmountSpace()];
        for (int i = 0, length = spaces.length; i < length; i++) {
            spaces[i] = defaultValue;
        }
        return Nd4j.create(spaces);
    }

    public static INDArray highs(Angel angel) {
        return spaces(angel, HIGH);
    }

    public static INDArray lows(Angel angel) {
        return spaces(angel, LOW);
    }

    public static INDArray normals(Angel angel) {
        return spaces(angel, NORMAL);
    }

}
