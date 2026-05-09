package manager;

import org.junit.Before;
import org.junit.Test;
import java.awt.event.KeyEvent;
import static org.junit.Assert.*;

public class KeyHandlerTest {

    private KeyHandler keyHandler;

    @Before
    public void setUp() {
        keyHandler = new KeyHandler();
        // Сбрасываем статические переменные перед каждым тестом
        KeyHandler.upPressed = false;
        KeyHandler.downPressed = false;
        KeyHandler.leftPressed = false;
        KeyHandler.rightPressed = false;
        KeyHandler.pausePressed = false;
    }

    @Test
    public void testKeyPressed_W_SetsUpPressedTrue() {
        KeyEvent event = new KeyEvent(new java.awt.Component() {}, 0, 0, 0, KeyEvent.VK_W, 'W');
        keyHandler.keyPressed(event);

        assertTrue(KeyHandler.upPressed);
        assertFalse(KeyHandler.downPressed);
        assertFalse(KeyHandler.leftPressed);
        assertFalse(KeyHandler.rightPressed);
    }

    @Test
    public void testKeyPressed_A_SetsLeftPressedTrue() {
        KeyEvent event = new KeyEvent(new java.awt.Component() {}, 0, 0, 0, KeyEvent.VK_A, 'A');
        keyHandler.keyPressed(event);

        assertTrue(KeyHandler.leftPressed);
        assertFalse(KeyHandler.upPressed);
        assertFalse(KeyHandler.downPressed);
        assertFalse(KeyHandler.rightPressed);
    }

    @Test
    public void testKeyPressed_S_SetsDownPressedTrue() {
        KeyEvent event = new KeyEvent(new java.awt.Component() {}, 0, 0, 0, KeyEvent.VK_S, 'S');
        keyHandler.keyPressed(event);

        assertTrue(KeyHandler.downPressed);
        assertFalse(KeyHandler.upPressed);
        assertFalse(KeyHandler.leftPressed);
        assertFalse(KeyHandler.rightPressed);
    }

    @Test
    public void testKeyPressed_D_SetsRightPressedTrue() {
        KeyEvent event = new KeyEvent(new java.awt.Component() {}, 0, 0, 0, KeyEvent.VK_D, 'D');
        keyHandler.keyPressed(event);

        assertTrue(KeyHandler.rightPressed);
        assertFalse(KeyHandler.upPressed);
        assertFalse(KeyHandler.leftPressed);
        assertFalse(KeyHandler.downPressed);
    }

    @Test
    public void testKeyPressed_Space_TogglesPausePressed() {
        KeyEvent event = new KeyEvent(new java.awt.Component() {}, 0, 0, 0, KeyEvent.VK_SPACE, ' ');

        // Первое нажатие - пауза включается
        keyHandler.keyPressed(event);
        assertTrue(KeyHandler.pausePressed);

        // Второе нажатие - пауза выключается
        keyHandler.keyPressed(event);
        assertFalse(KeyHandler.pausePressed);

        // Третье нажатие - пауза включается снова
        keyHandler.keyPressed(event);
        assertTrue(KeyHandler.pausePressed);
    }

    @Test
    public void testMultipleKeysPressed() {
        KeyEvent eventW = new KeyEvent(new java.awt.Component() {}, 0, 0, 0, KeyEvent.VK_W, 'W');
        KeyEvent eventA = new KeyEvent(new java.awt.Component() {}, 0, 0, 0, KeyEvent.VK_A, 'A');

        keyHandler.keyPressed(eventW);
        keyHandler.keyPressed(eventA);

        assertTrue(KeyHandler.upPressed);
        assertTrue(KeyHandler.leftPressed);
        assertFalse(KeyHandler.downPressed);
        assertFalse(KeyHandler.rightPressed);
    }

    @Test
    public void testKeyTyped_DoesNothing() {
        // Проверяем, что keyTyped не меняет состояния
        KeyEvent event = new KeyEvent(new java.awt.Component() {}, 0, 0, 0, KeyEvent.VK_W, 'W');
        keyHandler.keyTyped(event);

        assertFalse(KeyHandler.upPressed);
        assertFalse(KeyHandler.downPressed);
        assertFalse(KeyHandler.leftPressed);
        assertFalse(KeyHandler.rightPressed);
        assertFalse(KeyHandler.pausePressed);
    }

    @Test
    public void testKeyReleased_DoesNothing() {
        KeyEvent event = new KeyEvent(new java.awt.Component() {}, 0, 0, 0, KeyEvent.VK_W, 'W');
        keyHandler.keyPressed(event);
        assertTrue(KeyHandler.upPressed);

        // keyReleased не должен сбрасывать состояние
        keyHandler.keyReleased(event);
        assertTrue(KeyHandler.upPressed);
    }
}