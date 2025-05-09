package ru.imagebook.server.service.pdf;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class ImposerConfigTest {
    private ImposerConfig imposerConfig = null;
    private final float ZERO = 0f;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testImposerConfig() throws Exception {
        imposerConfig = new ImposerConfig();
        Field lineWidth = imposerConfig.getClass().getDeclaredField("lineWidth");
        lineWidth.setAccessible(true);
        Field figures = imposerConfig.getClass().getDeclaredField("figures");
        figures.setAccessible(true);
        assertEquals(lineWidth.get(imposerConfig), ZERO);
        assertNotNull(figures.get(imposerConfig));
    }

    @Test
    public void testImposerConfigFloat() throws Exception {
        final float expectedLineWidth = 123f;
        imposerConfig = new ImposerConfig(expectedLineWidth, 1, 9);
        Field lineWidth = imposerConfig.getClass().getDeclaredField("lineWidth");
        lineWidth.setAccessible(true);
        Field figures = imposerConfig.getClass().getDeclaredField("figures");
        figures.setAccessible(true);
        assertEquals(lineWidth.get(imposerConfig), expectedLineWidth);
        assertNotNull(figures.get(imposerConfig));
    }

    @Test
    public void testGetLineWidth() throws Exception {
        final float expectedLineWidth = 123f;
        imposerConfig = new ImposerConfig(expectedLineWidth, 2, 10);
        Field figures = imposerConfig.getClass().getDeclaredField("figures");
        figures.setAccessible(true);
        assertEquals(imposerConfig.getLineWidth(), expectedLineWidth, ZERO);
        assertNotNull(figures.get(imposerConfig));
    }

    @Test
    public void testSetLineWidth() throws Exception {
        final float expectedLineWidth = 123f;
        imposerConfig = new ImposerConfig();
        imposerConfig.setLineWidth(expectedLineWidth);
        Field figures = imposerConfig.getClass().getDeclaredField("figures");
        figures.setAccessible(true);
        Field lineWidth = imposerConfig.getClass().getDeclaredField("lineWidth");
        lineWidth.setAccessible(true);
        assertEquals(imposerConfig.getLineWidth(), expectedLineWidth, ZERO);
        assertNotNull(lineWidth.get(imposerConfig));
    }

    @Test
    public void testGetLines() throws Exception {
        final ArrayList<IFigure> expectedFigures = new ArrayList<IFigure>();
        imposerConfig = new ImposerConfig();
        imposerConfig.setLines(expectedFigures);
        Field figures = imposerConfig.getClass().getDeclaredField("figures");
        figures.setAccessible(true);
        Field lineWidth = imposerConfig.getClass().getDeclaredField("lineWidth");
        lineWidth.setAccessible(true);
        assertSame(imposerConfig.getLines(), expectedFigures);
        assertEquals(lineWidth.get(imposerConfig), ZERO);
    }

    @Test
    public void testSetLines() throws Exception {
        final ArrayList<IFigure> expectedFigures = new ArrayList<IFigure>();
        imposerConfig = new ImposerConfig();
        imposerConfig.setLines(expectedFigures);
        Field figures = imposerConfig.getClass().getDeclaredField("figures");
        figures.setAccessible(true);
        Field lineWidth = imposerConfig.getClass().getDeclaredField("lineWidth");
        lineWidth.setAccessible(true);
        assertSame(figures.get(imposerConfig), expectedFigures);
        assertEquals(lineWidth.get(imposerConfig), ZERO);
    }

    @Test
    public void testAddFigure() throws Exception {
        imposerConfig = new ImposerConfig();
        Field figures = imposerConfig.getClass().getDeclaredField("figures");
        figures.setAccessible(true);
        Field lineWidth = imposerConfig.getClass().getDeclaredField("lineWidth");
        lineWidth.setAccessible(true);
        assertEquals(imposerConfig.getLineWidth(), ZERO, ZERO);
        assertNotNull(lineWidth.get(imposerConfig));
        imposerConfig.addFigure(new Circle());
        imposerConfig.addFigure(new Circle());
        assertEquals(imposerConfig.getLines().size(), 2);
    }

    @Test
    public void testRemoveLine() throws Exception {
        imposerConfig = new ImposerConfig();
        Field figures = imposerConfig.getClass().getDeclaredField("figures");
        figures.setAccessible(true);
        Field lineWidth = imposerConfig.getClass().getDeclaredField("lineWidth");
        lineWidth.setAccessible(true);
        assertEquals(imposerConfig.getLineWidth(), ZERO, ZERO);
        assertNotNull(lineWidth.get(imposerConfig));
        imposerConfig.addFigure(new Circle());
        imposerConfig.addFigure(new Circle());
        assertEquals(imposerConfig.getLines().size(), 2);
        imposerConfig.removeLine(imposerConfig.getLines().get(0));
        assertEquals(imposerConfig.getLines().size(), 1);
    }
}