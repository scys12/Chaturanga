package chaturanga.player;

import chaturanga.Alliance1;
import chaturanga.board.Board1;
import chaturanga.board.Move1;
import chaturanga.piece.Piece1;

import java.util.Collection;

public class BlackPlayer1 extends Player1{
    public BlackPlayer1(final Board1 board, final Collection<Move1> whiteStandardLegalMoves, final Collection<Move1> blackStandardLegalMoves) {
        super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
    }

    @Override
    public Collection<Piece1> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance1 getAlliance() {
        return Alliance1.BLACK;
    }

    @Override
    public Player1 getOpponent() {
        return this.board.whitePlayer();
    }
}
