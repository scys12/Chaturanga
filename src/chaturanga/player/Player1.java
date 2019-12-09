package chaturanga.player;

import chaturanga.Alliance1;
import chaturanga.board.Board1;
import chaturanga.board.BoardUtils1;
import chaturanga.board.Move1;
import chaturanga.piece.Piece1;

import java.util.Collection;

public abstract class Player1 {
    protected final Board1 board;
    protected final Collection<Move1> legalMoves;
    private final boolean isInCheck;

    public Player1(final Board1 board, final Collection<Move1> legalMoves, final Collection<Move1> opponentMoves) {
        this.board = board;
        this.legalMoves = legalMoves;
        this.isInCheck = !Player1.getAllPosition(this.board,Alliance1.BLACK, Alliance1.WHITE);
    }

    private static boolean getAllPosition(final Board1 board, final Alliance1 black, final Alliance1 white ) {
        boolean whiteInRightPlace = true;
        boolean blackInRightPlace = true;
        for (int i = 0; i < BoardUtils1.NUM_TILES; i++) {
            if (board.getTile(i).isTileOccupied() && i <= 7) {
                final Piece1 piece = board.getTile(i).getPiece();
                if (piece.getPieceAlliance() != white) {
                    whiteInRightPlace = false;
                }
            } else if (!board.getTile(i).isTileOccupied() && i <= 7) {
                whiteInRightPlace = false;
            }
            if (board.getTile(i).isTileOccupied() && i >= 24) {
                final Piece1 piece = board.getTile(i).getPiece();
                if (piece.getPieceAlliance() != black) {
                    blackInRightPlace = false;
                }
            } else if (!board.getTile(i).isTileOccupied() && i >= 24) {
                blackInRightPlace = false;
            }
        }
        if (blackInRightPlace || whiteInRightPlace) {
            return true;
        }
        return false;
    }

    public boolean isMoveLegal(final Move1 move1) {
        return this.legalMoves.contains(move1);
    }

    public boolean isInCheckMate() {
        return this.isInCheck;
    }


    public abstract Collection<Piece1> getActivePieces();
    public abstract Alliance1 getAlliance();
    public abstract Player1 getOpponent();
    public Collection<Move1> getLegalMoves()  {
        return this.legalMoves;
    }

    public MoveTransition1 makeMove(final Move1 move) {
        //kalau movenya tidak sesuai dengan aturan
        if (!isMoveLegal(move)) {
            return new MoveTransition1(this.board, move, MoveStatus1.ILLEGAL_MOVE);
        }

        final Board1 transitionBoard = move.execute();
        final boolean checkPositionForCheckMate = Player1.getAllPosition(this.board, Alliance1.BLACK, Alliance1.WHITE);
        if (checkPositionForCheckMate) {
            return new MoveTransition1(this.board, move, MoveStatus1.LEAVES_PLAYER_IN_CHECK);
        }
        return new MoveTransition1(transitionBoard, move, MoveStatus1.DONE);

    }
}
