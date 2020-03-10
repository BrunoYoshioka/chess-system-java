package boardgame;

public class BoardException extends RuntimeException {
    private final static long serialVersionUID = 1L;

    public BoardException(String msg){
        super(msg);
    }
}
