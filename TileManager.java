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
        loadMap("/pfp/map/map1.txt");

    }

    public void getTileIMG(){
        try {

            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/pfp/earth/road.png"));


            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/pfp/earth/wall.png"));
            tile[1].collision = true;


            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/pfp/earth/sidewalk.png"));

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/pfp/earth/building.png"));
            tile[3].collision = true;

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/pfp/earth/park.png"));

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void loadMap(String path){
        try {
            InputStream is = getClass().getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int row = 0;

            while (row < gs.maxWorldRow) {
                String line = br.readLine();
                String numbers[] = line.split(" ");

                for (int col = 0; col < gs.maxWorldCol; col++) {
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                }

                row++;
            }

            br.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public void draw(Graphics2D g2){
        int worldCol = 0;
        int worldRow = 0;

        while(worldRow < gs.maxWorldRow) {
            while(worldCol < gs.maxWorldCol) {
                int tileNum = mapTileNum[worldCol][worldRow];

                int worldX = worldCol * gs.titleSize;
                int worldY = worldRow * gs.titleSize;
                int screenX = worldX - gs.player.worldX + gs.player.screenX;
                int screenY = worldY - gs.player.worldY + gs.player.screenY;

                // ✅ ПРОВЕРКА: рисуем только то, что видно
                if (worldX + gs.titleSize > gs.player.worldX - gs.player.screenX &&
                        worldX - gs.titleSize < gs.player.worldX + gs.player.screenX &&
                        worldY + gs.titleSize > gs.player.worldY - gs.player.screenY &&
                        worldY - gs.titleSize < gs.player.worldY + gs.player.screenY) {

                    g2.drawImage(tile[tileNum].image, screenX, screenY, gs.titleSize, gs.titleSize, null);
                }

                worldCol++;
            }
            worldCol = 0;
            worldRow++;
        }
    }

}
