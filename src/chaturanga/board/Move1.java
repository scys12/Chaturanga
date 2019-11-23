package chaturanga.board;

import chaturanga.piece.Piece1;

public abstract class Move1 {
    final Board1 board;
    final Piece1 movedPiece;
    final int destinationCoordinate;

    public Move1(final Board1 board, final Piece1 movedPiece, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public static final class MajorMove extends Move1{

        public MajorMove(final Board1 board, final Piece1 movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

    }

}
