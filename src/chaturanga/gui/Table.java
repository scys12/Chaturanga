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

    public static int code;
    private Move computerMove;

    public Table(final int option) {
        this.gameFrame = new JFrame("Chaturanga");
        this.gameFrame.setLayout(new BorderLayout());
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.gameFrame.setResizable(false);
//        Sound.playSound("/main-game-back-sound.wav");
        Sound.main_game_back_sound.loop();
        this.gameFrame.setLocationRelativeTo(null);
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setPlayerType(option);
        this.chessBoard = Board.createStandardBoard();
    }

    public JFrame getGameFrame() {
        return gameFrame;
    }
    public static void setCode(final int code) {
         Table.code =code;
    }

    public Board getGameBoard() {
        return this.chessBoard;
    }

    public void updateComputerMove(final Move move) {
        this.computerMove = move;
    }

    public void updateGameBoard(final Board board) {
        this.chessBoard = board;
    }



    public void setPlayerType(final int option) {
        if (option==2){
            whitePlayerType = PlayerType.HUMAN;
            blackPlayerType = PlayerType.COMPUTER;
        }
        else whitePlayerType = blackPlayerType = PlayerType.HUMAN;
    }

    public boolean isAIPlayer(final Player player) {
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

    protected void moveMadeUpdate(final PlayerType playerType) {
        setChanged();
        notifyObservers(playerType);
    }

    public void setBoard(Board transitionBoard) {
        this.chessBoard = transitionBoard;
    }

    enum PlayerType {
        HUMAN,
        COMPUTER
    }
}