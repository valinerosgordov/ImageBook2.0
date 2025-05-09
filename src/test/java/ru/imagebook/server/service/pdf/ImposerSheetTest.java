package ru.imagebook.server.service.pdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import com.itextpdf.text.DocumentException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class ImposerSheetTest {
    private ImposerSheet imposerSheet = null;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testImposerSheet() throws Exception {
        imposerSheet = new ImposerSheet();
        Field pages = imposerSheet.getClass().getDeclaredField("pages");
        pages.setAccessible(true);
        assertNotNull(pages.get(imposerSheet));
    }

    @Test
    public void testImposerSheetImposerPageArray() throws Exception {
        imposerSheet = new ImposerSheet(new ImposerPage(), new ImposerPage());
        Field pages = imposerSheet.getClass().getDeclaredField("pages");
        pages.setAccessible(true);
        assertNotNull(pages.get(imposerSheet));
        assertEquals(imposerSheet.getPages().size(), 2);
    }

    @Test
    public void testImposeSheet() throws Exception {
        Document document = Mockito.mock(Document.class);
        PdfReader pdfReader = Mockito.mock(PdfReader.class);
        PdfWriter pdfWriter = Mockito.mock(PdfWriter.class);
        ImposerConfig imposerConfig = new ImposerConfig();
        imposerConfig.addFigure(new Line());
        ImposerPage imposerPage = Mockito.mock(ImposerPage.class);
        imposerSheet = new ImposerSheet(imposerPage);
        PdfContentByte contentByte = Mockito.mock(PdfContentByte.class);
        Mockito.when(pdfWriter.getDirectContent()).thenReturn(contentByte);
        assertNotNull(imposerSheet.imposeSheet(document, pdfWriter, pdfReader, imposerConfig));
        Mockito.verify(imposerPage).imposePage(pdfWriter, pdfReader);
        Mockito.verify(document).add(Mockito.any(Image.class));
        Mockito.verify(contentByte).setLineWidth(Mockito.anyFloat());
        Mockito.verify(contentByte).stroke();
    }

    @Test
    public void testAddPage() throws Exception {
        ImposerPage imposerPage = new ImposerPage();
        imposerSheet = new ImposerSheet();
        imposerSheet.addPage(imposerPage);
        Field pages = imposerSheet.getClass().getDeclaredField("pages");
        pages.setAccessible(true);
        assertNotNull(imposerSheet.getPages());
        assertEquals(imposerSheet.getPages().size(), 1);
        assertEquals(imposerSheet.getPages().get(0), imposerPage);
    }

    @Test
    public void testRemovePage() throws Exception {
        imposerSheet = new ImposerSheet(new ImposerPage(), new ImposerPage());
        Field pages = imposerSheet.getClass().getDeclaredField("pages");
        pages.setAccessible(true);
        assertNotNull(pages.get(imposerSheet));
        assertEquals(imposerSheet.getPages().size(), 2);
        imposerSheet.removePage(imposerSheet.getPages().get(0));
        assertEquals(imposerSheet.getPages().size(), 1);
    }

    @Test
    public void testGetPages() throws IOException, DocumentException {
        imposerSheet = new ImposerSheet();
        ArrayList<ImposerPage> pages = new ArrayList<ImposerPage>();
        imposerSheet.setPages(pages);
        assertSame(imposerSheet.getPages(), pages);
    }

    @Test
    public void testSetPages() throws Exception {
        imposerSheet = new ImposerSheet();
        ArrayList<ImposerPage> pages = new ArrayList<ImposerPage>();
        imposerSheet.setPages(pages);
        Field pagesField = imposerSheet.getClass().getDeclaredField("pages");
        pagesField.setAccessible(true);
        assertSame(pagesField.get(imposerSheet), pages);
    }

}
