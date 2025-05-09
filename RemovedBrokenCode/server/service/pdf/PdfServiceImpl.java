package ru.imagebook.server.service.pdf;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Collections2;
import com.google.common.collect.ListMultimap;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import ru.imagebook.server.repository.PdfRepository;
import ru.imagebook.server.service.HeavyExecutorService;
import ru.imagebook.server.service.flash.FlashConfig;
import ru.imagebook.shared.model.*;
import ru.imagebook.shared.model.editor.Layout;
import ru.imagebook.shared.model.editor.Page;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.client.i18n.lang.ImplodeFunction;
import ru.minogin.core.client.i18n.lang.PrefixFunction;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.client.lang.template.Compiler;
import ru.minogin.core.server.ServiceLogger;
import ru.minogin.core.server.ftp.XFtpClient;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.List;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static ru.imagebook.server.service.pdf.PdfConst.MM_TO_PX;
import static ru.imagebook.shared.model.ProductType.HARD_COVER_FULL_PRINT;
import static ru.imagebook.shared.model.ProductType.TABLET;
import static ru.imagebook.shared.util.Functions.ENTITY_TO_ID_FUNCTION;

public class PdfServiceImpl implements PdfService {
    private static Logger LOGGER = Logger.getLogger(PdfServiceImpl.class);

    private static final int DELAY_SEC = 60;
    private final static String FONT_LOCATION = "/ru/imagebook/server/service/pdf/arial.ttf";
    private static final float INCH_TO_MM = 25.4f;
    private static final float MPHOTO_DPI = 300f;
    private static final float PX_TO_MM = INCH_TO_MM / MPHOTO_DPI;
    private final static float FONT_SIZE = 8;
    private final static float ADDRESS_FONT_SIZE = 11;

    private final static float COVER_WHITE_LINE_WIDTH = 5;    // в милиметрах
    private final static float COVER_WHITE_LINE_HEIGHT = 120;    // в милиметрах
    private final static float COVER_WHITE_LINE_MARGIN_RIGHT = 5;// отступ справа

    @Autowired
    private PdfTxService service;

    @Autowired
    private HeavyExecutorService executorService;

    @Autowired
    private FlashConfig flashConfig;

    @Autowired
    private CoreFactory coreFactory;

    @Autowired
    private MessageSource messages;

    @Autowired
    private PdfRepository repository;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final PdfConfig config;
    private final PdfUtil util;

    @Autowired
    public PdfServiceImpl(PdfConfig config) {
        this.config = config;
        this.util = new PdfUtil(config);
    }

