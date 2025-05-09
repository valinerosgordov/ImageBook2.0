package ru.imagebook.server.service.pdf;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

public class ImposerPageConfigTest {
    private ImposerPageConfig imposerPageConfig = null;
    private final float ZERO = 0;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testImposerPageConfig() throws Exception {
        imposerPageConfig = new ImposerPageConfig();
        Field blockWidth = imposerPageConfig.getClass().getDeclaredField("blockWidth");
        blockWidth.setAccessible(true);
        Field blockHeight = imposerPageConfig.getClass().getDeclaredField("blockHeight");
        blockHeight.setAccessible(true);
        Field gabx = imposerPageConfig.getClass().getDeclaredField("gabx");
        gabx.setAccessible(true);
        Field gaby = imposerPageConfig.getClass().getDeclaredField("gaby");
        gaby.setAccessible(true);
        Field x = imposerPageConfig.getClass().getDeclaredField("x");
        x.setAccessible(true);
        Field y = imposerPageConfig.getClass().getDeclaredField("y");
        y.setAccessible(true);
        assertEquals(blockWidth.get(imposerPageConfig), ZERO);
        assertEquals(blockHeight.get(imposerPageConfig), ZERO);
        assertEquals(gabx.get(imposerPageConfig), ZERO);
        assertEquals(gaby.get(imposerPageConfig), ZERO);
        assertEquals(x.get(imposerPageConfig), ZERO);
        assertEquals(y.get(imposerPageConfig), ZERO);
    }

    @Test
    public void testImposerPageConfigFloatFloatFloatFloatFloatFloat() throws Exception {
        final float expectedBlockWidth = 123f;
        final float expectedBlockHeight = 234f;
        final float expectedBlockGabX = 345f;
        final float expectedBlockGabY = 465f;
        final float expectedBlockX = 567f;
        final float expectedBlockY = 678f;
        imposerPageConfig = new ImposerPageConfig(expectedBlockWidth, expectedBlockHeight, expectedBlockGabX,
                expectedBlockGabY, expectedBlockX, expectedBlockY);
        Field blockWidth = imposerPageConfig.getClass().getDeclaredField("blockWidth");
        blockWidth.setAccessible(true);
        Field blockHeight = imposerPageConfig.getClass().getDeclaredField("blockHeight");
        blockHeight.setAccessible(true);
        Field gabx = imposerPageConfig.getClass().getDeclaredField("gabx");
        gabx.setAccessible(true);
        Field gaby = imposerPageConfig.getClass().getDeclaredField("gaby");
        gaby.setAccessible(true);
        Field x = imposerPageConfig.getClass().getDeclaredField("x");
        x.setAccessible(true);
        Field y = imposerPageConfig.getClass().getDeclaredField("y");
        y.setAccessible(true);
        assertEquals(blockWidth.get(imposerPageConfig), expectedBlockWidth);
        assertEquals(blockHeight.get(imposerPageConfig), expectedBlockHeight);
        assertEquals(gabx.get(imposerPageConfig), expectedBlockGabX);
        assertEquals(gaby.get(imposerPageConfig), expectedBlockGabY);
        assertEquals(x.get(imposerPageConfig), expectedBlockX);
        assertEquals(y.get(imposerPageConfig), expectedBlockY);
    }

