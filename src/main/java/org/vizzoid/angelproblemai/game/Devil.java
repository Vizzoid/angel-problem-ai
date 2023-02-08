package org.vizzoid.angelproblemai.game;

public class Devil {

    private final BoardPoint point;
    private final Board board;

    public Devil(Board board) {
        this.board = board;
        this.point = new BoardPoint();
    }

    public BoardPoint getPoint() {
        return point;
    }

    public void moveTo(int x, int y) {
        point.set(x, y);
        board.mark(x, y);
    }

}
