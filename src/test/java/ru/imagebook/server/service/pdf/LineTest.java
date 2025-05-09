package ru.imagebook.server.service.pdf;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.itextpdf.text.pdf.PdfContentByte;

public class LineTest {
    private Line line = null;
    private final float ZERO = 0f;

    @Before
    public void setUp() throws Exception {
    }

    private static Object getValue(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
        return null;
    }

    @Test
    public void testLine() {
        line = new Line();
        assertEquals(getValue(line, "startx"), ZERO);
        assertEquals(getValue(line, "starty"), ZERO);
        assertEquals(getValue(line, "endx"), ZERO);
        assertEquals(getValue(line, "endy"), ZERO);
    }

    @Test
    public void testLineFloatFloatFloatFloat() {
        final float startx = 123f, starty = 234f, endx = 345f, endy = 456f;
        line = new Line(startx, starty, endx, endy);
        assertEquals(getValue(line, "startx"), startx);
        assertEquals(getValue(line, "starty"), starty);
        assertEquals(getValue(line, "endx"), endx);
        assertEquals(getValue(line, "endy"), endy);
    }

    @Test
    public void testDraw() {
        final float startx = 123f, starty = 234f, endx = 345f, endy = 456f;
        line = new Line(startx, starty, endx, endy);
        PdfContentByte contentByte = Mockito.mock(PdfContentByte.class);
        line.draw(contentByte);
        Mockito.verify(contentByte).moveTo(startx, starty);
        Mockito.verify(contentByte).lineTo(endx, endy);
    }

    @Test
    public void testGetStartx() {
        final float startx = 123f, starty = 234f, endx = 345f, endy = 456f;
        line = new Line(startx, starty, endx, endy);
        assertEquals(line.getStartx(), startx, ZERO);
        assertEquals(getValue(line, "starty"), starty);
        assertEquals(getValue(line, "endx"), endx);
        assertEquals(getValue(line, "endy"), endy);
    }

    @Test
    public void testSetStartx() {
        final float startx = 123f;
        line = new Line();
        line.setStartx(startx);
        assertEquals(getValue(line, "startx"), startx);
        assertEquals(getValue(line, "starty"), ZERO);
        assertEquals(getValue(line, "endx"), ZERO);
        assertEquals(getValue(line, "endy"), ZERO);
    }

    @Test
    public void testGetStarty() {
        final float startx = 123f, starty = 234f, endx = 345f, endy = 456f;
        line = new Line(startx, starty, endx, endy);
        assertEquals(getValue(line, "startx"), startx);
        assertEquals(line.getStarty(), starty, ZERO);
        assertEquals(getValue(line, "endx"), endx);
        assertEquals(getValue(line, "endy"), endy);
    }

    @Test
    public void testSetStarty() {
        final float starty = 234f;
        line = new Line();
        line.setStarty(starty);
        assertEquals(getValue(line, "startx"), ZERO);
        assertEquals(getValue(line, "starty"), starty);
        assertEquals(getValue(line, "endx"), ZERO);
        assertEquals(getValue(line, "endy"), ZERO);
    }

    @Test
    public void testGetEndx() {
        final float startx = 123f, starty = 234f, endx = 345f, endy = 456f;
        line = new Line(startx, starty, endx, endy);
        assertEquals(getValue(line, "startx"), startx);
        assertEquals(getValue(line, "starty"), starty);
        assertEquals(line.getEndx(), endx, ZERO);
        assertEquals(getValue(line, "endy"), endy);
    }

    @Test
    public void testSetEndx() {
        final float endx = 345f;
        line = new Line();
        line.setEndx(endx);
        assertEquals(getValue(line, "startx"), ZERO);
        assertEquals(getValue(line, "starty"), ZERO);
        assertEquals(getValue(line, "endx"), endx);
        assertEquals(getValue(line, "endy"), ZERO);
    }

    @Test
    public void testGetEndy() {
        final float startx = 123f, starty = 234f, endx = 345f, endy = 456f;
        line = new Line(startx, starty, endx, endy);
        assertEquals(getValue(line, "startx"), startx);
        assertEquals(getValue(line, "starty"), starty);
        assertEquals(getValue(line, "endx"), endx);
        assertEquals(line.getEndy(), endy, ZERO);
    }

    @Test
    public void testSetEndy() {
        final float endy = 456f;
        line = new Line();
        line.setEndy(endy);
        assertEquals(getValue(line, "startx"), ZERO);
        assertEquals(getValue(line, "starty"), ZERO);
        assertEquals(getValue(line, "endx"), ZERO);
        assertEquals(getValue(line, "endy"), endy);
    }
}