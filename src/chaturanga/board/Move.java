package chaturanga.board;

import chaturanga.board.Board.Builder;
import chaturanga.piece.Piece;

public abstract class Move {
    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;

    public static final Move NULL_MOVE = new NullMove();

    public Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {
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

    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    public Board execute() {//asumsikan ketika membuat move yang legal, akan ngembaliin board  baru  dengan perintah execute
        final Builder builder = new Builder();
        for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
            if (!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
        builder.setPiece(this.movedPiece.movePiece(this));//move the moved piece
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
        return builder.build();
    }

    public static final class MajorMove extends Move {

        public MajorMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

    }

    public static final class JumpedMove extends Move {
        public JumpedMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static final class PawnMove extends Move {
        public PawnMove(final Board board, final Piece movePiece, final int destinationCoordinate){
            super(board, movePiece, destinationCoordinate);
        }
    }

    public static final class NullMove extends Move {
        public NullMove(){
            super(null, null,  -1);
        }
        @Override
        public Board execute(){
            throw new RuntimeException("Cannot Execute the Null Move");
        }
    }

    public static class MoveFactory{

        private MoveFactory(){
            throw new RuntimeException("Not instantiable!");
        }

        public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate){
            for (final Move move : board.getAllLegalMoves()){
                if (move.getCurrentCoordinate() == currentCoordinate &&
                    move.getDestinationCoordinate() == destinationCoordinate){
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }

}
