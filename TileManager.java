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

        mapTileNum = new int[gs.maxWorldCol][gs.maxWorldRow];
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

            while (col < gs.maxWorldCol && row < gs.maxWorldRow){
                String line = br.readLine();
                while (col < gs.maxWorldCol){
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gs.maxWorldCol){
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
        int worldCol = 0;
        int worldRow = 0;

        while(worldCol < gs.maxWorldCol && worldRow < gs.maxWorldRow){
            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gs.titleSize;
            int worldY = worldRow * gs.titleSize;
            int screenX = worldX - gs.player.worldX + gs.player.screenX;
            int screenY = worldY - gs.player.worldY + gs.player.screenY;

            g2.drawImage(tile[tileNum].image, screenX, screenY, gs.titleSize, gs.titleSize, null);
            worldCol++;

            if (worldCol == gs.maxWorldCol){
                worldCol = 0;
                worldRow++;


            }
        }
    }
}
