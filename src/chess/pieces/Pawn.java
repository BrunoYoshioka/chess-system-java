package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {

    private ChessMatch chessMatch;

    public Pawn(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch; // Associação
    }

    @Override
    public boolean[][] possibleMoves() {
        // Por enquanto implementei a instancia da matriz auxiliar com todos falso.
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        // Movimentos possíveis do Peao
        Position p = new Position(0, 0);

        // Tratando a regra do Peao Branco
        if (getColor() == Color.WHITE){ // testar se a cor da peça for branca
            p.setValues(position.getRow() - 1, position.getColumn()); // Uma casa acima
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)){ // Se a posição de uma linha acima existir e não tiver uma peça lá
                mat[p.getRow()][p.getColumn()] = true; // marco verdadeiro, indica que essa peça pode mover.
            }
            p.setValues(position.getRow() - 2, position.getColumn()); // Duas casas acima
            Position p2 = new Position(position.getRow() - 1, position.getColumn()); // Testar uma casa acima livre também
            if (getBoard().positionExists(p) /* Se a posição da segunda casa acima existir */ &&
                    !getBoard().thereIsAPiece(p) /* Não tiver uma peça na segunda casa acima */ &&
                    getBoard().positionExists(p2) /* A posição de uma linha acima existir */ &&
                    !getBoard().thereIsAPiece(p2) && /* Não tiver a peça na primeira casa acima*/
                    getMoveCount() == 0 /* Não houver movimento dessa peça (apenas primeira vez)*/ ){
                mat[p.getRow()][p.getColumn()] = true; // marco verdadeiro, indica que essa peça pode mover.
            }
            // testar as casas diagonais
            // Se houver peça adversária uma das diagonais, o peao pode mover
            p.setValues(position.getRow() - 1, position.getColumn() - 1); // Uma casa acima esquerda
            if (getBoard().positionExists(p) /*Se a posição de uma linha acima esquerda existir*/ &&
                    isThereOpponentPiece(p) /*E se Tiver uma peça adversária lá*/ ){
                mat[p.getRow()][p.getColumn()] = true; // marco verdadeiro, indica que essa peça pode mover capturando a peça adversária.
            }
            p.setValues(position.getRow() - 1, position.getColumn() + 1); // Uma casa acima direita
            if (getBoard().positionExists(p) /*Se a posição de uma linha acima direita existir*/ &&
                    isThereOpponentPiece(p) /*E se Tiver uma peça adversária lá*/ ){
                mat[p.getRow()][p.getColumn()] = true; // marco verdadeiro, indica que essa peça pode mover capturando a peça adversária.
            }

            // #specialmove en passant white
            if (position.getRow() == 3 /*Para brancas só pode fazer en passant se tiver na linha 5 (que é 3 na matriz)*/ ){
                Position left /*verificar a esquerda*/ = new Position(position.getRow(), position.getColumn() - 1);
                if (getBoard().positionExists(left) /*Testando se a posição da esquerda existe*/ &&
                        isThereOpponentPiece(left) /*Verifica se Tem a peça esquerda do oponente*/ &&
                        getBoard().piece(left) == chessMatch.getEnPassantVulnerable() /*Se essa peça esta vulnerável a tomar En Passant*/ ){
                    // O Peao pode capturar a peça da esquerda
                    mat[left.getRow() - 1][left.getColumn()] = true; // posição possível para o peao mover
                }
                Position right /*verificar a direita*/ = new Position(position.getRow(), position.getColumn() + 1);
                if (getBoard().positionExists(right) /*Testando se a posição da direita existe*/ &&
                        isThereOpponentPiece(right) /*Verifica se Tem a peça adversária do oponente */ &&
                        getBoard().piece(right) == chessMatch.getEnPassantVulnerable() /*Se essa peça esta vulnerável a tomar En Passant*/ ){
                    // O Peao pode capturar a peça da direita
                    mat[right.getRow() - 1][right.getColumn()] = true; // posição possível para o peao mover
                }
            }
        }
        else { // É preto
            p.setValues(position.getRow() + 1, position.getColumn()); // Uma casa abaixo
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)){ // Se a posição de uma linha abaixo existir e não tiver uma peça lá
                mat[p.getRow()][p.getColumn()] = true; // marco verdadeiro, indica que essa peça pode mover.
            }
            p.setValues(position.getRow() + 2, position.getColumn()); // Duas casas abaixo
            Position p2 = new Position(position.getRow() + 1, position.getColumn()); // Testar uma casa abaixo se é livre também
            if (getBoard().positionExists(p) /* Se a posição da segunda casa abaixo existir */ &&
                    !getBoard().thereIsAPiece(p) /* Não tiver uma peça na segunda casa abaixo */ &&
                    getBoard().positionExists(p2) /* A posição de uma linha abaixo existir */ &&
                    !getBoard().thereIsAPiece(p2) && /* Não tiver a peça na primeira casa abaixo*/
                    getMoveCount() == 0 /* Não houver movimento dessa peça (apenas primeira vez)*/ ){
                mat[p.getRow()][p.getColumn()] = true; // marco verdadeiro, indica que essa peça pode mover.
            }
            // testar as casas diagonais
            // Se houver peça adversária uma das diagonais, o peao pode mover
            p.setValues(position.getRow() + 1, position.getColumn() - 1); // Uma casa abaixo esquerda
            if (getBoard().positionExists(p) /*Se a posição de uma linha abaixo esquerda existir*/ &&
                    isThereOpponentPiece(p) /*E se Tiver uma peça adversária lá*/ ){
                mat[p.getRow()][p.getColumn()] = true; // marco verdadeiro, indica que essa peça pode mover capturando a peça adversária.
            }
            p.setValues(position.getRow() + 1, position.getColumn() + 1); // Uma casa abaixo direita
            if (getBoard().positionExists(p) /*Se a posição de uma linha abaixo direita existir*/ &&
                    isThereOpponentPiece(p) /*E se Tiver uma peça adversária lá*/ ){
                mat[p.getRow()][p.getColumn()] = true; // marco verdadeiro, indica que essa peça pode mover capturando a peça adversária.
            }

            // #specialmove en passant black
            if (position.getRow() == 4 /*Para pretas só pode fazer en passant se tiver na linha 4 (que é 4 na matriz)*/ ){
                Position left /*verificar a esquerda*/ = new Position(position.getRow(), position.getColumn() - 1);
                if (getBoard().positionExists(left) /*Testando se a posição da esquerda existe*/ &&
                        isThereOpponentPiece(left) /*Verifica se Tem a peça esquerda do oponente*/ &&
                        getBoard().piece(left) == chessMatch.getEnPassantVulnerable() /*Se essa peça esta vulnerável a tomar En Passant*/ ){
                    // O Peao pode capturar a peça da esquerda
                    mat[left.getRow() + 1][left.getColumn()] = true; // posição possível para o peao mover
                }
                Position right /*verificar a direita*/ = new Position(position.getRow(), position.getColumn() + 1);
                if (getBoard().positionExists(right) /*Testando se a posição da direita existe*/ &&
                        isThereOpponentPiece(right) /*Verifica se Tem a peça adversária do oponente */ &&
                        getBoard().piece(right) == chessMatch.getEnPassantVulnerable() /*Se essa peça esta vulnerável a tomar En Passant*/ ){
                    // O Peao pode capturar a peça da direita
                    mat[right.getRow() + 1][right.getColumn()] = true; // posição possível para o peao mover
                }
            }
        }
        return mat;
    }
    @Override
    public String toString() {
        return "P";
    }
}
