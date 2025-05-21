import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class NPC extends Entity{
    public NPC(gameScreen gs){
        super(gs);

        direction = "down";
        speed = 1;
//        getImg();
    }

//    public void getImg() {
//        try {
//            up1 = ImageIO.read(getClass().getResourceAsStream("/pfp/player/oldman_up_1.png"));
//            down1 = ImageIO.read(getClass().getResourceAsStream("/pfp/player/oldman_down_1.png"));
//            left1 = ImageIO.read(getClass().getResourceAsStream("/pfp/player/oldman_left_1.png"));
//            right1 = ImageIO.read(getClass().getResourceAsStream("/pfp/player/oldman_right_1.png"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }



//    public void draw(Graphics g2) {
//        BufferedImage image = null;
//
//        switch (direction) {
//            case "up":
//                image = up1;
//                break;
//            case "down":
//                image = down1;
//                break;
//            case "left":
//                image = left1;
//                break;
//            case "right":
//                image = right1;
//                break;
//        }
//
//
//    }


}
