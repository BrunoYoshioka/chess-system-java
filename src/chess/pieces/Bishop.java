package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

// classe peça Torre
public class Bishop extends ChessPiece {
    // construtor obrigatório a chamada a superclasse que informa o tabuleiro e a cor da peça
    public Bishop(Board board, Color color) {
        super(board, color); // repassar os dados da superclasse
    }

    @Override
    public String toString(){
        // convertendo uma torre para string
        return "B";
    }

    @Override
    public boolean[][] possibleMoves() {
        // Por enquanto implementei a instancia da matriz com todos falso.
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        // Movimentos possíveis da torre
        Position p = new Position(0, 0);

        /**
         * NW
         */
        // verificar diagonal noroeste
        p.setValues(position.getRow() - 1, position.getColumn() - 1);
        // testar que enquanto a posição existir e não tiver uma peça lá
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true; // marco verdadeiro X, indica que essa peça pode mover.
            p.setValues(p.getRow() - 1, p.getColumn() - 1); // a linha da posição irá andar na diagonal noroeste
        }
        // testar se existe uma casa e essa casa existe uma peça adversária, se for marca mais essa casa verdadeira
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) { // vou testar se existe uma casa e essa casa possui uma peça adversária
            mat[p.getRow()][p.getColumn()] = true; // marcar essa casa incluindo o adversário verdadeiro
        }

        /**
         * NE
         */
        // verificar diagonal nordeste
        p.setValues(position.getRow() /*Posição da peça*/ - 1 , position.getColumn() + 1);
        // testar que enquanto a posição existir e não tiver uma peça lá
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)){
            mat[p.getRow()][p.getColumn()] = true; // marco verdadeiro X, indica que essa peça pode mover.
            p.setValues(p.getRow() - 1, p.getColumn() + 1); // a linha da posição irá andar na diagonal nordeste
        }
        // testar se existe uma casa e essa casa existe uma peça adversária, se for marca mais essa casa verdadeira
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) { // vou testar se existe uma casa e essa casa possui uma peça adversária
            mat[p.getRow()][p.getColumn()] = true; // marcar essa casa incluindo o adversário verdadeiro
        }

        /**
         * SE
         */
        // verificar a diagonal sudeste
        p.setValues(position.getRow() + 1, /*Posição da peça*/ position.getColumn() + 1);
        // testar que enquanto a posição existir e não tiver uma peça lá
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)){
            mat[p.getRow()][p.getColumn()] = true; // marco verdadeiro X, indica que essa peça pode mover.
            p.setValues(p.getRow() + 1, p.getColumn() + 1); // a linha da posição irá andar na diagonal sudeste
        }
        // testar se existe uma casa e essa casa existe uma peça adversária, se for marca mais essa casa verdadeira
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) { // vou testar se existe uma casa e essa casa possui uma peça adversária
            mat[p.getRow()][p.getColumn()] = true; // marcar essa casa incluindo o adversário verdadeiro
        }

        // verificar a Baixo da minha peça
        /**
         * SW
         */

        p.setValues(position.getRow() + 1, position.getColumn() - 1);
        // testar que enquanto a posição existir e não tiver uma peça lá
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)){
            mat[p.getRow()][p.getColumn()] = true; // marco verdadeiro X, indica que essa peça pode mover.
            p.setValues(p.getRow() + 1, p.getColumn() - 1); // a linha da posição irá andar pra baixo
        }
        // testar se existe uma casa e essa casa existe uma peça adversária, se for marca mais essa casa verdadeira
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)){ // vou testar se existe uma casa e essa casa possui uma peça adversária
            mat[p.getRow()][p.getColumn()] = true; // marcar essa casa incluindo o adversário verdadeiro
        }

        return mat;
    }
}
