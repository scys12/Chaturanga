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

public class Table {
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private Board chessBoard;

    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;

    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(350, 700);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(0, 0);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(100, 100);

    private static String defaultPieceImagesPath = "src/art/";

    private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");
    private final Color hoverLightTileColor=Color.decode("#b59565");
    private final Color hoverDarkTileColor=Color.decode("#bfa071");

    public Table() {

        this.gameFrame = new JFrame("Chaturanga");
        this.gameFrame.setLayout(new BorderLayout());
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.chessBoard = Board.createStandardBoard();
        this.boardPanel = new BoardPanel();
        Sound.playContinuous("src/art/main-game-back-sound.wav");
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setLocationRelativeTo(null);
        this.gameFrame.setVisible(true);
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;

        BoardPanel() {
            super(new GridLayout(8,8));
            this.boardTiles = new ArrayList<>();
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }

            validate();

        }

        public void drawBoard(final Board board){
            removeAll();
            for(final TilePanel tilePanel : boardTiles){
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }

    private class TilePanel extends JPanel {
        private final int tileId;

        TilePanel(final BoardPanel boardPanel, final int tileId) {
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor(lightTileColor, darkTileColor);

            assignTilePieceIcon(chessBoard);

            System.out.println(chessBoard.currentPlayer().getAlliance().isWhite()+"bw");

            if (!chessBoard.currentPlayer().isInCheckMate()) {
                System.out.println("masuk");
                addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(final MouseEvent e) {
                        if (isRightMouseButton(e)) {
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }else if(isLeftMouseButton(e)) {

                            if (sourceTile == null) {
                                sourceTile = chessBoard.getTile(tileId);
                                humanMovedPiece = sourceTile.getPiece();
                                if (humanMovedPiece == null) {
                                    sourceTile = null;
                                }
                                Sound.playSound("src/art/clickpawn.wav");
                            } else {
                                destinationTile = chessBoard.getTile(tileId);
                                final Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
                                final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                                if (transition.getMoveStatus().isDone()) {
                                    Sound.playSound("src/art/move.wav");
                                    chessBoard = transition.getTransitionBoard();
                                }
                                sourceTile = null;
                                destinationTile = null;
                                humanMovedPiece = null;
                            }
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    boardPanel.drawBoard(chessBoard);
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
            }

            validate();
        }

        public void drawTile(final Board board){
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
                    final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath + board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0, 1) +""+
                            board.getTile(this.tileId).getPiece().toString()+".gif.png"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void highlightLegals(final Board board) {
            if (true) {
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