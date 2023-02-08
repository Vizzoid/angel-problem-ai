package org.vizzoid.angelproblemai.game;

public class AngelProblem {

    private final Devil devil;
    private final Angel angel;
    private final Board board;
    private final BoardEngine engine;

    public AngelProblem() {
        board = new Board();
        devil = new Devil(board);
        angel = new Angel(board);
        engine = new BoardEngine(this);
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


}
