package org.vizzoid.utils.random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RunnableRandom extends InputOutputRandom<Runnable, Void> {

    private final List<Runnable> objects = new ArrayList<>();
    private final int bound;

    public RunnableRandom(Runnable e, Runnable... es) {
        super();
        objects.add(e);
        objects.addAll(Arrays.asList(es));
        bound = es.length + 1;
    }

    public RunnableRandom(Runnable target, int maxTries, Runnable e, Runnable... es) {
        super(target, maxTries);
        objects.add(e);
        objects.addAll(Arrays.asList(es));
        bound = es.length + 1;
    }

    public RunnableRandom(Runnable target, int maxTries, int minTries, Runnable e, Runnable... es) {
        super(target, maxTries, minTries);
        objects.add(e);
        objects.addAll(Arrays.asList(es));
        bound = es.length + 1;
    }

    @Override
    public Void next() {
        super.next0().run();
        return null;
    }

    @Override
    protected Runnable getFromRandom(Random random) {
        return objects.get(random.nextInt(bound));
    }
}
