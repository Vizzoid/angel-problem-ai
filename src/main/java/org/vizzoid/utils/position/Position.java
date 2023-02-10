package org.vizzoid.utils.position;

/**
 * 3D immutable position template
 */
public interface Position {

    Position UP = new ImmoveablePosition(0, 1, 0);
    Position DOWN = new ImmoveablePosition(0, -1, 0);
    Position NORTH = new ImmoveablePosition(1, 0, 0);
    Position SOUTH = new ImmoveablePosition(-1, 0, 0);
    Position EAST = new ImmoveablePosition(0, 0, 1);
    Position WEST = new ImmoveablePosition(0, 0, -1);

    double getX();

    double getY();

    double getZ();

    MoveablePosition moveable();

    ImmoveablePosition immoveable();

    default MoveablePosition normal(Position position1, Position position2) {
        Position line1 = line(position1);
        Position line2 = line(position2);
        MoveablePosition normal = line1.crossProduct(line2);

        return normal.normalize();
    }

    default MoveablePosition crossProduct(Position position) {
        MoveablePosition crossProduct = new MoveablePosition();
        crossProduct.setX(getY() * position.getZ() - getZ() * position.getY());
        crossProduct.setY(getZ() * position.getX() - getX() * position.getZ());
        crossProduct.setZ(getX() * position.getY() - getY() * position.getX());

        return crossProduct;
    }

    default MoveablePosition line(Position position) {
        MoveablePosition line = new MoveablePosition();
        line.setX(position.getX() - getX());
        line.setY(position.getY() - getY());
        line.setZ(position.getZ() - getZ());

        return line;
    }

    default MoveablePosition normalize() {
        MoveablePosition position = moveable();
        double length = length();

        position.setX(position.getX() / length);
        position.setY(position.getY() / length);
        position.setZ(position.getZ() / length);

        return position;
    }

    default double length() {
        double x = getX();
        double y = getY();
        double z = getZ();

        return Math.sqrt(x * x + y * y + z * z);
    }

    default double dotProduct(Position position) {
        return getX() * position.getX() +
                getY() * position.getY() +
                getZ() * position.getZ();
    }

    // movement utility

    default MoveablePosition add(Position position1) {
        MoveablePosition position = new MoveablePosition();
        position.setX(getX() + position1.getX());
        position.setY(getY() + position1.getY());
        position.setZ(getZ() + position1.getZ());
        return position;
    }

    default MoveablePosition subtract(Position position1) {
        MoveablePosition position = new MoveablePosition();
        position.setX(getX() - position1.getX());
        position.setY(getY() - position1.getY());
        position.setZ(getZ() - position1.getZ());
        return position;
    }

    default MoveablePosition multiply(Position position1) {
        MoveablePosition position = new MoveablePosition();
        position.setX(getX() * position1.getX());
        position.setY(getY() * position1.getY());
        position.setZ(getZ() * position1.getZ());
        return position;
    }

    default MoveablePosition divide(Position position1) {
        MoveablePosition position = new MoveablePosition();
        position.setX(getX() / position1.getX());
        position.setY(getY() / position1.getY());
        position.setZ(getZ() / position1.getZ());
        return position;
    }

}