    @Test
    public void testGetBlockWidth() throws Exception {
        final float expectedBlockWidth = 123f;
        imposerPageConfig = new ImposerPageConfig(expectedBlockWidth, ZERO, ZERO, ZERO, ZERO, ZERO);
        Field blockWidth = imposerPageConfig.getClass().getDeclaredField("blockWidth");
        blockWidth.setAccessible(true);
        Field blockHeight = imposerPageConfig.getClass().getDeclaredField("blockHeight");
        blockHeight.setAccessible(true);
        Field gabx = imposerPageConfig.getClass().getDeclaredField("gabx");
        gabx.setAccessible(true);
        Field gaby = imposerPageConfig.getClass().getDeclaredField("gaby");
        gaby.setAccessible(true);
        Field x = imposerPageConfig.getClass().getDeclaredField("x");
        x.setAccessible(true);
        Field y = imposerPageConfig.getClass().getDeclaredField("y");
        y.setAccessible(true);
        assertEquals(imposerPageConfig.getBlockWidth(), expectedBlockWidth, ZERO);
        assertEquals(blockHeight.get(imposerPageConfig), ZERO);
        assertEquals(gabx.get(imposerPageConfig), ZERO);
        assertEquals(gaby.get(imposerPageConfig), ZERO);
        assertEquals(x.get(imposerPageConfig), ZERO);
        assertEquals(y.get(imposerPageConfig), ZERO);
    }

    @Test
    public void testSetBlockWidth() throws Exception {
        final float expectedBlockWidth = 123f;
        imposerPageConfig = new ImposerPageConfig();
        imposerPageConfig.setBlockWidth(expectedBlockWidth);
        Field blockWidth = imposerPageConfig.getClass().getDeclaredField("blockWidth");
        blockWidth.setAccessible(true);
        Field blockHeight = imposerPageConfig.getClass().getDeclaredField("blockHeight");
        blockHeight.setAccessible(true);
        Field gabx = imposerPageConfig.getClass().getDeclaredField("gabx");
        gabx.setAccessible(true);
        Field gaby = imposerPageConfig.getClass().getDeclaredField("gaby");
        gaby.setAccessible(true);
        Field x = imposerPageConfig.getClass().getDeclaredField("x");
        x.setAccessible(true);
        Field y = imposerPageConfig.getClass().getDeclaredField("y");
        y.setAccessible(true);
        assertEquals(blockWidth.get(imposerPageConfig), expectedBlockWidth);
        assertEquals(blockHeight.get(imposerPageConfig), ZERO);
        assertEquals(gabx.get(imposerPageConfig), ZERO);
        assertEquals(gaby.get(imposerPageConfig), ZERO);
        assertEquals(x.get(imposerPageConfig), ZERO);
        assertEquals(y.get(imposerPageConfig), ZERO);
    }

    @Test
    public void testGetBlockHeight() throws Exception {
        final float expectedBlockHeight = 123f;
        imposerPageConfig = new ImposerPageConfig(ZERO, expectedBlockHeight, ZERO, ZERO, ZERO, ZERO);
        Field blockWidth = imposerPageConfig.getClass().getDeclaredField("blockWidth");
        blockWidth.setAccessible(true);
        Field blockHeight = imposerPageConfig.getClass().getDeclaredField("blockHeight");
        blockHeight.setAccessible(true);
        Field gabx = imposerPageConfig.getClass().getDeclaredField("gabx");
        gabx.setAccessible(true);
        Field gaby = imposerPageConfig.getClass().getDeclaredField("gaby");
        gaby.setAccessible(true);
        Field x = imposerPageConfig.getClass().getDeclaredField("x");
        x.setAccessible(true);
        Field y = imposerPageConfig.getClass().getDeclaredField("y");
        y.setAccessible(true);
        assertEquals(blockWidth.get(imposerPageConfig), ZERO);
        assertEquals(imposerPageConfig.getBlockHeight(), expectedBlockHeight, ZERO);
        assertEquals(gabx.get(imposerPageConfig), ZERO);
        assertEquals(gaby.get(imposerPageConfig), ZERO);
        assertEquals(x.get(imposerPageConfig), ZERO);
        assertEquals(y.get(imposerPageConfig), ZERO);
    }

