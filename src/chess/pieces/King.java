package chess.pieces;

import boardgame.Board;
import chess.ChessPiece;
import chess.Color;

// classe Peça importante Rei
public class King extends ChessPiece {
    // construtor obrigatório a chamada a superclasse que informa o tabuleiro e a cor da peça
    public King(Board board, Color color) {
        super(board, color); // repassar os dados da superclasse
    }

    @Override
    public String toString(){
        // convertendo uma torre para string
        return "K";
    }
}
