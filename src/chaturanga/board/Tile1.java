package chaturanga.board;

import chaturanga.piece.Piece1;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;

public abstract class Tile1 {
    protected final int tileCoordinate;
    private static final Map<Integer, EmptyTile> EMPTY_TILE_CACHE = createAllPossiblyEmptyTiles();

    private static Map<Integer, EmptyTile> createAllPossiblyEmptyTiles() {
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

        for (int i = 0; i < BoardUtils1.NUM_TILES; i++) {
            emptyTileMap.put(i, new EmptyTile(i));
        }
        return ImmutableMap.copyOf(emptyTileMap);
    }

    public static Tile1 createTile(final int tileCoordinate, final Piece1 piece) {
        return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILE_CACHE.get(tileCoordinate);
    }

    private Tile1(final int tileCoordinate) {
        this.tileCoordinate = tileCoordinate;
    }

    public abstract boolean isTileOccupied();
    public abstract Piece1 getPiece();

    public int getTileCoordinate(){
        return this.tileCoordinate;
    }

    public static final class EmptyTile extends Tile1{

        private EmptyTile(final int coordinate) {
            super(coordinate);
        }

        @Override
        public boolean isTileOccupied() {
            return false;
        }

        @Override
        public Piece1 getPiece() {
            return null;
        }

        @Override
        public String toString() {
            return "-";
        }
    }

    public static final class OccupiedTile extends Tile1{
        private final Piece1 pieceOnTile;

        public OccupiedTile(final int tileCoordinate, final Piece1 pieceOnTile) {
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }

        @Override
        public boolean isTileOccupied() {
            return true;
        }

        @Override
        public Piece1 getPiece() {
            return this.pieceOnTile;
        }

        @Override
        public String toString() {
            return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase() :
                    getPiece().toString();
        }
    }

}
