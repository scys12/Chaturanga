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
        //this.isInCheck = !Player1.getAllPosition(this,) untuk check mate di video 19
    }

    public boolean isMoveLegal(final Move1 move1) {
        return this.legalMoves.contains(move1);
    }

//    public boolean isInCheckMate() {
//        return pieceFilledEightTile();
//    }

//    private boolean pieceFilledEightTile() {
//        for
//    }

    /*public MoveTransition1 makeMove(final Move1 move) {
        if (!isMoveLegal(move)) {
            return new MoveTransition1(this.board, move, MoveStatus1.ILLEGAL_MOVE);
        }
        final Board1 transitionBoard = move.execute();
        final Collection<Move1> kingAttacks = Player1.calculateAttacksOntile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.currentPlayer().getLegalMoves);
        return new MoveTransition1(transitionBoard, move, MoveStatus1.DONE);
    }*/

    public abstract Collection<Piece1> getActivePieces();
    public abstract Alliance1 getAlliance();
    public abstract Player1 getOpponent();
}
