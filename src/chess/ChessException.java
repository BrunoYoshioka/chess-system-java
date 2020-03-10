package chess;

public class ChessException extends RuntimeException /*Para ser uma excessão opcional de ser tratada*/ {
    private static final long serialVersionUID = 1L;

    public ChessException(String msg){
        super(msg);
    }
}
