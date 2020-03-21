package chaturanga.player.ai;

import chaturanga.board.Board;
import chaturanga.board.Move;
import chaturanga.piece.Piece;
import chaturanga.player.Player;

public final class StandardBoardEvaluator implements BoardEvaluator {
    private static final int CHECK_MATE_BONUS = 1000;

    @Override
    public int evaluate(final Move blackMove, final Move whiteMove, final Board board, final int depth) {
        int whiteLength = whiteMove.getDestinationCoordinate() - whiteMove.getCurrentCoordinate();
        int blackLength = blackMove.getDestinationCoordinate() - blackMove.getCurrentCoordinate();

        return whiteLength-blackLength;
    }

    private int scorePlayer(final Board board, final Player player, final int depth) {
        return mobility(player);
    }

    private int mobility(final Player player) {
        return player.getLegalMoves().size() + checkMate(player);
    }

    private int checkMate(final Player player) {
        return player.getOpponent().isInCheckMate() ? CHECK_MATE_BONUS : 0;
    }

    public static int pieceValue(final Player player) {
        int pieceValueScore = 0;
        for (final Piece piece : player.getActivePieces()) {
            pieceValueScore += piece.getPieceValue();
        }
        return pieceValueScore;
    }
}
