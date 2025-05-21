import inventory.InventoryComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.event.*;


public class gameScreen extends JPanel implements Runnable {
    public boolean attackHandled = false;
    public boolean showVictory = false;

    final int originalTitle = 16;
    final int scale = 3;

    public final int titleSize = originalTitle * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = titleSize * maxScreenCol;
    public final int screenHeight = titleSize * maxScreenRow;
    private float pauseAlpha = 0f;

    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = titleSize * maxWorldCol;
    public final int worldHeight = titleSize * maxWorldRow;
    public Sound bgm = new Sound();
    public Sound hitSound = new Sound();


    int FPS = 60;
    boolean showFPS = false;
    int currentFPS = 0;

    KeyHandler keyHandler = new KeyHandler(this);
    Thread gameThread;
    public Player player = new Player(this, keyHandler);
    BufferedImage mainMenuBackground;
    public Entity npc[] = new Entity[10];

    public int waveCount = 0;
    public boolean bossSpawned = false;
    public Boss boss;
    public boolean isGameWon = false;
    private static gameScreen instance;
    public boolean showInventory = false;
    public boolean showTutorial = true;
    public boolean inMainMenu = true;




    TileManager tileManager = new TileManager(this);

    private gameScreen() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        bgm.setFile("/pfp/sound/song.wav");
        bgm.loop();
        hitSound.setFile("/pfp/sound/punch.wav");


        try {
            mainMenuBackground = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/pfp/menu/main-menu.jpeg"));
            System.out.println("Main menu background loaded!");
        } catch (IOException e) {
            e.printStackTrace();
        }


        player = (Player) EntityFactory.createEntity("Player", this, keyHandler);


    }
    public static gameScreen getInstance() {
        if (instance == null) {
            instance = new gameScreen();
        }
        return instance;
    }



    public void startGameThread() {
        boss = new Boss(this);
        bossSpawned = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        long drawCount = 0;
        long lastTimer = System.currentTimeMillis();

        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (gameThread != null) {
            update();
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) remainingTime = 0;

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

        if (showTutorial) {
            if (keyHandler.tutorialClosed) {
                showTutorial = false;
            }
            return;
        }

        if (showTutorial) {
            if (keyHandler.tutorialClosed) {
                showTutorial = false;
            }
            return; // ⛔ ничего не обновляем, пока окно открыто
        }



        if (keyHandler.inMainMenu || keyHandler.isPaused) return;

        if (!player.isGameOver) {
            player.update();
            showFPS = keyHandler.f1Pressed;
        } else {
            if (keyHandler.enterPressed) {
                restartGame();
                keyHandler.enterPressed = false;
            }
            if (keyHandler.escPressed) {
                System.exit(0);
            }
        }

        if (isGameWon) {
            if (keyHandler.enterPressed) {
                restartGame();
                isGameWon = false;
                keyHandler.enterPressed = false;
            }
            if (keyHandler.escPressed) {
                System.exit(0);
            }
            return;
        }

        if (!player.isGameOver) {
            player.update();


            if (keyHandler.spacePressed && !keyHandler.spaceHandled) {
                player.attack();
                keyHandler.spaceHandled = true;
            }

            if (!keyHandler.spacePressed) {
                keyHandler.spaceHandled = false;
            }

            if (keyHandler.iPressed) {
                showInventory = true;
            } else {
                showInventory = false;
            }


        }



//        if (!bossSpawned && waveCount >= 5) {
//            boss = new Boss(this);
//            bossSpawned = true;
//        }
//
        if (bossSpawned && boss != null && boss.isAlive) {

            if (bossSpawned && boss != null && boss.isAlive) {
                boss.update();

                // Урон по пробелу (однократно)
                Rectangle bossArea = new Rectangle(boss.worldX, boss.worldY, titleSize, titleSize);
                Rectangle playerArea = new Rectangle(player.worldX, player.worldY, titleSize, titleSize);

                if (bossArea.intersects(playerArea) && keyHandler.spacePressed && !attackHandled) {
                    hitSound.play();
                    boss.takeDamage(20);
                    attackHandled = true;
                }

                if (!keyHandler.spacePressed) {
                    attackHandled = false;
                }

                // урон по пробелу при касании
                if (bossArea.intersects(playerArea) && keyHandler.spacePressed && !attackHandled) {
                    boss.takeDamage(1);
                    attackHandled = true;
                }

                if (!keyHandler.spacePressed) {
                    attackHandled = false;
                }

                // Проверка на столкновение
            }

        }
    }

    public void restartGame() {
        player = new Player(this, keyHandler);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (showTutorial) {
            g2.setColor(new Color(0, 0, 0, 200));
            g2.fillRect(0, 0, screenWidth, screenHeight);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 24));
            g2.drawString("Control", screenWidth / 2 - 70, 100);
            g2.setFont(new Font("Arial", Font.PLAIN, 18));
            g2.drawString("W / A / S / D - Movement", screenWidth / 2 - 100, 150);
            g2.drawString("SPACE - Attack", screenWidth / 2 - 100, 180);
            g2.drawString("I - Inventory", screenWidth / 2 - 100, 210);
            g2.drawString("ENTER - Close tooltip", screenWidth / 2 - 100, 250);
            g2.drawString("ESC - Pause menu", screenWidth / 2 - 100, 280);
            g2.drawString("F1 - FPS counter", screenWidth / 2 - 100, 310);
            return;
        }

        player.draw(g2);


        if (keyHandler.inMainMenu) {
            drawMainMenu(g2);
            return;
        }

        if (keyHandler.isPaused) {
            drawPauseMenu(g2);
            return;
        }

        tileManager.draw(g2);
        player.draw(g2);

        if (player.isGameOver) {
            drawGameOverScreen(g2);
            return;
        }

        if (showFPS) {
            g2.setColor(Color.yellow);
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.drawString("FPS: " + currentFPS, 10, 20);
        }

        if (showInventory) {
            g2.setColor(new Color(0, 0, 0, 200));
            g2.fillRoundRect(100, 100, 400, 300, 20, 20);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.drawString("INVENTORY", 250, 130);

            int y = 160;
            for (InventoryComponent item : player.inventory.getComponents()) {
                g2.drawString("- " + item.getName() + " (" + item.getCount() + ")", 130, y);
                y += 30;
            }
        }



