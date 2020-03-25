package chaturanga.gui;

import chaturanga.board.Move;
import chaturanga.gui.Table.PlayerType;
import chaturanga.player.ai.MiniMax;
import chaturanga.player.ai.MoveStrategy;
import chaturanga.sound.Sound;
import chaturanga.utils.TimeCounter;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.ExecutionException;

public class MainTable {
    private TimeCounter timeCounter;
    private Table table;
    private Menu menu;
    private SettingPanel settingPanel;
    private static final String AI_WIN = "YOU LOSE";
    private static final String AI_LOSE = "YOU WIN";
    private TimerTask timerTask;
    private Timer timer;
    private int secondPassed = 0;

    public MainTable(final int option) {
        this.table = new Table(option);
        this.timeCounter = new TimeCounter(0,0,0);
        this.settingPanel = new SettingPanel(this.table,option);
        table.getGameFrame().add(settingPanel.getBoardPanel(), BorderLayout.CENTER);
        this.menu = new Menu(option,this.table.getGameFrame());
        table.addObserver(new TableGameAIWatcher());
        table.getGameFrame().setVisible(true);
        Sound.main_game_back_sound.loop();
        this.timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                secondPassed++;
//                if (secondPassed%10==0) {
                System.out.println(secondPassed);
//                }
            }
        };
    }

    public void show() {
        timer.scheduleAtFixedRate(timerTask,10000, 10000);
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
