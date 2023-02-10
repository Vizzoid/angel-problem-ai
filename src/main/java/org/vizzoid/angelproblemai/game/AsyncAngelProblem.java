package org.vizzoid.angelproblemai.game;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AsyncAngelProblem extends AngelProblem {

    private boolean shouldUpdate = false;

    public AsyncAngelProblem() {
        super();

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::beginUpdateLoop, 1, 1, TimeUnit.SECONDS);
    }

    @SuppressWarnings("InfiniteRecursion")
    public void beginUpdateLoop() {
        if (shouldUpdate) {
            super.update();
            shouldUpdate = false;
        }
    }

    @Override
    public void update() {
        shouldUpdate = true;
    }
}
