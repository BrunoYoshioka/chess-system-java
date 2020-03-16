package boardgame;

// classe tabuleiro
public class Board {
    private int rows;
    private int columns;
    private Piece[][] pieces;

    public Board(int rows, int columns) {
        if (rows < 1 || columns < 1){
            throw new BoardException("Error creating board: there be at least 1 row and 1 column"); // Erro criando o tabuleiro: É necessário que haja pelo menos 1 linha e 1 coluna.
        }
        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns];
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Piece piece(int row, int column){
        if (!positionExists(row, column)){ // se essa posição não existir
            throw new BoardException("Position not on the board"); // lançar nova BoardException personalizada: Posição fora do quadro
        }
        return pieces[row][column];
    }

    public Piece piece(Position position){
        if (!positionExists(position)){ // se essa posição não existir
            throw new BoardException("Position not on the board"); // lançar nova BoardException personalizada: Posição fora do quadro
        }
        return pieces[position.getRow()][position.getColumn()];
    }

    // método de colocar uma peça numa dada posição
    public void placePiece(Piece piece, Position position){
        if (thereIsAPiece(position)){ // Se tiver a peça na posição
            throw new BoardException("There is already a piece on position " + position); // Já existe uma peça na posição
        }
        pieces[position.getRow()][position.getColumn()] = piece;
        // ter acesso livremente a peça
        piece.position = position;
    }

    public Piece removePiece(Position position){
        if (!positionExists(position)){ // Se existe essa posição
            throw new BoardException("Position not on the board"); // lançar nova BoardException personalizada: Posição fora do quadro
        }
        if (piece(position) == null){ // Se a peça na posição for nulo
            return null; // não tem nenhuma posição, simplesmente retorno nulo
        }
        // fazer o procedimento de retirar a peça
        Piece aux = piece(position);
        aux.position = null; // peça foi retirada
        pieces[position.getRow()][position.getColumn()] = null; // Essa matriz de peças na linha será nulo
        return aux;
    }

    // implementando essa método booleano para testar pela linha e coluna
    private boolean positionExists(int row, int column){
        // posição deve estar dentro do tabuleiro
        // essa linha deve ser menor do que altura do tabuleiro
        // a coluna deve ser menor que a qtde de colunas do tabuleiro
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }

    public boolean positionExists(Position position){
        // reaproveitando o método privado positionExists
        return positionExists(position.getRow(), position.getColumn());
    }

    // testar se tem a peça
    public boolean thereIsAPiece(Position position){
        if (!positionExists(position)){ // se essa posição não existir
            throw new BoardException("Position not on the board"); // lançar nova BoardException personalizada: Posição fora do quadro
        }
        // a peça que estiver na posição é diferente de nulo
        return piece(position) != null;
    }
}
