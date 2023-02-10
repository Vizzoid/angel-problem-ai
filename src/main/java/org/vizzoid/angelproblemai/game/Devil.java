package org.vizzoid.angelproblemai.game;

public class Devil {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Devil.class.getName());

    private final BoardPoint point;
    private final Board board;

    public Devil(Board board) {
        this.board = board;
        this.point = new BoardPoint();
        point.set(-1, -1);
    }

    public BoardPoint getPoint() {
        return point;
    }

    public void moveTo(int x, int y) {
        point.set(x, y);
        board.mark(x, y);
    }

}
