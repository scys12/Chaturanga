package chaturanga.board;

import chaturanga.board.Board1.Builder;
import chaturanga.piece.Piece1;

public abstract class Move1 {
    final Board1 board;
    final Piece1 movedPiece;
    final int destinationCoordinate;

    public static final Move1 NULL_MOVE = new NullMove();

    public Move1(final Board1 board, final Piece1 movedPiece, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public int getCurrentCoordinate(){
        return this.getMovedPiece().getPiecePosition();
    }

    public Piece1 getMovedPiece() {
        return this.movedPiece;
    }

    public Board1 execute() {//asumsikan ketika membuat move yang legal, akan ngembaliin board  baru  dengan perintah execute
        final Builder builder = new Builder();
        for (final Piece1 piece1 : this.board.currentPlayer().getActivePieces()) {
            if (!this.movedPiece.equals(piece1)) {
                builder.setPiece(piece1);
            }
        }
        for (final Piece1 piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
        builder.setPiece(this.movedPiece.movePiece(this));//move the moved piece
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
        return builder.build();
    }

    public static final class MajorMove extends Move1{

        public MajorMove(final Board1 board, final Piece1 movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

    }

    public static final class JumpedMove extends Move1{
        public JumpedMove(final Board1 board, final Piece1 movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static final class PawnMove extends Move1{
        public PawnMove(final Board1 board1, final Piece1 movePiece, final int destinationCoordinate){
            super(board1, movePiece, destinationCoordinate);
        }
    }

    public static final class NullMove extends Move1{
        public NullMove(){
            super(null, null,  -1);
        }
        @Override
        public Board1 execute(){
            throw new RuntimeException("Cannot Execute the Null Move");
        }
    }

    public static class MoveFactory{

        private MoveFactory(){
            throw new RuntimeException("Not instantiable!");
        }

        public static Move1 createMove(final Board1 board1, final int currentCoordinate, final int destinationCoordinate){
            for (final Move1 move1 : board1.getAllLegalMoves()){
                if (move1.getCurrentCoordinate() == currentCoordinate &&
                    move1.getDestinationCoordinate() == destinationCoordinate){
                    return move1;
                }
            }
            return NULL_MOVE;
        }
    }

}
