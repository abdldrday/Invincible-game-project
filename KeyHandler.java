import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public boolean upPressed = false, downPressed = false;
    public boolean leftPressed, rightPressed;
    public boolean f1Pressed = false;
    public boolean iPressed = false;
    public boolean hPressed = false;
    public boolean rPressed = false;
    public boolean enterPressed = false;
    public boolean escPressed = false;
    public boolean spacePressed = false;
    public boolean isPaused = false;
    public int pauseSelection = 0;
    public boolean inMainMenu = true;
    public int menuSelection = 0;
    public boolean spaceHandled = false;

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // Важно: обработка ESC до inMainMenu
        if (code == KeyEvent.VK_ESCAPE && !inMainMenu) {
            isPaused = !isPaused;
            return;
        }

        if (inMainMenu) {
            if (code == KeyEvent.VK_UP) {
                menuSelection--;
                if (menuSelection < 0) menuSelection = 1;
            } else if (code == KeyEvent.VK_DOWN) {
                menuSelection++;
                if (menuSelection > 1) menuSelection = 0;
            } else if (code == KeyEvent.VK_ENTER) {
                if (menuSelection == 0) inMainMenu = false;
                if (menuSelection == 1) System.exit(0);
            } else if (code == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
            return;
        }

        if (isPaused) {
            if (code == KeyEvent.VK_UP) {
                pauseSelection--;
                if (pauseSelection < 0) pauseSelection = 1;
            } else if (code == KeyEvent.VK_DOWN) {
                pauseSelection++;
                if (pauseSelection > 1) pauseSelection = 0;
            } else if (code == KeyEvent.VK_ENTER) {
                if (pauseSelection == 0) isPaused = false;
                if (pauseSelection == 1) System.exit(0);
            }
            return;
        }

        if (code == KeyEvent.VK_W) upPressed = true;
        if (code == KeyEvent.VK_S) downPressed = true;
        if (code == KeyEvent.VK_A) leftPressed = true;
        if (code == KeyEvent.VK_D) rightPressed = true;
        if (code == KeyEvent.VK_F1) f1Pressed = !f1Pressed;
        if (code == KeyEvent.VK_I) iPressed = !iPressed;
        if (code == KeyEvent.VK_H) hPressed = true;
        if (code == KeyEvent.VK_R) rPressed = true;
        if (code == KeyEvent.VK_ENTER) enterPressed = true;
        if (code == KeyEvent.VK_ESCAPE) escPressed = true;
        if (code == KeyEvent.VK_SPACE) spacePressed = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) upPressed = false;
        if (code == KeyEvent.VK_S) downPressed = false;
        if (code == KeyEvent.VK_A) leftPressed = false;
        if (code == KeyEvent.VK_D) rightPressed = false;
        if (code == KeyEvent.VK_H) hPressed = false;
        if (code == KeyEvent.VK_R) rPressed = false;
        if (code == KeyEvent.VK_ENTER) enterPressed = false;
        if (code == KeyEvent.VK_ESCAPE) escPressed = false;
        if (code == KeyEvent.VK_SPACE) spacePressed = false;
    }
}
