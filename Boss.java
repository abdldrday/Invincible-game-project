import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;


public class Boss extends Entity {
    gameScreen gs;
    public boolean isAlive = true;

    public Rectangle solidArea = new Rectangle(8, 8, 16, 16);
    public boolean collisionOn = false;


    public Boss(gameScreen gs) {
        this.gs = gs;
        direction = "left";
        speed = 2;
        maxHealth = 300;
        currentHealth = maxHealth;
        worldX = gs.titleSize * 45;
        worldY = gs.titleSize * 25;
        getBossImage();
    }

    public void getBossImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/pfp/boss/boss_up.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/pfp/boss/boss_down.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/pfp/boss/boss_left.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/pfp/boss/boss_right.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (!isAlive) return;

        int dx = gs.player.worldX - worldX;
        int dy = gs.player.worldY - worldY;

        if (Math.abs(dx) < gs.titleSize * 10 && Math.abs(dy) < gs.titleSize * 10) {
            if (Math.abs(dx) > Math.abs(dy)) {
                if (dx > 0) {
                    direction = "right";
                    checkTileCollision();
                    if (!collisionOn) worldX += speed;
                } else {
                    direction = "left";
                    checkTileCollision();
                    if (!collisionOn) worldX -= speed;
                }
            } else {
                if (dy > 0) {
                    direction = "down";
                    checkTileCollision();
                    if (!collisionOn) worldY += speed;
                } else {
                    direction = "up";
                    checkTileCollision();
                    if (!collisionOn) worldY -= speed;
                }
            }
        }
    }


    public void draw(Graphics2D g2) {
        if (!isAlive) return;

        BufferedImage image = switch (direction) {
            case "up" -> up1;
            case "down" -> down1;
            case "left" -> left1;
            case "right" -> right1;
            default -> down1;
        };

        int screenX = worldX - gs.player.worldX + gs.player.screenX;
        int screenY = worldY - gs.player.worldY + gs.player.screenY;

        g2.drawImage(image, screenX, screenY, gs.titleSize, gs.titleSize, null);

        // Полоса HP
        g2.setColor(Color.red);
        g2.fillRect(screenX, screenY - 10, gs.titleSize, 5);
        g2.setColor(Color.green);
        int hpWidth = (int) ((currentHealth / (double) maxHealth) * gs.titleSize);
        g2.fillRect(screenX, screenY - 10, hpWidth, 5);
    }

    public void checkTileCollision() {
        int tileLeftCol = (worldX + solidArea.x) / gs.titleSize;
        int tileRightCol = (worldX + solidArea.x + solidArea.width) / gs.titleSize;
        int tileTopRow = (worldY + solidArea.y) / gs.titleSize;
        int tileBottomRow = (worldY + solidArea.y + solidArea.height) / gs.titleSize;

        int tileNum1 = 0, tileNum2 = 0;

        switch (direction) {
            case "up" -> {
                tileTopRow = (worldY + solidArea.y - speed) / gs.titleSize;
                tileNum1 = gs.tileManager.mapTileNum[tileLeftCol][tileTopRow];
                tileNum2 = gs.tileManager.mapTileNum[tileRightCol][tileTopRow];
            }
            case "down" -> {
                tileBottomRow = (worldY + solidArea.y + solidArea.height + speed) / gs.titleSize;
                tileNum1 = gs.tileManager.mapTileNum[tileLeftCol][tileBottomRow];
                tileNum2 = gs.tileManager.mapTileNum[tileRightCol][tileBottomRow];
            }
            case "left" -> {
                tileLeftCol = (worldX + solidArea.x - speed) / gs.titleSize;
                tileNum1 = gs.tileManager.mapTileNum[tileLeftCol][tileTopRow];
                tileNum2 = gs.tileManager.mapTileNum[tileLeftCol][tileBottomRow];
            }
            case "right" -> {
                tileRightCol = (worldX + solidArea.x + solidArea.width + speed) / gs.titleSize;
                tileNum1 = gs.tileManager.mapTileNum[tileRightCol][tileTopRow];
                tileNum2 = gs.tileManager.mapTileNum[tileRightCol][tileBottomRow];
            }
        }

        collisionOn = gs.tileManager.tile[tileNum1].collision || gs.tileManager.tile[tileNum2].collision;
    }

}
