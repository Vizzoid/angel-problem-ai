package org.vizzoid.angelproblemai.game;

import org.vizzoid.utils.position.Point;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Board {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Board.class.getName());

    private volatile boolean[] latice;
    private final int width;

    public Board() {
        this(32767);
    }

    public Board(int width) {
        this.width = width;
        reset();
    }

    public void markRandom() {
        Random r = ThreadLocalRandom.current();
        mark(r.nextInt(width), r.nextInt(width));
    }

    public void mark(Point point) {
        mark((int) point.getX(), (int) point.getY());
    }

    public void mark(int x, int y) {
        setMarked(x, y, true);
    }

    public void unmark(Point point) {
        unmark((int) point.getX(), (int) point.getY());
    }

    public void unmark(int x, int y) {
        setMarked(x, y, false);
    }

    public void setMarked(Point point, boolean marked) {
        setMarked((int) point.getX(), (int) point.getY(), marked);
    }

    public void setMarked(int x, int y, boolean marked) {
        latice[toIndex(x, y)] = marked;
    }

    public boolean isMarked(Point point) {
        return isMarked((int) point.getX(), (int) point.getY());
    }

    public boolean isMarked(int x, int y) {
        return latice[toIndex(x, y)];
    }

    public boolean isInside(Point point) {
        return isInside(point.getX(), point.getY());
    }

    public boolean isInside(double x, double y) {
        return x >= 0 && y >= 0 && x < width && y < width;
    }

    public boolean isNotInside(Point point) {
        return isNotInside(point.getX(), point.getY());
    }

    public boolean isNotInside(double x, double y) {
        return x < 0 || y < 0 || x >= width || y >= width;
    }

    public int getWidth() {
        return width;
    }

    private int toIndex(int x, int y) {
        return (y * width) + x;
    }

    public void reset() {
        latice = new boolean[width * width];
    }
}
