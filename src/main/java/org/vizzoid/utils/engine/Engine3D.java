package org.vizzoid.utils.engine;

import org.vizzoid.utils.ImmovablePosition;
import org.vizzoid.utils.position.Position;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Engine3D extends JPanel {

    private final Toolkit toolkit = Toolkit.getDefaultToolkit();
    private final Camera camera;
    private Dimension dimension = new Dimension();
    private final Dimension center = new Dimension();
    private double aspectRatio = dimension.width / (double) dimension.height;
    private final JFrame frame = new JFrame();
    private final List<Object3D> object3DS = new ArrayList<>();

    public Engine3D(Camera camera) {
        this.camera = camera;
        setFocusable(true);
        frame.add(this);
        //frame.setUndecorated(true);
        //setDimension(toolkit.getScreenSize());
        setDimension(new Dimension(256, 240));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Mesh cube = new Mesh(
                new Triangle(
                        new ImmovablePosition(0, 0, 0),
                        new ImmovablePosition(0, 1, 0),
                        new ImmovablePosition(1, 1, 0)),
                new Triangle(
                        new ImmovablePosition(0, 0, 0),
                        new ImmovablePosition(1, 1, 0),
                        new ImmovablePosition(1, 0, 0)),

                new Triangle(
                        new ImmovablePosition(1, 0, 0),
                        new ImmovablePosition(1, 1, 0),
                        new ImmovablePosition(1, 1, 1)),
                new Triangle(
                        new ImmovablePosition(1, 0, 0),
                        new ImmovablePosition(1, 1, 1),
                        new ImmovablePosition(1, 0, 1)),

                new Triangle(
                        new ImmovablePosition(1, 0, 1),
                        new ImmovablePosition(1, 1, 1),
                        new ImmovablePosition(0, 1, 1)),
                new Triangle(
                        new ImmovablePosition(1, 0, 1),
                        new ImmovablePosition(0, 1, 1),
                        new ImmovablePosition(0, 0, 1)),

                new Triangle(
                        new ImmovablePosition(0, 0, 1),
                        new ImmovablePosition(0, 1, 1),
                        new ImmovablePosition(0, 1, 0)),
                new Triangle(
                        new ImmovablePosition(0, 0, 1),
                        new ImmovablePosition(0, 1, 0),
                        new ImmovablePosition(0, 0, 0)),

                new Triangle(
                        new ImmovablePosition(0, 1, 0),
                        new ImmovablePosition(0, 1, 1),
                        new ImmovablePosition(1, 1, 1)),
                new Triangle(
                        new ImmovablePosition(0, 1, 0),
                        new ImmovablePosition(1, 1, 1),
                        new ImmovablePosition(1, 1, 0)),

                new Triangle(
                        new ImmovablePosition(1, 0, 1),
                        new ImmovablePosition(0, 0, 1),
                        new ImmovablePosition(0, 0, 0)),
                new Triangle(
                        new ImmovablePosition(1, 0, 1),
                        new ImmovablePosition(0, 0, 0),
                        new ImmovablePosition(1, 0, 0)));

        object3DS.addAll(Arrays.asList(cube.getTriangles()));
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
        this.center.width = dimension.width / 2;
        this.center.height = dimension.height / 2;
        this.aspectRatio = dimension.width / (double) dimension.height;
        frame.setSize(dimension);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0,dimension.width, dimension.height);



        g.setColor(Color.BLACK);
        for (Object3D object3D : object3DS) {
            object3D.draw(g, this);
        }
    }

    public MoveablePosition mutate3dTo2d(MoveablePosition position) {
        return projectOntoScreen(position)
    }
    
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
