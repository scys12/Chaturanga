package chaturanga.menu;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Button {
    private int x;
    private int y;
    private int width;
    private int height;

    private boolean enabled;
    private boolean pressed;

    private String text;
    private final Font FONT = new Font("Verdana", Font.PLAIN, 14);
    private ActionListener listener;

    public Button(ActionListener listener, String text, int x, int y, int width, int height) {
        this.listener = listener;
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.enabled = true;
    }

    public void render(Graphics2D g) {
        if (pressed) {
            g.setColor(Color.orange);
        } else {
            g.setColor(Color.YELLOW);
        }
        if (enabled) {
            g.fillRect(x, y, width, height);
            g.setFont(FONT);
            g.setColor(Color.BLACK);
            int stringWidth = g.getFontMetrics().stringWidth(text);
            g.drawString(text, x + width / 2 - stringWidth / 2, y + height / 2);
        }
    }

    private boolean isPressed(int x, int y) {
        return x >=this.x && x <= this.x + width && y >= this.y && y <= this.y +  height;
    }

    public void mousePressed(MouseEvent e) {
        if (isPressed(e.getX(), e.getY())) {
            this.pressed = true;
        }
    }

    public void mouseReleased(MouseEvent event) {
        if (pressed && enabled) {
            listener.actionPerformed(new ActionEvent(this));
            this.pressed = false;
        }
    }
}
