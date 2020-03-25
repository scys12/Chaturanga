package chaturanga.gui;

import chaturanga.board.Board;
import chaturanga.board.BoardUtils;
import chaturanga.board.Move;
import chaturanga.board.Tile;
import chaturanga.piece.Piece;
import chaturanga.player.MoveTransition;
import chaturanga.sound.Sound;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class SettingPanel{
    private Table table;
    private BoardPanel boardPanel;
    private Menu menu;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;

    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(350, 700);
    public static final int WIDTH = 350;
    public static final int HEIGHT = 700;
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(0, 0);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(100, 100);

    private static String defaultPieceImagesPath = "src/art/";
    private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");
    private final Color hoverLightTileColor = Color.decode("#b59565");
    private final Color hoverDarkTileColor = Color.decode("#bfa071");
    private static final String BLACK_WIN = "BLACK PLAYER WIN";
    private static final String WHITE_WIN = "WHITE PLAYER WIN";

    public SettingPanel(Table table, final int option) {
        this.table = table;
        this.menu = new Menu(option, this.table.getGameFrame());
        this.boardPanel = new BoardPanel();
    }

    public BoardPanel getBoardPanel() {
        return this.boardPanel;
    }

    public class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;
        public BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            validate();
        }

        public void drawBoard(final Board board) {
            removeAll();
            for (final TilePanel tilePanel : boardTiles) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }

    public class TilePanel extends JPanel {
        private final int tileId;
        TilePanel(final BoardPanel boardPanels, final int tileId) {
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor(lightTileColor, darkTileColor);

            assignTilePieceIcon(table.getGameBoard());
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if(table.isAIPlayer(table.getGameBoard().currentPlayer()) ||
                            BoardUtils.isEndGame(table.getGameBoard())) {
                        return;
                    }
                    if (isRightMouseButton(e)) {
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                    } else if (isLeftMouseButton(e)) {
                        if (sourceTile == null) {
                            sourceTile = table.getGameBoard().getTile(tileId);
                            humanMovedPiece = sourceTile.getPiece();
                            if (humanMovedPiece == null) {
                                sourceTile = null;
                            }
//                            Sound.playSound("src/art/clickpawn.wav");
                            Sound.clickpawn.play();
                        } else {
                            destinationTile = table.getGameBoard().getTile(tileId);
                            final Move move = Move.MoveFactory.createMove(table.getGameBoard(), sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
                            final MoveTransition transition = table.getGameBoard().currentPlayer().makeMove(move);
                            if (transition.getMoveStatus().isDone()) {
                                Sound.move.play();
                                table.setBoard(transition.getTransitionBoard());
                            }
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                if (table.getGameBoard().currentPlayer().isInCheckMate() && !table.isAIPlayer(table.getGameBoard().blackPlayer())) {
                                    Sound.main_game_back_sound.stop();
                                    Sound.win.play();
                                    if (table.getGameBoard().currentPlayer().getOpponent().getAlliance().isBlack()) {
                                        menu.show(BLACK_WIN);
                                    } else menu.show(WHITE_WIN);
                                }
                                if (table.isAIPlayer(table.getGameBoard().currentPlayer())) {
                                    table.moveMadeUpdate(Table.PlayerType.HUMAN);
                                }
                                boardPanel.drawBoard(table.getGameBoard());
                                table.getGameBoard().setPlayer(table.getGameBoard().currentPlayer());
                            }
                        });
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }

            });
            validate();
        }

        public void drawTile(final Board board) {
            assignTileColor(lightTileColor, darkTileColor);
            assignTilePieceIcon(board);
            highlightLegals(board);
            validate();
            repaint();
        }

        private void assignTilePieceIcon(final Board board) {
            this.removeAll();
            if (board.getTile(this.tileId).isTileOccupied()) {
                try {
                    final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath + board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0, 1) + "" +
                            board.getTile(this.tileId).getPiece().toString() + ".gif.png"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private Collection<Move> pieceLegalJumpedMoves(final Board board) {
            if (humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()) {
                return humanMovedPiece.calculateLegalJumpedMoves(board, humanMovedPiece.getPiecePosition(), new HashMap<Integer, Boolean>());
            }
            return Collections.emptyList();
        }

        private Collection<Move> pieceLegalMoves(final Board board) {
            if (humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()) {
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }

        private void highlightLegals(final Board board) {
            for (final Move move : pieceLegalMoves(board)) {
                if (move.getDestinationCoordinate() == this.tileId) {
                    try {
                        assignTileColor(hoverLightTileColor, hoverDarkTileColor);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            for (final Move move : pieceLegalJumpedMoves(board)) {
                if (move.getDestinationCoordinate() == this.tileId) {
                    try {
                        assignTileColor(hoverLightTileColor, hoverDarkTileColor);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private void assignTileColor(Color lightTileColor, Color darkTileColor) {
            if (BoardUtils.EIGTH_RANK[this.tileId] || BoardUtils.SIXTH_RANK[this.tileId] ||
                    BoardUtils.FOURTH_RANK[this.tileId] || BoardUtils.SECOND_RANK[this.tileId]) {
                setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
            } else if (BoardUtils.SEVENTH_RANK[this.tileId] || BoardUtils.FIFTH_RANK[this.tileId] ||
                    BoardUtils.THIRD_RANK[this.tileId] || BoardUtils.FIRST_RANK[this.tileId]) {
                setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);

            }
        }
    }
}