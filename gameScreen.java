import javax.swing.*;
import java.awt.*;

public class gameScreen extends JPanel implements Runnable {

//    Screen settings
    final int originalTitle = 16;
    final int scale = 3;

    final int titlSize = originalTitle * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = titlSize * maxScreenCol;
    final int screenHeight = titlSize * maxScreenRow;

    int FPS = 60;
    boolean showFPS = false;
    int currentFPS = 0;

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;


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

        if (keyHandler.upPressed == true){
            playerY -= playerSpeed;
        }
        else if(keyHandler.downPressed == true){
            playerY += playerSpeed;
        }
        else if(keyHandler.leftPressed == true){
            playerX -= playerSpeed;
        }
        else if(keyHandler.rightPressed == true){
            playerX += playerSpeed;
        }

        showFPS = keyHandler.f1Pressed;
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.white);
        g2.fillRect(playerX, playerY, titlSize, titlSize);

        if (showFPS) {
            g2.setColor(Color.yellow);
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.drawString("FPS: " + currentFPS, 10, 20);
        }

        g2.dispose();


    }
}