    @Test
    public void testSetBlockHeight() throws Exception {
        final float expectedBlockHeight = 123f;
        imposerPageConfig = new ImposerPageConfig();
        imposerPageConfig.setBlockHeight(expectedBlockHeight);
        Field blockWidth = imposerPageConfig.getClass().getDeclaredField("blockWidth");
        blockWidth.setAccessible(true);
        Field blockHeight = imposerPageConfig.getClass().getDeclaredField("blockHeight");
        blockHeight.setAccessible(true);
        Field gabx = imposerPageConfig.getClass().getDeclaredField("gabx");
        gabx.setAccessible(true);
        Field gaby = imposerPageConfig.getClass().getDeclaredField("gaby");
        gaby.setAccessible(true);
        Field x = imposerPageConfig.getClass().getDeclaredField("x");
        x.setAccessible(true);
        Field y = imposerPageConfig.getClass().getDeclaredField("y");
        y.setAccessible(true);
        assertEquals(blockWidth.get(imposerPageConfig), ZERO);
        assertEquals(blockHeight.get(imposerPageConfig), expectedBlockHeight);
        assertEquals(gabx.get(imposerPageConfig), ZERO);
        assertEquals(gaby.get(imposerPageConfig), ZERO);
        assertEquals(x.get(imposerPageConfig), ZERO);
        assertEquals(y.get(imposerPageConfig), ZERO);
    }

    @Test
    public void testGetGabx() throws Exception {
        final float expectedGabX = 123f;
        imposerPageConfig = new ImposerPageConfig(ZERO, ZERO, expectedGabX, ZERO, ZERO, ZERO);
        Field blockWidth = imposerPageConfig.getClass().getDeclaredField("blockWidth");
        blockWidth.setAccessible(true);
        Field blockHeight = imposerPageConfig.getClass().getDeclaredField("blockHeight");
        blockHeight.setAccessible(true);
        Field gabx = imposerPageConfig.getClass().getDeclaredField("gabx");
        gabx.setAccessible(true);
        Field gaby = imposerPageConfig.getClass().getDeclaredField("gaby");
        gaby.setAccessible(true);
        Field x = imposerPageConfig.getClass().getDeclaredField("x");
        x.setAccessible(true);
        Field y = imposerPageConfig.getClass().getDeclaredField("y");
        y.setAccessible(true);
        assertEquals(blockWidth.get(imposerPageConfig), ZERO);
        assertEquals(blockHeight.get(imposerPageConfig), ZERO);
        assertEquals(imposerPageConfig.getGabx(), expectedGabX, ZERO);
        assertEquals(gaby.get(imposerPageConfig), ZERO);
        assertEquals(x.get(imposerPageConfig), ZERO);
        assertEquals(y.get(imposerPageConfig), ZERO);
    }

    @Test
    public void testSetGabx() throws Exception {
        final float expectedGabX = 123f;
        imposerPageConfig = new ImposerPageConfig();
        imposerPageConfig.setGabx(expectedGabX);
        Field blockWidth = imposerPageConfig.getClass().getDeclaredField("blockWidth");
        blockWidth.setAccessible(true);
        Field blockHeight = imposerPageConfig.getClass().getDeclaredField("blockHeight");
        blockHeight.setAccessible(true);
        Field gabx = imposerPageConfig.getClass().getDeclaredField("gabx");
        gabx.setAccessible(true);
        Field gaby = imposerPageConfig.getClass().getDeclaredField("gaby");
        gaby.setAccessible(true);
        Field x = imposerPageConfig.getClass().getDeclaredField("x");
        x.setAccessible(true);
        Field y = imposerPageConfig.getClass().getDeclaredField("y");
        y.setAccessible(true);
        assertEquals(blockWidth.get(imposerPageConfig), ZERO);
        assertEquals(blockHeight.get(imposerPageConfig), ZERO);
        assertEquals(gabx.get(imposerPageConfig), expectedGabX);
        assertEquals(gaby.get(imposerPageConfig), ZERO);
        assertEquals(x.get(imposerPageConfig), ZERO);
        assertEquals(y.get(imposerPageConfig), ZERO);
    }

