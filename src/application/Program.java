package application;

import chess.ChessMatch;

public class Program {
    public static void main(String[] args){

        ChessMatch chessMatch = new ChessMatch();
        // função para imprimir as partidas
        UI.printBoard(chessMatch.getPieces());
    }
}
