package org.vizzoid.utils.position;

public class MoveablePoint implements Point {

    private double x, y;

    public MoveablePoint() {

    }

    public MoveablePoint(double x, double y) {
        set(x, y);
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void set(double x, double y) {
        setX(x);
        setY(y);
    }

    public void moveX(double x) {
        setX(this.x + x);
    }

    public void moveY(double y) {
        setY(this.y + y);
    }

    public void move(double x, double y) {
        moveX(x);
        moveY(y);
    }

}
