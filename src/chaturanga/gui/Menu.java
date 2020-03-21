package chaturanga.gui;

import chaturanga.Chaturanga;
import javafx.application.Platform;
import javafx.stage.Stage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu  {

    JFrame window;
    Container container;
    JPanel titleNamePanel;
    JPanel restartButtonPanel;
    JPanel exitButtonPanel;
    JLabel titleNameLabel;
    JButton restartButton;
    JButton exitButton;
    Font titleFont = new Font("Times New Roman", Font.PLAIN, 30);
    Font normalFont = new Font("Times New Roman", Font.PLAIN,30);
    private static javafx.scene.text.Font font;

    public Menu(String win, JFrame gameFrame) {

        window = new JFrame();
        window.setSize(500,350);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.white);
        window.setLayout(null);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        container = window.getContentPane();

        titleNamePanel = new JPanel();
        titleNamePanel.setBounds(0,0, 500, 350);
        titleNamePanel.setBackground(Color.decode("#593E1A"));
        titleNameLabel = new JLabel(win);
        titleNameLabel.setForeground(Color.decode("#FFFACD"));
        titleNameLabel.setFont(titleFont);

        restartButtonPanel = new JPanel();
        restartButtonPanel.setBounds(10, 200, 200, 55);
        restartButtonPanel.setBackground(Color.decode("#593E1A"));

        restartButton = new JButton("RESTART");
        restartButton.setBackground(Color.decode("#593E1A"));
        restartButton.setForeground(Color.decode("#FFFACD"));
        restartButton.setFont(normalFont);
        restartButton.setFocusPainted(false);

        exitButtonPanel = new JPanel();
        exitButtonPanel.setBounds(250, 200, 250, 55);
        exitButtonPanel.setBackground(Color.decode("#593E1A"));

        exitButton = new JButton("MAIN MENU");
        exitButton.setBackground(Color.decode("#593E1A"));
        exitButton.setForeground(Color.decode("#FFFACD"));
        exitButton.setFont(normalFont);
        exitButton.setFocusPainted(false);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(() -> {
                    Chaturanga chaturanga = new Chaturanga();
                    initFx(chaturanga);
                });
                gameFrame.setVisible(false);
                window.setVisible(false);
            }
        });

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        window.setVisible(false);
                        gameFrame.setVisible(false);
                        Table.get(1).show();
                    }
                });
            }

        });

        titleNamePanel.add(titleNameLabel);
        restartButtonPanel.add(restartButton);
        exitButtonPanel.add(exitButton);
        container.add(exitButtonPanel);
        container.add(restartButtonPanel);
        container.add(titleNamePanel);

    }

    private void initFx(Chaturanga chaturanga) {
        Stage primaryStage = new Stage();
        try {
            chaturanga.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
