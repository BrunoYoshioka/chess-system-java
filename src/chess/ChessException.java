package chess;

import boardgame.BoardException;

public class ChessException extends BoardException /*Para ser uma excessão opcional de ser tratada*/ {
    private static final long serialVersionUID = 1L;

    public ChessException(String msg){
        super(msg);
    }
}
