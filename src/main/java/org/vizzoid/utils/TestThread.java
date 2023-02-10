package org.vizzoid.utils;

public abstract class TestThread extends Thread {

    public final TimeStamp now;

    public TestThread(TimeStamp now) {
        super();
        this.now = now;
    }

    @Override
    public abstract void run();

}
