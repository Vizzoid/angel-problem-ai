package org.vizzoid.utils.position;

/**
 * 3D immutable position template
 */
public interface Position {

    double getX();

    double getY();

    double getZ();

    MoveablePosition moveable();

    ImmoveablePosition immoveable();

}
