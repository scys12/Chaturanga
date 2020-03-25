package chaturanga.gui;

import chaturanga.Alliance;
import chaturanga.board.Board;
import chaturanga.board.BoardUtils;
import chaturanga.board.Move;
import chaturanga.board.Tile;
import chaturanga.gui.Table.PlayerType;
import chaturanga.piece.Piece;
import chaturanga.player.MoveTransition;
import chaturanga.player.Player;
import chaturanga.player.ai.MiniMax;
import chaturanga.player.ai.MoveStrategy;
import chaturanga.sound.Sound;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class MainTable {
    private Table table;
    private Menu menu;
    private SettingPanel settingPanel;
    public static final String AI_WIN = "YOU LOSE";
    public static final String AI_LOSE = "YOU WIN";

    public MainTable(final int option) {
        this.table = new Table(option);
        this.settingPanel = new SettingPanel(this.table,option);
        table.getGameFrame().add(settingPanel.getBoardPanel(), BorderLayout.CENTER);
        this.menu = new Menu(option,this.table.getGameFrame());
        table.addObserver(new TableGameAIWatcher());
        table.getGameFrame().setVisible(true);
    }

    public void show() {
        settingPanel.getBoardPanel().drawBoard(table.getGameBoard());
    }

    private class TableGameAIWatcher implements Observer {

        public TableGameAIWatcher() {
        }
        @Override
        public void update(final Observable o, final Object arg) {
            if (table.isAIPlayer(table.getGameBoard().currentPlayer()) && !table.getGameBoard().currentPlayer().isInCheckMate()) {
                final AIThinkTank thinkTank = new AIThinkTank();
                thinkTank.execute();
            }//create AI and execute it

            if (table.getGameBoard().currentPlayer().isInCheckMate()) {
                Sound.main_game_back_sound.stop();
//                Sound.playSound("/win.wav");
                Sound.win.play();
                String win;
                if (table.getGameBoard().currentPlayer().getOpponent().getAlliance().isBlack()) {
                    menu.show(AI_WIN);
                } else menu.show(AI_LOSE);
            }
        }
    }


    private class AIThinkTank extends SwingWorker<Move, String> {
        private AIThinkTank() {
        }

        @Override
        protected void done() {
            try {
                final Move bestMove = get();
                table.updateComputerMove(bestMove);
                table.updateGameBoard(table.getGameBoard().currentPlayer().makeMove(bestMove).getTransitionBoard());
                settingPanel.getBoardPanel().drawBoard(table.getGameBoard());
                table.moveMadeUpdate(PlayerType.COMPUTER);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Move doInBackground() throws Exception {
            final MoveStrategy miniMax = new MiniMax(4);
            final Move bestMove = miniMax.execute(table.getGameBoard());
            return bestMove;
        }
    }
}
