package org.vizzoid.angelproblemai.game;

import org.vizzoid.utils.position.Point;

public class Board {

    private final boolean[] latice;
    private final int width;

    public Board() {
        this(255);
    }

    public Board(int width) {
        latice = new boolean[width * width];
        this.width = width;
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
}
