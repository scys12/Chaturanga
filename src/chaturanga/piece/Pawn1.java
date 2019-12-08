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
            final int candidateDestinationCoordinate =  this.piecePosition + currentCandidateOffset;

            if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                    isEightColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                continue;
            }

            if (BoardUtils1.isValidTileCoordinate(candidateDestinationCoordinate)) {
                final Tile1 candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                if (!candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Collection<Move1> calculateLegalJumpedMoves(Board1 board, int oldPiecePosition, Map<Integer,Boolean> isVisited) {
        final List<Move1> jumpedMove = new ArrayList<>();
        int countJumped = 0;
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
            final int blockedPiecePosition = oldPiecePosition + currentCandidateOffset;

            if (isFirstColumnExclusion(oldPiecePosition, currentCandidateOffset) ||
                    isEightColumnExclusion(oldPiecePosition, currentCandidateOffset)) {
                continue;
            }

            if (BoardUtils1.isValidTileCoordinate(blockedPiecePosition)) {
                final Tile1 blockedPieceTile = board.getTile(blockedPiecePosition);

                if (blockedPieceTile.isTileOccupied()) {

                    if (isFirstColumnExclusion(blockedPiecePosition, currentCandidateOffset) ||
                            isEightColumnExclusion(blockedPiecePosition, currentCandidateOffset)) {
                        continue;
                    }

                    final int candidateJumpedCoordinate = blockedPiecePosition + currentCandidateOffset;
                    if (BoardUtils1.isValidTileCoordinate(candidateJumpedCoordinate)) {
                        final Tile1 jumpedCoordinateTile = board.getTile(candidateJumpedCoordinate);
                        if (!jumpedCoordinateTile.isTileOccupied()) {
                            if (!isVisited.containsKey(candidateJumpedCoordinate)) {
                                isVisited.put(candidateJumpedCoordinate, true);
                                jumpedMove.add(new JumpedMove(board, this, candidateJumpedCoordinate));
                                System.out.printf("%d %d %d%n",oldPiecePosition, candidateJumpedCoordinate,currentCandidateOffset);
                                jumpedMove.addAll(calculateLegalJumpedMoves(board, candidateJumpedCoordinate, isVisited));
                            }
                        }
                    }
                }
            }
        }
        return ImmutableList.copyOf(jumpedMove);
    }

    @Override
    public Pawn1 movePiece(Move1 move) {
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
