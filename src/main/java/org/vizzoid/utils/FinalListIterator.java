package org.vizzoid.utils;

import java.util.ListIterator;

/**
 * ListIterator that restricts editing the list it is contained in to create a purely immutable list without exceptions
 */
public class FinalListIterator<E> extends FinalIterator<E> {

    private final ListIterator<E> it;

    public FinalListIterator(ListIterator<E> it) {
        super(it);
        this.it = it;
    }

    public boolean hasPrevious() {
        return it.hasPrevious();
    }

    public E previous() {
        return it.previous();
    }

    public int nextIndex() {
        return it.nextIndex();
    }

    public int previousIndex() {
        return it.previousIndex();
    }
}
