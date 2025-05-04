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
            up1 = ImageIO.read(getClass().getResourceAsStream("/pfp/allen-up1.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/pfp/allen-down1.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/pfp/allen-left1.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/pfp/allen-right1.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void update(){
        if (keyHandler.upPressed == true){
            direction = "up";
            y -= speed;
        }
        else if(keyHandler.downPressed == true){
            direction = "down";
            y += speed;
        }
        else if(keyHandler.leftPressed == true){
            direction = "left";
            x -= speed;
        }
        else if(keyHandler.rightPressed == true){
            direction = "right";
            x += speed;
        }
    }

    public void draw(Graphics g2){
        BufferedImage image = null;

        switch (direction){
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


        g2.drawImage(image, x, y, gs.titleSize, gs.titleSize, null);
        }
    }

