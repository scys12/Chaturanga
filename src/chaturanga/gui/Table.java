package chaturanga.gui;

import chaturanga.board.Board1;
import chaturanga.board.BoardUtils1;
import chaturanga.board.Move1;
import chaturanga.board.Tile1;
import chaturanga.piece.Piece1;
import chaturanga.player.MoveTransition1;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table {
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private Board1 chessBoard;

    private Tile1 sourceTile;
    private Tile1 destinationTile;
    private Piece1 humanMovedPiece;

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
        this.chessBoard = Board1.createStandardBoard();
        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);

    }

    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;

        BoardPanel() {
            super(new GridLayout(8,8));
            this.boardTiles = new ArrayList<>();
            for (int i = 0; i < BoardUtils1.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }

            validate();

        }

        public void drawBoard(final Board1 board1){
            removeAll();
            for(final TilePanel tilePanel : boardTiles){
                tilePanel.drawTile(board1);
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
                        } else {
                            destinationTile = chessBoard.getTile(tileId);
                            final Move1 move = Move1.MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
                            final MoveTransition1 transition = chessBoard.currentPlayer().makeMove(move);
                            if (transition.getMoveStatus().isDone()) {
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

            validate();
        }

        public void drawTile(final Board1 board1){
            assignTileColor(lightTileColor, darkTileColor);
            assignTilePieceIcon(board1);
            highlightLegals(board1);
            validate();
            repaint();
        }

        private void assignTilePieceIcon(final Board1 board) {
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

        private void highlightLegals(final Board1 board) {
            if (true) {
                for (final Move1 move : pieceLegalMoves(board)) {
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

        private Collection<Move1> pieceLegalMoves(final Board1 board) {
            if (humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()) {
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }

        private void assignTileColor(Color lightTileColor, Color darkTileColor) {
            if (BoardUtils1.EIGTH_RANK[this.tileId] || BoardUtils1.SIXTH_RANK[this.tileId] ||
                    BoardUtils1.FOURTH_RANK[this.tileId] || BoardUtils1.SECOND_RANK[this.tileId]) {
                setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
            } else if (BoardUtils1.SEVENTH_RANK[this.tileId] || BoardUtils1.FIFTH_RANK[this.tileId] ||
                    BoardUtils1.THIRD_RANK[this.tileId] || BoardUtils1.FIRST_RANK[this.tileId]) {
                setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);

            }
        }
    }
}
