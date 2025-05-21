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
        worldX = gs.titleSize * 23 - (gs.titleSize / 2);
        worldY = gs.titleSize * 21 - (gs.titleSize / 2);
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

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case "up":
                image = up1;
                break;
            case "down":
                image = down1;
                break;
            case "left":
                image = left1;
                break;
            case "right":
                image = right1;
                break;
        }

        g2.drawImage(image, screenX, screenY, gs.titleSize, gs.titleSize, null);
    }


    public void update() {
        if (isGameOver) return;

        if (keyHandler.upPressed) {
            direction = "up";
            checkTileCollision();
            if (!collisionOn) worldY -= speed;
        } else if (keyHandler.downPressed) {
            direction = "down";
            checkTileCollision();
            if (!collisionOn) worldY += speed;
        } else if (keyHandler.leftPressed) {
            direction = "left";
            checkTileCollision();
            if (!collisionOn) worldX -= speed;
        } else if (keyHandler.rightPressed) {
            direction = "right";
            checkTileCollision();
            if (!collisionOn) worldX += speed;
        }

        // Ограничения: чтобы игрок не выходил за границы мира
        if (worldX < 0) worldX = 0;
        if (worldY < 0) worldY = 0;

        // ✅ Ограничение справа и снизу с учётом ширины экрана
        if (worldX > gs.worldWidth - gs.screenWidth + screenX) {
            worldX = gs.worldWidth - gs.screenWidth + screenX;
        }
        if (worldY > gs.worldHeight - gs.screenHeight + screenY) {
            worldY = gs.worldHeight - gs.screenHeight + screenY;
        }
    }


    public void checkTileCollision() {
        int tileLeftCol = (worldX + solidArea.x) / gs.titleSize;
        int tileRightCol = (worldX + solidArea.x + solidArea.width) / gs.titleSize;
        int tileTopRow = (worldY + solidArea.y) / gs.titleSize;
        int tileBottomRow = (worldY + solidArea.y + solidArea.height) / gs.titleSize;

        int tileNum1 = 0, tileNum2 = 0;

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
        }

        collisionOn = gs.tileManager.tile[tileNum1].collision || gs.tileManager.tile[tileNum2].collision;
    }

    public int getX() {
        return worldX;
    }

    public int getY() {
        return worldY;
    }
}
