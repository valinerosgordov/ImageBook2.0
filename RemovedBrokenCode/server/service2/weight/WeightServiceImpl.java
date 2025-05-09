package ru.imagebook.server.service2.weight;

import java.io.File;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.client.common.service.order.OrderService;
import ru.imagebook.server.repository2.weight.WeightRepository;
import ru.imagebook.server.service.FileConfig;
import ru.imagebook.server.service.request.RequestServiceImpl;
import ru.imagebook.shared.model.AlbumOrder;
import ru.imagebook.shared.model.Formats;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.PageLamination;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.ProductType;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.server.file.TempFile;
import ru.minogin.oo.server.OOClient;
import ru.minogin.oo.server.spreadsheet.Sheet;
import ru.minogin.oo.server.spreadsheet.SpreadsheetDoc;

public class WeightServiceImpl implements WeightService {
    public static final Logger LOGGER = Logger.getLogger(RequestServiceImpl.class);
    
    private static final double ZERO_WEIGHT = 0;
    private static final double DOUBLE_PAGE = 2.5;
    
    private static final double A5_SOFT_PAGE_WEIGHT = 3.7;
    private static final double A5_HARD_PAGE_WEIGHT = 3;
    private static final double A5_TABLET_PAGE_WEIGHT = A5_HARD_PAGE_WEIGHT * DOUBLE_PAGE;
    
    private static final double NONE_PAGE_LAMINATION_WEIGHT = 1;
    private static final double DEFAULT_PAGE_LAMINATION_WEIGHT = 1.2;
    
    private static final double A5_CLIP_COVER_WEIGHT = 0;
    private static final double A5_SPRING_COVER_WEIGHT = 32;
    private static final double A5_HARD_COVER_WEIGHT = 110;
    
    private static final int A5_PACKAGE_WEIGHT = 50;
    

    private static final Map<Integer, Double> A5_PAGE_WEIGHTS = new HashMap<Integer, Double>();
    private static final Map<Integer, Double> A5_COVER_WEIGHTS = new HashMap<Integer, Double>();
    private static final Map<Integer, Double> FORMAT_COEFF = new HashMap<Integer, Double>();
    
    static {
        A5_PAGE_WEIGHTS.put(ProductType.SPRING, A5_SOFT_PAGE_WEIGHT);
        A5_PAGE_WEIGHTS.put(ProductType.CLIP, A5_SOFT_PAGE_WEIGHT);
        A5_PAGE_WEIGHTS.put(ProductType.TRIAL, A5_SOFT_PAGE_WEIGHT);
        A5_PAGE_WEIGHTS.put(ProductType.CONGRATULATORY, A5_SOFT_PAGE_WEIGHT);
        A5_PAGE_WEIGHTS.put(ProductType.EVERFLAT_WHITE_MARGINS, A5_HARD_PAGE_WEIGHT);
        A5_PAGE_WEIGHTS.put(ProductType.EVERFLAT_FULL_PRINT, A5_HARD_PAGE_WEIGHT);
        A5_PAGE_WEIGHTS.put(ProductType.HARD_COVER_WHITE_MARGINS, A5_HARD_PAGE_WEIGHT);
        A5_PAGE_WEIGHTS.put(ProductType.HARD_COVER_FULL_PRINT, A5_HARD_PAGE_WEIGHT);
        A5_PAGE_WEIGHTS.put(ProductType.PANORAMIC, ZERO_WEIGHT);
        A5_PAGE_WEIGHTS.put(ProductType.STANDARD, ZERO_WEIGHT);
        A5_PAGE_WEIGHTS.put(ProductType.TABLET, A5_TABLET_PAGE_WEIGHT);
        
        A5_COVER_WEIGHTS.put(ProductType.CLIP, A5_CLIP_COVER_WEIGHT);
        A5_COVER_WEIGHTS.put(ProductType.TRIAL, A5_CLIP_COVER_WEIGHT);
        A5_COVER_WEIGHTS.put(ProductType.CONGRATULATORY, A5_CLIP_COVER_WEIGHT);
        A5_COVER_WEIGHTS.put(ProductType.PANORAMIC, A5_CLIP_COVER_WEIGHT);
        A5_COVER_WEIGHTS.put(ProductType.STANDARD, A5_CLIP_COVER_WEIGHT);
        A5_COVER_WEIGHTS.put(ProductType.SPRING, A5_SPRING_COVER_WEIGHT);
        A5_COVER_WEIGHTS.put(ProductType.EVERFLAT_WHITE_MARGINS, A5_HARD_COVER_WEIGHT);
        A5_COVER_WEIGHTS.put(ProductType.EVERFLAT_FULL_PRINT, A5_HARD_COVER_WEIGHT);
        A5_COVER_WEIGHTS.put(ProductType.HARD_COVER_WHITE_MARGINS, A5_HARD_COVER_WEIGHT);
        A5_COVER_WEIGHTS.put(ProductType.HARD_COVER_FULL_PRINT, A5_HARD_COVER_WEIGHT);
        A5_COVER_WEIGHTS.put(ProductType.TABLET, A5_HARD_COVER_WEIGHT);
        
        FORMAT_COEFF.put(Formats.CC_01, 1.0);
        FORMAT_COEFF.put(Formats.CC_02, 1.0);
        FORMAT_COEFF.put(Formats.CC_16, 1.0);
        FORMAT_COEFF.put(Formats.CC_99, 1.0);
        FORMAT_COEFF.put(Formats.CC_03, 1.5);
        FORMAT_COEFF.put(Formats.CC_11, 1.5);
        FORMAT_COEFF.put(Formats.CC_18, 1.5);
        FORMAT_COEFF.put(Formats.CC_37, 1.5);
        FORMAT_COEFF.put(Formats.CC_04, 2.0);
        FORMAT_COEFF.put(Formats.CC_05, 2.0);
        FORMAT_COEFF.put(Formats.CC_06, 2.0);
        FORMAT_COEFF.put(Formats.CC_07, 2.0);
        FORMAT_COEFF.put(Formats.CC_14, 2.0);
        FORMAT_COEFF.put(Formats.CC_27, 2.0);
        FORMAT_COEFF.put(Formats.CC_30, 2.0);
        FORMAT_COEFF.put(Formats.CC_31, 2.0);
        FORMAT_COEFF.put(Formats.CC_15, 2.0);
        FORMAT_COEFF.put(Formats.CC_12, 2.0);
        FORMAT_COEFF.put(Formats.CC_19, 2.0);
        FORMAT_COEFF.put(Formats.CC_20, 2.0);
        FORMAT_COEFF.put(Formats.CC_13, 2.0);
        FORMAT_COEFF.put(Formats.CC_36, 2.0);
        FORMAT_COEFF.put(Formats.CC_08, 3.0);
        FORMAT_COEFF.put(Formats.CC_32, 3.0);
        FORMAT_COEFF.put(Formats.CC_09, 4.0);
        FORMAT_COEFF.put(Formats.CC_10, 4.0);
        FORMAT_COEFF.put(Formats.CC_00, 4.0);
        FORMAT_COEFF.put(Formats.CC_21, 4.0);
        FORMAT_COEFF.put(Formats.CC_24, 4.0);
    }
    
