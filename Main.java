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
import java.util.Arrays;
import java.lang.Math;

public class Main {
    public static void main(String[] args) {


        DrawHandler dh = new DrawHandler();
        BoardState gb = new BoardState();

        for (;;) {
            for (int i = 0; i < 50; i++) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {

                }
                dh.frame.repaint();
            }
            dh.update();
        }
    }
}

class Mino {
    static int[][] presets = {
        {0,0,0,1,-1,0,1,0}, //T
        {0,0,0,-1,0,1,0,2}, //I
        {0,0,0,1,1,0,1,1}, //O
        {0,0,1,0,-1,0,-1,-1}, //L
        {0,0,-1,0,0,1,1,1}, //S
        {0,0,-1,0,1,0,1,-1}, //J
        {0,0,1,0,-1,1,0,1} //Z
    };
    int[] minolit = new int[8];
    int x = 5;
    int y = 20;
    int collision = 0;
    BoardState bs;

    Mino(int piece, BoardState bstate) {
        bs = bstate;

        minolit = presets[piece].clone();
        x = 5;
        y = 20;
        collision = 0;
    }

    public void move(int vx) {
        boolean domove = true;
        for (int i = 0; i < 4; i++) {
            try {
                if (minolit[i*2]+x+vx >= 10 || minolit[i*2]+x+vx < 0) {
                    domove = false;
                    System.out.println("ppap");
                } else if (bs.board[minolit[i*2+1] + y][minolit[i*2]+ x + vx] != 0) {
                    domove = false;
                    System.out.println("eae");
                }
            } catch(ArrayIndexOutOfBoundsException e) {
                System.out.println(e);
            }
        }
        if (domove) {
            x += vx;
        }
    }

    public void update() {
        y--;
        //rotate(1,-1);
    }

    public void rotate(int rx, int ry) {
        //System.out.println("old array: " + Arrays.toString(minolit));
        for (int i = 0; i < 4; i++) {
            //System.out.printf("[block %d]%nold:%nx:%d%ny:%d%n", i, minolit[i*2], minolit[i*2+1]);
            int xtemp = minolit[i*2];
            int ytemp = minolit[i*2+1];
            minolit[i*2] = rx * ytemp;
            minolit[i*2+1] = ry * xtemp;
            //System.out.printf("new:%nx:%d%ny:%d%n", minolit[i*2], minolit[i*2+1]);
        }
        //System.out.println("new array: " + Arrays.toString(minolit));
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
        frame.addKeyListener(new KeyDaemon(this));
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
            g2.fillRect((bstate.amino.x + bstate.amino.minolit[i * 2]) * 20 + 2, 402 - ((bstate.amino.y + bstate.amino.minolit[i * 2 + 1]) * 20), 19, 19);
        }
    }
    public void update() {
        bstate.update();
        frame.repaint();
    }
}

class BoardState {
    int[][] board = new int[20][10];
    Mino amino = new Mino(0, this);
    int score = 0;
    int level = 1;

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
    public void update() {
        amino.update();
        for (int i = 0; i < 4; i++) {
            if (amino.minolit[i*2+1] + amino.y < 1) {
                amino.collision++;
                amino.y = amino.y + 1;
                break;
            } else {
                try {
                    if (board[amino.minolit[i*2+1] + amino.y][amino.minolit[i*2] + amino.x] != 0) {
                        amino.collision++;
                        amino.y = amino.y + 1;
                        break;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                
                }
            }
        }
        if (amino.collision > 2) {
            for (int i = 0; i < 4; i++) {
                board[amino.minolit[i*2+1] + amino.y][amino.minolit[i*2]+amino.x] = 1;
            }
            amino = new Mino(0, this);
        }
    }
}


class KeyDaemon implements KeyListener {
    DrawHandler dh;
    public void keyTyped(KeyEvent e) {

    }
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case (68):
                dh.bstate.amino.rotate(-1, 1);
                break;
            case (70):
                dh.bstate.amino.rotate(1,-1);
                break;
            case (39):
                dh.bstate.amino.move(1);
                break;
            case(37):
                dh.bstate.amino.move(-1);
                break;
        }
    }
    public void keyReleased(KeyEvent e) {

    }
    public KeyDaemon(DrawHandler dhREF) {
        dh = dhREF;
    }
}

