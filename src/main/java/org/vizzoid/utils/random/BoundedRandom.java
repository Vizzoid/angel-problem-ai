package org.vizzoid.utils.random;

import java.util.Random;

/**
 * ControlledRandom that is bounded and has an origin
 */
public abstract class BoundedRandom<E> extends ControlledRandom<E> {

    private E bound;
    private boolean bounded;
    private E origin;
    private boolean originated;

    public BoundedRandom() {
        super();
    }

    public BoundedRandom(E target, int maxTries) {
        super(target, maxTries);
    }

    public BoundedRandom(E target, int maxTries, int minTries) {
        super(target, maxTries, minTries);
    }

    protected BoundedRandom(E target, int maxTries, boolean controlled) {
        super(target, maxTries, controlled);
    }

    public BoundedRandom(E bound) {
        this();
        setBound(bound);
    }

    public BoundedRandom(E target, int maxTries, E bound) {
        this(target, maxTries);
        setBound(bound);
    }

    public BoundedRandom(E target, int maxTries, int minTries, E bound) {
        this(target, maxTries, minTries);
        setBound(bound);
    }

    protected BoundedRandom(E target, int maxTries, boolean controlled, E bound) {
        this(target, maxTries, controlled);
        setBound(bound);
    }

    public BoundedRandom(E origin, E bound) {
        this(bound);
        setOrigin(origin);
    }

    public BoundedRandom(E target, int maxTries, E origin, E bound) {
        this(target, maxTries, bound);
        setOrigin(origin);
    }

    public BoundedRandom(E target, int maxTries, int minTries, E origin, E bound) {
        this(target, maxTries, minTries, bound);
        setOrigin(origin);
    }

    protected BoundedRandom(E target, int maxTries, boolean controlled, E origin, E bound) {
        this(target, maxTries, controlled, bound);
        setOrigin(origin);
    }

    public E getBound() {
        return bound;
    }

    protected void setBound(E bound) {
        bounded = true;
        this.bound = bound;
    }

    public E getOrigin() {
        return origin;
    }

    protected void setOrigin(E origin) {
        originated = true;
        this.origin = origin;
    }

    @Override
    public E next() {
        return super.next();
    }

    protected final E getFromRandom(Random random) {
        return bounded ? (originated ? getFromRandom(random, origin, bound) : getFromRandom(random, bound)) : getFromRandom0(random);
    }

    protected abstract E getFromRandom0(Random random);

    protected abstract E getFromRandom(Random random, E bound);

    protected abstract E getFromRandom(Random random, E origin, E bound);

}
