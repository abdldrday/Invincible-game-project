import java.awt.image.BufferedImage;

public class Entity {
    public int worldX, worldY;
    public int speed;
    public int maxHealth = 100;
    public int currentHealth = 100;
    public String direction = "down";
    public BufferedImage up1, down1, left1, right1;
}
