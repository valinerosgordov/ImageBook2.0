package ru.imagebook.server.service.pdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.easymock.PowerMock;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class ImposerPageTest {
    private ImposerPage imposerPage = null;
    private final int ZERO = 0;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testImposerPage() throws Exception {
        imposerPage = new ImposerPage();
        Field rotationDegrees = imposerPage.getClass().getDeclaredField("rotationDegrees");
        rotationDegrees.setAccessible(true);
        Field pageNumber = imposerPage.getClass().getDeclaredField("pageNumber");
        pageNumber.setAccessible(true);
        Field pageConfig = imposerPage.getClass().getDeclaredField("pageConfig");
        pageConfig.setAccessible(true);
        assertEquals(rotationDegrees.get(imposerPage), ZERO);
        assertEquals(pageNumber.get(imposerPage), ZERO);
        assertNull(pageConfig.get(imposerPage));
    }

    @Test
    public void testImposerPageIntIntImposerPageConfig() throws Exception {
        final int expectedRotationDegrees = 123;
        final int expectedPageNumber = 234;
        final ImposerPageConfig imposerPageConfig = new ImposerPageConfig();
        imposerPage = new ImposerPage(expectedRotationDegrees, expectedPageNumber, imposerPageConfig);
        Field rotationDegrees = imposerPage.getClass().getDeclaredField("rotationDegrees");
        rotationDegrees.setAccessible(true);
        Field pageNumber = imposerPage.getClass().getDeclaredField("pageNumber");
        pageNumber.setAccessible(true);
        Field pageConfig = imposerPage.getClass().getDeclaredField("pageConfig");
        pageConfig.setAccessible(true);
        assertEquals(rotationDegrees.get(imposerPage), expectedRotationDegrees);
        assertEquals(pageNumber.get(imposerPage), expectedPageNumber);
        assertSame(pageConfig.get(imposerPage), imposerPageConfig);
    }

    @Test
    public void testImposePage() throws BadElementException {
        final int expectedRotationDegrees = 123;
        final int expectedPageNumber = 234;
        final ImposerPageConfig imposerPageConfig = new ImposerPageConfig();
        PdfReader pdfReader = Mockito.mock(PdfReader.class);
        PdfWriter pdfWriter = Mockito.mock(PdfWriter.class);
        PdfImportedPage importedPage = Mockito.mock(PdfImportedPage.class);
        Mockito.when(pdfWriter.getImportedPage(pdfReader, expectedPageNumber)).thenReturn(importedPage);
        imposerPage = new ImposerPage(expectedRotationDegrees, expectedPageNumber, imposerPageConfig);
        Image image = Mockito.mock(Image.class);
        PowerMock.stub(PowerMock.method(Image.class, "getInstance", PdfImportedPage.class)).toReturn(image);
        assertNotNull(imposerPage.imposePage(pdfWriter, pdfReader));
        Mockito.verify(pdfWriter).getImportedPage(pdfReader, expectedPageNumber);
        Mockito.verify(importedPage).setBoundingBox(Mockito.any(Rectangle.class));
    }

    @Test
    public void testGetRotationDegrees() throws Exception {
        final int expectedRotationDegrees = 123;
        final int expectedPageNumber = 234;
        final ImposerPageConfig imposerPageConfig = new ImposerPageConfig();
        imposerPage = new ImposerPage(expectedRotationDegrees, expectedPageNumber, imposerPageConfig);
        Field rotationDegrees = imposerPage.getClass().getDeclaredField("rotationDegrees");
        rotationDegrees.setAccessible(true);
        Field pageNumber = imposerPage.getClass().getDeclaredField("pageNumber");
        pageNumber.setAccessible(true);
        Field pageConfig = imposerPage.getClass().getDeclaredField("pageConfig");
        pageConfig.setAccessible(true);
        assertEquals(imposerPage.getRotationDegrees(), expectedRotationDegrees);
        assertEquals(pageNumber.get(imposerPage), expectedPageNumber);
        assertSame(pageConfig.get(imposerPage), imposerPageConfig);
    }

    @Test
    public void testSetRotationDegrees() throws Exception {
        final int expectedRotationDegrees = 123;
        imposerPage = new ImposerPage();
        imposerPage.setRotationDegrees(expectedRotationDegrees);
        Field rotationDegrees = imposerPage.getClass().getDeclaredField("rotationDegrees");
        rotationDegrees.setAccessible(true);
        Field pageNumber = imposerPage.getClass().getDeclaredField("pageNumber");
        pageNumber.setAccessible(true);
        Field pageConfig = imposerPage.getClass().getDeclaredField("pageConfig");
        pageConfig.setAccessible(true);
        assertEquals(rotationDegrees.get(imposerPage), expectedRotationDegrees);
        assertEquals(pageNumber.get(imposerPage), ZERO);
        assertNull(pageConfig.get(imposerPage));
    }

    @Test
    public void testGetPageConfig() throws Exception {
        final int expectedRotationDegrees = 123;
        final int expectedPageNumber = 234;
        final ImposerPageConfig imposerPageConfig = new ImposerPageConfig();
        imposerPage = new ImposerPage(expectedRotationDegrees, expectedPageNumber, imposerPageConfig);
        Field rotationDegrees = imposerPage.getClass().getDeclaredField("rotationDegrees");
        rotationDegrees.setAccessible(true);
        Field pageNumber = imposerPage.getClass().getDeclaredField("pageNumber");
        pageNumber.setAccessible(true);
        Field pageConfig = imposerPage.getClass().getDeclaredField("pageConfig");
        pageConfig.setAccessible(true);
        assertEquals(rotationDegrees.get(imposerPage), expectedRotationDegrees);
        assertEquals(pageNumber.get(imposerPage), expectedPageNumber);
        assertSame(imposerPage.getPageConfig(), imposerPageConfig);
    }

    @Test
    public void testSetPageConfig() throws Exception {
        final ImposerPageConfig imposerPageConfig = new ImposerPageConfig();
        imposerPage = new ImposerPage();
        imposerPage.setPageConfig(imposerPageConfig);
        Field rotationDegrees = imposerPage.getClass().getDeclaredField("rotationDegrees");
        rotationDegrees.setAccessible(true);
        Field pageNumber = imposerPage.getClass().getDeclaredField("pageNumber");
        pageNumber.setAccessible(true);
        Field pageConfig = imposerPage.getClass().getDeclaredField("pageConfig");
        pageConfig.setAccessible(true);
        assertEquals(rotationDegrees.get(imposerPage), ZERO);
        assertEquals(pageNumber.get(imposerPage), ZERO);
        assertSame(pageConfig.get(imposerPage), imposerPageConfig);
    }

    @Test
    public void testGetPageNumber() throws Exception {
        final int expectedRotationDegrees = 123;
        final int expectedPageNumber = 234;
        final ImposerPageConfig imposerPageConfig = new ImposerPageConfig();
        imposerPage = new ImposerPage(expectedRotationDegrees, expectedPageNumber, imposerPageConfig);
        Field rotationDegrees = imposerPage.getClass().getDeclaredField("rotationDegrees");
        rotationDegrees.setAccessible(true);
        Field pageNumber = imposerPage.getClass().getDeclaredField("pageNumber");
        pageNumber.setAccessible(true);
        Field pageConfig = imposerPage.getClass().getDeclaredField("pageConfig");
        pageConfig.setAccessible(true);
        assertEquals(rotationDegrees.get(imposerPage), expectedRotationDegrees);
        assertEquals(imposerPage.getPageNumber(), expectedPageNumber);
        assertSame(pageConfig.get(imposerPage), imposerPageConfig);
    }

    @Test
    public void testSetPageNumber() throws Exception {
        final int expectedPageNumber = 234;
        imposerPage = new ImposerPage();
        imposerPage.setPageNumber(expectedPageNumber);
        Field rotationDegrees = imposerPage.getClass().getDeclaredField("rotationDegrees");
        rotationDegrees.setAccessible(true);
        Field pageNumber = imposerPage.getClass().getDeclaredField("pageNumber");
        pageNumber.setAccessible(true);
        Field pageConfig = imposerPage.getClass().getDeclaredField("pageConfig");
        pageConfig.setAccessible(true);
        assertEquals(rotationDegrees.get(imposerPage), ZERO);
        assertEquals(pageNumber.get(imposerPage), expectedPageNumber);
        assertNull(pageConfig.get(imposerPage));
    }
}