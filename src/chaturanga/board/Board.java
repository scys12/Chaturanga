package chaturanga.board;

import chaturanga.utils.Alliance;
import chaturanga.piece.Pawn;
import chaturanga.piece.Piece;
import chaturanga.player.BlackPlayer;
import chaturanga.player.Player;
import chaturanga.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;

public class Board {

    public static boolean isFirstMove;
    private final List<Tile> gameBoard;//you cant really have an immutable array in Java but you can have an immutable list
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;

    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private Player currentPlayer;

    public Board(final Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);

        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);

        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);

        //hasil dari next move maker berupa Alliance.WHITE/BLACK nantinya di class Alliance1 direturn
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
    }

    public Player blackPlayer() {
        return this.blackPlayer;
    }

    public Player whitePlayer() {
        return this.whitePlayer;
    }

    public Collection<Piece> getBlackPieces() {
        return this.blackPieces;
    }

    public Player currentPlayer() {
        return this.currentPlayer;
    }

    public void setPlayer(final Player player) {
        this.currentPlayer = player;
    }

    public Collection<Piece> getWhitePieces() {
        return this.whitePieces;
    }
    public Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
        //menghitung gerakan yang boleh dilakukan suatu pion dan ditambah semua
        final List<Move> legalMoves = new ArrayList<>();

        for (final Piece piece : pieces) {
            legalMoves.addAll(piece.calculateLegalMoves(this));
            legalMoves.addAll(piece.calculateLegalJumpedMoves(this, piece.getPiecePosition(), new HashMap<Integer, Boolean>()));
        }
        return ImmutableList.copyOf(legalMoves);
    }

    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Alliance alliance) {
        //ngehitung berapa banyak pion yang masih aktif di papan catur dan dilist
        final List<Piece> activePieces = new ArrayList<>();
        for (final Tile tile : gameBoard) {
            if (tile.isTileOccupied()) {
                final Piece piece = tile.getPiece();
                if (piece.getPieceAlliance() == alliance) {
                    activePieces.add(piece);
                }
            }
        }
        return ImmutableList.copyOf(activePieces);
    }

    public Tile getTile(final int tileCoordinate) {
        return gameBoard.get(tileCoordinate);
    }

    private static List<Tile> createGameBoard(final Builder builder) {
        //membuat list yang berisi tile2 sebanyak 32 yg merepresentasikan papan catur dan di loop ini
        //kita akan melihat jika tile nya ada pion diatasnya atau enggak
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {

            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }

    public static Board createStandardBoard() {
        //mengisi board dengan pion2
        final Builder builder = new Builder();
        //Black Layout
        builder.setPiece(new Pawn(Alliance.BLACK, 0));
        builder.setPiece(new Pawn(Alliance.BLACK, 1));
        builder.setPiece(new Pawn(Alliance.BLACK, 2));
        builder.setPiece(new Pawn(Alliance.BLACK, 3));
        builder.setPiece(new Pawn(Alliance.BLACK, 4));
        builder.setPiece(new Pawn(Alliance.BLACK, 5));
        builder.setPiece(new Pawn(Alliance.BLACK, 6));
        builder.setPiece(new Pawn(Alliance.BLACK, 7));

        //White Layout
        builder.setPiece(new Pawn(Alliance.WHITE, 24));
        builder.setPiece(new Pawn(Alliance.WHITE, 25));
        builder.setPiece(new Pawn(Alliance.WHITE, 26));
        builder.setPiece(new Pawn(Alliance.WHITE, 27));
        builder.setPiece(new Pawn(Alliance.WHITE, 28));
        builder.setPiece(new Pawn(Alliance.WHITE, 29));
        builder.setPiece(new Pawn(Alliance.WHITE, 30));
        builder.setPiece(new Pawn(Alliance.WHITE, 31));
        builder.setMoveMaker(Alliance.WHITE);

        isFirstMove = true;
        //white to move
        return builder.build();
    }

    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(), this.blackPlayer.getLegalMoves()));
    }

    public static class Builder {
        //inisialisasi papan catur awal, kenapa dibuat builder, karena papan catur jika diinisialisi membutuhkan kelas lain
        //jadi supaya datanya aman terpassing, dibuat inner class builder yg gunanya kyk constructor tpi lebih besar lagi

        Map<Integer, Piece> boardConfig;//untuk ngemap tile ID dari chess board ke piece2
        Alliance nextMoveMaker;//track person whose turn to move

        public Builder() {
            this.boardConfig = new HashMap<>();
        }

        public Builder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        public Builder setMoveMaker(final Alliance nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public Board build() {
            return new Board(this);
        }
    }
}