package chaturanga.piece;

import chaturanga.Alliance1;
import chaturanga.board.*;
import chaturanga.board.Move1.MajorMove;
import com.google.common.collect.ImmutableList;

import java.util.*;

import static chaturanga.board.BreadthFirstMove.*;
import static chaturanga.board.Move1.*;

public class Pawn1 extends Piece1 {
    public static final int[] CANDIDATE_MOVE_COORDINATE = {-5, -4, -3, -1, 1, 3, 4, 5};

    public Pawn1(final Alliance1 pieceAlliance, final int piecePosition) {
        super(PieceType.PAWN,piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move1> calculateLegalMoves(Board1 board) {
        //menghitung berapa banyak move yang boleh dilakukan suatu pion
        final List<Move1> legalMoves = new ArrayList<>();
        boolean canJumped;
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
            canJumped = true;
            final int candidateDestinationCoordinate =  piecePosition + currentCandidateOffset;

            if (isFirstColumnExclusion(piecePosition, currentCandidateOffset) ||
                    isEightColumnExclusion(piecePosition, currentCandidateOffset)) {
                continue;
            }

            if (BoardUtils1.isValidTileCoordinate(candidateDestinationCoordinate)) {
                final Tile1 candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                if (!candidateDestinationTile.isTileOccupied()) {
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    final int candidateJumpedCoordinate = candidateDestinationCoordinate + currentCandidateOffset;
                    if (BoardUtils1.isValidTileCoordinate(candidateJumpedCoordinate)) {
                        final Tile1 candidateJumpedTile = board.getTile(candidateJumpedCoordinate);

                        if (isFirstColumnExclusion(candidateDestinationCoordinate, currentCandidateOffset) ||
                                isEightColumnExclusion(candidateDestinationCoordinate, currentCandidateOffset)) {
                            canJumped = false;
                        }
                        if (!candidateJumpedTile.isTileOccupied() && canJumped) {
                            legalMoves.add(new JumpedMove(board, this, candidateJumpedCoordinate));
                            legalMoves.addAll(calculateJumpedMove(board, this, this.piecePosition, candidateJumpedCoordinate));

                        }
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    private Collection<Move1> calculateJumpedMove(Board1 board, Piece1 piece, int initialPosition, int newInitialCandidate) {
        final List<Move1> jumpedMoves = new ArrayList<>();
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
            final int candidateBlockedCoordinate = newInitialCandidate + currentCandidateOffset;
            final int candidateDestinationCoordinate = newInitialCandidate + currentCandidateOffset * 2;
            if (isFirstColumnExclusion(newInitialCandidate, currentCandidateOffset) ||
                    isEightColumnExclusion(newInitialCandidate, currentCandidateOffset) ||
                    isFirstColumnExclusion(candidateBlockedCoordinate,currentCandidateOffset)||
                    isEightColumnExclusion(candidateBlockedCoordinate,currentCandidateOffset)) {
                continue;
            }
            if (BoardUtils1.isValidTileCoordinate(candidateDestinationCoordinate)) {
                final Tile1 candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                final Tile1 candidateBlockedTile = board.getTile(candidateBlockedCoordinate);

                if (!candidateDestinationTile.isTileOccupied() && candidateBlockedTile.isTileOccupied()) {
                    jumpedMoves.add(new JumpedMove(board, this, candidateDestinationCoordinate));
                }
            }
        }
        return ImmutableList.copyOf(jumpedMoves);
    }

    @Override
    public Pawn1 movePiece(Move1 move) {
        //
        return new Pawn1(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils1.FIRST_COLUMN[currentPosition] && (candidateOffset == -5 || candidateOffset == -1 ||
                candidateOffset == 3);
    }

    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils1.FOURTH_COLUMN[currentPosition] && (candidateOffset == -3 || candidateOffset == 1 ||
                candidateOffset == 5);
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }
}
