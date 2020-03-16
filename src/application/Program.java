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
                UI.printMatch(chessMatch); // função para imprimir as partidas
                System.out.println(); // saltar uma linha
                System.out.print("Source: "); // Digite a posição de origem
                ChessPosition source = UI.readChessPosition(scanner); // ler a posição de origem

                boolean[][] possibleMoves = chessMatch.possibleMoves(source /*A partir dessa posição de origem que leu*/ );
                UI.clearScreen(); // limpar a tela
                // Imprimir novamente o novo tabuleiro
                UI.printBoard(chessMatch.getPieces(), possibleMoves /*Criar uma sobrecarga passando os movimentos possíveis*/ ); // responsável por imprimir o tabuleiro colorindo as posições possíveis para onde a minha peça pode mover
                // Pedir pro usuário entrar com a posição de destino

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