    @Override
    public void startAsync() {
        scheduler.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                LOGGER.debug("PDF generation: starting");

                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            generate();
                        }
                        catch (Throwable t) {
                            LOGGER.error("PDF generation: failed", t);
                        }
                    }
                });

                LOGGER.debug("PDF generation: started");
            }
        }, 0, DELAY_SEC, TimeUnit.SECONDS);
    }

    private void generate() {
        List<Order<?>> ordersToProcess = service.loadPaidOrders();
        if (ordersToProcess.isEmpty()) {
            LOGGER.debug("PDF Generation: no orders to process");
            return;
        }

        // grouping orders by bill
        ListMultimap<Bill, Order<?>> billToOrders = ArrayListMultimap.create();
        for (Order<?> order : ordersToProcess) {
            billToOrders.put(order.getBill(), order);
        }
        LOGGER.debug("Found " + billToOrders.size() + " orders to generate pdf");

        for (Map.Entry<Bill, Collection<Order<?>>> entry : billToOrders.asMap().entrySet()) {
            final Bill bill = entry.getKey();
            final Collection<Order<?>> orders = entry.getValue();
            LOGGER.debug("Submitting PDF generation, bill: " + bill.getId());

            try {
                updateOrdersState(orders, OrderState.PDF_GENERATION);

                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            doGeneratePdf(orders);
                        }
                        catch (Throwable t) {
                            LOGGER.error("Failed to generate pdf for orders of bill: " + bill.getId(), t);
                            updateOrdersState(orders, OrderState.PAID);
                        }
                        finally {
                            LOGGER.debug("PDF generation finished, bill: " + bill.getId());
                        }
                    }
                });
            }
            catch (Exception e) {
                LOGGER.error("Submit failed for bill: " + bill.getId(), e);
                updateOrdersState(orders, OrderState.PAID);
            }
        }
    }

    private void updateOrdersState(Collection<Order<?>> orders, int orderState) {
        Collection<Integer> orderIds = Collections2.transform(orders, ENTITY_TO_ID_FUNCTION);
        service.updateOrdersState(orderIds, orderState);
    }

    private void doGeneratePdf(Collection<Order<?>> orders) {
        final Map<MultiConditionKey, List<Order<?>>> ordersByCondition = new HashMap<>();
        final List<Order<?>> ordersNotIncluded = new ArrayList<>();
        final Map<String, Integer> packageOrdersCoverQuantity = new HashMap<>();

        for (final Order<?> order : orders) {
            if (order.getProduct().getMultiplicity() == 4) {
                MultiConditionKey key = new MultiConditionKey(
                        order.getProduct().getType(),
                        order.getProduct().getNumber(),
                        order.getPageLamination(),
                        order.getCoverLamination(),
                        order.getPageCount()
                );

                if (ordersByCondition.isEmpty()) {
                    List<Order<?>> _orders = new ArrayList<>();
                    _orders.add(order);
                    ordersByCondition.put(key, _orders);
                } else if (ordersByCondition.containsKey(key)) {
                    ordersByCondition.get(key).add(order);
                } else {
                    List<Order<?>> _orders = new ArrayList<>();
                    _orders.add(order);
                    ordersByCondition.put(key, _orders);
                }
            } else {
                ordersNotIncluded.add(order);
            }

            if (order.isPackaged()) {
                computeCoverQuantityForPackageOrders(order, packageOrdersCoverQuantity);
            }
        }

        if (!ordersByCondition.isEmpty()) {
            for (final Map.Entry<MultiConditionKey, List<Order<?>>> entry : ordersByCondition.entrySet()) {
                final List<Order<?>> _orders = entry.getValue();
                if (_orders.size() >= 10) {
                    doGenerateGroupOrders(_orders, packageOrdersCoverQuantity);
                } else {
                    int quantity = 0;
                    for (Order<?> order : _orders) {
                        quantity += order.getQuantity();
                    }
                    if (quantity >= 10) {
                        doGenerateGroupOrders(_orders, packageOrdersCoverQuantity);
                    } else {
                        processingOrders(_orders, packageOrdersCoverQuantity);
                    }
                }
            }
        }

        if (!ordersNotIncluded.isEmpty()) {
            processingOrders(ordersNotIncluded, packageOrdersCoverQuantity);
        }
    }

    /**
     * Compute total cover quantity for packages orders (only if cover is common)
     *
     * @param order                      - current order
     * @param packageOrdersCoverQuantity - Map [ Package number, Total cover quantity for package ]
     * @see <a href="http://jira.minogin.ru/browse/IMAGEBOOK-411">IMAGEBOOK-411</a>
     */
    private void computeCoverQuantityForPackageOrders(Order<?> order, Map<String, Integer> packageOrdersCoverQuantity) {
        Layout layout = order.getLayout();
        // TODO improve query, get only first page not all
        List<Page> pages = service.getPagesFromLayout(layout.getId());
        if (!pages.get(0).isCommon()) { // единая обложка
            return;
        }

        Integer quantity = packageOrdersCoverQuantity.get(order.getPackageNumber());
        if (quantity == null) {
            packageOrdersCoverQuantity.put(order.getPackageNumber(), order.getQuantity());
        } else {
            packageOrdersCoverQuantity.put(order.getPackageNumber(), quantity + order.getQuantity());
        }
    }

    private void processingOrders(List<Order<?>> _orders, final Map<String, Integer> packageOrdersCoverQuantity) {
        for (final Order<?> order : _orders) {
            doGeneratePdf(order, packageOrdersCoverQuantity.get(order.getPackageNumber()));
        }
    }

    private void doGenerateGroupOrders(List<Order<?>> _orders, Map<String, Integer> packageOrdersCoverQuantity) {
        final List<Order<?>> moreOneQuantityOrders = new ArrayList<>();
        final List<Order<?>> oneQuantityOrders = new ArrayList<>();

        for (Order<?> order : _orders) {
            if (order.getProduct().getType().equals(8)) {
                doGeneratePdf(order, packageOrdersCoverQuantity.get(order.getPackageNumber()));
            } else {
                if (order.getQuantity() > 1) {
                    moreOneQuantityOrders.add(order);
                } else {
                    oneQuantityOrders.add(order);
                }
            }
        }

        if (!moreOneQuantityOrders.isEmpty()) {
            for (Order<?> order : moreOneQuantityOrders) {
                Integer packageOrderCoverQuantity = packageOrdersCoverQuantity.get(order.getPackageNumber());
                doGenerateGroupPdf(order, order, packageOrderCoverQuantity, packageOrderCoverQuantity);
            }
        }

        if (!oneQuantityOrders.isEmpty()) {
            for (int index = 0; index < oneQuantityOrders.size(); index++) {
                final Order<?> order1 = oneQuantityOrders.get(index++);
                final Order<?> order2 = (index == oneQuantityOrders.size()) ? order1 : oneQuantityOrders.get(index);
                doGenerateGroupPdf(order1, order2,
                        packageOrdersCoverQuantity.get(order1.getPackageNumber()),
                        packageOrdersCoverQuantity.get(order2.getPackageNumber()));
            }
        }
    }

    private void doGenerateGroupPdf(Order<?> order1, Order<?> order2,
                                    final Integer packageOrder1CoverQuantity, final Integer packageOrder2CoverQuantity) {
        try {
            long start = new Date().getTime();

            renderPdf(order1, order2, packageOrder1CoverQuantity, packageOrder2CoverQuantity);

            long end = new Date().getTime();
            LOGGER.warn(order1.getNumber() + " pdf: " + (end - start) + "ms");
            if (order2 != null) {
                LOGGER.warn(order2.getNumber() + " pdf: " + (end - start) + "ms");
            }

            service.setPdfGenerated(order1.getId());
            if (order2 != null) {
                service.setPdfGenerated(order2.getId());
            }
        }
        catch (Exception e) {
            ServiceLogger.log(e);

            service.setPdfErrorState(order1.getId());
            if (order2 != null) {
                service.setPdfErrorState(order2.getId());
            }
        }
    }

    @Override
    public void generatePdfManually(Order<?> order) {
        doGeneratePdf(order, null);
    }

    private void doGeneratePdf(Order<?> order, Integer packageOrdersCoverQuantity) {
        try {
            long start = new Date().getTime();

            renderPdf(order, null, packageOrdersCoverQuantity, null);

            long end = new Date().getTime();
            LOGGER.warn(order.getNumber() + " pdf: " + (end - start) + "ms");

            service.setPdfGenerated(order.getId());
        }
        catch (Exception e) {
            ServiceLogger.log(e);
            service.setPdfErrorState(order.getId());
        }
    }

    @Override
    public void generateTestPdf(int orderId) {
        Order<?> order = repository.getOrder(orderId);
        renderPdf(order, null, null, null);
    }

    private void renderPdf(Order<?> order1, Order<?> order2,
                           final Integer packageOrder1CoverQuantity, final Integer packageOrder2CoverQuantity) {
        // TODO do we need to render the same order twice ? case order1 == order2
        doRenderPdf(order1, packageOrder1CoverQuantity);
        if (order2 != null) {
            doRenderPdf(order2, packageOrder2CoverQuantity);
            doRenderGroupPdf(order1, order2);
        }
        impose(order1, order2);
    }

    private void doRenderGroupPdf(Order<?> order1, Order<?> order2) {
        try {
            LOGGER.debug("Deleting previous PDFs if exist: " + order1.getNumber() + ", " + order2.getNumber());

            new File(util.getGroupPdfPath(order1, order2)).delete();

            LOGGER.debug("Generating non imposed PDF: " + order1.getNumber() + ", " + order2.getNumber());

            PdfReader pdfReader1 = null;
            PdfReader pdfReader2 = null;
            Document document = null;
            String nonImposedPath1 = util.getNonimposedPdfPath(order1);
            String nonImposedPath2 = util.getNonimposedPdfPath(order2);
            Album album = (Album) order1.getProduct();
            int widthMm = album.getWidth();
            int heightMm = album.getHeight();
            float width = widthMm * MM_TO_PX;
            float height = heightMm * MM_TO_PX;
            try {
                document = new Document(new Rectangle(width, height));
                document.setMargins(0, 0, 0, 0);
                FileOutputStream os = new FileOutputStream(util.getGroupPdfPath(order1, order2));
                PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
                document.open();
                LOGGER.debug("Grouping: " + order1.getNumber() + ", " + order2.getNumber());

                pdfReader1 = new PdfReader(nonImposedPath1);
                pdfReader2 = new PdfReader(nonImposedPath2);
                final boolean isAlbum = pdfReader1.getPageSize(1).getHeight() <= pdfReader1.getPageSize(1).getWidth();

                final int totalCount = pdfReader1.getNumberOfPages() > pdfReader2.getNumberOfPages() ? pdfReader1.getNumberOfPages() : pdfReader2.getNumberOfPages();
                for (int index = 1; index <= totalCount; index++) {
                    PdfImportedPage importedPage1 = pdfReader1.getNumberOfPages() >= index ? pdfWriter.getImportedPage(pdfReader1, index) : null;
                    PdfImportedPage importedPage2 = pdfReader2.getNumberOfPages() >= index ? pdfWriter.getImportedPage(pdfReader2, index) : null;
                    if (importedPage1 == null && importedPage2 == null) break;
                    Image image1 = Image.getInstance(importedPage1 == null ? importedPage2 : importedPage1);
                    Image image2 = Image.getInstance(importedPage2 == null ? importedPage1 : importedPage2);

					/*image1.scaleAbsolute(isAlbum ? importedPage1.getWidth()/2 : importedPage1.getWidth(), isAlbum ? importedPage1.getHeight() : importedPage1.getHeight()/2);
					image2.scaleAbsolute(isAlbum ? importedPage2.getWidth()/2 : importedPage2.getWidth(), isAlbum ? importedPage2.getHeight() : importedPage2.getHeight()/2);
					//image1.setAbsolutePosition(importedPage1.getWidth(), importedPage2.getHeight());
					/*PdfPTable table = new PdfPTable(isAlbum ? 2 : 1);
					table.setTotalWidth(importedPage1.getWidth());
					PdfPCell cell1 = new PdfPCell(isAlbum ? (index%2 == 0 ? image1 : image2) : image1, false);
					cell1.setFixedHeight(isAlbum ? importedPage1.getHeight() : importedPage1.getHeight()/2);
					if (isAlbum && index%2 != 0) {
						cell1.setRotation(180);
					}
					PdfPCell cell2 = new PdfPCell(isAlbum ? (index%2 == 0 ? image2 : image1) : image2, false);
					cell2.setFixedHeight(isAlbum ? importedPage2.getHeight() : importedPage2.getHeight()/2);
					if (isAlbum && index%2 == 0) {
						cell2.setRotation(180);
					}
					table.addCell(cell1);
					table.addCell(cell2);
					
					table.completeRow();
					table.writeSelectedRows(0, -1, 0, 0, pdfWriter.getDirectContent());
					document.add(table);*/

                    Rectangle pageSize1 = pdfReader1.getPageSize(1);
                    Rectangle pageSize2 = pdfReader2.getPageSize(2);

                    /*
                     * Спец. логика для Вертикальных книг
                     * @see <a href="http://jira.minogin.ru/browse/IMAGEBOOK-426">Проблема с двойными спусками - вертикальные книги</a>
                     * @see <a href="http://jira.minogin.ru/secure/attachment/12831/%D1%81%D0%BF%D1%83%D1%81%D0%BA.pdf">спуск.pdf</a>
                     *
                     * Спуск страница 1 : мак1стр1(180) - мак2стр1
                     * Спуск страница 2 : мак2стр2 - мак1стр2(180)
                     * Спуск страница 3 : мак1стр3(180) - мак2стр3
                     * Спуск страница 4 : мак2стр4 - мак1стр4(180)
                     * ...
                     */
                    if (!isAlbum) {
                        image1.setRotationDegrees(180);

                        if (index % 2 == 0) { // swap images
                            Image tmp = image1;
                            image1 = image2;
                            image2 = tmp;

                            pageSize1 = pdfReader2.getPageSize(1);
                            pageSize2 = pdfReader1.getPageSize(1);
                        }
                    }

                    addImage(document, image1, pageSize1, width, height);
                    addImage(document, image2, pageSize2, width, height);
                }
            }
            finally {
                if (pdfReader1 != null) {
                    pdfReader1.close();
                }
                if (pdfReader2 != null) {
                    pdfReader2.close();
                }
                if (document != null) {
                    document.close();
                }
            }

            LOGGER.debug("PDF Generated: " + order1.getNumber() + ", " + order2.getNumber());
        }
        catch (Exception e) {
            Exceptions.rethrow(e);
        }
    }

    private void addImage(Document document, Image image, Rectangle pageSize, float width, float height)
            throws DocumentException {

        document.setPageSize(pageSize);
        document.newPage();
        image.scaleAbsolute(width, height);
        document.add(image);
    }

    private void doRenderPdf(Order<?> order, final Integer packageOrderCoverQuantity) {
        try {
            LOGGER.debug("Deleting previous PDFs if exist: " + order.getNumber());

            new File(util.getImposedPdfPath(order)).delete();
            new File(util.getNonimposedPdfPath(order)).delete();

            LOGGER.debug("Generating non imposed PDF: " + order.getNumber());

            Album album = (Album) order.getProduct();
            Integer type = album.getType();
            Integer number = album.getNumber();
            int widthMm = album.getWidth();
            int heightMm = album.getHeight();
            int pageCount = order.getPageCount();

            String jpegFolder = flashConfig.getJpegPath() + "/" + order.getId();

            float width = widthMm * MM_TO_PX;
            float height = heightMm * MM_TO_PX;

            float textWidth = 3f * MM_TO_PX;
            float rectangleHeight = 15f * MM_TO_PX;
            float bottomMargin = 10f * MM_TO_PX;
            float textMargin = 0.5f * MM_TO_PX;

            Document document = new Document(new Rectangle(width, height));
            document.setMargins(0, 0, 0, 0);
            FileOutputStream os = new FileOutputStream(util.getNonimposedPdfPath(order));
            PdfWriter writer = PdfWriter.getInstance(document, new BufferedOutputStream(os));
            BaseFont font = BaseFont.createFont(FONT_LOCATION, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            document.open();
            try {
                PdfContentByte cb = writer.getDirectContent();

                for (int page = 1; page <= pageCount; page++) {
                    String text = page + "/" + pageCount + " - " + order.getNumber() + " - "
                            + order.getQuantity() + " экз. - "
                            + album.getSize() + " - ";
                    Integer coverLamination = order.getCoverLamination();
                    if (coverLamination != CoverLamination.NONE)
                        text += CoverLamination.shorts.get(coverLamination) + " - ";
                    Integer pageLamination = order.getPageLamination();
                    if (pageLamination != PageLamination.NONE)
                        text += PageLamination.shorts.get(pageLamination) + " - ";
                    text += Binding.shorts.get(album.getBinding());

                    Flyleaf flyleaf = order.getFlyleaf();
                    if (flyleaf != null) {
                        text += " - ФОРЗАЦ: " + flyleaf.getInnerName();
                    }

                    float textHeight = font.getWidthPoint(text, FONT_SIZE);

                    // TODO refactor
                    Rectangle testBoxRectangle;
                    if (order.getProduct().isTablet()) {
                        float xMargin = 10f * MM_TO_PX;

                        if (page % 2 == 1) {
                            testBoxRectangle = new Rectangle(xMargin + textMargin, bottomMargin, xMargin + textMargin + textWidth, bottomMargin + textHeight
                                    + textMargin * 2);
                        } else {
                            testBoxRectangle = new Rectangle(width - xMargin - textWidth - textMargin, bottomMargin, width - xMargin - textMargin, bottomMargin
                                    + textHeight + textMargin * 2);
                        }
                        testBoxRectangle.setBackgroundColor(BaseColor.WHITE);
                        cb.rectangle(testBoxRectangle);
                        cb.stroke();

                        cb.beginText();
                        cb.setFontAndSize(font, FONT_SIZE);
                        if (page % 2 == 1)
                            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, text, xMargin + textWidth, bottomMargin
                                + textMargin, 90);
                        else
                            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, text, width - xMargin - 2 * textMargin,
                                bottomMargin + textMargin, 90);
                        cb.endText();

                        // TODO do we need it for tablet
//						if (type == ProductType.EVERFLAT_WHITE_MARGINS || type == ProductType.EVERFLAT_FULL_PRINT) {
//							int margin = (int) (7f * MM_TO_PX);
//							if (page % 2 == 1)
//								rectangle = new Rectangle(0, 0, margin, height);
//							else
//								rectangle = new Rectangle(width - margin, 0, width, height);
//							rectangle.setBackgroundColor(BaseColor.WHITE);
//							cb.rectangle(rectangle);
//							cb.stroke();
//						}
                    } else {
                        if (page % 2 == 1)
                            testBoxRectangle = new Rectangle(width - textWidth, bottomMargin, width, bottomMargin
                                    + textHeight + textMargin
                                    * 2);
                        else
                            testBoxRectangle = new Rectangle(0, bottomMargin, textWidth, bottomMargin + textHeight
                                    + textMargin * 2);
                        testBoxRectangle.setBackgroundColor(BaseColor.WHITE);
                        cb.rectangle(testBoxRectangle);
                        cb.stroke();

                        cb.beginText();
                        cb.setFontAndSize(font, FONT_SIZE);
                        if (page % 2 == 1)
                            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, text, width - textMargin, bottomMargin
                                    + textMargin, 90);
                        else
                            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, text, textWidth - textMargin,
                                    bottomMargin + textMargin, 90);
                        cb.endText();

                        if (type == ProductType.EVERFLAT_WHITE_MARGINS || type == ProductType.EVERFLAT_FULL_PRINT) {
                            int margin = (int) (7f * MM_TO_PX);
                            Rectangle rectangle;
                            if (page % 2 == 1)
                                rectangle = new Rectangle(0, 0, margin, height);
                            else
                                rectangle = new Rectangle(width - margin, 0, width, height);
                            rectangle.setBackgroundColor(BaseColor.WHITE);
                            cb.rectangle(rectangle);
                            cb.stroke();
                        }

                        if (album.isAddressPrinted() && page == pageCount) {
                            Compiler compiler = coreFactory.createCompiler();
                            compiler.registerFunction("implode", new ImplodeFunction());
                            compiler.registerFunction("prefix", new PrefixFunction());
                            Locale locale = new Locale(Locales.RU);
                            String addressTemplate = messages.getMessage("deliveryAddressTemplate", null, locale);
                            Address address = order.getAddress();
                            if (address != null) {
                                String addressText = compiler.compile(addressTemplate, address);
                                addressText += ", " + address.getFullName();
                                float fontSize = ADDRESS_FONT_SIZE;
                                while (!renderAddress(cb, addressText, font, fontSize)) {
                                    fontSize--;
                                    if (fontSize < 9.5)
                                        throw new AddressRenderError();
                                }
                            }
                        }
                    }

                    float flyleafHY = addFlyleafColorBox(order, textWidth, cb, testBoxRectangle.getRight(),
                        testBoxRectangle.getTop());
                    addVellumBlockInfo(order, textWidth, font, cb, testBoxRectangle.getRight(), flyleafHY);

                    Image image = Image.getInstance(jpegFolder + "/" + page + ".jpg");
                    image.scaleAbsolute(width, height);
                    document.add(image);
                }
            } finally {
                document.close();
            }

            if (album.isSeparateCover()) {
                widthMm = album.getPdfCoverWidth();
                heightMm = album.getPdfCoverHeight();
                width = widthMm * MM_TO_PX;
                height = heightMm * MM_TO_PX;

                String text = packageOrderCoverQuantity == null ? order.getNumber() : order.getPackageNumber();
                text += " - ";
                Integer coverLamination = order.getCoverLamination();
                if (coverLamination != CoverLamination.NONE) {
                    text += CoverLamination.shortValues.get(coverLamination);
                }

                try {
                    Image image = Image.getInstance(jpegFolder + "/c.jpg");
                    if (album.isWhiteCover()) {
                        float coverWidthMm = 488;
                        float coverWidth = coverWidthMm * MM_TO_PX;
                        document = new Document(new Rectangle(coverWidth, height));
                        document.setMargins(0, 0, 0, 0);
                        os = new FileOutputStream(util.getCoverPdfPath(order, packageOrderCoverQuantity));
                        writer = PdfWriter.getInstance(document, new BufferedOutputStream(os));
                        document.open();

                        // закрашиваем номер из jpeg белой полосой
                        Rectangle rectangle = new Rectangle(coverWidth
                                - ((COVER_WHITE_LINE_WIDTH + COVER_WHITE_LINE_MARGIN_RIGHT) * MM_TO_PX), (height / 2)
                                - ((COVER_WHITE_LINE_HEIGHT / 2) * MM_TO_PX), coverWidth
                                - (COVER_WHITE_LINE_MARGIN_RIGHT * MM_TO_PX),
                                (height / 2) + ((COVER_WHITE_LINE_HEIGHT / 2) * MM_TO_PX));
                        rectangle.setBackgroundColor(BaseColor.WHITE);

                        PdfContentByte cb = writer.getDirectContent();

                        cb.rectangle(rectangle);
                        cb.stroke();

                        cb.beginText();
                        cb.setFontAndSize(font, FONT_SIZE);

                        final float startX = coverWidth - textMargin - (COVER_WHITE_LINE_MARGIN_RIGHT * MM_TO_PX);
                        final float startY = rectangle.getBottom() + rectangleHeight - textMargin; // based on rectangle
                        // OLD way final float startY = (height / 2) - ((COVER_WHITE_LINE_HEIGHT / 2) * MM_TO_PX) + bottomMargin + textMargin;
                        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, text, startX, startY, 90);

                        cb.endText();
                        cb.stroke();
                        // -------

                        final float recLY = startY + rectangleHeight;
                        final float recHY = recLY + rectangleHeight;
                        Rectangle rectangleLamination;
                        rectangleLamination = new Rectangle(startX - textWidth, recLY, startX, recHY);

                        BaseColor color;
                        if (coverLamination == CoverLamination.GLOSSY) {
                            color = BaseColor.BLUE;
                        } else if (coverLamination == CoverLamination.SAND) {
                            color = BaseColor.GREEN;
                        } else {
                            color = BaseColor.RED;
                        }
                        rectangleLamination.setBackgroundColor(color);

                        cb.rectangle(rectangleLamination);
                        cb.stroke();

                        float flyleafHY = addFlyleafCoverInfo(order, textWidth, font, cb, startX, recHY);
                        addVellumCoverInfo(order, textWidth, font, cb, startX, flyleafHY);

                        float xMm;
                        if (album.isSuperAlbum())
                            xMm = coverWidthMm - widthMm;
                        else
                            xMm = coverWidthMm - widthMm + 35;
                        float x = xMm * MM_TO_PX;
                        image.scaleAbsolute(width, height);
                        image.setAbsolutePosition(x, 0);
                        document.add(image);
                    } else {
                        float imageWidthPx = image.getWidth();
                        float imageWidthMm = imageWidthPx * PX_TO_MM;
                        float imageWidth = imageWidthMm * MM_TO_PX;
                        float docWidth = imageWidth;

                        float imageHeightPx = image.getHeight();
                        float imageHeightMm = imageHeightPx * PX_TO_MM;
                        float imageHeight = imageHeightMm * MM_TO_PX;

                        float MAX_WIDTH = 478f;
                        if ((type == HARD_COVER_FULL_PRINT || type == TABLET) && (number == 1 || number == 0)) {
                            if (imageWidthMm > MAX_WIDTH) {
                                docWidth = MAX_WIDTH * MM_TO_PX;
                            }
                        }

                        document = new Document(new Rectangle(docWidth, height));
                        document.setMargins(0, 0, 0, 0);
                        os = new FileOutputStream(util.getCoverPdfPath(order, packageOrderCoverQuantity));
                        writer = PdfWriter.getInstance(document, new BufferedOutputStream(os));
                        document.open();
                        image.scaleAbsolute(imageWidth, imageHeight);

                        // закрашивает номер из jpeg белой полосой
                        Rectangle rectangle = new Rectangle(
                                docWidth - (COVER_WHITE_LINE_WIDTH * MM_TO_PX),
                                (height / 2) - ((COVER_WHITE_LINE_HEIGHT / 2) * MM_TO_PX),
                                docWidth,
                                (height / 2) + ((COVER_WHITE_LINE_HEIGHT / 2) * MM_TO_PX)
                        );
                        rectangle.setBackgroundColor(BaseColor.WHITE);

                        PdfContentByte cb = writer.getDirectContent();

                        cb.rectangle(rectangle);
                        cb.stroke();

                        cb.beginText();
                        cb.setFontAndSize(font, FONT_SIZE);

                        final float startX = docWidth - textMargin;
                        final float startY = rectangle.getBottom() + rectangleHeight - textMargin; // based on rectangle
                        // OLD way final float startY = (docHeight / 2) - ((COVER_WHITE_LINE_HEIGHT / 2) * MM_TO_PX) + bottomMargin + textMargin;
                        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, text, startX, startY, 90);

                        cb.endText();
                        cb.stroke();

                        final float recLY = startY + rectangleHeight;
                        final float recHY = recLY + rectangleHeight;
                        Rectangle rectangleLamination;
                        rectangleLamination = new Rectangle(startX - textWidth, recLY, startX, recHY);

                        BaseColor color;
                        if (coverLamination == CoverLamination.GLOSSY) {
                            color = BaseColor.BLUE;
                        } else if (coverLamination == CoverLamination.SAND) {
                            color = BaseColor.GREEN;
                        } else {
                            color = BaseColor.RED;
                        }
                        rectangleLamination.setBackgroundColor(color);

                        cb.rectangle(rectangleLamination);
                        cb.stroke();

                        float flyleafHY = addFlyleafCoverInfo(order, textWidth, font, cb, startX, recHY);
                        addVellumCoverInfo(order, textWidth, font, cb, startX, flyleafHY);

                        // ---------------
                        if ((type == HARD_COVER_FULL_PRINT || type == TABLET) && album.getNumber() == 7) {
                            float shiftMm = 3.5f;
                            float shift = shiftMm * MM_TO_PX;
                            image.setAbsolutePosition(0, -shift);
                        }

                        if ((type == HARD_COVER_FULL_PRINT || type == TABLET) && album.getNumber() == 1) {
                            if (imageWidthMm > MAX_WIDTH) {
                                float shiftMm = imageWidthMm - MAX_WIDTH;
                                float shift = shiftMm * MM_TO_PX;
                                image.setAbsolutePosition(-shift, 0);
                            }
                        }

                        // Mark the spine for external (Pickbook) orders
                        if (order.getType() == OrderType.EXTERNAL
                                && (type == HARD_COVER_FULL_PRINT || type == TABLET)) {
                            float spineWidthMm = 10;
                            float spineWidthPx = spineWidthMm * MM_TO_PX;

                            float x = (docWidth - spineWidthPx) / 2;
                            float margin = 8 * MM_TO_PX;

                            float dx = 0.2f * MM_TO_PX;
                            cb.setLineWidth(dx);

                            cb.setColorStroke(BaseColor.BLACK);
                            cb.moveTo(x, 0);
                            cb.lineTo(x, margin);
                            cb.moveTo(x, height - margin);
                            cb.lineTo(x, height);
                            cb.stroke();

                            cb.setColorStroke(BaseColor.WHITE);
                            cb.moveTo(x + dx, 0);
                            cb.lineTo(x + dx, margin);
                            cb.moveTo(x + dx, height - margin);
                            cb.lineTo(x + dx, height);
                            cb.stroke();

                            x = (docWidth + spineWidthPx) / 2;
                            cb.setColorStroke(BaseColor.BLACK);
                            cb.moveTo(x, 0);
                            cb.lineTo(x, margin);
                            cb.moveTo(x, height - margin);
                            cb.lineTo(x, height);
                            cb.stroke();

                            cb.setColorStroke(BaseColor.WHITE);
                            cb.moveTo(x + dx, 0);
                            cb.lineTo(x + dx, margin);
                            cb.moveTo(x + dx, height - margin);
                            cb.lineTo(x + dx, height);
                            cb.stroke();
                        }

                        document.add(image);
                    }
                } finally {
                    document.close();
                }
            }

            LOGGER.debug("PDF Generated: " + order.getNumber());
        }
        catch (Exception e) {
            Exceptions.rethrow(e);
        }
    }

    private float addFlyleafCoverInfo(Order<?> order, float textWidth, BaseFont font, PdfContentByte cb, float startX,
                                      float recHY) {
        Flyleaf flyleaf = order.getFlyleaf();
        if (flyleaf != null) {
            float x = startX;
            float y = recHY + 5 * MM_TO_PX;

            cb.beginText();
            cb.setFontAndSize(font, FONT_SIZE);
            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Форзац:", x, y, 90);
            cb.endText();
            cb.stroke();

            y += 12 * MM_TO_PX;
            Rectangle rect = new Rectangle(startX - textWidth, y, startX, y + 5 * MM_TO_PX);
            rect.setBackgroundColor(new BaseColor(Color.decode("#" + flyleaf.getColorRGB())));
            cb.rectangle(rect);
            cb.stroke();

            y += 6 * MM_TO_PX;
            cb.beginText();
            cb.setFontAndSize(font, FONT_SIZE);
            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, flyleaf.getInnerName(), x, y, 90);
            cb.endText();
            cb.stroke();

            return y + font.getWidthPoint(flyleaf.getInnerName(), FONT_SIZE);
        } else {
            return recHY;
        }
    }

    private float addFlyleafColorBox(Order<?> order, float textWidth, PdfContentByte cb, float startX, float recHY) {
        Flyleaf flyleaf = order.getFlyleaf();
        if (flyleaf == null) {
            return recHY;
        }

        float y = recHY + 5 * MM_TO_PX;

        Rectangle rect = new Rectangle(startX - textWidth, recHY, startX, y);
        rect.setBackgroundColor(new BaseColor(Color.decode("#" + flyleaf.getColorRGB())));
        cb.rectangle(rect);
        cb.stroke();

        return rect.getTop();
    }

    private void addVellumBlockInfo(Order<?> order, float textWidth, BaseFont font, PdfContentByte cb, float startX,
                                    float recHY) {
        Album album = (Album) order.getProduct();
        if (!album.isSupportsVellum()) {
            return;
        }

        Vellum vellum = order.getVellum();

        float x = startX;
        float y = recHY;

        String text = " - КАЛЬКА: " + ((vellum != null) ? vellum.getInnerName() : "Нет");
        float colorBoxWidth = 5 * MM_TO_PX;
        float vellumRectY = y + font.getWidthPoint(text, FONT_SIZE);

        Rectangle vellumRectangle = new Rectangle(x - textWidth, y, x, vellumRectY);
        vellumRectangle.setBackgroundColor(BaseColor.WHITE);
        cb.rectangle(vellumRectangle);
        cb.stroke();

        cb.beginText();
        cb.setFontAndSize(font, FONT_SIZE);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, text, x, y, 90);
        cb.endText();
        cb.stroke();

        if (vellum != null) {
            y += font.getWidthPoint(text, FONT_SIZE);
            Rectangle rect = new Rectangle(x - textWidth, y, x, y + colorBoxWidth);
            rect.setBackgroundColor(new BaseColor(Color.decode("#" + vellum.getColorRGB())));
            cb.rectangle(rect);
            cb.stroke();
        }
    }

    private void addVellumCoverInfo(Order<?> order, float textWidth, BaseFont font, PdfContentByte cb, float startX,
                                    float recHY) {
        Album album = (Album) order.getProduct();
        if (!album.isSupportsVellum()) {
            return;
        }

        Vellum vellum = order.getVellum();

        float x = startX;
        float y = recHY + 5 * MM_TO_PX;

        String text = "Калька:" + (vellum == null ? "Нет" : "");

        cb.beginText();
        cb.setFontAndSize(font, FONT_SIZE);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, text, x, y, 90);
        cb.endText();
        cb.stroke();

        if (vellum != null) {
            y += 12 * MM_TO_PX;
            Rectangle rect = new Rectangle(startX - textWidth, y, startX, y + 5 * MM_TO_PX);
            rect.setBackgroundColor(new BaseColor(Color.decode("#" + vellum.getColorRGB())));
            cb.rectangle(rect);
            cb.stroke();

            y += 6 * MM_TO_PX;
            cb.beginText();
            cb.setFontAndSize(font, FONT_SIZE);
            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, vellum.getInnerName(), x, y, 90);
            cb.endText();
            cb.stroke();
        }
    }

    private void impose(Order<?> order1, Order<?> order2) {
        new Imposer(order1, order2, LOGGER, util).impose();
    }

    @SuppressWarnings("unused")
    private void sendPdfFiles(Order<?> order) {
        Album album = (Album) order.getProduct();
        String typeName = album.getName().get(Locales.RU);

        XFtpClient client = new XFtpClient();
        try {
            client.connect(config.getHost(), config.getUser(), config.getPassword());

            String toFolder = DateFormat.getDateInstance().format(order.getPrintDate());
            client.mkdir(toFolder);
            client.cd(toFolder);

            client.mkdir(typeName);
            client.cd(typeName);

            String tempFileName = UUID.randomUUID().toString();
            String path = util.getImposedPdfPath(order);
            String fileName = util.getImposedPdfFileName(order);
            if (!new File(path).exists()) {
                path = util.getNonimposedPdfPath(order);
                fileName = util.getNonimposedPdfFileName(order);
            }
            client.saveFile(path, tempFileName);
            client.deleteFile(fileName);
            client.rename(tempFileName, fileName);

            if (album.isSeparateCover()) {
                tempFileName = UUID.randomUUID().toString();
                path = util.getCoverPdfPath(order);
                client.saveFile(path, tempFileName);
                fileName = util.getCoverPdfFileName(order, null);
                client.deleteFile(fileName);
                client.rename(tempFileName, fileName);
            }
        }
        finally {
            client.disconnect();
        }
    }

    private boolean renderAddress(PdfContentByte cb, String addressText, BaseFont font, float fontSize)
            throws DocumentException {
        float x1 = 125 * MM_TO_PX;
        float y1 = 25 * MM_TO_PX;
        float x2 = 190 * MM_TO_PX;
        float y2 = 50 * MM_TO_PX;

        HyphenationAuto auto = new HyphenationAuto("ru", "RU", 2, 2);

        ColumnText columnText = new ColumnText(cb);
        columnText.setSimpleColumn(x1, y1, x2, y2);
        float pos = columnText.getYLine();
        Font addressFont = new Font(font, fontSize);
        Chunk chunk = new Chunk(addressText, addressFont);
        chunk.setHyphenation(auto);
        columnText.addText(chunk);
        int status = columnText.go(true);
        if (status == ColumnText.NO_MORE_COLUMN) {
            return false;
        } else {
            columnText.setYLine(pos);
            columnText.addText(chunk);
            columnText.go();
            return true;
        }
    }
}