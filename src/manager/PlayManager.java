package manager;

import mino.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class PlayManager {
    // Main Play Area
    final int WIDTH = 360;
    final int HEIGHT = 600;

    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    //Mino
    Mino currentMino;
    final int MINO_START_X;
    final int MINO_START_Y;
    Mino nextMino;
    final int NEXT_MINO_X;
    final int NEXT_MINO_Y;
    public static ArrayList<Block> staticBlocks = new ArrayList<>();

    // Others
    public static int dropInterval = 60; // mino drops in every 60 frames

    public PlayManager() {
        left_x = (GamePanel.WIDTH/2) - (WIDTH/2);
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;

        MINO_START_X = left_x + (WIDTH/2) - Block.SIZE;
        MINO_START_Y = top_y + Block.SIZE;

        NEXT_MINO_X = right_x + 175;
        NEXT_MINO_Y = top_y + 500;

        // Set the starting Mino
        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);

        nextMino = pickMino();
        nextMino.setXY(NEXT_MINO_X, NEXT_MINO_Y);
    }

    private Mino pickMino() {

        // Pick a random mino
        Mino mino = null;
        int i = new Random().nextInt(7);

        mino = switch (i) {
            case 0 -> new Mino_L1();
            case 1 -> new Mino_L2();
            case 2 -> new Mino_Square();
            case 3 -> new Mino_Bar();
            case 4 -> new Mino_T();
            case 5 -> new Mino_Z1();
            case 6 -> new Mino_Z2();
            default -> mino;
        };

        return mino;
    }

    public void update() {
        // Check if the currentMino is active
        if (!currentMino.active) {
            // If the mino is not active, put it into the staticBlocks
            staticBlocks.add(currentMino.b[0]);
            staticBlocks.add(currentMino.b[1]);
            staticBlocks.add(currentMino.b[2]);
            staticBlocks.add(currentMino.b[3]);

            // replace the currentMino with the nextMino
            currentMino = nextMino;
            currentMino.setXY(MINO_START_X, MINO_START_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXT_MINO_X, NEXT_MINO_Y);
        } else {
            currentMino.update();
        }

    }

    public void draw(Graphics2D g2) {

        // Draw Play Area Frame
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x-4, top_y-4, WIDTH+8, HEIGHT+8);

        // Draw Next Mino Frame
        int x = right_x + 100;
        int y = bottom_y - 200;
        g2.drawRect(x, y, 200, 200);
        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x+60, y+60);

        // Draw the currentMino
        if (currentMino != null) {
            currentMino.draw(g2);
        }

        // Draw the nextMino
        nextMino.draw(g2);

        //Draw static blocks
        for (Block staticBlock : staticBlocks) {
            staticBlock.draw(g2);
        }

        // Draw Pause
        g2.setColor(Color.yellow);
        g2.setFont(g2.getFont().deriveFont(50f));
        if (KeyHandler.pausePressed) {
            x = left_x + 70;
            y = top_y + 320;
            g2.drawString("PAUSED", x, y);
        }
    }
}
