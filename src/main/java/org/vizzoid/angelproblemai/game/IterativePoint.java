package org.vizzoid.angelproblemai.game;

public class IterativePoint {

    public Board board;
    public int x;
    public int y;
    public int relativeX;
    public int relativeY;
    public boolean marked;

    public void mark() {
        setMarked(true);
    }

    public void unmark() {
        setMarked(false);
    }

    public void setMarked(boolean marked) {
        board.setMarked(x, y, marked);
    }

}
