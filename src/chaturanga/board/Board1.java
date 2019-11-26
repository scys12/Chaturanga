package chaturanga.board;

import chaturanga.Alliance1;
import chaturanga.piece.Pawn1;
import chaturanga.piece.Piece1;
import chaturanga.player.BlackPlayer1;
import chaturanga.player.Player1;
import chaturanga.player.WhitePlayer1;
import com.google.common.collect.ImmutableList;

import java.util.*;

public class Board1 {

    private final List<Tile1> gameBoard;//you cant really have an immutable array in Java but you can have an immutable list
    private final Collection<Piece1> whitePieces;
    private final Collection<Piece1> blackPieces;

    private final WhitePlayer1 whitePlayer;
    private final BlackPlayer1 blackPlayer;
    private final Player1 currentPlayer;

    public Board1(final Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, Alliance1.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Alliance1.BLACK);

        final Collection<Move1> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move1> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);

        this.whitePlayer = new WhitePlayer1(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer1(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < BoardUtils1.NUM_TILES; i++) {
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if ((i + 1) % BoardUtils1.NUM_TILES_PER_ROW == 0) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    public Player1 blackPlayer() {
        return this.blackPlayer;
    }

    public Player1 whitePlayer() {
        return this.whitePlayer;
    }

    public Collection<Piece1> getBlackPieces() {
        return this.blackPieces;
    }

    public Player1 currentPlayer() {
        return this.currentPlayer;
    }

    public Collection<Piece1> getWhitePieces() {
        return this.whitePieces;
    }
    public Collection<Move1> calculateLegalMoves(final Collection<Piece1> pieces) {
        final List<Move1> legalMoves = new ArrayList<>();

        for (final Piece1 piece : pieces) {
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return ImmutableList.copyOf(legalMoves);
    }

    private static Collection<Piece1> calculateActivePieces(final List<Tile1> gameBoard, final Alliance1 alliance) {
        final List<Piece1> activePieces = new ArrayList<>();
        for (final Tile1 tile : gameBoard) {
            if (tile.isTileOccupied()) {
                final Piece1 piece = tile.getPiece();
                if (piece.getPieceAlliance() == alliance) {
                    activePieces.add(piece);
                }
            }
        }
        return ImmutableList.copyOf(activePieces);
    }

    public Tile1 getTile(final int tileCoordinate) {
        return gameBoard.get(tileCoordinate);
    }

    private static List<Tile1> createGameBoard(final Builder builder) {
        final Tile1[] tiles = new Tile1[BoardUtils1.NUM_TILES];
        for (int i = 0; i < BoardUtils1.NUM_TILES; i++) {
            tiles[i] = Tile1.createTile(i, builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }

    public static Board1 createStandardBoard() {
        final Builder builder = new Builder();
        //Black Layout
        builder.setPiece(new Pawn1(Alliance1.BLACK, 0));
        builder.setPiece(new Pawn1(Alliance1.BLACK, 1));
        builder.setPiece(new Pawn1(Alliance1.BLACK, 2));
        builder.setPiece(new Pawn1(Alliance1.BLACK, 3));
        builder.setPiece(new Pawn1(Alliance1.BLACK, 4));
        builder.setPiece(new Pawn1(Alliance1.BLACK, 5));
        builder.setPiece(new Pawn1(Alliance1.BLACK, 6));
        builder.setPiece(new Pawn1(Alliance1.BLACK, 7));

        //White Layout
        builder.setPiece(new Pawn1(Alliance1.WHITE, 24));
        builder.setPiece(new Pawn1(Alliance1.WHITE, 25));
        builder.setPiece(new Pawn1(Alliance1.WHITE, 26));
        builder.setPiece(new Pawn1(Alliance1.WHITE, 27));
        builder.setPiece(new Pawn1(Alliance1.WHITE, 28));
        builder.setPiece(new Pawn1(Alliance1.WHITE, 29));
        builder.setPiece(new Pawn1(Alliance1.WHITE, 30));
        builder.setPiece(new Pawn1(Alliance1.WHITE, 31));
        builder.setMoveMaker(Alliance1.WHITE);
        //white to move
        return builder.build();
    }

    public static class Builder {
        Map<Integer, Piece1> boardConfig;//untuk ngemap tile ID dari chess board ke piece2
        Alliance1 nextMoveMaker;//track person whose turn to move

        public Builder() {
            this.boardConfig = new HashMap<>();
        }

        public Builder setPiece(final Piece1 piece) {
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        public Builder setMoveMaker(final Alliance1 nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public Board1 build() {
            return new Board1(this);
        }
    }
}

