package org.vizzoid.utils.random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class SupplierRandom<E> extends InputOutputRandom<Supplier<E>, E> {

    private final List<Supplier<E>> objects = new ArrayList<>();
    private final int bound;

    @SafeVarargs
    public SupplierRandom(Supplier<E> e, Supplier<E>... es) {
        super();
        objects.add(e);
        objects.addAll(Arrays.asList(es));
        bound = es.length + 1;
    }

    @SafeVarargs
    public SupplierRandom(Supplier<E> target, int maxTries, Supplier<E> e, Supplier<E>... es) {
        super(target, maxTries);
        objects.add(e);
        objects.addAll(Arrays.asList(es));
        bound = es.length + 1;
    }

    @SafeVarargs
    public SupplierRandom(Supplier<E> target, int maxTries, int minTries, Supplier<E> e, Supplier<E>... es) {
        super(target, maxTries, minTries);
        objects.add(e);
        objects.addAll(Arrays.asList(es));
        bound = es.length + 1;
    }

    @Override
    public E next() {
        return super.next0().get();
    }

    @Override
    protected Supplier<E> getFromRandom(Random random) {
        return objects.get(random.nextInt(bound));
    }
}
