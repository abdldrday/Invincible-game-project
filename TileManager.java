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

            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/pfp/earth/grass.png"));

            tile[6] = new Tile();
            tile[6].image = ImageIO.read(getClass().getResourceAsStream("/pfp/earth/crosswalk.png"));

            tile[7] = new Tile();
            tile[7].image = ImageIO.read(getClass().getResourceAsStream("/pfp/earth/cracked.png"));


            tile[8] = new Tile();
            tile[8].image = ImageIO.read(getClass().getResourceAsStream("/pfp/earth/ruins.png"));

            tile[9] = new Tile();
            tile[9].image = ImageIO.read(getClass().getResourceAsStream("/pfp/earth/metal.png"));

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


    public void draw(Graphics2D g2) {
        for (int row = 0; row < gs.maxWorldRow; row++) {
            for (int col = 0; col < gs.maxWorldCol; col++) {

                int tileNum = mapTileNum[col][row];

                int worldX = col * gs.titleSize;
                int worldY = row * gs.titleSize;

                int screenX = worldX - gs.player.worldX + gs.player.screenX;
                int screenY = worldY - gs.player.worldY + gs.player.screenY;

                // Левый край карты
                if (gs.player.worldX < gs.player.screenX) {
                    screenX = worldX;
                }
                // Правый край карты
                if (gs.player.worldX > gs.worldWidth - (gs.screenWidth - gs.player.screenX)) {
                    screenX = gs.screenWidth - (gs.worldWidth - worldX);
                }
                // Верхний край карты
                if (gs.player.worldY < gs.player.screenY) {
                    screenY = worldY;
                }
                // Нижний край карты
                if (gs.player.worldY > gs.worldHeight - (gs.screenHeight - gs.player.screenY)) {
                    screenY = gs.screenHeight - (gs.worldHeight - worldY);
                }

                // Проверка видимости на экране
                if (screenX + gs.titleSize > 0 && screenX < gs.screenWidth &&
                        screenY + gs.titleSize > 0 && screenY < gs.screenHeight) {

                    g2.drawImage(tile[tileNum].image, screenX, screenY, gs.titleSize, gs.titleSize, null);
                }
            }
        }
    }





}
