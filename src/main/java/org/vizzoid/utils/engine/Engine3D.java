package org.vizzoid.utils.engine;

import org.vizzoid.utils.position.ImmoveablePosition;
import org.vizzoid.utils.position.MoveablePosition;
import org.vizzoid.utils.position.Position;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Engine used as screen from converting objects in a world onto the plane (screen).
 * All position altering methods must take in a position as the first input and output a position.
 * If altering xyz coordinates, must return MoveablePosition and use position.moveable() to convert
 * the position interface accepted into a mutable state. This provides it easily useable for mutation
 * into 3d but also easy use outside of inside Engine3D.
 * <p>
 * Modeled after javidx9's 3d engine
 */
@SuppressWarnings("UnusedReturnValue")
public class Engine3D extends JPanel {

    private final Toolkit toolkit = Toolkit.getDefaultToolkit();
    private final Camera camera;
    private final Dimension dimension = new Dimension();
    private final Dimension center = new Dimension();
    private double aspectRatio;
    private final JFrame frame = new JFrame();
    private final List<Object3D> object3DS = new ArrayList<>();
    private final Sleeper sleeper = new Sleeper();
    private final Position lightDirection = new MoveablePosition(0, 0, -1);//.normalize();
    private final List<ColoredPolygon> toRaster = new ArrayList<>();

    public Engine3D(Camera camera) {
        this.camera = camera;
        frame.add(this);
        prepare();
/*
        Mesh cube = new Mesh(
                new Triangle(
                        0, 0, 0,
                        0, 1, 0,
                        1, 1, 0),
                new Triangle(
                        0, 0, 0,
                        1, 1, 0,
                        1, 0, 0),

                new Triangle(
                        1, 0, 0,
                        1, 1, 0,
                        1, 1, 1),
                new Triangle(
                        1, 0, 0,
                        1, 1, 1,
                        1, 0, 1),

                new Triangle(
                        1, 0, 1,
                        1, 1, 1,
                        0, 1, 1),
                new Triangle(
                        1, 0, 1,
                        0, 1, 1,
                        0, 0, 1),

                new Triangle(
                        0, 0, 1,
                        0, 1, 1,
                        0, 1, 0),
                new Triangle(
                        0, 0, 1,
                        0, 1, 0,
                        0, 0, 0),

                new Triangle(
                        0, 1, 0,
                        0, 1, 1,
                        1, 1, 1),
                new Triangle(
                        0, 1, 0,
                        1, 1, 1,
                        1, 1, 0),

                new Triangle(
                        1, 0, 1,
                        0, 0, 1,
                        0, 0, 0),
                new Triangle(
                        1, 0, 1,
                        0, 0, 0,
                        1, 0, 0));*/
        // mesh is cube test

        //object3DS.add(cube);
        //object3DS.addAll(Arrays.asList(cube.getTriangles()));
        object3DS.add(Mesh.load("D:\\Users\\vtyso\\Downloads\\New Text Document.txt"));
    }

    /**
     * Prepares screen and frame. Should initialize dimension
     */
    protected void prepare() {
        setFocusable(true);
        //frame.setUndecorated(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //setDimension(toolkit.getScreenSize());
        setDimension(new Dimension(256 * 4, 240 * 4));
        frame.setVisible(true);
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
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, dimension.width, dimension.height);
    }

    @Override
    @SuppressWarnings("ForLoopReplaceableByForEach")
    protected void paintComponent(Graphics g) {
        theta += 0.001;

        for (int i = 0, object3DSSize = object3DS.size(); i < object3DSSize; i++) {
            Object3D object3D = object3DS.get(i);
            object3D.draw(g, this);
        }
        clearScreen(g);
        toRaster.sort(Comparator.comparingDouble(ColoredPolygon::midpoint));
        for (int i = 0; i < toRaster.size(); i++) {
            ColoredPolygon polygon = toRaster.get(i);
            polygon.draw(g);
        }
        toRaster.clear();

        sleeper.sleep();
        repaint();
    }

    double theta = 0;

    public void queue(ColoredPolygon polygon) {
        toRaster.add(polygon);
    }

    /**
     * Mutates position in 3d world into the 2d screen through projection, accounting for rotation, offset and scaled.
     */
    public MoveablePosition mutate3dTo2d(Position position0) {
        MoveablePosition position = position0.moveable();
        prepareProjection(position);
        completeProjection(position0);

        return position;
    }

    public MoveablePosition prepareProjection(Position position0) {
        MoveablePosition position = position0.moveable();
        rotateZ(position, theta);
        rotateX(position, theta * 0.5);
        offset(position);

        return position;
    }

    public MoveablePosition completeProjection(Position position0) {
        MoveablePosition position = position0.moveable();
        projectOntoScreen(position);
        scale(position);

        return position;
    }

    /**
     * Rotation matrix, moves position akin to z rotation depending on theta
     */
    public MoveablePosition rotateZ(Position position0, double theta) {
        MoveablePosition position = position0.moveable();
        double x = position.getX();
        double y = position.getY();
        position.setX((x * Math.cos(theta)) + (y * -Math.sin(theta)));
        position.setY((x * Math.sin(theta)) + (y * Math.cos(theta)));

        return position;
    }

    /**
     * Rotation matrix, moves position akin to x rotation depending on theta
     */
    public MoveablePosition rotateX(Position position0, double theta) {
        MoveablePosition position = position0.moveable();
        double y = position.getY();
        double z = position.getZ();
        position.setY((y * Math.cos(theta)) + (z * -Math.sin(theta)));
        position.setZ((y * Math.sin(theta)) + (z * Math.cos(theta)));

        return position;
    }

    /**
     * Offset z onto screen
     */
    public MoveablePosition offset(Position position0) {
        MoveablePosition position = position0.moveable();
        position.moveZ(8);

        return position;
    }

    /**
     * Projection matrix, projects the 3d point onto 2d plane
     */
    public MoveablePosition projectOntoScreen(Position position0) {
        MoveablePosition position = position0.moveable();
        double x = position.getX();
        double y = position.getY();
        double z = position.getZ();

        double x2D = (camera.getScalingFactor() * x * aspectRatio);
        double y2D = (camera.getScalingFactor() * y);
        double z2D = (camera.getProjectionMultiplier() * z - camera.getProjectionSubtractive());

        if (z == 0) z = 1;
        position.setX(x2D / z);
        position.setY(y2D / z);
        position.setZ(z2D / z);

        return position;
    }

    /**
     * Scale x and y by center to view, normalize onto middle of screen being 0, 0
     */
    public MoveablePosition scale(Position position0) {
        MoveablePosition position = position0.moveable();
        position.setX((position.getX() + 1) * center.width);
        position.setY((position.getY() + 1) * center.height);

        return position;
    }

    public Camera getCamera() {
        return camera;
    }

    public Sleeper getSleeper() {
        return sleeper;
    }

    public Position getLightDirection() {
        return lightDirection;
    }

}
