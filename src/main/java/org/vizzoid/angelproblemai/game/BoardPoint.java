package org.vizzoid.angelproblemai.game;

import org.vizzoid.utils.position.MoveablePoint;
import org.vizzoid.utils.position.Point;

public class BoardPoint extends MoveablePoint {

    public BoardPoint(Board board) {
        middle(board);
    }

    public BoardPoint(int x, int y) {
        super(x, y);
    }

    public void middle(Board board) {
        int middle = (int) (board.getWidth() * 0.5);
        set(middle, middle);
    }

    public BoardPoint() {

    }

    public int getXInt() {
        return (int) getX();
    }

    public int getYInt() {
        return (int) getY();
    }

    @Override
    public void setX(double x) {
        super.setX((int) x);
    }

    @Override
    public void setY(double y) {
        super.setY((int) y);
    }

    public double distance(Point point) {
        return Math.max(Math.abs(point.getX() - getX()), Math.abs(point.getY() - getY()));
    }

}
