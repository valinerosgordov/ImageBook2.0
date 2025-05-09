package ru.imagebook.server.service.pdf;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.easymock.PowerMock;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.ProductType;

public class ImposerTest {
    private Imposer imposer = null;
    private Logger logger = Logger.getLogger(getClass());

    @Test
    public void testImpose() {
        final String imposedPdfPath = "imposedPdfPath";
        final String pdfPath = "pdfPath";
        Product product = Mockito.mock(Product.class);
        Order<?> order = PowerMock.createMock(Order.class);
        Order<?> order2 = PowerMock.createMock(Order.class);
        order.getProduct();
        PowerMock.expectLastCall().andReturn(product).anyTimes();
        PowerMock.replay(order);
        PdfUtil pdfUtil = Mockito.mock(PdfUtil.class);
        Mockito.when(pdfUtil.getImposedPdfPath(order)).thenReturn(imposedPdfPath);
        Mockito.when(pdfUtil.getNonimposedPdfPath(order)).thenReturn(pdfPath);
        PowerMock.stub(PowerMock.method(File.class, "renameTo", File.class));
        imposer = new Imposer(order, order2, logger, pdfUtil) {
            @Override
            public void impose010108(String nonImposedPath, String path) {
            }

            @Override
            public void impose010208(String nonImposedPath, String path) {
            }

            @Override
            public void impose010999(String nonImposedPath, String path) {
            }

            @Override
            public void impose010803(String nonImposedPath, String path) {
            }

            @Override
            public void impose010805(String nonImposedPath, String path) {
            }

            @Override
            public void impose010807(String nonImposedPath, String path) {
            }

            @Override
            public void impose010802(String nonImposedPath, String path) {
            }

            @Override
            public void impose010703(String nonImposedPath, String path) {
            }

            @Override
            public void impose010704(String nonImposedPath, String path) {
            }

            @Override
            public void impose010705(String nonImposedPath, String path) {
            }

            @Override
            public void impose010706(String nonImposedPath, String path) {
            }

            @Override
            public void impose010707(String nonImposedPath, String path) {
            }

            @Override
            public void impose010708(String nonImposedPath, String path) {
            }

            @Override
            public void impose010709(String nonImposedPath, String path) {
            }

            @Override
            public void impose010710(String nonImposedPath, String path) {
            }

            @Override
            public void impose010403(String nonImposedPath, String path) {
            }

            @Override
            public void impose010404(String nonImposedPath, String path) {
            }

            @Override
            public void impose010406(String nonImposedPath, String path) {
            }

            @Override
            public void impose010408(String nonImposedPath, String path) {
            }

            @Override
            public void impose010409(String nonImposedPath, String path) {
            }

            @Override
            public void impose010503(String nonImposedPath, String path) {
            }

            @Override
            public void impose010504(String nonImposedPath, String path) {
            }

            @Override
            public void impose010506(String nonImposedPath, String path) {
            }

            @Override
            public void impose010508(String nonImposedPath, String path) {
            }

            @Override
            public void impose010509(String nonImposedPath, String path) {
            }

            @Override
            public void impose010510(String nonImposedPath, String path) {
            }
        };
        //
        Mockito.when(product.getType()).thenReturn(ProductType.EVERFLAT_WHITE_MARGINS);
        Mockito.when(product.getNumber()).thenReturn(8);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.EVERFLAT_FULL_PRINT);
        Mockito.when(product.getNumber()).thenReturn(8);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.TRIAL);
        Mockito.when(product.getNumber()).thenReturn(1);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.CONGRATULATORY);
        Mockito.when(product.getNumber()).thenReturn(1);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.HARD_COVER_FULL_PRINT);
        Mockito.when(product.getNumber()).thenReturn(3);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.HARD_COVER_FULL_PRINT);
        Mockito.when(product.getNumber()).thenReturn(4);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.HARD_COVER_FULL_PRINT);
        Mockito.when(product.getNumber()).thenReturn(6);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.HARD_COVER_FULL_PRINT);
        Mockito.when(product.getNumber()).thenReturn(8);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.HARD_COVER_FULL_PRINT);
        Mockito.when(product.getNumber()).thenReturn(9);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.HARD_COVER_FULL_PRINT);
        Mockito.when(product.getNumber()).thenReturn(10);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.HARD_COVER_WHITE_MARGINS);
        Mockito.when(product.getNumber()).thenReturn(3);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.HARD_COVER_WHITE_MARGINS);
        Mockito.when(product.getNumber()).thenReturn(4);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.HARD_COVER_WHITE_MARGINS);
        Mockito.when(product.getNumber()).thenReturn(6);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.HARD_COVER_WHITE_MARGINS);
        Mockito.when(product.getNumber()).thenReturn(9);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.HARD_COVER_WHITE_MARGINS);
        Mockito.when(product.getNumber()).thenReturn(8);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.CLIP);
        Mockito.when(product.getNumber()).thenReturn(1);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.CLIP);
        Mockito.when(product.getNumber()).thenReturn(2);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.CLIP);
        Mockito.when(product.getNumber()).thenReturn(3);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.CLIP);
        Mockito.when(product.getNumber()).thenReturn(5);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.CLIP);
        Mockito.when(product.getNumber()).thenReturn(7);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.SPRING);
        Mockito.when(product.getNumber()).thenReturn(3);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.SPRING);
        Mockito.when(product.getNumber()).thenReturn(4);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.SPRING);
        Mockito.when(product.getNumber()).thenReturn(5);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.SPRING);
        Mockito.when(product.getNumber()).thenReturn(6);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.SPRING);
        Mockito.when(product.getNumber()).thenReturn(7);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.SPRING);
        Mockito.when(product.getNumber()).thenReturn(8);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.SPRING);
        Mockito.when(product.getNumber()).thenReturn(9);
        imposer.impose();
        //
        Mockito.when(product.getType()).thenReturn(ProductType.SPRING);
        Mockito.when(product.getNumber()).thenReturn(10);
        imposer.impose();
    }

    @Test
    public void testImpose010108() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose010108(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose010208() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose010208(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose010999() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose010999(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose010802() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose010802(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose010803() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose010803(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose010805() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose010805(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose010807() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose010807(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose010703() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose010703(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose010704() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose010704(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose010705() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose010705(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose010706() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose010706(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose010707() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose010707(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose010403() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose010403(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose010404() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose010404(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose010406() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose010406(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose010408() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose010408(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose010409() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose010409(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose010503() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose010503(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose010504() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose010504(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose010506() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose010506(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose010508() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose010508(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose010509() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose010509(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose010510() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose010510(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose010708() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose010708(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose010709() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose010709(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose010710() throws IOException { // TODO: 
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose010710(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose011106() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose011106(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImpose011107() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.impose011107(nonImposedPath, path);
            }
        });
    }

    @Test
    public void testImposeDefault() throws IOException {
        decorateTestImpose(new ImposeTester() {
            @Override
            public void impose(String nonImposedPath, String path) {
                imposer.imposeDefault(nonImposedPath, path);
            }
        });
    }

    private void decorateTestImpose(ImposeTester tester) throws IOException {
        final int pageCount = 8;
        final String orderNumber = "123";
        final File path = new File("src/test/resources/ru/imagebook/server/service/pdf/testOutNonImposedPath.pdf");
        final File nonImposedPath = new File("src/test/resources/ru/imagebook/server/service/pdf/testNonImposedPath.pdf");
        try {
            if (!nonImposedPath.exists()) {
                fail("test nonimposed pdf file '" + nonImposedPath.getName() + "' not found");
            }
            // Clear out file
            path.delete();
            path.createNewFile();

            Order<?> order = PowerMock.createMock(Order.class);
            Order<?> order2 = PowerMock.createMock(Order.class);
            order.getNumber();
            PowerMock.expectLastCall().andReturn(orderNumber).anyTimes();
            order.getPageCount();
            PowerMock.expectLastCall().andReturn(pageCount).anyTimes();
            PowerMock.replay(order);
            PdfUtil pdfUtil = Mockito.mock(PdfUtil.class);
            imposer = new Imposer(order, order2, logger, pdfUtil);
            // Call tested method
            tester.impose(nonImposedPath.getAbsolutePath(), path.getAbsolutePath());

            assertTrue(path.exists());
            assertTrue(path.length() != 0);
        }
        finally {
            path.delete();
        }
    }

    private interface ImposeTester {
        void impose(String nonImposedPath, String path);
    }
}
