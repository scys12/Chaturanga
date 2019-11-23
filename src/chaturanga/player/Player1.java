package chaturanga.player;

import chaturanga.Alliance1;
import chaturanga.board.Board1;
import chaturanga.board.Move1;
import chaturanga.piece.Piece1;

import java.util.Collection;

public abstract class Player1 {
    protected final Board1 board;
    protected final Collection<Move1> legalMoves;


    public Player1(final Board1 board, final Collection<Move1> legalMoves, final Collection<Move1> opponentMoves) {
        this.board = board;
        this.legalMoves = legalMoves;

    }

    public boolean isMoveLegal(final Move1 move1) {
        return this.legalMoves.contains(move1);
    }

    public boolean isInCheck() {
        return false;
    }

    public boolean isInCheckMate() {
        return false;
    }

    public MoveTransition1 makeMove(final Move1 move1) {
        return null;
    }

    public abstract Collection<Piece1> getActivePieces();
    public abstract Alliance1 getAlliance();
    public abstract Player1 getOpponent();
}
