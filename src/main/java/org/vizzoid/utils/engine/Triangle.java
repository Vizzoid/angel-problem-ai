package org.vizzoid.utils.engine;

import org.vizzoid.utils.position.Position;

import java.awt.*;

/**
 * 3 pointed 3d object triangle
 */
public class Triangle implements Object3D {

    private final Position pos1;
    private final Position pos2;
    private final Position pos3;

    public Triangle(Position pos1, Position pos2, Position pos3) {
        this.pos1 = pos1.immoveable();
        this.pos2 = pos2.immoveable();
        this.pos3 = pos3.immoveable();
        // potentially this polygon can be created earlier and used consistently?
        // perhaps a tracked polygon which will reset and reinitialize when there is a significant change in engine or camera?
        /* could be:
        this.polygon = engine.addIfAbsent(new TrackedPolygon() { // engine.addIfAbsent does not add if already added
            /**
             * called on fov, rendering, position, angle change, or other reason
             */
        /*
            public void reset(Engine3D engine) {
                createPolygon(engine, this);
            }
        })
        */
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
        Polygon polygon = createPolygon(engine, new Polygon());
        graphics.drawPolygon(polygon);
    }

    protected Polygon createPolygon(Engine3D engine, Polygon polygon) {
        Position pos2d1 = engine.prepareProjection(pos1);
        Position pos2d2 = engine.prepareProjection(pos2);
        Position pos2d3 = engine.prepareProjection(pos3);

        if (pos2d1.normal(pos2d2, pos2d3).getZ() >= 0) return polygon; // do not load triangle if unseeable

        pos2d1 = engine.completeProjection(pos2d1);
        pos2d2 = engine.completeProjection(pos2d2);
        pos2d3 = engine.completeProjection(pos2d3);

        polygon.xpoints = new int[]{(int) pos2d1.getX(), (int) pos2d2.getX(), (int) pos2d3.getX()};
        polygon.ypoints = new int[]{(int) pos2d1.getY(), (int) pos2d2.getY(), (int) pos2d3.getY()};
        polygon.npoints = 3;
        return polygon;
    }


}
