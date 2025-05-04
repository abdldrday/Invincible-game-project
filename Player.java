import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOError;
import java.io.IOException;

public class Player extends Entity{
    gameScreen gs;
    KeyHandler keyHandler;

    public Player(gameScreen gs, KeyHandler keyHandler) {
        this.gs = gs;
        this.keyHandler = keyHandler;
        setDefValue();
        getPlayerImg();
    }

    public void setDefValue(){
        x = 100;
        y = 100;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImg(){
        try {
            down1 = ImageIO.read(getClass().getResourceAsStream("/pfp/allen-down1.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void update(){
        if (keyHandler.upPressed == true){
            y -= speed;
        }
        else if(keyHandler.downPressed == true){
            y += speed;
        }
        else if(keyHandler.leftPressed == true){
            x -= speed;
        }
        else if(keyHandler.rightPressed == true){
            x += speed;
        }
    }

    public void draw(Graphics g2){
        BufferedImage image = null;

        switch (direction){
            case "down":
                image = down1;
                break;
            }


        g2.drawImage(image, x, y, gs.titlSize, gs.titlSize, null);
        }
    }

