package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

// classe Peça importante Rei
public class Knight extends ChessPiece {
    // construtor obrigatório a chamada a superclasse que informa o tabuleiro e a cor da peça
    public Knight(Board board, Color color) {
        super(board, color); // repassar os dados da superclasse
    }

    @Override
    public String toString(){
        // convertendo uma torre para string
        return "N";
    }

    // Método auxiliar para que possa mover (podeMover)
    public boolean canMove(Position position){
        ChessPiece p = (ChessPiece)getBoard().piece(position); //pegar a peça p que estiver na posição
        // retornar a verificar que a peça p que esta na posição, que a casa esteja vazia, ou tem uma peça adversária
        return p == null || p.getColor() != getColor();
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        // Movimentos possíveis de um cavalo
        Position p = new Position(0,0); // Criar uma posição auxiliar, começar com a intancia com new position

        // testar cada uma das 8 direções possíveis para as quais o cavalo possa mover
        p.setValues(position.getRow() - 1, position.getColumn() - 2 );
        if (getBoard().positionExists(p) && canMove(p)) { // testar se a posição existe do tabuleiro e que pode mover
            mat[p.getRow()][p.getColumn()] = true; // marcar a posição da matriz como verdadeiro
        }

        p.setValues(position.getRow() - 2, position.getColumn() - 1 );
        if (getBoard().positionExists(p) && canMove(p)) { // testar se a posição existe do tabuleiro e que pode mover
            mat[p.getRow()][p.getColumn()] = true; // marcar a posição da matriz como verdadeiro
        }

        p.setValues(position.getRow() - 2, position.getColumn() + 1);
        if (getBoard().positionExists(p) && canMove(p)) { // testar se a posição existe do tabuleiro e que pode mover
            mat[p.getRow()][p.getColumn()] = true; // marcar a posição da matriz como verdadeiro
        }

        p.setValues(position.getRow() - 1, position.getColumn() + 2);
        if (getBoard().positionExists(p) && canMove(p)) { // testar se a posição existe do tabuleiro e que pode mover
            mat[p.getRow()][p.getColumn()] = true; // marcar a posição da matriz como verdadeiro
        }

        p.setValues(position.getRow() + 1, position.getColumn() + 2);
        if (getBoard().positionExists(p) && canMove(p)) { // testar se a posição existe do tabuleiro e que pode mover
            mat[p.getRow()][p.getColumn()] = true; // marcar a posição da matriz como verdadeiro
        }

        p.setValues(position.getRow() + 2, position.getColumn() + 1);
        if (getBoard().positionExists(p) && canMove(p)) { // testar se a posição existe do tabuleiro e que pode mover
            mat[p.getRow()][p.getColumn()] = true; // marcar a posição da matriz como verdadeiro
        }

        p.setValues(position.getRow() + 2, position.getColumn() - 1);
        if (getBoard().positionExists(p) && canMove(p)) { // testar se a posição existe do tabuleiro e que pode mover
            mat[p.getRow()][p.getColumn()] = true; // marcar a posição da matriz como verdadeiro
        }

        p.setValues(position.getRow() + 1, position.getColumn() - 2);
        if (getBoard().positionExists(p) && canMove(p)) { // testar se a posição existe do tabuleiro e que pode mover
            mat[p.getRow()][p.getColumn()] = true; // marcar a posição da matriz como verdadeiro
        }

        return mat;
    }
}
