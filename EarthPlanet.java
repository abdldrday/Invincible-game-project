import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class EarthPlanet {
    public int x, y, width, height;
    public boolean active = true;
    private BufferedImage image;

    public EarthPlanet(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/pfp/earth/earth_planet.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        if (active && image != null) {
            g2.drawImage(image, x, y, width, height, null);
        }
    }

    public boolean intersects(int px, int py, int pSize) {
        Rectangle playerRect = new Rectangle(px, py, pSize, pSize);
        Rectangle portalRect = new Rectangle(x, y, width, height);
        return playerRect.intersects(portalRect);
    }
}
