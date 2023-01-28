package org.vizzoid.utils.engine;

import org.vizzoid.utils.position.ImmoveablePosition;
import org.vizzoid.utils.position.MoveablePosition;
import org.vizzoid.utils.position.Position;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Engine used as screen from converting objects in a world onto the plane (screen)
 */
public class Engine3D extends JPanel {

    private final Toolkit toolkit = Toolkit.getDefaultToolkit();
    private final Camera camera;
    private final Dimension dimension = new Dimension();
    private final Dimension center = new Dimension();
    private double aspectRatio;
    private final JFrame frame = new JFrame();
    private final List<Object3D> object3DS = new ArrayList<>();

    public Engine3D(Camera camera) {
        this.camera = camera;
        frame.add(this);
        prepare();

        Mesh cube = new Mesh(
                new Triangle(
                        new ImmoveablePosition(0, 0, 0),
                        new ImmoveablePosition(0, 1, 0),
                        new ImmoveablePosition(1, 1, 0)),
                new Triangle(
                        new ImmoveablePosition(0, 0, 0),
                        new ImmoveablePosition(1, 1, 0),
                        new ImmoveablePosition(1, 0, 0)),

                new Triangle(
                        new ImmoveablePosition(1, 0, 0),
                        new ImmoveablePosition(1, 1, 0),
                        new ImmoveablePosition(1, 1, 1)),
                new Triangle(
                        new ImmoveablePosition(1, 0, 0),
                        new ImmoveablePosition(1, 1, 1),
                        new ImmoveablePosition(1, 0, 1)),

                new Triangle(
                        new ImmoveablePosition(1, 0, 1),
                        new ImmoveablePosition(1, 1, 1),
                        new ImmoveablePosition(0, 1, 1)),
                new Triangle(
                        new ImmoveablePosition(1, 0, 1),
                        new ImmoveablePosition(0, 1, 1),
                        new ImmoveablePosition(0, 0, 1)),

                new Triangle(
                        new ImmoveablePosition(0, 0, 1),
                        new ImmoveablePosition(0, 1, 1),
                        new ImmoveablePosition(0, 1, 0)),
                new Triangle(
                        new ImmoveablePosition(0, 0, 1),
                        new ImmoveablePosition(0, 1, 0),
                        new ImmoveablePosition(0, 0, 0)),

                new Triangle(
                        new ImmoveablePosition(0, 1, 0),
                        new ImmoveablePosition(0, 1, 1),
                        new ImmoveablePosition(1, 1, 1)),
                new Triangle(
                        new ImmoveablePosition(0, 1, 0),
                        new ImmoveablePosition(1, 1, 1),
                        new ImmoveablePosition(1, 1, 0)),

                new Triangle(
                        new ImmoveablePosition(1, 0, 1),
                        new ImmoveablePosition(0, 0, 1),
                        new ImmoveablePosition(0, 0, 0)),
                new Triangle(
                        new ImmoveablePosition(1, 0, 1),
                        new ImmoveablePosition(0, 0, 0),
                        new ImmoveablePosition(1, 0, 0)));
        // mesh is cube test

        object3DS.add(cube);
    }

    /**
     * Prepares screen and frame. Should initialize dimension
     */
    protected void prepare() {
        setFocusable(true);
        frame.setUndecorated(true);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setDimension(toolkit.getScreenSize());
    }

    /**
     * Sets dimension. Should set dimension field and center field, as well as aspect ratio and JFrame size.
     */
    public void setDimension(Dimension dimension) {
        this.dimension.width = dimension.width;
        this.dimension.height = dimension.height;
        this.center.width = (int) (dimension.width * 0.5);
        this.center.height = (int) (dimension.height * 0.5);
        this.aspectRatio = dimension.width / (double) dimension.height;
        frame.setSize(dimension);
    }

    /**
     * Clears graphics image. Common ways are by wiping the screen with white, others color it in skybox color, such as blue, some can not clear screen at all, for
     * example if outside the skybox.
     */
    protected void clearScreen(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, dimension.width, dimension.height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        clearScreen(g);

        g.setColor(Color.BLACK);
        for (Object3D object3D : object3DS) {
            object3D.draw(g, this);
        }
    }

    /**
     * Mutates position in 3d world into the 2d screen through projection, accounting for rotation, offset and scaled.
     */
    public MoveablePosition mutate3dTo2d(MoveablePosition position) {
        return projectOntoScreen(position);
    }

    /**
     * Projection matrix
     */
    public MoveablePosition projectOntoScreen(MoveablePosition position) {
        double x = position.getX();
        double y = position.getY();
        double z = position.getZ() + 3;
        if (z == 0) z = 1;

        double x2D = (camera.getScalingFactor() * x * aspectRatio) / z;
        double y2D = (camera.getScalingFactor() * y) / z;

        position.setX((x2D + 1) * center.width); // normalize onto middle of screen being 0, 0
        position.setY((y2D + 1) * center.height); // normalize onto middle of screen being 0, 0
        position.setZ((camera.getProjectionMultiplier() * z - camera.getProjectionSubtractive()) / z);

        return position;
    }

}
