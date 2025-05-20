import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    gameScreen gs;
    KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;


    public int maxHealth = 100;
    public int currentHealth = 100;
    public Inventory inventory = new Inventory();
    public boolean isGameOver = false;

    public Rectangle solidArea = new Rectangle(8, 8, 16, 16);
    public boolean collisionOn = false;


    public Player(gameScreen gs, KeyHandler keyHandler) {
        this.gs = gs;
        this.keyHandler = keyHandler;

        screenX = gs.screenWidth / 2 - gs.titleSize / 2;
        screenY = gs.screenHeight / 2 - gs.titleSize / 2;


        setDefValue();
        getPlayerImg();

        inventory.addItem(new Item("Crystal"));
    }

    public void setDefValue() {
        worldX = gs.titleSize * 23 - (gs.titleSize/2);
        worldY = gs.titleSize * 21 - (gs.titleSize/2);
        speed = 4;
        direction = "down";
    }

    private int x, y;

    public int getX() {
        return worldX;
    }

    public int getY() {
        return worldY;
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
            worldY -= speed;
        } else if (keyHandler.downPressed) {
            direction = "down";
            worldY += speed;
        } else if (keyHandler.leftPressed) {
            direction = "left";
            worldX -= speed;
        } else if (keyHandler.rightPressed) {
            direction = "right";
            worldX  += speed;
        }

        if (keyHandler.hPressed) {
            takeDamage(10);
            keyHandler.hPressed = false;
        }

        if (keyHandler.rPressed) {
            restoreHealth();
            keyHandler.rPressed = false;
        }

        if (keyHandler.upPressed) {
            direction = "up";
            checkTileCollision();
            if (!collisionOn) worldY -= speed;
        }
        else if (keyHandler.downPressed) {
            direction = "down";
            checkTileCollision();
            if (!collisionOn) worldY += speed;
        }
        else if (keyHandler.leftPressed) {
            direction = "left";
            checkTileCollision();
            if (!collisionOn) worldX -= speed;
        }
        else if (keyHandler.rightPressed) {
            direction = "right";
            checkTileCollision();
            if (!collisionOn) worldX += speed;
        }

        if (worldX < 0) worldX = 0;
        if (worldY < 0) worldY = 0;

        if (worldX > gs.worldWidth - gs.titleSize)
            worldX = gs.worldWidth - gs.titleSize;

        if (worldY > gs.worldHeight - gs.titleSize)
            worldY = gs.worldHeight - gs.titleSize;

        checkCollision();

    }

    public void checkCollision() {
        int tileCol = (worldX + solidArea.x) / gs.titleSize;
        int tileRow = (worldY + solidArea.y) / gs.titleSize;

        int tileNum = gs.tileManager.mapTileNum[tileCol][tileRow];

        if (gs.tileManager.tile[tileNum].collision) {
            collisionOn = true;
        } else {
            collisionOn = false;
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

    public void checkTileCollision() {
        int tileLeftCol = (worldX + solidArea.x) / gs.titleSize;
        int tileRightCol = (worldX + solidArea.x + solidArea.width) / gs.titleSize;
        int tileTopRow = (worldY + solidArea.y) / gs.titleSize;
        int tileBottomRow = (worldY + solidArea.y + solidArea.height) / gs.titleSize;

        int tileNum1, tileNum2;

        switch (direction) {
            case "up":
                tileTopRow = (worldY + solidArea.y - speed) / gs.titleSize;
                tileNum1 = gs.tileManager.mapTileNum[tileLeftCol][tileTopRow];
                tileNum2 = gs.tileManager.mapTileNum[tileRightCol][tileTopRow];
                break;
            case "down":
                tileBottomRow = (worldY + solidArea.y + solidArea.height + speed) / gs.titleSize;
                tileNum1 = gs.tileManager.mapTileNum[tileLeftCol][tileBottomRow];
                tileNum2 = gs.tileManager.mapTileNum[tileRightCol][tileBottomRow];
                break;
            case "left":
                tileLeftCol = (worldX + solidArea.x - speed) / gs.titleSize;
                tileNum1 = gs.tileManager.mapTileNum[tileLeftCol][tileTopRow];
                tileNum2 = gs.tileManager.mapTileNum[tileLeftCol][tileBottomRow];
                break;
            case "right":
                tileRightCol = (worldX + solidArea.x + solidArea.width + speed) / gs.titleSize;
                tileNum1 = gs.tileManager.mapTileNum[tileRightCol][tileTopRow];
                tileNum2 = gs.tileManager.mapTileNum[tileRightCol][tileBottomRow];
                break;
            default:
                return;
        }

        // Проверка столкновений
        if (gs.tileManager.tile[tileNum1].collision || gs.tileManager.tile[tileNum2].collision) {
            collisionOn = true;
        } else {
            collisionOn = false;
        }
    }


    public void draw(Graphics g) {
        BufferedImage image = switch (direction) {
            case "up" -> up1;
            case "down" -> down1;
            case "left" -> left1;
            case "right" -> right1;
            default -> down1;
        };

        g.drawImage(image, screenX, screenY, gs.titleSize, gs.titleSize, null);
        drawHealthBar((Graphics2D) g);

        if (keyHandler.iPressed) {
            drawInventory((Graphics2D) g);
        }
    }

    public void drawHealthBar(Graphics2D g) {
        int barWidth = 100;
        int barHeight = 10;
        int x = screenX + (gs.titleSize - barWidth) / 2;
        int y = screenY - 15;

        int filledWidth = (currentHealth * barWidth) / maxHealth;
        if (filledWidth < 0) filledWidth = 0;

        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, barWidth, barHeight);

        g.setColor(Color.GREEN);
        g.fillRect(x, y, filledWidth, barHeight);

        g.setColor(Color.BLACK);
        g.drawRect(x, y, barWidth, barHeight);
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