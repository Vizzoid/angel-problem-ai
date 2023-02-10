package org.vizzoid.angelproblemai.ai;

import org.deeplearning4j.rl4j.space.Encodable;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.vizzoid.angelproblemai.game.Angel;
import org.vizzoid.angelproblemai.game.AngelProblem;
import org.vizzoid.angelproblemai.game.BoardPoint;

public class AngelMovement implements Encodable {

    private final Angel angel;
    private final double[] array;
    private final INDArray data;
    private final AngelProblem problem;

    public AngelMovement(AngelProblem problem) {
        this.problem = problem;
        this.angel = problem.getAngel();
        int power = angel.getPower();
        array = new double[angel.getAmountSpace()];

        angel.iterateMoves((p) -> {
            array[(p.relativeX + power) + (p.relativeY + power)] = AIRewards.extreme(!p.marked);
        }, false);

        this.data = Nd4j.create(array);
    }

    public BoardPoint getTo(int input) {
        int x = (input % 5) - angel.getPower();
        int y = (input / 5) - angel.getPower();
        BoardPoint angelPoint = angel.getPoint();
        return new BoardPoint(x + angelPoint.getXInt(), y + angelPoint.getYInt());
    }

    public void moveTo(int input) {
        BoardPoint point = getTo(input);
        problem.moveAngel(point.getXInt(), point.getYInt());
    }

    @Override
    public double[] toArray() {
        return array;
    }

    @Override
    public boolean isSkipped() {
        return false;
    }

    @Override
    public INDArray getData() {
        return data;
    }

    @Override
    public Encodable dup() {
        return new AngelMovement(problem);
    }
}
