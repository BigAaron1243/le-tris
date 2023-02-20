import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.LinkedList;
import java.util.TimerTask;
import java.lang.Math;

public class Main {
    public static void main(String[] args) {


        DrawHandler dooba = new DrawHandler();
        BoardState gb = new BoardState();

        for (;;) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
            dooba.update();
        }
    }
}

class Mino {
    int[] T = {0,0,0,1,-1,0,1,0};
    int[] I = {0,0,0,-1,0,1,0,2};
    int[] structvar = new int[8];
    int x = 5;
    int y = 5;

    public void update() {
        y--;
    }

    public void rotate(int rx, int ry) {
        x = rx * x;
        y = ry * y;
    }
    
}

class DrawHandler extends JPanel{
    JFrame frame = new JFrame();
    BoardState bstate = new BoardState();

    DrawHandler() {
        frame.setPreferredSize(new Dimension(500, 500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 10; x++) {
                if (bstate.board[y][x] > 0) {
                    g2.fillRect(x * 20  + 2, 402 - (y * 20), 19, 19);
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            g2.fillRect((bstate.amino.x + bstate.amino.T[i * 2]) * 20 + 2, 402 - ((bstate.amino.y + bstate.amino.T[i * 2 + 1]) * 20), 19, 19);
        }
    }
    public void update() {
        bstate.amino.update();
        frame.repaint();
    }
}

class BoardState {
    int[][] board = new int[20][10];
    Mino amino = new Mino();

    BoardState() {
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 10; x++) {
                board[y][x] = 0;
            }
        }
        /*for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.printf("%2d", board[i][j]);
            }
            System.out.printf("%n");
        }*/
    }
}


/*class KeyDaemon implements KeyListener {
    SnakeDaemon jpD;
    public void keyTyped(KeyEvent e) {

    }
    public void keyPressed(KeyEvent e) {
        jpD.dirMan(Character.toLowerCase(e.getKeyChar()));
    }
    public void keyReleased(KeyEvent e) {

    }
    public KeyDaemon(SnakeDaemon jpDREF) {
        jpD = jpDREF;
    }
}
*/
