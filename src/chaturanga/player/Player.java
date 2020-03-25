package chaturanga.player;

import chaturanga.utils.Alliance;
import chaturanga.board.Board;
import chaturanga.board.BoardUtils;
import chaturanga.board.Move;
import chaturanga.piece.Piece;

import java.util.Collection;

public abstract class Player {
    protected final Board board;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;

    public Player(final Board board, final Collection<Move> legalMoves, final Collection<Move> opponentMoves) {
        this.board = board;
        this.legalMoves = legalMoves;
        this.isInCheck = Player.getAllPosition(this.board, Alliance.BLACK, Alliance.WHITE);
    }

    private static boolean getAllPosition(final Board board, final Alliance black, final Alliance white ) {
        boolean whiteInRightPlace = true;
        boolean blackInRightPlace = true;
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            if (board.getTile(i).isTileOccupied() && i <= 7) {
                final Piece piece = board.getTile(i).getPiece();
                if (piece.getPieceAlliance() != white) {
                    whiteInRightPlace = false;
                }
            } else if (!board.getTile(i).isTileOccupied() && i <= 7) {
                whiteInRightPlace = false;
            }
            if (board.getTile(i).isTileOccupied() && i >= 24) {
                final Piece piece = board.getTile(i).getPiece();
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

    public boolean isMoveLegal(final Move move) {
        return this.legalMoves.contains(move);
    }

    public boolean isInCheckMate() {
        return Player.getAllPosition(this.board, Alliance.BLACK, Alliance.WHITE);
    }


    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
    public Collection<Move> getLegalMoves()  {
        return this.legalMoves;
    }

    public MoveTransition makeMove(final Move move) {
        //kalau movenya tidak sesuai dengan aturan
        if (!isMoveLegal(move)) {
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        final Board transitionBoard = move.execute();
        final boolean checkPositionForCheckMate = Player.getAllPosition(this.board, Alliance.BLACK, Alliance.WHITE);
        if (checkPositionForCheckMate) {
            return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);

    }
}
