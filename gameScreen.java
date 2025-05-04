import javax.swing.*;
import java.awt.*;

public class gameScreen extends JPanel implements Runnable {

//    Screen settings
    final int originalTitle = 16;
    final int scale = 4;

    public final int titleSize = originalTitle * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = titleSize * maxScreenCol;
    final int screenHeight = titleSize * maxScreenRow;

    int FPS = 60;
    boolean showFPS = false;
    int currentFPS = 0;

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyHandler);


//    Default position if player
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public gameScreen(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        long drawCount = 0;
        long lastTimer = System.currentTimeMillis();

        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        while(gameThread != null){
            update();

            repaint();


            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0){
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            drawCount++;

            if (System.currentTimeMillis() - lastTimer >= 1000) {
                currentFPS = (int) drawCount;
                drawCount = 0;
                lastTimer = System.currentTimeMillis();
            }

        }
    }

    public void update(){

        player.update();

        showFPS = keyHandler.f1Pressed;
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        player.draw(g2);

        if (showFPS) {
            g2.setColor(Color.yellow);
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.drawString("FPS: " + currentFPS, 10, 20);
        }

        g2.dispose();


    }
}
