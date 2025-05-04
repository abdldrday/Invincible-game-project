import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    gameScreen gs;
    Tile[] tile;
    int mapTileNum[][];


    public TileManager(gameScreen gs) {
        this.gs = gs;
        tile = new Tile[10];

        mapTileNum = new int[gs.maxScreenCol][gs.maxScreenRow];
        getTileIMG();
        loadMap();

    }

    public void getTileIMG(){
        try {

            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/pfp/earth/grass01.png"));


            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/pfp/earth/grass01.png"));


            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/pfp/earth/road00.png"));

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void loadMap(){
        try {
            InputStream is = getClass().getResourceAsStream("/map/map1.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gs.maxScreenCol && row < gs.maxScreenRow){
                String line = br.readLine();

                while (col < gs.maxScreenCol){
                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }



                if(col == gs.maxScreenCol){
                    col = 0;
                    row++;
                }
            }
            br.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2){
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while(col < gs.maxScreenCol && row < gs.maxScreenRow){
            int tileNum = mapTileNum[col][row];

            g2.drawImage(tile[tileNum].image, x, y, gs.titleSize, gs.titleSize, null);
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
