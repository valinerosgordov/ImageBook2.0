package ru.imagebook.server.service.pdf;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.itextpdf.text.pdf.PdfContentByte;

public class CircleTest {
    private Circle circle = null;
    private float ZERO = 0;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testCircle() throws Exception {
        circle = new Circle();
        Field fieldX = circle.getClass().getDeclaredField("x");
        fieldX.setAccessible(true);
        Field fieldY = circle.getClass().getDeclaredField("y");
        fieldY.setAccessible(true);
        Field fieldR = circle.getClass().getDeclaredField("r");
        fieldR.setAccessible(true);
        assertEquals(fieldX.get(circle), ZERO);
        assertEquals(fieldY.get(circle), ZERO);
        assertEquals(fieldR.get(circle), ZERO);
    }

    @Test
    public void testCircleFloatFloatFloat() throws Exception {
        final float expectedX = 123f, expectedY = 234f, expectedR = 345f;
        circle = new Circle(expectedX, expectedY, expectedR);
        Field fieldX = circle.getClass().getDeclaredField("x");
        fieldX.setAccessible(true);
        Field fieldY = circle.getClass().getDeclaredField("y");
        fieldY.setAccessible(true);
        Field fieldR = circle.getClass().getDeclaredField("r");
        fieldR.setAccessible(true);
        assertEquals(fieldX.get(circle), expectedX);
        assertEquals(fieldY.get(circle), expectedY);
        assertEquals(fieldR.get(circle), expectedR);
    }

    @Test
    public void testDraw() {
        final float expectedX = 123f, expectedY = 234f, expectedR = 345f;
        circle = new Circle(expectedX, expectedY, expectedR);
        PdfContentByte contentByte = Mockito.mock(PdfContentByte.class);
        circle.draw(contentByte);
        Mockito.verify(contentByte).circle(expectedX, expectedY, expectedR);
    }

    @Test
    public void testGetX() throws Exception {
        final float expectedX = 123f;
        circle = new Circle(expectedX, ZERO, ZERO);
        Field fieldX = circle.getClass().getDeclaredField("x");
        fieldX.setAccessible(true);
        Field fieldY = circle.getClass().getDeclaredField("y");
        fieldY.setAccessible(true);
        Field fieldR = circle.getClass().getDeclaredField("r");
        fieldR.setAccessible(true);
        assertEquals(fieldX.get(circle), expectedX);
        assertEquals(fieldY.get(circle), ZERO);
        assertEquals(fieldR.get(circle), ZERO);
    }

    @Test
    public void testSetX() throws Exception {
        final float expectedX = 123f;
        circle = new Circle();
        circle.setX(expectedX);
        Field fieldX = circle.getClass().getDeclaredField("x");
        fieldX.setAccessible(true);
        Field fieldY = circle.getClass().getDeclaredField("y");
        fieldY.setAccessible(true);
        Field fieldR = circle.getClass().getDeclaredField("r");
        fieldR.setAccessible(true);
        assertEquals(fieldX.get(circle), expectedX);
        assertEquals(fieldY.get(circle), ZERO);
        assertEquals(fieldR.get(circle), ZERO);
    }

    @Test
    public void testGetY() throws Exception {
        final float expectedY = 123f;
        circle = new Circle(ZERO, expectedY, ZERO);
        Field fieldX = circle.getClass().getDeclaredField("x");
        fieldX.setAccessible(true);
        Field fieldY = circle.getClass().getDeclaredField("y");
        fieldY.setAccessible(true);
        Field fieldR = circle.getClass().getDeclaredField("r");
        fieldR.setAccessible(true);
        assertEquals(fieldX.get(circle), ZERO);
        assertEquals(fieldY.get(circle), expectedY);
        assertEquals(fieldR.get(circle), ZERO);
    }

    @Test
    public void testSetY() throws Exception {
        final float expectedY = 123f;
        circle = new Circle();
        circle.setY(expectedY);
        Field fieldX = circle.getClass().getDeclaredField("x");
        fieldX.setAccessible(true);
        Field fieldY = circle.getClass().getDeclaredField("y");
        fieldY.setAccessible(true);
        Field fieldR = circle.getClass().getDeclaredField("r");
        fieldR.setAccessible(true);
        assertEquals(fieldX.get(circle), ZERO);
        assertEquals(fieldY.get(circle), expectedY);
        assertEquals(fieldR.get(circle), ZERO);
    }

    @Test
    public void testGetR() throws Exception {
        final float expectedR = 123f;
        circle = new Circle(ZERO, ZERO, expectedR);
        Field fieldX = circle.getClass().getDeclaredField("x");
        fieldX.setAccessible(true);
        Field fieldY = circle.getClass().getDeclaredField("y");
        fieldY.setAccessible(true);
        Field fieldR = circle.getClass().getDeclaredField("r");
        fieldR.setAccessible(true);
        assertEquals(fieldX.get(circle), ZERO);
        assertEquals(fieldY.get(circle), ZERO);
        assertEquals(fieldR.get(circle), expectedR);
    }

    @Test
    public void testSetR() throws Exception {
        final float expectedR = 123f;
        circle = new Circle();
        circle.setR(expectedR);
        Field fieldX = circle.getClass().getDeclaredField("x");
        fieldX.setAccessible(true);
        Field fieldY = circle.getClass().getDeclaredField("y");
        fieldY.setAccessible(true);
        Field fieldR = circle.getClass().getDeclaredField("r");
        fieldR.setAccessible(true);
        assertEquals(fieldX.get(circle), ZERO);
        assertEquals(fieldY.get(circle), ZERO);
        assertEquals(fieldR.get(circle), expectedR);
    }
}