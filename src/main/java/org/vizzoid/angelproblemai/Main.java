package org.vizzoid.angelproblemai;

import org.deeplearning4j.rl4j.learning.configuration.QLearningConfiguration;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.network.configuration.DQNDenseNetworkConfiguration;
import org.deeplearning4j.rl4j.network.dqn.DQN;
import org.nd4j.linalg.learning.config.RmsProp;
import org.vizzoid.angelproblemai.ai.AngelAI;
import org.vizzoid.angelproblemai.ai.AngelMovement;
import org.vizzoid.angelproblemai.game.AngelProblem;

import java.io.File;
import java.io.IOException;

public class Main {

    public static String AI_FILE = "src/main/resources/angel-ai.zip";
    private static AngelProblem problem;

    public static void main(String[] args) {
        problem = new AngelProblem();
        train();
    }

    @SuppressWarnings("InfiniteRecursion")
    public static void train() {
        AngelAI angelAI = new AngelAI(problem);
        QLearningDiscreteDense<AngelMovement> learning = new QLearningDiscreteDense<>(angelAI,
                DQNDenseNetworkConfiguration.builder().l2(0.001)
                        .updater(new RmsProp(0.000025))
                        .numHiddenNodes(300)
                        .numLayers(2)
                        .build(),
                QLearningConfiguration.builder()
                        .seed(123L)
                        .maxEpochStep(200)
                        .maxStep(15000)
                        .expRepMaxSize(150000)
                        .batchSize(128)
                        .targetDqnUpdateFreq(500)
                        .updateStart(10)
                        .rewardFactor(0.01)
                        .gamma(0.99)
                        .errorClamp(1.0)
                        .minEpsilon(0.1f)
                        .epsilonNbStep(1000)
                        .doubleDQN(true)
                        .build());
        if (new File(AI_FILE).exists()) {
            try {
                //noinspection unchecked
                learning.getNeuralNet().copy(DQN.load(AI_FILE));
            } catch (IOException ignored) {
            }
        }
        learning.train();
        try {
            learning.getNeuralNet().save(AI_FILE);
        } catch (IOException ignored) {
        }

        train();
    }
}