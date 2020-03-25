package chaturanga.player.ai;

import chaturanga.board.Board;
import chaturanga.board.BoardUtils;
import chaturanga.board.Move;
import chaturanga.piece.Piece;
import chaturanga.player.Player;

import java.util.List;
import java.util.PriorityQueue;

public final class StandardBoardEvaluator implements BoardEvaluator {
    private static final int CHECK_MATE_BONUS = 1000;
    private static final int CHECK_MATE_POSITION = -100;
    private static final int BOUNDS_POSITION = -100;

    @Override
    public int evaluate(final Move blackMove, final Move whiteMove, final Board board, final int depth) {

        return scorePlayer(whiteMove, board, board.whitePlayer(), depth) - scorePlayer(blackMove, board, board.blackPlayer(), depth);
    }

    private int scorePlayer(final Move move,final Board board, final Player player, final int depth) {
        return rowScore(move)+checkMate(player)+checkPosition(move)+boundsPosition(player,move);
    }

    private int boundsPosition(Player player, Move move) {
        PriorityQueue<Integer> pieceRowPosition = new PriorityQueue<>();
        for (Piece piece : player.getActivePieces()) {
            if (pieceRowPosition.isEmpty()) pieceRowPosition.add(BoardUtils.checkRow(piece.getPiecePosition()));
            else if (!pieceRowPosition.contains(BoardUtils.checkRow(piece.getPiecePosition()))) pieceRowPosition.add(BoardUtils.checkRow(piece.getPiecePosition()));
        }
        int firstElement = pieceRowPosition.poll();
        int secondElement = pieceRowPosition.poll();
        if ((secondElement-firstElement) >= 3) return BOUNDS_POSITION;
        else return 0;
    }

    private int rowScore(final Move move) {
        int destinationRow = BoardUtils.checkRow(move.getDestinationCoordinate());
        int currentRow = BoardUtils.checkRow(move.getCurrentCoordinate());
        return destinationRow-currentRow;
    }

    private int checkMate(final Player player) {
        return player.getOpponent().isInCheckMate() ? CHECK_MATE_BONUS : 0;
    }

    private int checkPosition(final Move move) {
        if (BoardUtils.checkRow(move.getCurrentCoordinate()) == 7 || BoardUtils.checkRow(move.getCurrentCoordinate()) == 8) {
            return CHECK_MATE_POSITION;
        }
        else return 0;
    }
}
