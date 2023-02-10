package org.vizzoid.utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class ThreadManager {

    private interface List extends Iterable<Thread> {
        void add(Thread thread);

        @NotNull
        @Override
        Iterator<Thread> iterator();
    }

    private final List threadList;

    public ThreadManager(int size) {
        Thread[] list = new Thread[size];
        threadList = new List() {

            private int index = 0;

            @Override
            public void add(Thread thread) {
                list[index] = thread;
                index++;
            }

            @Override
            public @NotNull Iterator<Thread> iterator() {
                return Arrays.stream(list).iterator();
            }
        };
    }

    public ThreadManager() {
        ArrayList<Thread> list = new ArrayList<>();
        threadList = new List() {
            @Override
            public void add(Thread thread) {
                list.add(thread);
            }

            @Override
            public @NotNull Iterator<Thread> iterator() {
                return list.iterator();
            }
        };
    }

    public void create(Runnable runnable) {
        Thread thread = new Thread(runnable);
        threadList.add(thread);
    }

    public void start() {
        for (Thread thread : threadList) {
            thread.start();
        }
    }

    public void join() throws InterruptedException {
        for (Thread thread : threadList) {
            thread.join();
        }
    }

}
