package boardgame;

public abstract class Piece {

    protected Position position; // está protegido para que não seja visível na camada de xadrez
    private Board board;

    public Piece(Board board) {
        this.board = board;
        position = null;
    }

    protected Board getBoard() {
        return board;
    }

    public abstract boolean[][] possibleMoves(); // método abstrato possibleMoves que retorna uma matriz de boleano

    // testar se a peça pode mover para uma dada posição
    // método concreto
    public boolean possibleMove(Position position){
        return possibleMoves()[position.getRow()][position.getColumn()]; // utilizando método abstrato
    }

    // Operação para contar se Existe pelo menos um movimento possível para essa peça, é útil para ver se essa paça está travada (sem movimento)
    public boolean isThereAnyPossibleMove(){
        // chamar o método abstrato possibleMoves que retorna uma matriz de boleano varrendo essa matriz para verificar se existe pelo menos uma posição da matriz que seja verdadeira
        boolean[][] mat = possibleMoves();
        for (int i=0; i < mat.length; i++){
            for (int j=0; j < mat.length; j++){
                if (mat[i][j]){ // se a matriz for verdade
                    return true;
                }
            }
        }
        return false;
    }
}
