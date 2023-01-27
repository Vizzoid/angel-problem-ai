package org.vizzoid.utils.engine;

import org.vizzoid.utils.position.Position;

import java.awt.*;

public class Triangle implements Object3D {

    private final Position pos1;
    private final Position pos2;
    private final Position pos3;

    public Triangle(Position pos1, Position pos2, Position pos3) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.pos3 = pos3;
    }

    public Position getPos1() {
        return pos1;
    }

    public Position getPos2() {
        return pos2;
    }

    public Position getPos3() {
        return pos3;
    }

    @Override
    public void draw(Graphics graphics, Engine3D engine) {
        Position pos2d1 = engine.mutate3dTo2d(pos1);
        Position pos2d2 = engine.mutate3dTo2d(pos2);
        Position pos2d3 = engine.mutate3dTo2d(pos3);

        Polygon polygon = new Polygon();
        polygon.xpoints = new int[]{(int) pos2d1.getX(), (int) pos2d2.getX(), (int) pos2d3.getX()};
        polygon.ypoints = new int[]{(int) pos2d1.getY(), (int) pos2d2.getY(), (int) pos2d3.getY()};
        polygon.npoints = 3;

        graphics.drawPolygon(polygon);
    }
}
