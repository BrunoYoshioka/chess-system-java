package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.*;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Partida do Xadrez ( O coração do sistema xadrez )
public class ChessMatch {

    private int turn;
    private Color currentPlayer;
    private Board board; // tem que ter o tabuleiro
    private boolean check; // dizer se está em check
    private boolean checkMate;
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted; // propriedade para promoção (Torre, Bispo, cavaleiro e Rainha) quando a peça peao chegar no fim do tabuleiro

    private List<Piece> piecesOnTheBoard = new ArrayList<>(); // lista de peças no tabuleiro
    private List<Piece> capturedPieces = new ArrayList<>(); // lista de peças capturadas

    // construtor da partida
    public ChessMatch(){
        board = new Board(8, 8); // criei o tabuleiro dimensão 8X8
        turn = 1; // no início da partida ele vale 1
        currentPlayer = Color.WHITE; // no início da partida, começa com branco que é o padrão do jogo
        initialSetup();
    }

    public int getTurn(){ // só terá o método get para que não possa ser alterado
        return turn;
    }

    public Color getCurrentPlayer(){ // só terá o método get para que não possa ser alterado
        return currentPlayer;
    }

    //Expor a propriedade check para testar a aplicação ter acesso na aplicação principal
    public boolean getCheck(){
        return check;
    }

    public boolean getCheckMate(){
        return checkMate;
    }

    public ChessPiece getEnPassantVulnerable(){
        return enPassantVulnerable;
    }

    public ChessPiece getPromoted(){
        return promoted;
    }

    // metodo que retorna uma matriz de peças
    public ChessPiece[][] getPieces(){
        ChessPiece[][] mat = new ChessPiece
                [board.getRows() /*Qtde de linhas do tabuleiro*/ ]
                [board.getColumns() /*Qtde de colunas do tabuleiro*/ ]; // criei a matriz

        // Percorrendo a matriz de peças do tabuleiro
        for (int i = 0; i < board.getRows(); i++ ) /*para cada peça do tabuleiro*/ {
            for (int j = 0; j < board.getColumns(); j++) /*Percorrendo a colunas da matriz*/ {
                mat[i][j] = (ChessPiece) board.piece(i, j);
            }
            // dando um DownCasting do ChessPiece
        }
        return mat; // retorna a matriz de peças da partida de xadrez
    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition){
        Position position = sourcePosition.toPosition(); // converter essa posição de xadrez para uma posição de matriz normal
        validateSourcePosition(position); // validar a posição
        return board.piece(position).possibleMoves(); // retornar os movimentos possíveis da peça dessa posição
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition){
        // convertendo as duas posições na matriz
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        // validar se a posição havia uma peça
        validateSourcePosition(source); // essa operação responsabiliza por validar essa posição de origem, se ela não existir essa operação vai lançar uma excessão
        validateTargetPosition(source, target); // validar posição de destino, recebendo a posição de destino e origem
        Piece capturedPiece = makeMove(source, target); // recebendo a posição de origem e destino

        //testar esse movimento colocou o proprio jogador em check (não pode deixar)
        if (testCheck(currentPlayer)/*Jogador atual*/){
            undoMove(source, target, capturedPiece); // desfazer o movimento
            throw new ChessException("You can't put yourself in check."); // vc não pode se colocar em check
        }

        ChessPiece movedPiece = (ChessPiece)board.piece(target); // Uma referencia para que a peça que se moveu foi para o destino

        // #specialmove promotion
        // Testar a promoção
        // Deve ser testado antes do check, pois, quando o peao chega no final deverá receber a promoção (Torre, Bispo, cavaleiro e Rainha), o adversário pode ficar em check
        promoted = null; // assegurar que estou fazendo novo teste
        if (movedPiece instanceof Pawn) /*Se a peça movida foi um peao*/{
            if ((movedPiece.getColor() == Color.WHITE && target.getRow() == 0) /*A Peça Branca chegou no final*/ ||
            movedPiece.getColor() == Color.BLACK && target.getRow() == 7/*A Peça Preta chegou no final*/) {
                promoted = (ChessPiece)board.piece(target); // A peça promovida foi o peao que chegou no final
                promoted = replacePromotedPiece("Q"); // Trocar o peao por uma peça mais poderosa
            }
        }

        //testar se o oponente ficou em check
        check = (testCheck/*Se o testCheck*/(opponent/*do oponente*/(currentPlayer/*Jogador atual*/))) ? true /*minha partida está em check*/ : false;

        if (testCheckMate(opponent(currentPlayer))){ // Caso a jogada que eu fiz e deixei o oponente em checkmate, o jogo deve terminar
            checkMate = true;
        }
        else { // caso contrário a partida continua
            nextTurn(); // Chamar o próximo turn
        }

        // #specialmove en passant
        if (movedPiece instanceof Pawn /*Testar se a peça movida foi o peao*/ &&
            /*Se o peão foi movido duas casas.*/
            (target.getRow() == source.getRow() - 2 /*Para cima. (Branca)*/ ||
            target.getRow() == source.getRow() + 2) /*Para baixo. (Preta)*/ ){
            enPassantVulnerable = movedPiece; // O peao será marcado como peao vulnerável a En Passant
        }
        else {
            enPassantVulnerable = null;
        }

        return (ChessPiece)capturedPiece; // retornar peça capturada
    }

