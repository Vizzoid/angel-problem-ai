package org.vizzoid.utils;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Immutable iterator container
 */
public class FinalIterator<E> {

    private final Iterator<E> iterator;

    public FinalIterator(Iterator<E> iterator) {
        this.iterator = iterator;
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public E next() {
        return iterator.next();
    }

    public void forEachRemaining(Consumer<? super E> action) {
        iterator.forEachRemaining(action);
    }
}