//        for(int i = 0; i < npc.length; i++){
//            if (npc[i] != null){
//                npc[i].draw(g2);
//            }
//        }

        if (bossSpawned && boss != null && boss.isAlive) {
            boss.draw(g2);
        }

        if (showVictory) {
            drawGameWonScreen(g2);
            return;
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
        if (mainMenuBackground != null) {
            g.drawImage(mainMenuBackground, 0, 0, screenWidth, screenHeight, null);
        } else {
            g.setColor(new Color(0, 174, 239));
            g.fillRect(0, 0, screenWidth, screenHeight);
        }
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        String[] options = {"Start Game", "Exit"};
        for (int i = 0; i < options.length; i++) {
            g.setColor(keyHandler.menuSelection == i ? Color.YELLOW : Color.WHITE);
            g.drawString(options[i], screenWidth / 2 - 60, 400 + i * 40);
        }
    }

    public void GameWon(Graphics2D g2) {
        if (isGameWon) {
            g2.setColor(new Color(0, 0, 0, 170)); // затемнённый фон
            g2.fillRect(0, 0, screenWidth, screenHeight);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 48));
            g2.drawString("You win!", screenWidth / 2 - 250, screenHeight / 2);
            return;
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
        g.drawString("Pause", screenWidth / 2 - 60, 150);

        String[] options = {"Continue", "Exit"};
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        for (int i = 0; i < options.length; i++) {
            g.setColor(keyHandler.pauseSelection == i ? Color.YELLOW : Color.WHITE);
            g.drawString(options[i], screenWidth / 2 - 120, 220 + i * 40);
        }

        g.setComposite(original);
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

    public void drawGameWonScreen(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, screenWidth, screenHeight);

        g2.setColor(Color.white);

        // Заголовок — крупный
        g2.setFont(new Font("Arial", Font.BOLD, 48));
        String title = "WIN!";
        int titleX = (screenWidth - g2.getFontMetrics().stringWidth(title)) / 2;
        g2.drawString(title, titleX, screenHeight / 2 - 50);

        // Первая строка
        g2.setFont(new Font("Arial", Font.PLAIN, 28));
        String line1 = "You won the strongest character in the game";
        int line1X = (screenWidth - g2.getFontMetrics().stringWidth(line1)) / 2;
        g2.drawString(line1, line1X, screenHeight / 2 + 20);

        // Вторая строка
        String line2 = "Good luck, Champion";
        int line2X = (screenWidth - g2.getFontMetrics().stringWidth(line2)) / 2;
        g2.drawString(line2, line2X, screenHeight / 2 + 60);
    }


}
