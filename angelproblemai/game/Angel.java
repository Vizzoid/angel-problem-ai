package org.vizzoid.angelproblemai.game;

public class Angel {

    private final BoardPoint point;
    private int power = 3;

    public Angel(Board board) {
        this.point = new BoardPoint(board);
    }

    public BoardPoint getPoint() {
        return point;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }
}
