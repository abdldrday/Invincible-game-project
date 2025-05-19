import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.event.*;

public class gameScreen extends JPanel implements Runnable {

    //    Screen settings
    final int originalTitle = 16;
    final int scale = 3;

    public final int titleSize = originalTitle * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = titleSize * maxScreenCol;
    public final int screenHeight = titleSize * maxScreenRow;
    private float pauseAlpha = 0f;

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
        if (keyHandler.isPaused) {
            if (pauseAlpha < 1f) pauseAlpha += 0.05f;
        } else {
            if (pauseAlpha > 0f) pauseAlpha -= 0.05f;
        }

        if (keyHandler.inMainMenu || keyHandler.isPaused) {
            return;
        }
        if (!player.isGameOver) {
            player.update();
            showFPS = keyHandler.f1Pressed;
            if (earthPlanet.active && earthPlanet.intersects(player.getX(), player.getY(), titleSize)) {
                earthPlanet.active = false;
                onEarth = true;
            }

            if (onEarth && keyHandler.spacePressed) {
                int col = player.getX() / titleSize;
                int row = player.getY() / titleSize;
                if (tileManager.mapTileNum[col][row] == 3) {
                    onEarth = false;
                    try {
                        backgroundImage = ImageIO.read(getClass().getResourceAsStream("/pfp/earth/space_background.png"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    earthPlanet.active = true; // Снова активируем планету
                }
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

        if (keyHandler.isPaused) {
            if (pauseAlpha < 1f) pauseAlpha += 0.05f;
        } else {
            if (pauseAlpha > 0f) pauseAlpha -= 0.05f;
        }
    }



    public void restartGame() {
        player = new Player(this, keyHandler);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        // Главное меню
        if (keyHandler.inMainMenu) {
            drawMainMenu(g2);
            return;
        }

        // Пауза
        if (keyHandler.isPaused) {
            drawPauseMenu(g2);
            return;
        }

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

    public void drawMainMenu(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, screenWidth, screenHeight);

        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.setColor(Color.WHITE);
        g.drawString("Invincible Game", screenWidth / 2 - 160, 150);

        g.setFont(new Font("Arial", Font.PLAIN, 30));
        for (int i = 0; i < 2; i++) {
            if (keyHandler.menuSelection == i) {
                g.setColor(Color.YELLOW);
            } else {
                g.setColor(Color.WHITE);
            }
            String text = (i == 0) ? "Start Game" : "Exit";
            g.drawString(text, screenWidth / 2 - 100, 250 + i * 40);
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (keyHandler.isPaused) {
            int mouseX = e.getX();
            int mouseY = e.getY();
            for (int i = 0; i < 2; i++) {
                int x = screenWidth / 2 - 120;
                int y = 220 + i * 40;
                if (mouseX >= x && mouseX <= x + 240 && mouseY >= y - 30 && mouseY <= y) {
                    keyHandler.pauseSelection = i;
                    if (i == 0) keyHandler.isPaused = false;
                    if (i == 1) System.exit(0);
                }
            }
        }
    }

    public void drawPauseMenu(Graphics2D g) {
        Composite original = g.getComposite();
        float alpha = Math.max(0f, Math.min(1f, pauseAlpha));
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, screenWidth, screenHeight);

        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.setColor(Color.WHITE);
        g.drawString("Пауза", screenWidth / 2 - 60, 150);

        String[] options = { "Продолжить игру", "Выход" };
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        for (int i = 0; i < options.length; i++) {
            if (keyHandler.pauseSelection == i) {
                g.setColor(Color.YELLOW);
            } else {
                g.setColor(Color.WHITE);
            }
            g.drawString(options[i], screenWidth / 2 - 120, 220 + i * 40);
        }

        g.setComposite(original);  // ✅ вернули стандартную прозрачность
    }


}
