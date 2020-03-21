package chaturanga.gui;

import chaturanga.Alliance;
import chaturanga.board.Board;
import chaturanga.board.BoardUtils;
import chaturanga.board.Move;
import chaturanga.board.Tile;
import chaturanga.piece.Piece;
import chaturanga.player.MoveTransition;
import chaturanga.player.Player;
import chaturanga.player.ai.MiniMax;
import chaturanga.player.ai.MoveStrategy;
import chaturanga.sound.Sound;
import javafx.scene.control.Tab;

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
import java.util.concurrent.ExecutionException;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table extends Observable {
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private Board chessBoard;

    private PlayerType whitePlayerType;
    private PlayerType blackPlayerType;

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

    public static int code=2;
    private Move computerMove;
    private static final Table INSTANCE = new Table();

    private Table() {
        code=getCode();
        System.out.println(code);
        this.gameFrame = new JFrame("Chaturanga");
        this.gameFrame.setLayout(new BorderLayout());
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.chessBoard = Board.createStandardBoard();
        this.boardPanel = new BoardPanel();
        this.addObserver(new TableGameAIWatcher());
        Sound.playContinuous("src/art/main-game-back-sound.wav");
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setLocationRelativeTo(null);
        this.gameFrame.setVisible(true);
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPlayerType();
    }

    public static Table get(final int options) {
        code=options;
        return INSTANCE;
    }

    public void show() {
        Table.get(code).getBoardPanel().drawBoard(Table.get(code).getGameBoard());
    }

    private BoardPanel getBoardPanel() {
        return this.boardPanel;
    }

    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;

        BoardPanel() {
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
    public static int getCode() {
        return code;
    }

    private Board getGameBoard() {
        return this.chessBoard;
    }

    private static class TableGameAIWatcher implements Observer {
        @Override

        public void update(final Observable o, final Object arg) {
            System.out.println(code+"a");
            if (Table.get(code).isAIPlayer(Table.get(code).getGameBoard().currentPlayer()) && !Table.get(code).getGameBoard().currentPlayer().isInCheckMate()) {
                final AIThinkTank thinkTank = new AIThinkTank();
                thinkTank.execute();
            }//create AI and execute it

            if (Table.get(code).getGameBoard().currentPlayer().isInCheckMate()) {
                JOptionPane.showMessageDialog(Table.get(code).getBoardPanel(),
                        "Game Over: Player " + Table.get(code).getGameBoard().currentPlayer() + " is in checkmate!", "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private static class AIThinkTank extends SwingWorker<Move, String> {
        private AIThinkTank() {

        }

        @Override
        protected void done() {
            try {
                final Move bestMove = get();
                Table.get(code).updateComputerMove(bestMove);
                Table.get(code).updateGameBoard(Table.get(code).getGameBoard().currentPlayer().makeMove(bestMove).getTransitionBoard());
                Table.get(code).getBoardPanel().drawBoard(Table.get(code).getGameBoard());
                Table.get(code).moveMadeUpdate(PlayerType.COMPUTER);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Move doInBackground() throws Exception {
            final MoveStrategy miniMax = new MiniMax(2);
            final Move bestMove = miniMax.execute(Table.get(code).getGameBoard());
            return bestMove;
        }
    }

    private void updateComputerMove(final Move move) {
        this.computerMove = move;
    }



    private void updateGameBoard(final Board board) {
        this.chessBoard = board;
    }



    public void setPlayerType() {
        if (code==2){
            whitePlayerType = PlayerType.HUMAN;
            blackPlayerType = PlayerType.COMPUTER;
        } else if(code==1){
            whitePlayerType = blackPlayerType = PlayerType.HUMAN;
        }
    }

    public boolean isAIPlayer(final Player player) {
        System.out.println("b");
        if (player.getAlliance() == Alliance.WHITE) {
            return getWhitePlayerType() == PlayerType.COMPUTER;
        }
        return getBlackPlayerType() == PlayerType.COMPUTER;
    }

    public PlayerType getWhitePlayerType() {
        return this.whitePlayerType;
    }

    public PlayerType getBlackPlayerType() {
        return this.blackPlayerType;
    }

    private void moveMadeUpdate(final PlayerType playerType) {
        setChanged();
        notifyObservers(playerType);
    }

    enum PlayerType {
        HUMAN,
        COMPUTER
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
                    if(Table.get(code).isAIPlayer(Table.get(code).getGameBoard().currentPlayer()) ||
                            BoardUtils.isEndGame(Table.get(code).getGameBoard())) {
                        return;
                    }
                    if (isRightMouseButton(e)) {
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                    } else if (isLeftMouseButton(e)) {

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
                                if (chessBoard.currentPlayer().isInCheckMate()) {
                                    Sound.playSound("src/art/win.wav");
                                    String win;
                                    if (chessBoard.currentPlayer().getOpponent().getAlliance().isBlack()) {
                                        win = "Black Player Win";
                                    } else win = "White Player Win";
                                    endGameMenu(win, gameFrame);
                                }
                                if (isAIPlayer(chessBoard.currentPlayer())) {
                                    System.out.println("m");
                                    Table.get(code).moveMadeUpdate(PlayerType.HUMAN);
                                }
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
//            if (chessBoard.currentPlayer().isInCheckMate()) {
//                String win;
//                if (chessBoard.currentPlayer().getOpponent().getAlliance().isBlack()) {
//                    win = "Black Player Win";
//                }
//                else win = "White Player Win";
//                endGameMenu(win);
//            }
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

    public void endGameMenu(String win, JFrame gameFrame) {
        Menu menu = new Menu(win, gameFrame);
    }
}