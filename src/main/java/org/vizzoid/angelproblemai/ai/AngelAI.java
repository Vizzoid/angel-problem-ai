package org.vizzoid.angelproblemai.ai;

import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.ObservationSpace;
import org.vizzoid.angelproblemai.game.Angel;
import org.vizzoid.angelproblemai.game.AngelProblem;
import org.vizzoid.angelproblemai.game.BoardPoint;
import org.vizzoid.angelproblemai.game.Endgame;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class AngelAI implements MDP<AngelMovement, Integer, DiscreteSpace> {

    private final Angel angel;
    private DiscreteSpace space;
    private final AngelSpace observation;
    private final AngelProblem problem;

    public AngelAI(AngelProblem problem) {
        this.problem = problem;
        this.angel = problem.getAngel();
        this.observation = new AngelSpace(angel);
        resetSpace();
    }

    public void resetSpace() {
        this.space = new DiscreteSpace(angel.getAmountSpace());
    }


    @Override
    public ObservationSpace<AngelMovement> getObservationSpace() {
        return observation;
    }

    @Override
    public DiscreteSpace getActionSpace() {
        if (angel.isCloseBorder()) {
            resetSpace();
        }
        return space;
    }

    @Override
    public AngelMovement reset() {
        problem.reset();
        return new AngelMovement(problem);
    }

    @Override
    public void close() {

    }

    @Override
    public StepReply<AngelMovement> step(Integer integer) {
        AngelMovement movement = new AngelMovement(problem);
        BoardPoint point = movement.getTo(integer);

        boolean marked = problem.getBoard().isMarked(point);
        if (!marked) {
            problem.moveAngel(point.getXInt(), point.getYInt());
        }
        Random r = ThreadLocalRandom.current();
        problem.getBoard().mark(point.getXInt() + (r.nextInt(angel.getPowerDiameter() + 10) * (r.nextBoolean() ? 1 : -1)), point.getYInt() + (r.nextInt(angel.getPowerDiameter() + 10) * (r.nextBoolean() ? 1 : -1)));
        /*try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/
        double reward;
        if (problem.getEndgame() == Endgame.DEVIL) {
            reward = -100;
        } else if (marked) {
            reward = -1;
        } else {
            reward = 1;
        }

        return new StepReply<>(
                movement,
                reward,
                isDone(),
                null
        );
    }

    @Override
    public boolean isDone() {
        return problem.getEndgame().victory();
    }

    @Override
    public MDP<AngelMovement, Integer, DiscreteSpace> newInstance() {
        return new AngelAI(problem);
    }
}
