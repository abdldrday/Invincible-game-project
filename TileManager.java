import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class TileManager {
    gameScreen gs;
    Tile[] tile;

    public TileManager(gameScreen gs) {
        this.gs = gs;
        tile = new Tile[10];
        getTileIMG();
    }

    public void getTileIMG(){
        try {

            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/pfp/earth/grass01.png"));


            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/pfp/earth/grass01.png"));

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2){
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while(col < gs.maxScreenCol && row < gs.maxScreenRow){
            g2.drawImage(tile[0].image, x, y, gs.titleSize, gs.titleSize, null);
            col++;
            x += gs.titleSize;
            if (col == gs.maxScreenCol){
                col = 0;
                x = 0;
                row++;
                y += gs.titleSize;

            }
        }
    }
}
