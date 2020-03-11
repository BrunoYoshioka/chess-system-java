package chess;

import boardgame.Board;
import boardgame.Piece;

// Peça de Xadrez
public abstract class ChessPiece extends Piece { // classe abstrata que pode não implementar a operação dos movimentos possíveis
    private Color color;

    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
