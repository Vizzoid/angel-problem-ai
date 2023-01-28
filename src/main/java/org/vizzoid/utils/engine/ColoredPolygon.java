package org.vizzoid.utils.engine;

import org.vizzoid.utils.position.Position;

import java.awt.*;

public class ColoredPolygon extends Polygon implements Cloneable {

    public Color color = Color.WHITE;
    public int[] zpoints;
    public FillMode mode = FillMode.OUTLINE;

    public ColoredPolygon() {
        zpoints = new int[4];
    }

    public ColoredPolygon(Position... positions) {
        load(positions);
    }

    /**
     * Creates polygon with color but does not modify anything else (arrays will update each other as a result)
     */
    public ColoredPolygon withColor(Color color) {
        ColoredPolygon polygon = clone();
        polygon.color = color;
        return polygon;
    }

    /**
     * Creates polygon with mode but does not modify anything else (arrays will update each other as a result)
     */
    public ColoredPolygon withMode(FillMode mode) {
        ColoredPolygon polygon = clone();
        polygon.mode = mode;
        return polygon;
    }

    public ColoredPolygon withContrast() {
        ColoredPolygon clone = clone();
        clone.contrast();
        return clone;
    }

    public void load(Position... positions) {
        npoints = positions.length;
        xpoints = new int[npoints];
        ypoints = new int[npoints];
        zpoints = new int[npoints];

        for (int i = 0, positionsLength = positions.length; i < positionsLength; i++) {
            Position position = positions[i];
            xpoints[i] = (int) position.getX();
            ypoints[i] = (int) position.getY();
            zpoints[i] = (int) position.getZ();
        }
    }

    public void draw(Graphics graphics) {
        mode.draw(graphics, this);
    }

    public void outline(Graphics graphics) {
        graphics.setColor(color);
        graphics.drawPolygon(this);
    }

    public void fill(Graphics graphics) {
        graphics.setColor(color);
        graphics.fillPolygon(this);
    }

    public void contrast() {
        color = new Color(
                255 - color.getRed(),
                255 - color.getBlue(),
                255 - color.getGreen()
        );
    }

    public double midpoint() {
        double zSum = 0;
        for (int zpoint : zpoints) {
            zSum += zpoint;
        }
        zSum /= npoints;
        return zSum;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public ColoredPolygon clone() {
        ColoredPolygon polygon = new ColoredPolygon();
        polygon.npoints = npoints;
        polygon.xpoints = xpoints;
        polygon.ypoints = ypoints;
        polygon.zpoints = zpoints;
        polygon.color = color;
        polygon.mode = mode;
        return polygon;
    }
}
