package chaturanga.piece;

import chaturanga.utils.Alliance;
import chaturanga.board.Board;
import chaturanga.board.Move;

import java.util.Collection;
import java.util.Map;

public abstract class Piece {
    protected final PieceType pieceType;
    protected int piecePosition;
    protected final Alliance pieceAlliance;
    protected final int cachedHashCode;

    Piece(final PieceType pieceType, final int piecePosition, final Alliance pieceAlliance) {
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.pieceType = pieceType;
        this.cachedHashCode = computeHashCode();
    }

    private int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        return result;
    }

    @Override
    public int hashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Piece)) {
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() &&
                pieceAlliance == otherPiece.getPieceAlliance();
    }

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public void setPiecePosition(int piecePosition) {
        this.piecePosition = piecePosition;
    }

    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    public int getPieceValue() {
        return this.pieceType.getPieceValue();
    }

    public abstract Collection<Move> calculateLegalMoves (final Board board);

    public abstract Collection<Move> calculateLegalJumpedMoves(final Board board, final int oldPiecePosition, Map<Integer,Boolean> isVisited);

    public abstract Piece movePiece(Move move);//return a new piece that will update to move to its destination

    public enum PieceType {
        PAWN(1,"P" );

        private String pieceName;
        private int pieceValue;

        PieceType(final int pieceValue,final String pieceName) {
            this.pieceName = pieceName;
            this.pieceValue = pieceValue;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        public int getPieceValue() {
            return this.pieceValue;
        }
        public void setPieceValue(int pieceValue) {
            this.pieceValue = pieceValue;
        }
    }
}
