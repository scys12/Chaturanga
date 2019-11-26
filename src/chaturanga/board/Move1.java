package chaturanga.board;

import chaturanga.board.Board1.Builder;
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
        final Piece1 jumpedPiece;

        public JumpedMove(final Board1 board, final Piece1 movedPiece, final int destinationCoordinate, final Piece1 jumpedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.jumpedPiece = jumpedPiece
        }

        public Board1 execute() {
            return null;
        }
    }

}
