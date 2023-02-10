package org.vizzoid.angelproblemai.game;

import java.util.function.Consumer;
import java.util.function.Function;

public class Angel {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Angel.class.getName());

    private final BoardPoint point;
    private final Board board;
    private int power = 1;

    public Angel(Board board) {
        this.point = new BoardPoint(board) {
            @Override
            public void set(double x, double y) {
                super.set(x, y);
            }
        };
        this.board = board;
    }

    public BoardPoint getPoint() {
        return point;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getAmountSpace() {
        return getAmountSpace(false);
    }

    public int getAmountSpace(boolean ignoreMarked) {
        var ref = new Object() {
            int space = 0;
        };
        iterateMoves(p -> {
            ref.space++;
        }, ignoreMarked);
        return ref.space;
    }

    // x, y -> LoopAction if should act
    public void iterateMoves(Function<IterativePoint, LoopAction> point) {
        iterateMoves(point, true);
    }

    private int boardMin(int angel) {
        int start = -power;
        if (angel + start < 0) start = 0;
        return start;
    }

    private int boardMax(int angel) {
        int start = power;
        int end = board.getWidth() - 1;
        if (angel + start > end) start = end;
        return start;
    }

    // x, y -> LoopAction if should act
    public void iterateMoves(Function<IterativePoint, LoopAction> point, boolean ignoreMarked) {
        int angelX = this.point.getXInt();
        int angelY = this.point.getYInt();
        final IterativePoint point1 = new IterativePoint(); // avoid creating multiple, use the same one, fails if point is stored or not sync
        point1.board = board;

        int startX = boardMin(angelX);
        int startY = boardMin(angelY);
        int endX = boardMax(angelX);
        int endY = boardMax(angelY);
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                if (x == 0 && y == 0) continue;
                int pointX = angelX + x;
                int pointY = angelY + y;

                if (ignoreMarked) {
                    if (board.isMarked(pointX, pointY)) {
                        continue;
                    }
                    point1.marked = false;
                }
                else {
                    point1.marked = board.isMarked(pointX, pointY);
                }

                point1.x = pointX;
                point1.y = pointY;
                point1.relativeX = x;
                point1.relativeY = y;

                LoopAction action = point.apply(point1);
                if (action == LoopAction.RETURN) return;
                if (action == LoopAction.BREAK) break;
            }
        }
    }

    public void iterateMoves(Consumer<IterativePoint> consumer) {
        iterateMoves(consumer, true);
    }

    public void iterateMoves(Consumer<IterativePoint> consumer, boolean ignoreMarked) {
        iterateMoves((point) -> {
            consumer.accept(point);
            return LoopAction.NONE;
        }, ignoreMarked);
    }

    public boolean isCloseBorder() {
        return point.getX() - power < 0 || point.getY() - power < 0 ||
                point.getX() + power >= board.getWidth() || point.getY() + power >= board.getWidth();
    }

    public int getPowerDiameter() {
        return power + power + 1;
    }
}