    @Test
    public void testGetGaby() throws Exception {
        final float expectedGabY = 123f;
        imposerPageConfig = new ImposerPageConfig(ZERO, ZERO, ZERO, expectedGabY, ZERO, ZERO);
        Field blockWidth = imposerPageConfig.getClass().getDeclaredField("blockWidth");
        blockWidth.setAccessible(true);
        Field blockHeight = imposerPageConfig.getClass().getDeclaredField("blockHeight");
        blockHeight.setAccessible(true);
        Field gabx = imposerPageConfig.getClass().getDeclaredField("gabx");
        gabx.setAccessible(true);
        Field gaby = imposerPageConfig.getClass().getDeclaredField("gaby");
        gaby.setAccessible(true);
        Field x = imposerPageConfig.getClass().getDeclaredField("x");
        x.setAccessible(true);
        Field y = imposerPageConfig.getClass().getDeclaredField("y");
        y.setAccessible(true);
        assertEquals(blockWidth.get(imposerPageConfig), ZERO);
        assertEquals(blockHeight.get(imposerPageConfig), ZERO);
        assertEquals(gabx.get(imposerPageConfig), ZERO);
        assertEquals(imposerPageConfig.getGaby(), expectedGabY, ZERO);
        assertEquals(x.get(imposerPageConfig), ZERO);
        assertEquals(y.get(imposerPageConfig), ZERO);
    }

    @Test
    public void testSetGaby() throws Exception {
        final float expectedGabY = 123f;
        imposerPageConfig = new ImposerPageConfig();
        imposerPageConfig.setGaby(expectedGabY);
        Field blockWidth = imposerPageConfig.getClass().getDeclaredField("blockWidth");
        blockWidth.setAccessible(true);
        Field blockHeight = imposerPageConfig.getClass().getDeclaredField("blockHeight");
        blockHeight.setAccessible(true);
        Field gabx = imposerPageConfig.getClass().getDeclaredField("gabx");
        gabx.setAccessible(true);
        Field gaby = imposerPageConfig.getClass().getDeclaredField("gaby");
        gaby.setAccessible(true);
        Field x = imposerPageConfig.getClass().getDeclaredField("x");
        x.setAccessible(true);
        Field y = imposerPageConfig.getClass().getDeclaredField("y");
        y.setAccessible(true);
        assertEquals(blockWidth.get(imposerPageConfig), ZERO);
        assertEquals(blockHeight.get(imposerPageConfig), ZERO);
        assertEquals(gabx.get(imposerPageConfig), ZERO);
        assertEquals(gaby.get(imposerPageConfig), expectedGabY);
        assertEquals(x.get(imposerPageConfig), ZERO);
        assertEquals(y.get(imposerPageConfig), ZERO);
    }

    @Test
    public void testGetX() throws Exception {
        final float expectedX = 123f;
        imposerPageConfig = new ImposerPageConfig(ZERO, ZERO, ZERO, ZERO, expectedX, ZERO);
        Field blockWidth = imposerPageConfig.getClass().getDeclaredField("blockWidth");
        blockWidth.setAccessible(true);
        Field blockHeight = imposerPageConfig.getClass().getDeclaredField("blockHeight");
        blockHeight.setAccessible(true);
        Field gabx = imposerPageConfig.getClass().getDeclaredField("gabx");
        gabx.setAccessible(true);
        Field gaby = imposerPageConfig.getClass().getDeclaredField("gaby");
        gaby.setAccessible(true);
        Field x = imposerPageConfig.getClass().getDeclaredField("x");
        x.setAccessible(true);
        Field y = imposerPageConfig.getClass().getDeclaredField("y");
        y.setAccessible(true);
        assertEquals(blockWidth.get(imposerPageConfig), ZERO);
        assertEquals(blockHeight.get(imposerPageConfig), ZERO);
        assertEquals(gabx.get(imposerPageConfig), ZERO);
        assertEquals(gaby.get(imposerPageConfig), ZERO);
        assertEquals(imposerPageConfig.getX(), expectedX, ZERO);
        assertEquals(y.get(imposerPageConfig), ZERO);
    }

