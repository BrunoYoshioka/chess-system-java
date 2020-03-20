package application;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Program {
    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch(); // Instanciar a partida
        List<ChessPiece> captured = new ArrayList<>(); // Declarei uma lista de peças

        while (!chessMatch.getCheckMate()){ // O programa vai rodar enquanto não estiver em checkmate
            try {
                UI.clearScreen(); // Limpar a tela cada vez que volta no while
                UI.printMatch(chessMatch, captured /*Passando o argumento para imprimir partidas*/); // função para imprimir as partidas
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

                ChessPiece capturedPiece = chessMatch.performChessMove(source, target); // Efetuar a movimentação de origem para o destino

                if (capturedPiece != null) { // Se alguma peça foi capturada
                    captured.add(capturedPiece); // adiciono na lista de peças capturadas.
                }

                if (chessMatch.getPromoted() != null){ // se essa partida for promovida
                    System.out.print("Enter piece for promotion (B/N/R/Q): ");
                    String type = scanner.nextLine();
                    chessMatch.replacePromotedPiece(type);
                }
            }
            catch (ChessException e){
                System.out.println(e.getMessage());
                scanner.nextLine();
            }
            catch (InputMismatchException e){
                System.out.println(e.getMessage());
                scanner.nextLine();
            }
        } // Chegar aqui o Jogo termina (CheckMate)
        UI.clearScreen();
        UI.printMatch(chessMatch,captured); // Imprimir a partida finalizada.
    }
}