    public ChessPiece replacePromotedPiece(String type /*Tipo da peça*/ ){
        if (promoted == null) throw new IllegalStateException("There is no piece to be promoted."); // Não há peça para ser promovida
        if (!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")) throw new InvalidParameterException("Invalid type for promotion.");

        Position position = promoted.getChessPosition().toPosition();
        Piece piece = board.removePiece(position);
        piecesOnTheBoard.remove(piece); // Excluir a peça na lista de peças no tabuleiro

        ChessPiece newPiece = newPiece(type, promoted.getColor()); // Instanciar a nova peça promovida
        board.placePiece(newPiece, position); // Colocar a nova peça na posição peça promovida
        piecesOnTheBoard.add(newPiece);

        return newPiece;
    }

    // Método auxiliar para instanciar a peça específica por promover
    private ChessPiece newPiece(String type, Color color){
        if (type.equals("B")) return new Bishop(board, color);
        if (type.equals("N")) return new Knight(board, color);
        if (type.equals("Q")) return new Queen(board, color);
        return new Rook(board, color);
    }

    // Operação makeMove será responsável por realizar o movimento da peça
    private Piece makeMove(Position source, Position target){
        ChessPiece piece = (ChessPiece) board.removePiece(source); // retirei a peça que estava na posição de origem, fazendo downcasting para ChessPiece que é do tipo ChessPiece
        piece.increaseMoveCount(); // quando está em movimento da peça, Incremento a contagem das peças
        Piece capturedPiece = board.removePiece(target); // remover a possível peça que esteja na posição de destino
        // após remoção de peça que estava na posição de origem e remover a possível peça que esteja na posição de destino
        board.placePiece(piece /*Repare que foi feito UpCasting*/ , target); // Colocar a posição que estava na origem lá na posição de destino

        if (capturedPiece != null) { // capturei uma peça
            piecesOnTheBoard.remove(capturedPiece); //remover essa peça na lista de peças no tabuleiro
            capturedPieces.add(capturedPiece); // adicionando essa peça na lista de peças capturadas
        }

        // #specialmove castling kingside rook
        if (piece instanceof King && /*Se a peça que foi movida for uma instancia de Rei*/
        target.getColumn() == source.getColumn() + 2 /*A posição de destino for igual a posição de origem + 2 casas a direita*/){
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3); /*A posição de origem da torre + 3 colunas a direita do Rei*/
            Position targetT = new Position(source.getRow(), source.getColumn() + 1); /*A posição de origem da torre + 1 colunas a direita do Rei*/
            ChessPiece rook = (ChessPiece)board.removePiece(sourceT); // Retirar a torre da posição de origem dela
            board.placePiece(rook, targetT); // colocar essa torre na posição de destino
            rook.increaseMoveCount(); // como a torre foi movida incremento a quantidade de movimentos dela.
        }

        // #specialmove castling queenside rook
        if (piece instanceof King && /*Se a peça que foi movida for uma instancia de Rei*/
                target.getColumn() == source.getColumn() - 2 /*A posição de destino for igual a posição de origem + 2 casas a esquerda*/){
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4); /*A posição de origem da torre + 3 colunas a direita do Rei*/
            Position targetT = new Position(source.getRow(), source.getColumn() - 1); /*A posição de origem da torre + 1 colunas a direita do Rei*/
            ChessPiece rook = (ChessPiece)board.removePiece(sourceT); // Retirar a torre da posição de origem dela
            board.placePiece(rook, targetT); // colocar essa torre na posição de destino
            rook.increaseMoveCount(); // como a torre foi movida incremento a quantidade de movimentos dela.
        }

        // #specialmove en passant
        if (piece instanceof Pawn /*Testar se A peça que for movida ela seja uma instancia de Peao*/){
            if (source.getColumn() != target.getColumn() /*Se a coluna de origem for diferente da coluna do destino, quer dizer que andou pela diagonal*/
            && capturedPiece == null /*Não capturou peça*/){
                Position pawnPosition; //Foi En Passant
                if (piece.getColor() == Color.WHITE /*Se a peça que moveu for a cor da peça branca*/){
                    //A peça que tem que ser capturada, ou seja, o peao preto está em baixo do peao branco
                    pawnPosition = new Position(target.getRow() + 1, target.getColumn());
                }
                else {
                    pawnPosition = new Position(target.getRow() - 1 /*Em cima*/, target.getColumn());
                }
                capturedPiece = board.removePiece(pawnPosition); // removendo o peao do tabuleiro;
                capturedPieces.add(capturedPiece); // Adicionar esse peao na lista de capturadas
                piecesOnTheBoard.remove(capturedPiece); // removendo a peça do tabuleiro
            }
        }

