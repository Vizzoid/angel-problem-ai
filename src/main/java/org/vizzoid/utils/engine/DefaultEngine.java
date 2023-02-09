package org.vizzoid.utils.engine;

import java.awt.*;
import java.util.function.Consumer;
import java.awt.event.*;

import javax.swing.*;

public class DefaultEngine {
    
    private final Toolkit toolkit = Toolkit.getDefaultToolkit();
    public final Dimension dimension = new Dimension();
    public final Dimension center = new Dimension();
    private Consumer<Graphics> painter;
    private boolean shouldRepaint;
    private final JFrame frame = new JFrame();
    private final JPanel panel = new JPanel();
    private double aspectRatio;
    private final Sleeper sleeper = new Sleeper();

    public DefaultEngine() {
        frame.add(panel);
        panel.setFocusable(true);
        //frame.setUndecorated(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setDimension(toolkit.getScreenSize());
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

    public void setShouldRepaint(boolean shouldRepaint) {
        this.shouldRepaint = shouldRepaint;
    }

    protected void clearScreen(Graphics g) {

    }

    public void setPainter(Consumer<Graphics> painter) {
        this.painter = painter;
    }

    public void addMotionListener(MouseMotionListener motion) {
        panel.addMouseMotionListener(motion);
    }

    public void addMouseListener(MouseListener mouse) {
        panel.addMouseListener(mouse);
    }

    public void paint(Graphics g) {
        clearScreen(g);

        painter.accept(g);

        if (shouldRepaint) {
            sleeper.sleep();
            panel.repaint();
        }
    }

}