    @Test
    public void testSetX() throws Exception {
        final float expectedX = 123f;
        imposerPageConfig = new ImposerPageConfig();
        imposerPageConfig.setX(expectedX);
        Field blockWidth = imposerPageConfig.getClass().getDeclaredField("blockWidth");
        blockWidth.setAccessible(true);
        Field blockHeight = imposerPageConfig.getClass().getDeclaredField("blockHeight");
        blockHeight.setAccessible(true);
        Field gabx = imposerPageConfig.getClass().getDeclaredField("gabx");
        gabx.setAccessible(true);
        Field gaby = imposerPageConfig.getClass().getDeclaredField("gaby");
        gaby.setAccessible(true);
        Field x = imposerPageConfig.getClass().getDeclaredField("x");
        x.setAccessible(true);
        Field y = imposerPageConfig.getClass().getDeclaredField("y");
        y.setAccessible(true);
        assertEquals(blockWidth.get(imposerPageConfig), ZERO);
        assertEquals(blockHeight.get(imposerPageConfig), ZERO);
        assertEquals(gabx.get(imposerPageConfig), ZERO);
        assertEquals(gaby.get(imposerPageConfig), ZERO);
        assertEquals(x.get(imposerPageConfig), expectedX);
        assertEquals(y.get(imposerPageConfig), ZERO);
    }

    @Test
    public void testGetY() throws Exception {
        final float expectedY = 123f;
        imposerPageConfig = new ImposerPageConfig(ZERO, ZERO, ZERO, ZERO, ZERO, expectedY);
        Field blockWidth = imposerPageConfig.getClass().getDeclaredField("blockWidth");
        blockWidth.setAccessible(true);
        Field blockHeight = imposerPageConfig.getClass().getDeclaredField("blockHeight");
        blockHeight.setAccessible(true);
        Field gabx = imposerPageConfig.getClass().getDeclaredField("gabx");
        gabx.setAccessible(true);
        Field gaby = imposerPageConfig.getClass().getDeclaredField("gaby");
        gaby.setAccessible(true);
        Field x = imposerPageConfig.getClass().getDeclaredField("x");
        x.setAccessible(true);
        Field y = imposerPageConfig.getClass().getDeclaredField("y");
        y.setAccessible(true);
        assertEquals(blockWidth.get(imposerPageConfig), ZERO);
        assertEquals(blockHeight.get(imposerPageConfig), ZERO);
        assertEquals(gabx.get(imposerPageConfig), ZERO);
        assertEquals(gaby.get(imposerPageConfig), ZERO);
        assertEquals(x.get(imposerPageConfig), ZERO);
        assertEquals(imposerPageConfig.getY(), expectedY, ZERO);
    }

    @Test
    public void testSetY() throws Exception {
        final float expectedY = 123f;
        imposerPageConfig = new ImposerPageConfig();
        imposerPageConfig.setY(expectedY);
        Field blockWidth = imposerPageConfig.getClass().getDeclaredField("blockWidth");
        blockWidth.setAccessible(true);
        Field blockHeight = imposerPageConfig.getClass().getDeclaredField("blockHeight");
        blockHeight.setAccessible(true);
        Field gabx = imposerPageConfig.getClass().getDeclaredField("gabx");
        gabx.setAccessible(true);
        Field gaby = imposerPageConfig.getClass().getDeclaredField("gaby");
        gaby.setAccessible(true);
        Field x = imposerPageConfig.getClass().getDeclaredField("x");
        x.setAccessible(true);
        Field y = imposerPageConfig.getClass().getDeclaredField("y");
        y.setAccessible(true);
        assertEquals(blockWidth.get(imposerPageConfig), ZERO);
        assertEquals(blockHeight.get(imposerPageConfig), ZERO);
        assertEquals(gabx.get(imposerPageConfig), ZERO);
        assertEquals(gaby.get(imposerPageConfig), ZERO);
        assertEquals(x.get(imposerPageConfig), ZERO);
        assertEquals(y.get(imposerPageConfig), expectedY);
    }
}