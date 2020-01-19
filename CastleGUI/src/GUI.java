import javax.swing.*;

public class GUI extends JFrame{
    public final static int WIDTH = 800;
    public final static int HEIGHT = 600;
    int grid_width;
    int grid_height;

    public static void main(String[] args) {
        GUI g = new GUI();

    }

    public GUI(){
        this.setTitle("Counting-Castles");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH,HEIGHT);
        this.setVisible(true);
    }
}
