import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JFrame {
    public final static int WIDTH = 1920;
    public final static int HEIGHT = 1080;

    JPanel grid_panel = new GridPanel();

    public static void main(String[] args) {
        //GUI g = new GUI();
        //for (int i = 1; i < 20; i++) {
        //    System.out.println("2: " + BruteForce.brute_force(i, 2) + " 3: " + BruteForce.brute_force(i, 3) + " 4: " + BruteForce.brute_force(i, 4));
        //}
        for (int i = 2; i <= 13; i++) {
            System.out.println("10 at "+i+" : " + BruteForce.brute_force(10, i));
        }
    }

    public GUI() {
        this.setTitle("Counting-Castles");
        this.add(grid_panel);
        grid_panel.setFocusable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setUndecorated(true);
        this.setSize(WIDTH, HEIGHT);
        this.setVisible(true);
    }

}

class GridPanel extends JPanel {
    public final static Color STANDARD_COLOR = new Color(143, 78, 41);
    public final static Color MARK_COLOR = Color.RED;
    public final static int X_OFFSET = 20;
    public final static int Y_OFFSET = 20;
    public final static int GRID_WIDTH = 5;
    public final static int GRID_HEIGHT = 2;
    Color[][] grid = new Color[GRID_WIDTH][GRID_HEIGHT];
    int moving_colmn = -1;

    public GridPanel() {
        resetGrid();
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                Point translate = translateCoordinates(e.getPoint());
                if (e.getButton() == MouseEvent.BUTTON3) {
                    toggleGrid(translate, true);
                    repaint();
                } else if (e.getButton() == MouseEvent.BUTTON1) {
                    toggleGrid(translate, false);
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                moving_colmn = -1;
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    Point translate = translateCoordinates(e.getPoint());
                    toggleGrid(translate, true);
                    repaint();
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    Point translate = translateCoordinates(e.getPoint());
                    toggleGrid(translate, false);
                    repaint();
                } else if (SwingUtilities.isMiddleMouseButton(e)) {
                    Point translate = translateCoordinates(e.getPoint());
                    if (isValidDragCoordinates(translate)) {
                        if (moving_colmn == -1) {
                            moving_colmn = translate.x;
                        } else {
                            //swap
                            int curr_colm = translate.x;
                            Color[] grid_colm = grid[moving_colmn];
                            grid[moving_colmn] = grid[curr_colm];
                            grid[curr_colm] = grid_colm;
                            moving_colmn = curr_colm;
                            repaint();
                        }
                    }
                }

            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 82) {
                    resetGrid();
                    repaint();
                } else if (e.getKeyCode() == 84) {
                    deleteFloatingBlocks();
                    repaint();
                } else if (e.getKeyCode() == 70) {
                    fill();
                    repaint();
                } else if (e.getKeyCode() == 74) {
                    Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
                    Point translate = translateCoordinates(mouseLoc);
                    markGrid(translate);
                    repaint();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                moving_colmn = -1;
            }
        });
    }

    public void resetGrid() {
        this.grid = new Color[GRID_WIDTH][GRID_HEIGHT];
        for (int i = 0; i < this.grid.length; i++) {
            this.grid[i][0] = STANDARD_COLOR;
        }
    }

    public boolean isValidGridCoordinates(Point at) {
        return at.x >= 0 && at.x < this.grid.length && at.y > 0 && at.y < this.grid[0].length;
    }

    public boolean isValidDragCoordinates(Point at) {
        return at.x >= 0 && at.x < this.grid.length && at.y >= 0 && at.y < this.grid[0].length;
    }

    public void toggleGrid(Point at, boolean bool) {
        if (isValidGridCoordinates(at)) {
            if (bool) {
                if (!MARK_COLOR.equals(this.grid[at.x][at.y])) {
                    this.grid[at.x][at.y] = STANDARD_COLOR;
                }
            } else {
                this.grid[at.x][at.y] = null;
            }
        }
    }

    public void markGrid(Point at) {
        if (isValidDragCoordinates(at)) {
            if (MARK_COLOR.equals(this.grid[at.x][at.y])) {
                this.grid[at.x][at.y] = STANDARD_COLOR;
            } else {
                this.grid[at.x][at.y] = MARK_COLOR;
            }
        }
    }

    public Point translateCoordinates(Point click) {
        int available_width = this.getWidth() - 2 * X_OFFSET;
        int available_height = this.getHeight() - 2 * Y_OFFSET;
        int width = available_width / this.grid.length;
        int height = available_height / (this.grid[0].length + 1);
        height = Math.min(height, width);
        int x = click.x;
        int y = click.y;
        int grid_x = (x - X_OFFSET) / width;
        int grid_y = (available_height - y - Y_OFFSET) / height;
        return new Point(grid_x, grid_y);
    }

    public void fill() {
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = this.grid[i].length - 2; j >= 0; j--) {
                this.grid[i][j] = this.grid[i][j + 1] != null ? this.grid[i][j] == null ? STANDARD_COLOR : this.grid[i][j] : this.grid[i][j];
            }
        }
    }

    public void deleteFloatingBlocks() {
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 1; j < this.grid[i].length; j++) {
                this.grid[i][j] = this.grid[i][j - 1] == null ? null : this.grid[i][j];
            }
        }
    }

    public int numberOfBlocks() {
        int counter = 0;
        for (int j = 0; j < this.grid[0].length; j++) {
            for (int i = 0; i < this.grid.length; i++) {
                if (this.grid[i][j] != null && (i == 0 || (this.grid[i - 1][j] == null))) {
                    counter += 1;
                }
            }
        }
        return counter;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int available_width = this.getWidth() - 2 * X_OFFSET;
        int available_height = this.getHeight() - 2 * Y_OFFSET;

        int width = available_width / this.grid.length;
        int height = available_height / (this.grid[0].length + 1);
        height = Math.min(height, width);
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                if (grid[i][j] != null) {
                    if (i == moving_colmn) {
                        g.setColor(Color.GREEN);
                    } else {
                        g.setColor(grid[i][j]);
                    }
                    g.fillRect(X_OFFSET + i * width, available_height - (Y_OFFSET + (j + 1) * height), width, height);
                    g.setColor(Color.BLACK);
                }
                g.drawRect(X_OFFSET + i * width, available_height - (Y_OFFSET + (j + 1) * height), width, height);
            }
        }
        int blocks = numberOfBlocks();
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("Blocks: " + blocks, 50, 80);
    }
}
