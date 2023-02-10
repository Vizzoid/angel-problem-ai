package org.vizzoid.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * List that has no modifying methods, meant to create an immutable list without exceptions
 */
// ImmutableLists are cool but suck
public class FinalList<E> {

    private final List<E> list;

    @SafeVarargs
    public FinalList(E... list) {
        this(List.of(list));
    }

    public FinalList(List<E> list) {
        this.list = list;
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean contains(Object o) {
        return list.contains(o);
    }

    @NotNull
    public FinalIterator<E> iterator() {
        return new FinalIterator<>(list.iterator());
    }

    @NotNull
    public Object[] toArray() {
        return list.toArray();
    }

    @NotNull
    public <T> T[] toArray(@NotNull T[] a) {
        return list.toArray(a);
    }

    public boolean containsAll(@NotNull Collection<?> c) {
        return list.containsAll(c);
    }

    public E get(int index) {
        return list.get(index);
    }

    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @NotNull
    public FinalListIterator<E> listIterator() {
        return new FinalListIterator<>(list.listIterator());
    }

    @NotNull
    public FinalListIterator<E> listIterator(int index) {
        return new FinalListIterator<>(list.listIterator(index));
    }

    @NotNull
    public List<E> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }
}
