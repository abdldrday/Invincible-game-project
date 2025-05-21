import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {

    gameScreen gs;
    public int worldX, worldY;
    public int speed;
    public int maxHealth = 100;
    public int currentHealth = 100;
    public String direction = "down";
    public BufferedImage up1, down1, left1, right1;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);

    public Entity(gameScreen gs){
        this.gs = gs;
    }

    public void draw(Graphics g2){
        int screenX = worldX - gs.player.screenX + gs.player.screenX;
        int screenY = worldY - gs.player.screenY + gs.player.screenY;

        if(worldX + gs.titleSize > gs.player.worldX - gs.player.screenX &&
                worldX - gs.titleSize < gs.player.worldX + gs.player.screenX &&
                worldY + gs.titleSize > gs.player.worldY - gs.player.screenY &&
                worldY - gs.titleSize > gs.player.worldY + gs.player.screenY
        ) {
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
    }
}
