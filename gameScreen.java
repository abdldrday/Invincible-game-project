import javax.swing.*;
import java.awt.*;

public class gameScreen extends JPanel {

//    Screen settings
    final int originalTitle = 16;
    final int scale = 3;

    final int titlSize = originalTitle * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = titlSize * maxScreenCol;
    final int screenHeight = titlSize * maxScreenRow;

    public gameScreen(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
    }
}
