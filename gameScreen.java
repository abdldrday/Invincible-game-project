import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class gameScreen extends JPanel implements Runnable {

//    Screen settings
    final int originalTitle = 16;
    final int scale = 4;

    public final int titleSize = originalTitle * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = titleSize * maxScreenCol;
    public final int screenHeight = titleSize * maxScreenRow;

    int FPS = 60;
    boolean showFPS = false;
    int currentFPS = 0;
    boolean onEarth = false;


    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyHandler);
    BufferedImage backgroundImage;
    EarthPlanet earthPlanet;


    TileManager tileManager = new TileManager(this);



    public gameScreen(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/pfp/earth/space_background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        earthPlanet = new EarthPlanet(500, 350, titleSize * 3, titleSize * 3);
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

    public void update() {
        if (!player.isGameOver) {
            player.update();
            showFPS = keyHandler.f1Pressed;
            if (earthPlanet.active && earthPlanet.intersects(player.getX(), player.getY(), titleSize)) {
                earthPlanet.active = false;
                onEarth = true;
            }

        } else {
            if (keyHandler.enterPressed) {
                restartGame();
                keyHandler.enterPressed = false;
            }
            if (keyHandler.escPressed) {
                System.exit(0);
            }
        }
    }



    public void restartGame() {
        player = new Player(this, keyHandler);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, screenWidth, screenHeight, null);
        }

        if (earthPlanet != null) {
            earthPlanet.draw(g2);
        }

        if (onEarth) {
            tileManager.draw(g2); // рисуем землю поверх фона
        }

        player.draw(g2);
        if (player.isGameOver) {
            drawGameOverScreen(g2);
        }

        if (showFPS) {
            g2.setColor(Color.yellow);
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.drawString("FPS: " + currentFPS, 10, 20);
        }

        g2.dispose();


    }

    public void drawGameOverScreen(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, screenWidth, screenHeight);

        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", Font.BOLD, 48));
        g2.drawString("GAME OVER", screenWidth / 2 - 150, screenHeight / 2 - 50);

        g2.setFont(new Font("Arial", Font.PLAIN, 28));
        g2.drawString("Press ENTER to Restart", screenWidth / 2 - 160, screenHeight / 2 + 20);
        g2.drawString("Press ESC to Exit", screenWidth / 2 - 120, screenHeight / 2 + 60);
    }
}
