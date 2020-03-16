package chess;

import boardgame.Board;
import boardgame.Piece;
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

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition){
        // convertendo as duas posições na matriz
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        // validar se a posição havia uma peça
        validateSourcePosition(source); // essa operação responsabiliza por validar essa posição de origem, se ela não existir essa operação vai lançar uma excessão
        validateTargetPosition(source, target); // validar posição de destino, recebendo a posição de destino e origem
        Piece capturedPiece = makeMove(source, target); // recebendo a posição de origem e destino
        return (ChessPiece)capturedPiece; // retornar peça capturada
    }

    // Operação makeMove será responsável por realizar o movimento da peça
    private Piece makeMove(Position source, Position target){
        Piece piece = board.removePiece(source); // retirei a peça que estava na posição de origem
        Piece capturedPiece = board.removePiece(target); // remover a possível peça que esteja na posição de destino
        // após remoção de peça que estava na posição de origem e remover a possível peça que esteja na posição de destino
        board.placePiece(piece, target); // Colocar a posição que estava na origem lá na posição de destino
        return capturedPiece;
    }

    private void validateSourcePosition(Position position){
        // testar se não estiver essa peça na posição
        if (!board.thereIsAPiece(position)){
            throw new ChessException("There is no piece on source position"); // não existe peça na posição de origem.
        }
        if (!board.piece(position).isThereAnyPossibleMove()){ // testar se não tiver nenhum movimento possíveis nessa peça
            throw new ChessException("There is no possible moves for the chosen piece."); // Não existe movimentos possíveis para essa peça
        }
    }

    private void validateTargetPosition(Position source, Position target){
        // validar se a posição de destino é válida em relação a posição de origem
        if (!board.piece(source).possibleMove(target)) { // testar se a posição de destino ela é o movimento possível em relação a peça que estiver na posição de origem
            throw new ChessException("The chosen piece can´t move to target position."); // A peça escolhida não pode se mover para posição de destino.
        }
    }

    // Esse método ele vai receber as coordenadas do xadrez
    private void placeNewPiece(char column, int row, ChessPiece piece){
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
    }

    private void initialSetup(){
        // colocando as posições devidas do jogo inicial do xadrez
        placeNewPiece('c', 1, new Rook(board, Color.WHITE));
        placeNewPiece('c', 2, new Rook(board, Color.WHITE));
        placeNewPiece('d', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new King(board, Color.WHITE));

        placeNewPiece('c', 7, new Rook(board, Color.BLACK));
        placeNewPiece('c', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 8, new King(board, Color.BLACK));
    }
}
