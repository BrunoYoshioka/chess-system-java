package application;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Program {
    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch(); // Instanciar a partida

        while (true){
            try {
                UI.clearScreen(); // Limpar a tela cada vez que volta no while
                UI.printBoard(chessMatch.getPieces()); // função para imprimir as partidas
                System.out.println(); // saltar uma linha
                System.out.print("Source: "); // Digite a posição de origem
                ChessPosition source = UI.readChessPosition(scanner); // ler a posição de origem

                System.out.println();
                System.out.print("Target: "); // Digite a posição de destino
                ChessPosition target = UI.readChessPosition(scanner); // ler a posição de destino

                ChessPiece capturedPiece = chessMatch.performChessMove(source, target); // movendo de origem para o destino
            }
            catch (ChessException e){
                System.out.println(e.getMessage());
                scanner.nextLine();
            }
            catch (InputMismatchException e){
                System.out.println(e.getMessage());
                scanner.nextLine();
            }
        }
    }
}
