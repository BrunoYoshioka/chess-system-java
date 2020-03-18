package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

// Peça de Xadrez
public abstract class ChessPiece extends Piece { // classe abstrata que pode não implementar a operação dos movimentos possíveis
    private Color color;
    private int moveCount;

    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public int getMoveCount(){
        return moveCount;
    }

    public void increaseMoveCount(){
        moveCount++;
    }

    public void decreaseMoveCount(){
        moveCount--;
    }

    public ChessPosition getChessPosition(){
        return ChessPosition.fromPosition(position);
        // converter position (herdada da classe genérica Piece) para ChessPosition
    }

    // verificar se existe uma peça adversária nessa posição
    protected boolean isThereOpponentPiece(Position position) {
        ChessPiece p = (ChessPiece)getBoard().piece(position); // atribuir a peça que estiver na posição
        return p != null && p.getColor() != color; // retornar para que essa peça seja adversária
    }
}
