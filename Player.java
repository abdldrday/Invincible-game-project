import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    gameScreen gs;
    KeyHandler keyHandler;

    public int maxHealth = 100;
    public int currentHealth = 100;
    public Inventory inventory = new Inventory();
    public boolean isGameOver = false;

    public Player(gameScreen gs, KeyHandler keyHandler) {
        this.gs = gs;
        this.keyHandler = keyHandler;
        setDefValue();
        getPlayerImg();

        inventory.addItem(new Item("Crystal"));
    }

    public void setDefValue() {
        x = 100;
        y = 100;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImg() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/pfp/player/allen-up1.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/pfp/player/allen-down1.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/pfp/player/allen-left1.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/pfp/player/allen-right1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (isGameOver) return;

        if (keyHandler.upPressed) {
            direction = "up";
            y -= speed;
        } else if (keyHandler.downPressed) {
            direction = "down";
            y += speed;
        } else if (keyHandler.leftPressed) {
            direction = "left";
            x -= speed;
        } else if (keyHandler.rightPressed) {
            direction = "right";
            x += speed;
        }

        if (keyHandler.hPressed) {
            takeDamage(10);
            keyHandler.hPressed = false;
        }

        if (keyHandler.rPressed) {
            restoreHealth();
            keyHandler.rPressed = false;
        }
    }

    public void takeDamage(int amount) {
        currentHealth -= amount;
        if (currentHealth <= 0) {
            currentHealth = 0;
            isGameOver = true;
        }
    }

    public void restoreHealth() {
        currentHealth = maxHealth;
    }

    public void draw(Graphics g) {
        BufferedImage image = switch (direction) {
            case "up" -> up1;
            case "down" -> down1;
            case "left" -> left1;
            case "right" -> right1;
            default -> down1;
        };

        g.drawImage(image, x, y, gs.titleSize, gs.titleSize, null);
        drawHealthBar((Graphics2D) g);

        if (keyHandler.iPressed) {
            drawInventory((Graphics2D) g);
        }
    }

    public void drawHealthBar(Graphics2D g2) {
        int barWidth = gs.titleSize;
        int barHeight = 8;
        int barX = x;
        int barY = y - 12;

        double healthPercent = (double) currentHealth / maxHealth;
        int filled = (int) (barWidth * healthPercent);

        g2.setColor(Color.red);
        g2.fillRect(barX, barY, barWidth, barHeight);
        g2.setColor(Color.green);
        g2.fillRect(barX, barY, filled, barHeight);
        g2.setColor(Color.white);
        g2.drawRect(barX, barY, barWidth, barHeight);
    }

    public void drawInventory(Graphics2D g2) {
        int x = 20;
        int y = 60;
        int width = 200;
        int lineHeight = 25;

        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRoundRect(x - 10, y - 30, width + 20, inventory.getItems().size() * lineHeight + 40, 15, 15);

        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString("Inventory", x, y - 10);

        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        for (int i = 0; i < inventory.getItems().size(); i++) {
            g2.drawString("- " + inventory.getItems().get(i).name, x, y + i * lineHeight + 10);
        }
    }
}