package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

// classe Peça importante Rei
public class King extends ChessPiece {

    // Incluindo uma dependência para partida
    private ChessMatch chessMatch;

    // construtor obrigatório a chamada a superclasse que informa o tabuleiro e a cor da peça
    public King(Board board, Color color, ChessMatch chessMatch) {
        super(board, color); // repassar os dados da superclasse
        this.chessMatch = chessMatch;
    }

    @Override
    public String toString(){
        // convertendo uma torre para string
        return "K";
    }

    // Método auxiliar para que possa mover (podeMover)
    public boolean canMove(Position position){
        ChessPiece p = (ChessPiece)getBoard().piece(position); //pegar a peça p que estiver na posição
        // retornar a verificar que a peça p que esta na posição, que a casa esteja vazia, ou tem uma peça adversária
        return p == null || p.getColor() != getColor();
    }

    // Método auxiliar para testar a condição de Roque (Castling)
    private boolean testRookCastling(Position position){ // testar se a torre estará apto para o Roque
        ChessPiece p = (ChessPiece)getBoard().piece(position); // Peguei a peça que esta na posição
        return p != null &&
                p instanceof Rook /*Se essa peça é uma Torre*/ &&
                p.getColor() == getColor() /*Se a cor dessa torre seja igual a cor do rei próprio.*/ &&
                p.getMoveCount() == 0; // Não tiver nenhum movimento.
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        // Movimentos possíveis de um rei
        Position p = new Position(0,0); // Criar uma posição auxiliar, começar com a intancia com new position

        // testar cada uma das 8 direções possíveis para as quais o rei possa mover
        // Above
        p.setValues(position.getRow() - 1, position.getColumn()); // Posição Acima do Rei
        if (getBoard().positionExists(p) && canMove(p)) { // testar se a posição existe do tabuleiro e que pode mover
            mat[p.getRow()][p.getColumn()] = true; // marcar a posição da matriz como verdadeiro
        }

        // below
        p.setValues(position.getRow() + 1, position.getColumn()); // Posição Acima do Rei
        if (getBoard().positionExists(p) && canMove(p)) { // testar se a posição existe do tabuleiro e que pode mover
            mat[p.getRow()][p.getColumn()] = true; // marcar a posição da matriz como verdadeiro
        }

        // left - esquerda
        p.setValues(position.getRow(), position.getColumn() - 1); // Posição Acima do Rei
        if (getBoard().positionExists(p) && canMove(p)) { // testar se a posição existe do tabuleiro e que pode mover
            mat[p.getRow()][p.getColumn()] = true; // marcar a posição da matriz como verdadeiro
        }

        // right - direita
        p.setValues(position.getRow(), position.getColumn() + 1); // Posição Acima do Rei
        if (getBoard().positionExists(p) && canMove(p)) { // testar se a posição existe do tabuleiro e que pode mover
            mat[p.getRow()][p.getColumn()] = true; // marcar a posição da matriz como verdadeiro
        }

        // nw - noroeste
        p.setValues(position.getRow() - 1, position.getColumn() - 1); // Posição Acima do Rei
        if (getBoard().positionExists(p) && canMove(p)) { // testar se a posição existe do tabuleiro e que pode mover
            mat[p.getRow()][p.getColumn()] = true; // marcar a posição da matriz como verdadeiro
        }

        // ne - nordeste
        p.setValues(position.getRow() - 1, position.getColumn() + 1); // Posição Acima do Rei
        if (getBoard().positionExists(p) && canMove(p)) { // testar se a posição existe do tabuleiro e que pode mover
            mat[p.getRow()][p.getColumn()] = true; // marcar a posição da matriz como verdadeiro
        }

        // sw - sudoeste
        p.setValues(position.getRow() + 1, position.getColumn() - 1); // Posição Acima do Rei
        if (getBoard().positionExists(p) && canMove(p)) { // testar se a posição existe do tabuleiro e que pode mover
            mat[p.getRow()][p.getColumn()] = true; // marcar a posição da matriz como verdadeiro
        }

        // se - sudeste
        p.setValues(position.getRow() + 1, position.getColumn() + 1); // Posição Acima do Rei
        if (getBoard().positionExists(p) && canMove(p)) { // testar se a posição existe do tabuleiro e que pode mover
            mat[p.getRow()][p.getColumn()] = true; // marcar a posição da matriz como verdadeiro
        }

        // #specialmove castling
        if (getMoveCount() == 0 && !chessMatch.getCheck()){ // testar se não houve movimento do rei e que a partida não está em check
            // #specialmove castling kingside rook
            Position posT1 = new Position(position.getRow(), position.getColumn() + 3); // a posição onde deve estar a torre do rei
            if (testRookCastling(posT1)){ // testar se existe a torre lá que está apto para roque
                Position p1 = new Position(position.getRow(), position.getColumn() + 1); // uma casa a direita ao Rei
                Position p2 = new Position(position.getRow(), position.getColumn() + 2); // duas casas a direita ao Rei

                // testar se existe duas casas vazias do lado direito
                if (getBoard().piece(p1) == null /*Se não tiver a peça do lado direito*/
                        && getBoard().piece(p2) == null /*Se não tiver a peça de duas casas a direita*/ ){
                    mat[position.getRow()][position.getColumn() + 2] = true; // Fazer o Roque castling. O Rei pode mover duas casas a direita
                }
            }
            // #specialmove castling queenside rook
            Position posT2 = new Position(position.getRow(), position.getColumn() - 4); // a posição onde deve estar a torre do rei
            if (testRookCastling(posT2)){ // testar se existe a torre lá que está apto para roque
                Position p1 = new Position(position.getRow(), position.getColumn() - 1); // uma casa a esquerda ao Rei
                Position p2 = new Position(position.getRow(), position.getColumn() - 2); // duas casas a esquerda ao Rei
                Position p3 = new Position(position.getRow(), position.getColumn() - 3); // três casas a esquerda ao Rei

                // testar se existe duas casas vazias do lado direito
                if (getBoard().piece(p1) == null /*Se não tiver a peça do lado esquerdo*/
                        && getBoard().piece(p2) == null /*Se não tiver a peça de duas casas a esquerda*/
                        && getBoard().piece(p3) == null /*Se não tiver a peça de três casas a esquerda*/){
                    mat[position.getRow()][position.getColumn() - 2] = true; // Fazer o Roque castling. O Rei pode mover duas casas a esquerda
                }
            }
        }

        return mat;
    }
}
