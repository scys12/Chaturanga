package chaturanga.menu;

import java.awt.*;
import java.awt.event.MouseEvent;

public class PlayState implements ActionListener {

    private Button button;
    private Button button1;

    public PlayState() {
        button = new Button(this,"Button", 300, 100, 100, 40);
        button = new Button(this,"Exit", 300, 200, 100, 40);
    }

    public void update() {

    }

    public void render(Graphics2D g) {
        button.render(g);
        button1.render(g);
    }

    public void mousePressed(MouseEvent e) {
        button.mousePressed(e);
        button1.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e) {
        button.mouseReleased(e);
        button1.mouseReleased(e);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            //jalankan game
        }
        if (e.getSource() == button1) {
            //
        }
    }
}
