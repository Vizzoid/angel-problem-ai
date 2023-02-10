package org.vizzoid.angelproblemai.game;

import org.vizzoid.angelproblemai.Main;
import org.vizzoid.utils.engine.DefaultEngine;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.net.URL;

public class BoardEngine implements MouseMotionListener, MouseListener, KeyListener {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(BoardEngine.class.getName());

    public static final Color YELLOW_WHITE = new Color(220, 182, 110);
    public static final Color DARK_BROWN = new Color(66, 51, 35);
    public static final Color MARKED_COLOR = new Color(219,7,61);
    public static final int SQUARE_SIZE = 81;
    public static final int HALF_SQUARE_SIZE = (int) (SQUARE_SIZE * 0.5);
    private static final Image ANGEL_IMAGE;
    private static final Image DEVIL_IMAGE;

    static {
        ClassLoader loader = Main.class.getClassLoader();
        try {
            URL angelURL = loader.getResource("angel.png");
            URL devilURL = loader.getResource("devil.png");
            if (angelURL == null) throw new FileNotFoundException("Angel");
            if (devilURL == null) throw new FileNotFoundException("Devil");

            ANGEL_IMAGE = ImageIO.read(angelURL);
            DEVIL_IMAGE = ImageIO.read(devilURL);

            if (ANGEL_IMAGE == null) throw new FileNotFoundException("Angel");
            if (DEVIL_IMAGE == null) throw new FileNotFoundException("Devil");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final DefaultEngine internalEngine;
    private final AngelProblem angelProblem;
    private final int squaresHalfWidth;
    private final int squaresHalfHeight;
    private final BoardPoint mouse = new BoardPoint();

    public BoardEngine(AngelProblem angelProblem) {
        this.angelProblem = angelProblem;
        internalEngine = new DefaultEngine() {
            @Override
            protected void clearScreen(Graphics g) {

            }
        };
        this.internalEngine.setPainter(this::paint);
        this.internalEngine.addMouseListener(this);
        this.internalEngine.addMotionListener(this);
        this.internalEngine.addKeyListener(this);

        {
            Dimension dimension = internalEngine.getCenter();
            double squaresWidthD = (dimension.width - (SQUARE_SIZE * 0.5)) / (double) SQUARE_SIZE;
            double squaresHeightD = (dimension.height - (SQUARE_SIZE * 0.5)) / (double) SQUARE_SIZE;
            int squaresWidthI = (int) squaresWidthD;
            int squaresHeightI = (int) squaresHeightD;

            this.squaresHalfWidth = squaresWidthI + (squaresWidthI != squaresWidthD ? 1 : 0);
            this.squaresHalfHeight = squaresHeightI + (squaresHeightI != squaresHeightD ? 1 : 0);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            System.exit(0);
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    protected void paint(Graphics graphics) {
        ((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        drawBoard(graphics);
        drawAngel(graphics);
        drawDevil(graphics);

        if (angelProblem.getEndgame().victory()) {
            graphics.setFont(new Font(Font.SERIF, Font.BOLD, 200));
            graphics.drawString("Game Over", 0, internalEngine.center.height);
        }

        internalEngine.setShouldRepaint(true);
    }

    private void drawBoard(Graphics graphics) {
        Angel angel = angelProblem.getAngel();
        BoardPoint angelPoint = angel.getPoint();
        int xPoint = angelPoint.getXInt();
        int yPoint = angelPoint.getYInt();
        Board board = angelProblem.getBoard();

        for (int x = -squaresHalfWidth,
             maxSquaresWidth = squaresHalfWidth + 1,
             maxSquaresHeight = squaresHalfHeight + 1;
             x < maxSquaresWidth; x++) {
            for (int y = -squaresHalfHeight; y < maxSquaresHeight; y++) {

                int xTile = xPoint + x;
                int yTile = yPoint + y;

                Color color;
                if (board.isNotInside(xTile, yTile)) color = Color.WHITE;
                else if (board.isMarked(xTile, yTile)) color = MARKED_COLOR;
                else if ((x + squaresHalfWidth) % 2 != (y + squaresHalfHeight) % 2) color = DARK_BROWN;
                else color = YELLOW_WHITE;
                graphics.setColor(color);
                int screenX = (x * SQUARE_SIZE) + (internalEngine.center.width - HALF_SQUARE_SIZE);
                int screenY = (y * SQUARE_SIZE) + (internalEngine.center.height - HALF_SQUARE_SIZE);
                graphics.fillRect(
                        screenX,
                        screenY,
                        SQUARE_SIZE, SQUARE_SIZE);

                graphics.setColor(Color.WHITE);
                graphics.drawString(xTile + ", " + ((board.getWidth() - 1) - yTile), screenX, screenY + SQUARE_SIZE);
            }
        }

        // angel range
        graphics.setColor(new Color(255, 255, 255, 64));
        int angelPower = angel.getPower();
        int bottomCorner = ((angelPower * SQUARE_SIZE) + HALF_SQUARE_SIZE);
        int size = (((angelPower * 2) + 1) * SQUARE_SIZE);
        graphics.fillRect(
                internalEngine.center.width - bottomCorner,
                internalEngine.center.height - bottomCorner,
                size, size);

        graphics.fillRect(
                toScreenX(mouse.getXInt()),
                toScreenY(mouse.getYInt()),
                SQUARE_SIZE, SQUARE_SIZE
        );

        graphics.setColor(Color.WHITE);
        graphics.drawString(mouse.getXInt() + ", " + mouse.getYInt(), 10, 10);
    }

    private void drawAngel(Graphics graphics) {
        graphics.drawImage(ANGEL_IMAGE,
                internalEngine.center.width - HALF_SQUARE_SIZE,
                internalEngine.center.height - HALF_SQUARE_SIZE,
                SQUARE_SIZE, SQUARE_SIZE, null, null);
    }

    private void drawDevil(Graphics graphics) {
        BoardPoint point = devilPoint();
        graphics.drawImage(DEVIL_IMAGE,
                toScreenX(point.getXInt()),
                toScreenY(point.getYInt()),
                SQUARE_SIZE, SQUARE_SIZE, null, null);
    }

    private int toScreenX(int x) {
        return (int) ((x - angelPoint().getX() - 0.5) * SQUARE_SIZE) + internalEngine.center.width;
    }

    private int toScreenY(int y) {
        return (int) ((y - angelPoint().getY() - 0.5) * SQUARE_SIZE) + internalEngine.center.height;
    }

    private double fromScreenX(int screenX) {
        return ((screenX - internalEngine.center.width) / (double) SQUARE_SIZE) + angelPoint().getX() + 0.5;
    }

    private double fromScreenY(int screenY) {
        return ((screenY - internalEngine.center.height) / (double) SQUARE_SIZE) + angelPoint().getY() + 0.5;
    }

    private BoardPoint angelPoint() {
        return angelProblem.getAngel().getPoint();
    }

    private BoardPoint devilPoint() {
        return angelProblem.getDevil().getPoint();
    }

    // listener methods

    @Override // do not redraw if mouse does not change
    public void mouseClicked(MouseEvent e) {
        Board board = angelProblem.getBoard();
        Angel angel = angelProblem.getAngel();
        BoardPoint angelPoint = angelPoint();
        if (angelPoint.getX() == mouse.getX() && angelPoint.getY() == mouse.getY()) return;
        if (board.isNotInside(mouse)) return;
        if (board.isMarked(mouse)) return;
        if (angelPoint.distance(mouse) > angel.getPower()) return;

        double xDiff = mouse.getX() - angelPoint.getX();
        double yDiff = mouse.getY() - angelPoint.getY();
        angelProblem.moveAngel(mouse.getXInt(), mouse.getYInt());
        mouse.move(xDiff, yDiff);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouse.set(
                fromScreenX(e.getX()),
                fromScreenY(e.getY()));
    }
}