	@Autowired
	private OOClient ooClient;
	@Autowired
	private FileConfig fileConfig;
	@Autowired
	private WeightRepository repository;
	@Autowired
	private CoreFactory coreFactory;

	@Override
	public int getItemWeight(AlbumOrder order) {
	    Product product = order.getProduct();
	    
	    double a5PageWeight = 0;
	    double a5CoverWeight = 0;
        double formatCoeff = 0;

        try {
            a5PageWeight = A5_PAGE_WEIGHTS.get(product.getType());
            a5CoverWeight = A5_COVER_WEIGHTS.get(product.getType());
            formatCoeff = FORMAT_COEFF.get(product.getNumber());
        } catch (NullPointerException ex) {
            String msg =
                String.format("A5_PAGE_WEIGHT, A5_COVER_WEIGHT, FORMAT_COEFF not defined for OrderId=%s, BB=%s, CC=%s",
                order.getId(), product.getType(), product.getNumber()
            );
            LOGGER.error(msg);
        }    
	    
	    double pageLaminationCoeff = (order.getPageLamination() == PageLamination.NONE)
	            ? NONE_PAGE_LAMINATION_WEIGHT
	            : DEFAULT_PAGE_LAMINATION_WEIGHT;
	    
	    double a5InternalBlockWeight = order.getPageCount() * a5PageWeight * pageLaminationCoeff;

	    double weight = (a5InternalBlockWeight + a5CoverWeight + A5_PACKAGE_WEIGHT) * formatCoeff;		
		return (int) Math.ceil(weight);
	}

	@Override
	public int getTotalWeight(AlbumOrder order) {
		int itemWeight = getItemWeight(order);
		return itemWeight * order.getQuantity();
	}

	@Transactional
	@Override
	public TempFile createWeightReport() {
		OrderService clientOrderService = new OrderService(coreFactory);

		List<Order<?>> orders = repository.loadYearOrders();

		SpreadsheetDoc doc = ooClient.createSpreadsheetDoc();

		Sheet sheet = doc.getSheet(0);
		int y = 0;
		int x = 0;
		sheet.setText(x++, y, "Номер");
		sheet.setText(x++, y, "Дата");
		sheet.setText(x++, y, "Артикул");
		sheet.setText(x++, y, "Продукт");
		sheet.setText(x++, y, "Кол-во экз.");
		sheet.setText(x++, y, "Вес одного экз.");
		sheet.setText(x++, y, "Вес всего заказа");
		sheet.setText(x, y, "Код отправления");
		y++;

		DateFormat dateFormat = DateFormat.getDateInstance();
		for (Order<?> order : orders) {
			x = 0;
			AlbumOrder albumOrder = (AlbumOrder) order;
			int itemWeight = getItemWeight(albumOrder);
			int totalWeight = getTotalWeight(albumOrder);
			sheet.setText(x++, y, order.getNumber());
			sheet.setText(x++, y, dateFormat.format(order.getDate()));
			sheet.setText(x++, y, clientOrderService.getOrderArticle(order));
			sheet.setText(x++, y, order.getProduct().getName().get(Locales.RU));
			sheet.setNumber(x++, y, order.getQuantity());
			sheet.setNumber(x++, y, itemWeight);
			sheet.setNumber(x++, y, totalWeight);
			sheet.setText(x, y, order.getDeliveryCode());
			y++;
		}

		String id = UUID.randomUUID().toString();
		String fileName = "Вес.xls";
		String path = fileConfig.getTempPath() + "/" + id;
		doc.saveAsExcel(path);
		doc.close();

		return new TempFile(id, fileName, new File(path));
	}
}
