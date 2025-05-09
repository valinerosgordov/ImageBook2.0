package ru.imagebook.server.service.pdf;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import ru.imagebook.shared.model.Order;

public class PdfUtilTest {
    private static final String ORDER_NUMBER = "123";
    private Order<?> order = null;
    private PdfUtil pdfUtil = null;
    private PdfConfig pdfConfig = null;

    @Before
    public void setUp() throws Exception {
        pdfConfig = new PdfConfig();
        pdfUtil = new PdfUtil(pdfConfig);
        order = EasyMock.createMock(Order.class);
        EasyMock.expect(order.getNumber()).andReturn(ORDER_NUMBER);
        EasyMock.replay(order);
    }

    @Test
    public void testGetImposedPdfFileName() {
        String actual = ORDER_NUMBER + ".pdf";
        String expected = pdfUtil.getImposedPdfFileName(order);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPdfFileName() {
        String actual = ORDER_NUMBER + "_nonimposed.pdf";
        String expected = pdfUtil.getNonimposedPdfFileName(order);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetImposedPdfPath() {
        String path = "/test";
        pdfConfig.setPdfPath(path);
        String actual = path + "/" + ORDER_NUMBER + ".pdf";
        String expected = pdfUtil.getImposedPdfPath(order);
        assertEquals(expected, actual);
    }
}