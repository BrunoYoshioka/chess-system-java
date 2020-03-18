package chess;

import boardgame.Position;

public class ChessPosition {

    private char column;
    private int row;

    public ChessPosition(char column, int row) {
        if (column < 'a' || column > 'h' || row < 1 || row > 8){
            throw new ChessException("Error instantiating ChessPosition. Valid values are from a1 to a8."); // Erro ao instanciar o ChessPosition. Os valores válidos são de a1 a a8
        }
        this.column = column;
        this.row = row;
    }

    public char getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    // as linhas e colunas não serão livremente alteradas por isso não é necessário ter os sets

    protected Position toPosition(){
        return new Position(8 - row, column - 'a');
    }

    protected static ChessPosition fromPosition(Position position){
        return new ChessPosition((char)('a' + position.getColumn()), 8 - position.getRow()); // O código da letra + a coluna da matriz
    }

    @Override
    public String toString(){
        // Imprimir a posição de xadrez na ordem
        return "" + column + row;
    }
}
