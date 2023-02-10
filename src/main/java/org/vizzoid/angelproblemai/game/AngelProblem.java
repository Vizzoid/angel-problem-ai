package org.vizzoid.angelproblemai.game;

import com.google.errorprone.annotations.CanIgnoreReturnValue;

public class AngelProblem {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(AngelProblem.class.getName());

    private final Devil devil;
    private final Angel angel;
    private final Board board;
    private final BoardEngine engine;
    private Endgame endgame = Endgame.NONE;
    private boolean shouldEnd = false;

    @CanIgnoreReturnValue
    public AngelProblem() {
        board = new Board();
        devil = new Devil(board);
        angel = new Angel(board);
        engine = new BoardEngine(this);
    }

    public void reset() {
        devil.getPoint().set(-1, -1);
        angel.getPoint().middle(board);
        board.reset();
        endgame = Endgame.NONE;
        shouldEnd = false;
    }

    public Devil getDevil() {
        return devil;
    }

    public Angel getAngel() {
        return angel;
    }

    public Board getBoard() {
        return board;
    }

    public BoardEngine getEngine() {
        return engine;
    }

    public void update() {
        // if angel cannot move, then devil wins
        shouldEnd = true;
        angel.iterateMoves((p) -> { // if there are any moves, do not end
            shouldEnd = false;
            return LoopAction.RETURN;
        });
        if (!shouldEnd) return;
        endgame = Endgame.DEVIL;
    }

    public void moveAngel(int x, int y) {
        angel.getPoint().set(x, y);
        update();
    }

    public void moveDevil(int x, int y) {
        devil.moveTo(x, y);
        update();
    }

    public void mark(int x, int y) {
        board.mark(x, y);
        update();
    }

    public Endgame getEndgame() {
        return endgame;
    }

    public void setEndgame(Endgame endgame) {
        this.endgame = endgame;
    }
}
