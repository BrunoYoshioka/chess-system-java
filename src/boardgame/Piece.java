package boardgame;

public class Piece {

    protected Position position; // está protegido para que não seja visível na camada de xadrez
    private Board board;

    public Piece(Board board) {
        this.board = board;
        position = null;
    }

    protected Board getBoard() {
        return board;
    }
}