        return capturedPiece;
    }

    // Método desfazer o movimento
    private void undoMove(Position source /*posição de origem*/, Position target /*posição de destino*/, Piece capturedPiece /*Possível peça capturada*/){
        ChessPiece p = (ChessPiece) board.removePiece(target); // tirar a peça que moveu do destino, fazendo downcasting para ChessPiece que é do tipo ChessPiece
        p.decreaseMoveCount(); // quando está desfazendo o movimento da peça, Decremento a contagem das peças
        board.placePiece(p /*Repare que foi feito UpCasting*/ , source); // com a peça tirada, devolver para posição de origem

        if (capturedPiece != null) { // se a peça tinha sido capturada
            board.placePiece(capturedPiece, target); // voltar a peça capturada para posição de destino
            capturedPieces.remove(capturedPiece); // tirar a peça da lista de peças capturadas, e colocar novamente na lista de peças no tabuleiro
            piecesOnTheBoard.add(capturedPiece); // Adicionar essa peça na lista de peças no tabuleiro
        }

        // Desfazendo movimento do Roque da direita
        // #specialmove castling kingside rook
        if (p instanceof King && /*Se a peça que foi movida for uma instancia de Rei*/
                target.getColumn() == source.getColumn() + 2 /*A posição de destino for igual a posição de origem + 2 casas a direita*/){
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3); /*A posição de origem da torre + 3 colunas a direita do Rei*/
            Position targetT = new Position(source.getRow(), source.getColumn() + 1); /*A posição de origem da torre + 1 colunas a direita do Rei*/
            ChessPiece rook = (ChessPiece)board.removePiece(targetT); // Retirar a torre da posição de destino dela
            board.placePiece(rook, sourceT); // devolver essa torre na posição de origem
            rook.decreaseMoveCount(); // decremento a quantidade de movimentos dela.
        }

        // #specialmove castling queenside rook
        if (p instanceof King && /*Se a peça que foi movida for uma instancia de Rei*/
                target.getColumn() == source.getColumn() - 2 /*A posição de destino for igual a posição de origem + 2 casas a esquerda*/){
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4); /*A posição de origem da torre + 3 colunas a direita do Rei*/
            Position targetT = new Position(source.getRow(), source.getColumn() - 1); /*A posição de origem da torre + 1 colunas a direita do Rei*/
            ChessPiece rook = (ChessPiece)board.removePiece(targetT); // Retirar a torre da posição de destino dela
            board.placePiece(rook, sourceT); // devolver essa torre na posição de destino
            rook.decreaseMoveCount(); // como a torre foi movida incremento a quantidade de movimentos dela.
        }

        // #specialmove en passant
        if (p instanceof Pawn /*Testar se A peça que for movida ela seja uma instancia de Peao*/){
            if (source.getColumn() != target.getColumn() /*Se a coluna de origem for diferente da coluna do destino, quer dizer que andou pela diagonal*/
                    && capturedPiece == enPassantVulnerable /*A peça capturada foi a peça vulnerável a en passant*/){
                ChessPiece pawn = (ChessPiece)board.removePiece(target); // removendo a peça no lugar errado
                Position pawnPosition; //Foi En Passant
                if (p.getColor() == Color.WHITE /*Se a peça que moveu for a cor da peça branca*/){
                    //A peça que tem que ser capturada, ou seja, o peao preto está em baixo do peao branco
                    pawnPosition = new Position(3, target.getColumn());
                }
                else {
                    pawnPosition = new Position(4, target.getColumn());
                }
                board.placePiece(pawn, pawnPosition); // Colocar a peça que estava no lugar errado na posição certa
            }
        }
        // desfez a jogada
    }

    private void validateSourcePosition(Position position){
        // testar se não estiver essa peça na posição
        if (!board.thereIsAPiece(position)){
            throw new ChessException("There is no piece on source position"); // não existe peça na posição de origem.
        }
        // verificar se o jogador atual não é igual a cor da peça escolhido.
        if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()){
            throw new ChessException("The chosen piece is not yours"); // A peça escolhida não é sua
        }
        if (!board.piece(position).isThereAnyPossibleMove()){ // testar se não tiver nenhum movimento possíveis nessa peça
            throw new ChessException("There is no possible moves for the chosen piece."); // Não existe movimentos possíveis para essa peça
        }
    }

    private void validateTargetPosition(Position source, Position target){
        // validar se a posição de destino é válida em relação a posição de origem
        if (!board.piece(source).possibleMove(target)) { // testar se a posição de destino ela é o movimento possível em relação a peça que estiver na posição de origem
            throw new ChessException("The chosen piece can´t move to target position."); // A peça escolhida não pode se mover para posição de destino.
        }
    }

    // Método que troca o turn
    private void nextTurn(){
        turn++; // incrementar turn
        // mudar o jogador atual
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private Color opponent(Color color) {
        return (color == Color.WHITE) ? // se essa cor que passei como argumento for igual a cor branca
        Color.BLACK : Color.WHITE;
    }

    private ChessPiece king (Color color) {
        // Procurar na lista de peças em jogo qual que é o rei dessa cor
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color /*Se a cor for = a cor que veio do argumento, entrará na filtragem*/ ).collect(Collectors.toList()); // fazer listagem de uma lista
        // procurar para cada peça na lista
        for (Piece p : list){
            if (p instanceof King){ // se a lista é uma instancia de Rei
                return (ChessPiece)p; // Encontrei o Rei
            }
        }
        throw new IllegalStateException("There is no " + color + " king on the board"); // não existe o rei da cor tal do tabuleiro
    }

    // Testando se o Rei dessa cor está em check
    private boolean testCheck(Color color) {
        Position kingPosition = king(color).getChessPosition().toPosition(); // pegar a posição do rei no formato de matriz
        // Lista de oponente dessa cor, a lista será as peças no tabuleiro filtradas com a cor do oponente desse Rei
        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color) /*Se a cor do oponente for = a cor que veio do argumento, entrará na filtragem*/ ).collect(Collectors.toList()); // fazer listagem de uma lista
        for (Piece p : opponentPieces){ // para cada peça na lista de oponentes
            // Testar se tem movimento possivel que leva a posição do rei
            boolean[][] mat /*A matriz de movimentos possíveis da peça adversária p*/ = p.possibleMoves();
            if (mat[kingPosition.getRow()][kingPosition.getColumn()]){ // se nessa matriz a posição corresponde a posição do rei for verdade, estará em check
                return true;
            }
        }
        return false;
    }

    private boolean testCheckMate(Color color){
        if (!testCheck(color)){ // testar já eliminar a possibilidade desse caso não estar em check
            return false; // Não está em check
        }

        // se todas as peças dessa cor não tiver o movimento possível pra ela que tire do check, ele estará em checkmate
        // Criar uma lista de todas as peças da mesma cor
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color /*Se a cor for = a cor que veio do argumento, entrará na filtragem*/ ).collect(Collectors.toList()); // fazer listagem de uma lista
        // percorrendo todas as peças que pertence na lista
        for (Piece p : list){
            // se existir alguma peça dessa lista que possua o movimento que tira do check // Não esta em checkmate, no caso retorna falso
            boolean[][] mat = p.possibleMoves(); // matriz boleano
            for (int i=0; i<board.getRows(); i++){ // Percorrendo as linhas da matriz
                for (int j=0; j<board.getColumns(); j++){ // Percorrendo as colunas da matriz
                    //para cada elemento da matriz
                    if (mat[i][j]){ //Se essa posição da matriz é um movimento possível que tira do check
                        // Pegar a peça do tipo p, mover para a posição do movimento possível
                        Position source = ((ChessPiece)p).getChessPosition().toPosition();
                        Position target = new Position(i, j);

                        Piece capturedPiece = makeMove(source, target); // realizar o movimento de origem para destino
                        // testar se ainda está em check.
                        boolean testCheck = testCheck(color);
                        undoMove(source, target, capturedPiece);
                        if (!testCheck){ //se não tiver em check, então esse movimento tirou o rei do check e retorna falso
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    // Esse método ele vai receber as coordenadas do xadrez
    private void placeNewPiece(char column, int row, ChessPiece piece){
        board.placePiece(piece, new ChessPosition(column, row).toPosition()); // colocando a peça no tabuleiro
        piecesOnTheBoard.add(piece); // já coloco a peça na lista de peças no tabuleiro
    }

    private void initialSetup(){
        // colocando as posições devidas do jogo inicial do xadrez
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this /*Passar a partida na hora de instanciar o rei, uso de Auto-referência*/));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this /*Mesmo objeto, uso de Auto-referência*/));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this /*Mesmo objeto, uso de Auto-referência*/));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this /*Mesmo objeto, uso de Auto-referência*/));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this /*Mesmo objeto, uso de Auto-referência*/));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this /*Mesmo objeto, uso de Auto-referência*/));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this /*Mesmo objeto, uso de Auto-referência*/));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this /*Mesmo objeto, uso de Auto-referência*/));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this /*Mesmo objeto, uso de Auto-referência*/));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this /*Auto-referência*/));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
    }
}
