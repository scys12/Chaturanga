package chaturanga.piece;

import chaturanga.Alliance1;
import chaturanga.board.Board1;
import chaturanga.board.Move1;

import java.util.Collection;

public abstract class Piece1 {
    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Alliance1 pieceAlliance;

    Piece1(final PieceType pieceType,final int piecePosition, final Alliance1 pieceAlliance) {
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.pieceType = pieceType;
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
