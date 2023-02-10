package org.vizzoid.utils.random;

import java.util.*;

public class ObjectRandom<E> extends ControlledRandom<E> {

    private final List<E> objects = new ArrayList<>();
    private final int bound;

    @SafeVarargs
    public ObjectRandom(E e, E... es) {
        super();
        objects.add(e);
        objects.addAll(Arrays.asList(es));
        bound = es.length + 1;
    }

    @SafeVarargs
    public ObjectRandom(E target, int maxTries, E e, E... es) {
        super(target, maxTries);
        objects.add(e);
        objects.addAll(Arrays.asList(es));
        bound = es.length + 1;
    }

    @SafeVarargs
    public ObjectRandom(E target, int maxTries, int minTries, E e, E... es) {
        super(target, maxTries, minTries);
        objects.add(e);
        objects.addAll(Arrays.asList(es));
        bound = es.length + 1;
    }

    public ObjectRandom(Collection<? extends E> collection) {
        super();
        objects.addAll(collection);
        bound = collection.size() + 1;
    }

    public ObjectRandom(E target, int maxTries, Collection<? extends E> collection) {
        super(target, maxTries);
        objects.addAll(collection);
        bound = collection.size() + 1;
    }

    public ObjectRandom(E target, int maxTries, int minTries, Collection<? extends E> collection) {
        super(target, maxTries, minTries);
        objects.addAll(collection);
        bound = collection.size() + 1;
    }

    @Override
    protected E getFromRandom(Random random) {
        return objects.get(random.nextInt(bound));
    }
}
