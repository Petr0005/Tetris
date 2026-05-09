package mino;

import manager.KeyHandler;
import manager.PlayManager;
import org.junit.Before;
import org.junit.Test;
import java.awt.Color;
import static org.junit.Assert.*;

public class MinoTest {

    private Mino_L1 testMino;

    @Before
    public void setUp() {
        PlayManager.left_x = 180;
        PlayManager.right_x = 540;
        PlayManager.top_y = 50;
        PlayManager.bottom_y = 650;
        PlayManager.staticBlocks.clear();
        PlayManager.dropInterval = 60;

        KeyHandler.upPressed = false;
        KeyHandler.downPressed = false;
        KeyHandler.leftPressed = false;
        KeyHandler.rightPressed = false;
        KeyHandler.pausePressed = false;

        testMino = new Mino_L1();
        testMino.setXY(200, 100);
    }

    @Test
    public void testCreateInitializesBlocks() {
        Mino_L1 mino = new Mino_L1();

        assertNotNull(mino.b);
        assertNotNull(mino.tempB);
        assertEquals(4, mino.b.length);
        assertEquals(4, mino.tempB.length);

        for (Block block : mino.b) {
            assertNotNull(block);
            assertEquals(Color.ORANGE, block.c);
        }
    }

    @Test
    public void testSetXY() {
        testMino.setXY(300, 200);

        assertEquals(300, testMino.b[0].x);
        assertEquals(200, testMino.b[0].y);
        assertEquals(300, testMino.b[1].x);
        assertEquals(200 - Block.SIZE, testMino.b[1].y);
        assertEquals(300, testMino.b[2].x);
        assertEquals(200 + Block.SIZE, testMino.b[2].y);
        assertEquals(300 + Block.SIZE, testMino.b[3].x);
        assertEquals(200 + Block.SIZE, testMino.b[3].y);
    }

    @Test
    public void testUpdateXYUpdatesBlocksWhenNoCollision() {
        testMino.tempB[0].x = 300;
        testMino.tempB[0].y = 200;
        testMino.tempB[1].x = 300;
        testMino.tempB[1].y = 200 - Block.SIZE;
        testMino.tempB[2].x = 300;
        testMino.tempB[2].y = 200 + Block.SIZE;
        testMino.tempB[3].x = 300 + Block.SIZE;
        testMino.tempB[3].y = 200 + Block.SIZE;

        testMino.updateXY(2);

        assertEquals(2, testMino.direction);
        assertEquals(300, testMino.b[0].x);
        assertEquals(200, testMino.b[0].y);
    }

    @Test
    public void testGetDirection1() {
        testMino.getDirection1();
    }

    @Test
    public void testGetDirection2() {
        testMino.getDirection2();
    }

    @Test
    public void testGetDirection3() {
        testMino.getDirection3();
    }

    @Test
    public void testGetDirection4() {
        testMino.getDirection4();
    }

    @Test
    public void testCheckMovementCollisionLeftWall() {
        testMino.b[0].x = PlayManager.left_x;
        testMino.checkMovementCollision();
        assertTrue(testMino.leftCollision);
    }

    @Test
    public void testCheckMovementCollisionRightWall() {
        testMino.b[0].x = PlayManager.right_x - Block.SIZE;
        testMino.checkMovementCollision();
        assertTrue(testMino.rightCollision);
    }

    @Test
    public void testCheckMovementCollisionBottomWall() {
        testMino.b[0].y = PlayManager.bottom_y - Block.SIZE;
        testMino.checkMovementCollision();
        assertTrue(testMino.bottomCollision);
    }

    @Test
    public void testCheckRotationCollisionLeftWall() {
        testMino.tempB[0].x = PlayManager.left_x - 10;
        testMino.checkRotationCollision();
        assertTrue(testMino.leftCollision);
    }

    @Test
    public void testCheckRotationCollisionRightWall() {
        testMino.tempB[0].x = PlayManager.right_x - Block.SIZE + 10;
        testMino.checkRotationCollision();
        assertTrue(testMino.rightCollision);
    }

    @Test
    public void testCheckRotationCollisionBottomWall() {
        testMino.tempB[0].y = PlayManager.bottom_y - Block.SIZE + 10;
        testMino.checkRotationCollision();
        assertTrue(testMino.bottomCollision);
    }

    @Test
    public void testUpdateMovesDownWithKeyPress() {
        KeyHandler.downPressed = true;
        int oldY = testMino.b[0].y;

        testMino.update();

        assertEquals(oldY + Block.SIZE, testMino.b[0].y);
        assertFalse(KeyHandler.downPressed);
    }

    @Test
    public void testUpdateDoesNotMoveDownWhenBottomCollision() {
        testMino.b[0].y = PlayManager.bottom_y - Block.SIZE;
        testMino.checkMovementCollision();

        KeyHandler.downPressed = true;
        int oldY = testMino.b[0].y;

        testMino.update();

        assertEquals(oldY, testMino.b[0].y);
    }

    @Test
    public void testUpdateMovesLeftWithKeyPress() {
        KeyHandler.leftPressed = true;
        int oldX = testMino.b[0].x;

        testMino.update();

        assertEquals(oldX - Block.SIZE, testMino.b[0].x);
        assertFalse(KeyHandler.leftPressed);
    }

    @Test
    public void testUpdateDoesNotMoveLeftWhenLeftCollision() {
        testMino.b[0].x = PlayManager.left_x;
        testMino.checkMovementCollision();

        KeyHandler.leftPressed = true;
        int oldX = testMino.b[0].x;

        testMino.update();

        assertEquals(oldX, testMino.b[0].x);
        assertFalse(KeyHandler.leftPressed);
    }

    @Test
    public void testUpdateMovesRightWithKeyPress() {
        KeyHandler.rightPressed = true;
        int oldX = testMino.b[0].x;

        testMino.update();

        assertEquals(oldX + Block.SIZE, testMino.b[0].x);
        assertFalse(KeyHandler.rightPressed);
    }

    @Test
    public void testUpdateDoesNotMoveRightWhenRightCollision() {
        testMino.b[0].x = PlayManager.right_x - Block.SIZE;
        testMino.checkMovementCollision();

        KeyHandler.rightPressed = true;
        int oldX = testMino.b[0].x;

        testMino.update();

        assertEquals(oldX, testMino.b[0].x);
        assertFalse(KeyHandler.rightPressed);
    }

    @Test
    public void testActiveFlagDefaultTrue() {
        assertTrue(testMino.active);
    }

    @Test
    public void testDirectionDefaultValue() {
        assertEquals(1, testMino.direction);
    }
}