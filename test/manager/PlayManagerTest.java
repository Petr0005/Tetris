package manager;

import mino.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Method;
import static org.junit.Assert.*;

public class PlayManagerTest {

    private PlayManager playManager;

    @Before
    public void setUp() {
        // Сбрасываем статические переменные
        PlayManager.staticBlocks.clear();
        PlayManager.dropInterval = 60;
        playManager = new PlayManager();
    }

    @Test
    public void testConstructorInitializesPlayArea() {
        assertNotNull(playManager);
        assertNotNull(PlayManager.staticBlocks);
        assertNotNull(playManager.currentMino);
        assertNotNull(playManager.nextMino);
    }

    @Test
    public void testPickMinoReturnsDifferentMinos() throws Exception {
        Method pickMinoMethod = PlayManager.class.getDeclaredMethod("pickMino");
        pickMinoMethod.setAccessible(true);

        // Проверяем что метод возвращает разные типы мино
        boolean foundDifferent = false;
        Mino firstMino = (Mino) pickMinoMethod.invoke(playManager);

        for (int i = 0; i < 20; i++) {
            Mino newMino = (Mino) pickMinoMethod.invoke(playManager);
            if (newMino.getClass() != firstMino.getClass()) {
                foundDifferent = true;
                break;
            }
        }

        assertTrue("PickMino should return different types of minos", foundDifferent);
    }

    @Test
    public void testUpdateWhenMinoActive() {
        playManager.currentMino.active = true;
        playManager.update();
        // Миню должно остаться активным или обработаться его обновление
        assertNotNull(playManager.currentMino);
    }

    @Test
    public void testUpdateWhenMinoInactive() {
        int initialStaticSize = PlayManager.staticBlocks.size();
        playManager.currentMino.active = false;

        playManager.update();

        // Блоки неактивного мино добавились в staticBlocks
        assertEquals(initialStaticSize + 4, PlayManager.staticBlocks.size());
        // Текущее мино заменилось на следующее
        assertNotNull(playManager.currentMino);
        assertNotNull(playManager.nextMino);
    }

    @Test
    public void testCheckDeleteRemovesFullHorizontalLine() throws Exception {
        // Создаем полную линию блоков
        int yLine = PlayManager.top_y + Block.SIZE;
        for (int x = 0; x < 12; x++) {
            Block block = new Block(java.awt.Color.RED);
            block.x = PlayManager.left_x + (x * Block.SIZE);
            block.y = yLine;
            PlayManager.staticBlocks.add(block);
        }

        // Добавляем дополнительный блок выше
        Block extraBlock = new Block(java.awt.Color.BLUE);
        extraBlock.x = PlayManager.left_x;
        extraBlock.y = yLine - Block.SIZE;
        PlayManager.staticBlocks.add(extraBlock);

        int sizeBefore = PlayManager.staticBlocks.size();

        Method checkDelete = PlayManager.class.getDeclaredMethod("checkDelete");
        checkDelete.setAccessible(true);
        checkDelete.invoke(playManager);

        // Полная линия удалена (12 блоков)
        assertEquals(sizeBefore - 12, PlayManager.staticBlocks.size());

        // Блок выше должен сдвинуться вниз
        boolean blockMovedDown = false;
        for (Block block : PlayManager.staticBlocks) {
            if (block == extraBlock && block.y == yLine) {
                blockMovedDown = true;
                break;
            }
        }
        assertTrue("Blocks above should move down", blockMovedDown);
    }

    @Test
    public void testCheckDeleteDoesNothingWhenNoFullLines() throws Exception {
        // Добавляем блоки, но не полную линию
        for (int i = 0; i < 5; i++) {
            Block block = new Block(java.awt.Color.RED);
            block.x = PlayManager.left_x + (i * Block.SIZE);
            block.y = PlayManager.top_y + Block.SIZE;
            PlayManager.staticBlocks.add(block);
        }

        int sizeBefore = PlayManager.staticBlocks.size();

        Method checkDelete = PlayManager.class.getDeclaredMethod("checkDelete");
        checkDelete.setAccessible(true);
        checkDelete.invoke(playManager);

        assertEquals(sizeBefore, PlayManager.staticBlocks.size());
    }

    @Test
    public void testGameOverFlagWhenMinoCollidesAtSpawn() throws Exception {
        // Создаем ситуацию когда мино сразу сталкивается
        java.lang.reflect.Field gameOverField = PlayManager.class.getDeclaredField("gameOver");
        gameOverField.setAccessible(true);

        // Изначально gameOver = false
        assertFalse((Boolean) gameOverField.get(playManager));

        // Делаем currentMino неактивным и на той же позиции
        playManager.currentMino.active = false;

        // Добавляем блок на стартовую позицию
        Block collisionBlock = new Block(java.awt.Color.RED);
        collisionBlock.x = playManager.currentMino.b[0].x;
        collisionBlock.y = playManager.currentMino.b[0].y;
        PlayManager.staticBlocks.add(collisionBlock);

        playManager.update();

        // gameOver должно стать true
        assertTrue((Boolean) gameOverField.get(playManager));
    }

    @Test
    public void testUpdateReplacesCurrentMinoWithNextMino() {
        Mino originalNextMino = playManager.nextMino;
        playManager.currentMino.active = false;

        playManager.update();

        // Текущее мино становится бывшим следующим
        assertSame(originalNextMino, playManager.currentMino);
        // Новое следующее мино создано
        assertNotNull(playManager.nextMino);
        assertNotSame(originalNextMino, playManager.nextMino);
    }

    @Test
    public void testMultipleLineDeletion() throws Exception {
        // Создаем 2 полные линии
        for (int line = 0; line < 2; line++) {
            int yLine = PlayManager.top_y + Block.SIZE + (line * Block.SIZE);
            for (int x = 0; x < 12; x++) {
                Block block = new Block(java.awt.Color.RED);
                block.x = PlayManager.left_x + (x * Block.SIZE);
                block.y = yLine;
                PlayManager.staticBlocks.add(block);
            }
        }

        Method checkDelete = PlayManager.class.getDeclaredMethod("checkDelete");
        checkDelete.setAccessible(true);
        checkDelete.invoke(playManager);

        // Обе линии должны быть удалены (24 блока)
        boolean foundBlockAtLineY = false;
        for (Block block : PlayManager.staticBlocks) {
            if (block.y == PlayManager.top_y + Block.SIZE ||
                    block.y == PlayManager.top_y + Block.SIZE * 2) {
                foundBlockAtLineY = true;
                break;
            }
        }
        assertFalse("Both lines should be deleted", foundBlockAtLineY);
    }

    @Test
    public void testDropIntervalCanBeChanged() {
        assertEquals(60, PlayManager.dropInterval);

        PlayManager.dropInterval = 30;
        assertEquals(30, PlayManager.dropInterval);

        // Восстанавливаем
        PlayManager.dropInterval = 60;
    }
}