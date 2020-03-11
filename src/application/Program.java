package application;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Scanner;

public class Program {
    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch(); // Instanciar a partida

        while (true){
            // função para imprimir as partidas
            UI.printBoard(chessMatch.getPieces());
            System.out.println(); // saltar uma linha
            System.out.print("Source: "); // Digite a posição de origem
            ChessPosition source = UI.readChessPosition(scanner); // ler a posição de origem

            System.out.println();
            System.out.print("Target: "); // Digite a posição de destino
            ChessPosition target = UI.readChessPosition(scanner); // ler a posição de destino

            ChessPiece capturedPiece = chessMatch.performChessMove(source, target); // movendo de origem para o destino
        }

    }
}
