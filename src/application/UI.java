package application;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UI {

    // códigos especiais das cores para imprimir no console
    // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    // https://stackoverflow.com/questions/2979383/java-clear-the-console
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // Ler uma posição do usuário
    public static ChessPosition readChessPosition(Scanner scanner){
        try {
            String s = scanner.nextLine();
            char column = s.charAt(0); // Ele é primeiro caracter do string "a"
            int row = Integer.parseInt(s.substring(1)); // recortar a string a partir da posição 1 e converter o resultado para inteiro, e atribui a linha
            return new ChessPosition(column, row); // retornar a posição do xadrez recebendo a coluna e a linha.
        }
        catch (RuntimeException e){
            throw new InputMismatchException("Error reading ChessPosition. Valid values are from a1 to h8."); // Erro lendo a posição do xadrez. Valores válidos são de a1 até h8.
        }
    }

    // imprimir a partida
    public static void printMatch(ChessMatch chessMatch, List<ChessPiece> captured){
        printBoard(chessMatch.getPieces()); // Imprimir o tabuleiro
        System.out.println();
        printCapturedPieces(captured); // Imprimir peças capturadas
        System.out.println();
        System.out.println("Turn : " + chessMatch.getTurn()); // imprimir o turno
        if (!chessMatch.getCheckMate()){ // Se não estiver em checkmate
            System.out.println("Waiting player: " + chessMatch.getCurrentPlayer()); // Esperando o jogador atual jogar
            if (chessMatch.getCheck()){
                System.out.println("CHECK!");//Acrescentando a informação de check
            }
        }
        else { // Se checkmate for positivo
            System.out.println("CHECKMATE!");
            System.out.println("Winner: " + chessMatch.getCurrentPlayer()); // vencedor: (Jogador atual)
        }
    }

    public static void printBoard(ChessPiece[][] pieces) {
        for (int i=0; i<pieces.length; i++) {
            System.out.print((8 - i) + " ");
            for (int j=0; j<pieces.length; j++) {
                printPiece(pieces[i][j], false); // falso para indicar que nenhuma peça é prater o fundo colorido
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    // Imprimir o tabuleiro com as peças marcadas
    public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves /*Receber movimentos possíveis*/) {
        for (int i=0; i<pieces.length; i++) {
            System.out.print((8 - i) + " ");
            for (int j=0; j<pieces.length; j++) {
                printPiece(pieces[i][j], possibleMoves[i][j]); // pintar o fundo colorido dependendo dessa variável
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    private static void printPiece(ChessPiece piece, boolean background /*Variavel para indicar se eu devo colorir ou não o fundo da minha peça*/) {
        if (background){
            System.out.print(ANSI_BLUE_BACKGROUND); // mudar a cor do fundo da minha tela
        }
        if (piece == null) {
            System.out.print("-" + ANSI_RESET);
        }
        else {
            // testar se a cor for branco ou preto
            if (piece.getColor() == Color.WHITE) { // se a cor for branco
                System.out.print(ANSI_WHITE + piece + ANSI_RESET);
            }
            else { //
                System.out.print(ANSI_YELLOW + piece + ANSI_RESET);
            }
        }
        System.out.print(" ");
    }

    // Imprimir peças capturadas
    private static void printCapturedPieces(List<ChessPiece> captured){
        // criei a lista das peças brancas capturadas
        List<ChessPiece> white = captured.stream().filter(x -> x.getColor() == Color.WHITE).collect(Collectors.toList()); // Filtrando da minha lista todos cuja a cor é branca
        List<ChessPiece> black = captured.stream().filter(x -> x.getColor() == Color.BLACK).collect(Collectors.toList()); // Filtrar todos que tiver a cor preta

        System.out.println("Captured pieces: ");
        System.out.print("White: ");
        System.out.print(ANSI_WHITE);
        System.out.println(Arrays.toString(white.toArray())); // Imprimir a lista
        System.out.print(ANSI_RESET); // Resetar a cor da impressão

        System.out.print("Black: ");
        System.out.print(ANSI_YELLOW);
        System.out.println(Arrays.toString(black.toArray())); // Imprimir a lista
        System.out.print(ANSI_RESET); // Resetar a cor da impressão
    }
}
