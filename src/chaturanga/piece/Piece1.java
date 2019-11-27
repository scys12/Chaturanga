package chaturanga.piece;

import chaturanga.Alliance1;
import chaturanga.board.Board1;
import chaturanga.board.Move1;

import java.util.Collection;

public abstract class Piece1 {
    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Alliance1 pieceAlliance;
    protected final int cachedHashCode;
    protected final boolean isFirstMove;

    Piece1(final PieceType pieceType, final int piecePosition, final Alliance1 pieceAlliance) {
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.pieceType = pieceType;
        this.isFirstMove = false;
        this.cachedHashCode = computeHashCode();
    }

    private int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Piece1)) {
            return false;
        }
        final Piece1 otherPiece = (Piece1) other;
        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() &&
                pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove;
    }

    @Override
    public int hashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public Alliance1 getPieceAlliance() {
        return this.pieceAlliance;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    public abstract Collection<Move1> calculateLegalMoves (final Board1 board);

    public abstract Piece1 movePiece(Move1 move);//return a new piece that will update to move to its destination

    public enum PieceType {

        PAWN("P");
        private String pieceName;

        PieceType(final String pieceName) {
            this.pieceName = pieceName;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }
    }
}
