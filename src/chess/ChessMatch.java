package chess;

import boardgame.Board;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

// Partida do Xadrez ( O coração do sistema xadrez )
public class ChessMatch {

    private Board board; // tem que ter o tabuleiro

    // construtor da partida
    public ChessMatch(){
        board = new Board(8, 8); // criei o tabuleiro dimensão 8X8
        initialSetup();
    }

    // metodo que retorna uma matriz de peças
    public ChessPiece[][] getPieces(){
        ChessPiece[][] mat = new ChessPiece
                [board.getRows() /*Qtde de linhas do tabuleiro*/ ]
                [board.getColumns() /*Qtde de colunas do tabuleiro*/ ]; // criei a matriz

        // Percorrendo a matriz de peças do tabuleiro
        for (int i = 0; i < board.getRows(); i++ ) /*para cada peça do tabuleiro*/ {
            for (int j = 0; j < board.getColumns(); j++) /*Percorrendo a colunas da matriz*/ {
                mat[i][j] = (ChessPiece) board.piece(i, j);
            }
            // dando um DownCasting do ChessPiece
        }
        return mat; // retorna a matriz de peças da partida de xadrez
    }

    private void initialSetup(){
        board.placePiece(new Rook(board, Color.WHITE), new Position(2, 1));
        board.placePiece(new King(board, Color.BLACK), new Position(0, 4));
        board.placePiece(new King(board, Color.WHITE), new Position(7, 4));
    }
}
