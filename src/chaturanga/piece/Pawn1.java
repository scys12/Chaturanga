package chaturanga.piece;

import chaturanga.Alliance1;
import chaturanga.board.Board1;
import chaturanga.board.BoardUtils1;
import chaturanga.board.Move1;
import chaturanga.board.Move1.MajorMove;
import chaturanga.board.Tile1;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn1 extends Piece1 {
    public static final int[] CANDIDATE_MOVE_COORDINATE = {-5, -4, -3, -1, 1, 3, 4, 5};

    public Pawn1(final Alliance1 pieceAlliance, final int piecePosition) {
        super(PieceType.PAWN,piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move1> calculateLegalMoves(Board1 board) {
        final List<Move1> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                    isEightColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                continue;
            }

            if (BoardUtils1.isValidTileCoordinate(candidateDestinationCoordinate)) {
                final Tile1 candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                if (!candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    System.out.println("ac");
                } else {
                    //kalau ada pion didepan belakang samping kiri kanan
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
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
