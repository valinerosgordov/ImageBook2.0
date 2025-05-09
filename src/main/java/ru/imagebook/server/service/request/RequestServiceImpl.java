package ru.imagebook.server.service.request;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.mail.internet.MimeMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import ru.imagebook.client.admin.view.finishing.CoverUtil;
import ru.imagebook.server.model.export.request.OrderExpCollection;
import ru.imagebook.server.model.export.request.OrderExpItem;
import ru.imagebook.server.repository.RequestRepository;
import ru.imagebook.server.service.DocConfig;
import ru.imagebook.server.service.FileConfig;
import ru.imagebook.server.service.OrderService;
import ru.imagebook.server.service.PrintEffectConfig;
import ru.imagebook.server.service.VendorService;
import ru.imagebook.server.service.notify.MailSenderLocator;
import ru.imagebook.server.tools.DateUtil;
import ru.imagebook.server.tools.xwpf.XWPFFormat;
import ru.imagebook.server.tools.xwpf.XWPFUtils;
import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.AlbumOrder;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.Binding;
import ru.imagebook.shared.model.Cover;
import ru.imagebook.shared.model.CoverLamination;
import ru.imagebook.shared.model.Email;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderState;
import ru.imagebook.shared.model.PageLamination;
import ru.imagebook.shared.model.Paper;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.Request;
import ru.imagebook.shared.model.RequestState;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.format.Formatter;
import ru.minogin.core.client.i18n.lang.ImplodeFunction;
import ru.minogin.core.client.i18n.lang.PrefixFunction;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.client.lang.template.Compiler;
import ru.minogin.core.client.text.StringUtil;
import ru.minogin.core.server.ServiceLogger;
import ru.minogin.core.server.file.TempFile;
import ru.minogin.core.server.format.MoneyFormat;
import ru.minogin.core.server.ftp.XFtpClient;
import ru.minogin.core.server.ftp.XFtpClientFactory;
import ru.minogin.util.shared.exceptions.Exceptions;

public class RequestServiceImpl implements RequestService {
	public static final Logger LOGGER = Logger.getLogger(RequestServiceImpl.class);
	
	private static final String DATE_FORMAT = "dd.MM.yyyy_HH.mm";

	@Autowired
	private RequestRepository repository;

	@Autowired
	private DocConfig docConfig;

	@Autowired
	private CoreFactory coreFactory;

	@Autowired
	private FileConfig fileConfig;

	@Autowired
	private PrintEffectConfig printEffectConfig;

	@Autowired
	private OrderService orderService;

	@Autowired
	private MessageSource messages;

	@Autowired
	private VendorService vendorService;

	@Autowired
	private RequestConfig config;

    @Autowired
    private MailSenderLocator mailSenderLocator;
	
	protected XFtpClientFactory ftpClientFactory = new XFtpClientFactory();

	@Override
	public List<Request> loadRequests(int offset, int limit) {
		return repository.loadRequests(offset, limit);
	}

	@Override
	public long countRequests() {
		return repository.countRequests();
	}

	@Override
	public List<Order<?>> loadNonBasketOrders() {
		return repository.loadNonBasketOrders();
	}

	@Override
	public long countNonBasketOrders() {
		return repository.countNonBasketOrders();
	}

	@Override
	public List<Order<?>> loadBasketOrders() {
		return repository.loadBasketOrders();
	}

	@Override
	public long countBasketOrders() {
		return repository.countBasketOrders();
	}

	@Override
	public void putToBasket(List<Integer> orderIds) {
		List<Order<?>> orders = repository.loadOrders(orderIds);
		for (Order<?> order : orders) {
			order.setInRequestBasket(true);
		}
	}

	@Override
	public void removeFromBasket(List<Integer> orderIds) {
		List<Order<?>> orders = repository.loadOrders(orderIds);
		for (Order<?> order : orders) {
			order.setInRequestBasket(false);
		}
	}

	@Override
	public void createRequest(boolean urgentFlag) {
		Request request = new Request();
		int maxNumber = repository.getMaxRequestNumber();
		request.setNumber(maxNumber + 1);
		request.setUrgent(urgentFlag);

		List<Order<?>> basketOrders = repository.loadAllBasketOrders();
		for (Order<?> order : basketOrders) {
			request.addOrder(order);
			order.setInRequestBasket(false);
			order.setState(OrderState.PRINTING);
		}

		setRequestTotals(request);

		repository.saveRequest(request);
	}

	@Override
	public void deleteRequests(List<Integer> requestIds) {
		repository.deleteRequests(requestIds);
	}

	@Override
	public TempFile printRequest(int requestId) {
		String locale = Locales.RU;

		Request request = repository.getRequest(requestId);

		Set<Order<?>> orders = request.getOrders();
		Map<Product, List<Order<?>>> groups = new LinkedHashMap<Product, List<Order<?>>>();
		for (Order<?> order : orders) {
			Product product = order.getProduct();
			List<Order<?>> productOrders = groups.get(product);
			if (productOrders == null) {
				productOrders = new ArrayList<Order<?>>();
				groups.put(product, productOrders);
			}
			productOrders.add(order);
		}

		XWPFDocument document =
			XWPFUtils.getDocument(docConfig.getTemplatePath() + "/print_request.docx");
		
		Date date = request.getDate();

		String dateText = DateFormat.getDateInstance().format(date);
		XWPFUtils.replaceTextInDocument(document, "${ДАТА}", dateText);
		XWPFUtils.replaceTextInDocument(document, "${ДАТА_СДАЧИ}", getDeadlineText(date));

		XWPFTable table = document.getTables().get(0);
		XWPFTableRow firstRow = table.getRow(1);
		
		XWPFFormat[] formats = XWPFUtils.getFormats(table, firstRow);
		XWPFFormat[] totalFormats = XWPFUtils.getFormats(table, firstRow);

		Formatter formatter = coreFactory.createFormatter();

		int number = 1;
		int total = 0;
		for (Product product : groups.keySet()) {
			List<Order<?>> group = groups.get(product);
			Integer type = product.getType();
			int subtotal = 0;
			for (Order<?> order : group) {
				XWPFTableRow r = table.createRow();
				
				String[] values = new String[r.getTableCells().size()];
				values[0] = number + "";
				values[1] = order.getNumber();
				values[2] = "01-" + formatter.n2(type) + "-" + formatter.n2(product.getNumber());
				values[3] = product.getName().get(locale);

				int quantity = order.getQuantity();
                subtotal += quantity;
                total += quantity;
                values[4] = quantity + "";

				Album album = (Album) product;
				values[5] = album.getSize();
				values[6] = Binding.values.get(product.getBinding()).get(locale);
				values[7] = CoverUtil.getCoverName((AlbumOrder) order, locale);
				values[8] = Paper.values.get(product.getPaper()).get(locale);
				values[9] = order.getPageCount() + "";
				values[10] = CoverLamination.values.get(order.getCoverLamination()).get(locale);
				values[11] = PageLamination.values.get(order.getPageLamination()).get(locale);
				
				for (int x = 0; x < r.getTableCells().size(); x++) {
					XWPFUtils.setCellValue(r, x, values[x], formats[x]);
				}
				number++;
			}
			
	        XWPFTableRow r = table.createRow();
	        XWPFUtils.setCellValue(r, 3, "ИТОГО:", formats[3]);
	        XWPFUtils.setCellValue(r, 4, subtotal + "", formats[4]);
	        for (int x = 0; x < r.getTableCells().size(); x++) {
	        	totalFormats[x].setBold(true);
	        	totalFormats[x].setBackgroundColor("DDDDDD");
	            XWPFUtils.setCellFormat(r.getCell(x), totalFormats[x]);
	        }
		}

		XWPFTableRow r = table.createRow();
        XWPFUtils.setCellValue(r, 3, "ВСЕГО:", formats[3]);
        XWPFUtils.setCellValue(r, 4, total + "", formats[4]);
        for (int x = 0; x < r.getTableCells().size(); x++) {
        	totalFormats[x].setBold(true);
        	totalFormats[x].setBackgroundColor("CCCCCC");
            XWPFUtils.setCellFormat(r.getCell(x), totalFormats[x]);
        }

		String id = UUID.randomUUID().toString();
		String path = fileConfig.getTempPath() + "/" + id;
		String fileName;
		if (request.isUrgent()) {
			fileName = "Заявка_на_печать_срочные_заказы_№" + dateText + "-П.docx";
		} else {
			fileName = "Заявка_на_печать_№" + dateText + "-П.docx";
		}
		
		table.removeRow(1);
		XWPFUtils.saveDocument(path, document);

		return new TempFile(id, fileName, new File(path));
	}
	
	@Override
	public TempFile bookRequest(int requestId) {
		Request request = repository.getRequest(requestId);
		return bookRequest(request.getDate(), request.getOrders(), false);
	}

	private TempFile bookRequest(Date requestDate, Collection<Order<?>> requestOrders, boolean weeklyRequest) {
		List<Order<?>> orders = new ArrayList<Order<?>>();
		for (Order<?> order : requestOrders) {
			Bill bill = order.getBill();
			if (bill != null && bill.isAdv())
				continue;
			orders.add(order);
		}
		
		XWPFDocument document =
			XWPFUtils.getDocument(docConfig.getTemplatePath() + "/book_request.docx");

		String dateText = DateFormat.getDateInstance().format(requestDate);
		XWPFUtils.replaceTextInDocument(document, "${ДАТА}", dateText);
		
		if (weeklyRequest) {
			Calendar c = Calendar.getInstance();
			c.setTime(requestDate);
			
			Date startDate = DateUtil.firstDayOfLastWeek(c);
			String startDateText = DateFormat.getDateInstance().format(startDate);
			Date endDate = DateUtil.lastDayOfLastWeek(c);
			String endDateText = DateFormat.getDateInstance().format(endDate);
			XWPFUtils.replaceTextInDocument(document, "${ПЕРИОД}", startDateText + " - " + endDateText);
		} else {
			XWPFUtils.replaceTextInDocument(document, "${ПЕРИОД}", "Срок сдачи:\t" + getDeadlineText(requestDate));
		}
		
		ru.imagebook.client.common.service.order.OrderService clientOrderService =
			new ru.imagebook.client.common.service.order.OrderService(coreFactory);

		Map<String, List<Order<?>>> groups = new LinkedHashMap<String, List<Order<?>>>();
		for (Order<?> order : orders) {
			String article = clientOrderService.getOrderArticle(order);
			List<Order<?>> articleOrders = groups.get(article);
			if (articleOrders == null) {
				articleOrders = new ArrayList<Order<?>>();
				groups.put(article, articleOrders);
			}
			articleOrders.add(order);
		}

		XWPFTable table = document.getTables().get(0);
		XWPFTableRow firstRow = table.getRow(1);
		
		XWPFFormat[] formats = XWPFUtils.getFormats(table, firstRow);
		XWPFFormat[] totalFormats = XWPFUtils.getFormats(table, firstRow);
		
		int number = 1;
		int total = 0;
		int totalCost = 0;

		DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");
		for (String article : groups.keySet()) {
			XWPFTableRow r = table.createRow();
			
			List<Order<?>> articleOrders = groups.get(article);
			int groupQuantity = 0;
			int groupCost = 0;
			for (Order<?> order : articleOrders) {
				groupQuantity += order.getQuantity();
				groupCost += order.getPhCost();
			}
			total += groupQuantity;
			totalCost += groupCost;

			Order<?> firstOrder = articleOrders.get(0);

			String[] values = new String[r.getTableCells().size()];
			values[0] = number + "";
			values[1] = article;
			values[2] = "Фотоальбом";
			values[3] = decimalFormat.format(firstOrder.getPhPrice());
			values[4] = groupQuantity + "";
			values[5] = decimalFormat.format(groupCost);

			for (int x = 0; x < r.getTableCells().size(); x++) {
				XWPFUtils.setCellValue(r, x, values[x], formats[x]);
			}
			number++;
		}

        XWPFTableRow r = table.createRow();
        XWPFUtils.setCellValue(r, 2, "ИТОГО:", formats[2]);
        XWPFUtils.setCellValue(r, 4, total + "", formats[4]);
        XWPFUtils.setCellValue(r, 5, decimalFormat.format(totalCost), formats[5]);
        for (int x = 0; x < r.getTableCells().size(); x++) {
        	totalFormats[x].setBold(true);
        	totalFormats[x].setBackgroundColor("CCCCCC");
            XWPFUtils.setCellFormat(r.getCell(x), totalFormats[x]);
        }

		BigDecimal totalCostDecimal = new BigDecimal(totalCost);
		BigDecimal totalVat = new BigDecimal(0.18).multiply(totalCostDecimal)
			.divide(new BigDecimal(1.18), 2, RoundingMode.HALF_UP);

        r = table.createRow();
        XWPFUtils.setCellValue(r, 2, "В т.ч. НДС:", formats[2]);
        XWPFUtils.setCellValue(r, 5, decimalFormat.format(totalVat), formats[5]);
        for (int x = 0; x < r.getTableCells().size(); x++) {
        	totalFormats[x].setBold(true);
        	totalFormats[x].setBackgroundColor("CCCCCC");
            XWPFUtils.setCellFormat(r.getCell(x), totalFormats[x]);
        }

		String id = UUID.randomUUID().toString();
		String path = fileConfig.getTempPath() + "/" + id;
		String fileName = "Заявка_в_бухгалтерию_№" + dateText + "-Б.docx";

		table.removeRow(1);
		XWPFUtils.saveDocument(path, document);
		
		return new TempFile(id, fileName, new File(path));
	}

	@Override
	public TempFile urgentRequest(int requestId) {
		String locale = Locales.RU;

		Request request = repository.getRequest(requestId);

		Set<Order<?>> orders = request.getOrders();
		Map<User, Map<Product, List<Order<?>>>> groups = new HashMap<User, Map<Product, List<Order<?>>>>();
		for (Order<?> order : orders) {
			User user = order.getUser();
			Product product = order.getProduct();

			Map<Product, List<Order<?>>> productMap = groups.get(user);
			if (productMap == null) {
				productMap = new HashMap<Product, List<Order<?>>>();
				groups.put(user, productMap);
			}

			List<Order<?>> productOrders = productMap.get(product);
			if (productOrders == null) {
				productOrders = new ArrayList<Order<?>>();
				productMap.put(product, productOrders);
			}
			productOrders.add(order);
		}

		XWPFDocument document =
			XWPFUtils.getDocument(docConfig.getTemplatePath() + "/urgent_request.docx");
		
		Date date = request.getDate();

		String dateText = DateFormat.getDateInstance().format(date);
		XWPFUtils.replaceTextInDocument(document, "${ДАТА}", dateText);

		XWPFTable table = document.getTables().get(0);
		XWPFTableRow firstRow = table.getRow(1);
		
		XWPFFormat[] formats = XWPFUtils.getFormats(table, firstRow);
		XWPFFormat[] totalFormats = XWPFUtils.getFormats(table, firstRow);
		
		XWPFFormat groupFormat = new XWPFFormat();
		groupFormat.setBold(true);

		DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");
		for (User user : groups.keySet()) {
			// user's active emails
			Collection<String> activeEmails = Lists.newArrayList();
			for (Email email : user.getEmails()) {
				if (email.isActive()) {
					activeEmails.add(email.getEmail());
				}
			}
			String userActiveEmails = !CollectionUtils.isEmpty(activeEmails) ? " ("
					+ StringUtils.join(activeEmails, ",") + ")" : "";
			String userValue = user.getFullName() + userActiveEmails;
			// add user group line
			XWPFTableRow r = table.createRow();
			XWPFUtils.setCellValue(r, 0, userValue, formats[0]);
			XWPFUtils.setCellFormat(r.getCell(r.getTableCells().size() - 1), formats[r.getTableCells().size() - 1]);
	        XWPFUtils.setCellFormat(r.getCell(0), groupFormat);
			XWPFUtils.hMergeCells(r, 0, (r.getTableCells().size() - 1));

			int totalQuantity = 0;
			int totalCost = 0;
			Map<Product, List<Order<?>>> productOrders = groups.get(user);
			for (Product product : productOrders.keySet()) {
				// add product group line
				r = table.createRow();
				XWPFUtils.setCellValue(r, 0, product.getName().get(locale), formats[0]);
				XWPFUtils.setCellFormat(r.getCell(r.getTableCells().size() - 1), formats[r.getTableCells().size() - 1]);
		        XWPFUtils.setCellFormat(r.getCell(0), groupFormat);
				XWPFUtils.hMergeCells(r, 0, (r.getTableCells().size() - 1));

				List<Order<?>> pOrders = productOrders.get(product);
				for (Order<?> order : pOrders) {
					r = table.createRow();
					
					String[] values = new String[r.getTableCells().size()];
					values[0] = order.getNumber();

					// prepayment
					Bill bill = order.getBill();
					if (bill != null && !bill.isAdv()) {
						values[1] = "v";
					}

					// quantity
					int quantity = order.getQuantity();
					totalQuantity += quantity;
					values[2] = quantity + "";

					// cost
					int cost = order.getCost();
                    values[3] = decimalFormat.format(cost);
                    totalCost += cost;

					for (int x = 0; x < r.getTableCells().size(); x++) {
						XWPFUtils.setCellValue(r, x, values[x], formats[x]);
					}
				}
			}

	        r = table.createRow();
	        XWPFUtils.setCellValue(r, 0, "Итого", formats[0]);
	        XWPFUtils.setCellValue(r, 2, totalQuantity + "", formats[2]);
	        XWPFUtils.setCellValue(r, 3, decimalFormat.format(totalCost), formats[3]);
	        for (int x = 0; x < r.getTableCells().size(); x++) {
	        	totalFormats[x].setBold(true);
	        	totalFormats[x].setBackgroundColor("DDDDDD");
	            XWPFUtils.setCellFormat(r.getCell(x), totalFormats[x]);
	        }
		}

		String id = UUID.randomUUID().toString();
		String path = fileConfig.getTempPath() + "/" + id;
		String fileName = "Заявка_по_срочным_заказам_№" + dateText + "-C.docx";

		table.removeRow(1);
		XWPFUtils.saveDocument(path, document);

		return new TempFile(id, fileName, new File(path));
	}

	@Override
	public TempFile act(int requestId) {
		Request request = repository.getRequest(requestId);

		List<Order<?>> orders = new ArrayList<Order<?>>();
		for (Order<?> order : request.getOrders()) {
			Bill bill = order.getBill();
			if (bill != null && bill.isAdv())
				continue;
			orders.add(order);
		}
		
		XWPFDocument document =
			XWPFUtils.getDocument(docConfig.getTemplatePath() + "/act.docx");
		
		Date date = request.getDate();

		String dateText = DateFormat.getDateInstance().format(date);
		XWPFUtils.replaceTextInDocument(document, "${ДАТА}", dateText);
		XWPFUtils.replaceTextInDocument(document, "${ДАТА_СДАЧИ}", getDeadlineText(date));

		XWPFTable table = document.getTables().get(0);
		XWPFTableRow firstRow = table.getRow(1);
		
		XWPFFormat[] formats = XWPFUtils.getFormats(table, firstRow);
		XWPFFormat[] totalFormats = XWPFUtils.getFormats(table, firstRow);

		ru.imagebook.client.common.service.order.OrderService clientOrderService =
			new ru.imagebook.client.common.service.order.OrderService(coreFactory);

		int number = 1;
		int total = 0;
		int totalCost = 0;

		DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");
		for (Order<?> order : orders) {
			XWPFTableRow r = table.createRow();
			
			String[] values = new String[r.getTableCells().size()];
			values[0] = number + "";
			values[1] = order.getNumber();
			values[2] = clientOrderService.getOrderArticle(order);
			values[3] = "Фотоальбом";
			values[4] = decimalFormat.format(order.getPhPrice());

			int quantity = order.getQuantity();
            total += quantity;
            values[5] = quantity + "";

			int cost = order.getPhCost();
            values[6] = decimalFormat.format(cost);
            totalCost += cost;

			for (int x = 0; x < r.getTableCells().size(); x++) {
				XWPFUtils.setCellValue(r, x, values[x], formats[x]);
			}
			number++;
		}

        XWPFTableRow r = table.createRow();
        XWPFUtils.setCellValue(r, 3, "ИТОГО:", formats[3]);
        XWPFUtils.setCellValue(r, 5, total + "", formats[5]);
        XWPFUtils.setCellValue(r, 6, decimalFormat.format(totalCost), formats[6]);
        for (int x = 0; x < r.getTableCells().size(); x++) {
            totalFormats[x].setBold(true);
        	totalFormats[x].setBackgroundColor("CCCCCC");
            XWPFUtils.setCellFormat(r.getCell(x), totalFormats[x]);
        }

		BigDecimal totalCostDecimal = new BigDecimal(totalCost);
		BigDecimal totalVat = new BigDecimal(0.18).multiply(totalCostDecimal)
			.divide(new BigDecimal(1.18), 2, RoundingMode.HALF_UP);

        r = table.createRow();
        XWPFUtils.setCellValue(r, 3, "В т.ч. НДС:", formats[3]);
        XWPFUtils.setCellValue(r, 6, decimalFormat.format(totalVat), formats[6]);
        for (int x = 0; x < r.getTableCells().size(); x++) {
        	totalFormats[x].setBold(true);
        	totalFormats[x].setBackgroundColor("CCCCCC");
            XWPFUtils.setCellFormat(r.getCell(x), totalFormats[x]);
        }

		XWPFUtils.replaceTextInDocument(document, "${ВСЕГО}", decimalFormat.format(totalCost));
		MoneyFormat moneyFormat = new MoneyFormat();
		XWPFUtils.replaceTextInDocument(document, "${ВСЕГО_ПРОПИСЬЮ}", moneyFormat.format(totalCost));
		XWPFUtils.replaceTextInDocument(document, "${НДС}", decimalFormat.format(totalVat));

		String id = UUID.randomUUID().toString();
		String path = fileConfig.getTempPath() + "/" + id;
		String fileName = "Акт_сдачи-приемки_работ_№" + dateText + ".docx";

		table.removeRow(1);
		XWPFUtils.saveDocument(path, document);

		return new TempFile(id, fileName, new File(path));
	}

	@Override
	public void closeRequests(List<Integer> requestIds) {
		List<Request> requests = repository.loadRequests(requestIds);
		for (Request request : requests) {
			if (request.getState() == RequestState.REQUEST)
				request.setState(RequestState.ACT);
		}
	}

	@Override
	public TempFile delivery(int requestId) {
		String locale = Locales.RU;
		Locale xLocale = new Locale(locale);

		Request request = repository.getRequest(requestId);

		Set<Order<?>> orders = request.getOrders();
		Map<User, List<Order<?>>> groups = new LinkedHashMap<User, List<Order<?>>>();
		for (Order<?> order : orders) {
			User user = order.getUser();
			List<Order<?>> userOrders = groups.get(user);
			if (userOrders == null) {
				userOrders = new ArrayList<Order<?>>();
				groups.put(user, userOrders);
			}
			userOrders.add(order);
		}

		XWPFDocument document =
			XWPFUtils.getDocument(docConfig.getTemplatePath() + "/delivery_request.docx");
		
		Date date = request.getDate();

		String dateText = DateFormat.getDateInstance().format(date);
		XWPFUtils.replaceTextInDocument(document, "${ДАТА}", dateText);

		XWPFTable table = document.getTables().get(0);
		XWPFTableRow firstRow = table.getRow(1);
		
		XWPFFormat[] formats = XWPFUtils.getFormats(table, firstRow);
		XWPFFormat[] totalFormats = XWPFUtils.getFormats(table, firstRow);

		Formatter formatter = coreFactory.createFormatter();

		int number = 1;
		int total = 0;
		for (User user : groups.keySet()) {
			List<Order<?>> group = groups.get(user);
			int subtotal = 0;
			for (Order<?> order : group) {
				XWPFTableRow r = table.createRow();
				
				Product product = order.getProduct();
				Integer type = product.getType();

				String[] values = new String[r.getTableCells().size()];
				values[0] = number + "";
				values[1] = order.getNumber();
				values[2] = "01-" + formatter.n2(type) + "-"
						+ formatter.n2(product.getNumber());
				values[3] = product.getName().get(locale);

				int quantity = order.getQuantity();
                subtotal += quantity;
                total += quantity;
                values[4] = quantity + "";

				values[5] = product.getBlockFormat();
				values[6] = Binding.values.get(product.getBinding()).get(locale);

				String cover;
				if (type == 1 || type == 2 || type == 4 || type == 5 || type == 8) {
					cover = Cover.values.get(product.getCover()).get(locale);
			 	} else {
					cover = order.getColor().getName().get(locale);
				}	
				
				values[7] = cover;
				values[8] = Paper.values.get(product.getPaper()).get(locale);
				values[9] = order.getPageCount() + "";
				values[10] = CoverLamination.values.get(order.getCoverLamination()).get(locale);
				values[11] = PageLamination.values.get(order.getPageLamination()).get(locale);

				Address address = order.getAddress();
				if (address != null) {
					Compiler compiler = coreFactory.createCompiler();
					compiler.registerFunction("implode", new ImplodeFunction());
					compiler.registerFunction("prefix", new PrefixFunction());
					String addressTemplate = messages.getMessage("addressTemplate", null,
							xLocale);
					values[12] = compiler.compile(addressTemplate, address);
				}

				for (int x = 0; x < r.getTableCells().size(); x++) {
					XWPFUtils.setCellValue(r, x, values[x], formats[x]);
				}
				number++;
			}
			
	        XWPFTableRow r = table.createRow();
	        XWPFUtils.setCellValue(r, 3, "ИТОГО:", formats[3]);
	        XWPFUtils.setCellValue(r, 4, subtotal + "", formats[4]);
	        for (int x = 0; x < r.getTableCells().size(); x++) {
	        	totalFormats[x].setBold(true);
	        	totalFormats[x].setBackgroundColor("DDDDDD");
	            XWPFUtils.setCellFormat(r.getCell(x), totalFormats[x]);
	        }
		}

		XWPFTableRow r = table.createRow();
        XWPFUtils.setCellValue(r, 3, "ВСЕГО:", formats[3]);
        XWPFUtils.setCellValue(r, 4, total + "", formats[4]);
        for (int x = 0; x < r.getTableCells().size(); x++) {
        	totalFormats[x].setBold(true);
        	totalFormats[x].setBackgroundColor("CCCCCC");
            XWPFUtils.setCellFormat(r.getCell(x), totalFormats[x]);
        }

		String id = UUID.randomUUID().toString();
		String path = fileConfig.getTempPath() + "/" + id;
		String fileName = "Заявка_на_доставку_№" + dateText + "-Д.docx";

		table.removeRow(1);
		XWPFUtils.saveDocument(path, document);

		return new TempFile(id, fileName, new File(path));
	}
	
    private String getDeadlineText(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date deadline = calendar.getTime();
        return DateFormat.getDateInstance().format(deadline);
    }

	@Override
	public void updateRequest(Request modified) {
		Request request = repository.getRequest(modified.getId());
		request.setUrgent(modified.isUrgent());
		request.setPaid(modified.isPaid());
		request.setConfirmed(modified.isConfirmed());
		request.setBillNumber(modified.getBillNumber());
		request.setBillDate(modified.getBillDate());

		for (Order<?> xOrder : modified.getOrders()) {
			if (!request.getOrders().contains(xOrder)) {
				Order<?> order = orderService.getOrder(xOrder.getId());
				order.setState(OrderState.PRINTING);
				order.setRequest(request);
				request.addOrder(order);
			}
		}

		Iterator<Order<?>> iterator = request.getOrders().iterator();
		while (iterator.hasNext()) {
			Order<?> order = iterator.next();
			if (!modified.getOrders().contains(order)) {
				order.setRequest(null);
				order.setState(OrderState.PRINTING);
				iterator.remove();
			}
		}

		setRequestTotals(request);
	}

	private void setRequestTotals(Request request) {
		int total = 0;
		int total2 = 0;
		for (Order<?> order : request.getOrders()) {
			Bill bill = order.getBill();
			if (bill != null && bill.isAdv())
				total2 += order.getPhCost();
			else
				total += order.getPhCost();
		}
		request.setTotal(total);
		request.setTotal2(total2);
	}
	
	@Override
	@Transactional
	public void sendWeeklyBookRequest() {
		LOGGER.debug("Request service started.");

		List<Order<?>> orders = repository.loadLastWeekOrders();
//		removeOrdersWithNullableQuantity(orders);
		LOGGER.debug("Orders loaded = " + orders.size());
		
		if (CollectionUtils.isEmpty(orders)) {
			LOGGER.debug("Skip book request generation: no orders found for last week.");
			return;
		}
		TempFile bookReq = bookRequest(new Date(), orders, true);
		LOGGER.debug("Book request generated");
		
		// request notification
		Locale locale = new Locale(Locales.RU);
		Date date = new Date();
		String dateText = DateFormat.getDateInstance().format(date);
		Vendor mainVendor = vendorService.getMainVendor();

		LOGGER.debug("Sending e-mails");
		try {
		    if (bookReq != null) {
    			bookSubjectNotification(bookReq, dateText, mainVendor, locale);
    			LOGGER.debug("Sent book request e-mails");
		    }	
		} catch (Exception e) {
            LOGGER.debug("Sending book request e-mails failed");
			ServiceLogger.log(e);
		} finally {
			if (bookReq != null && bookReq.getFile() != null) {
				bookReq.getFile().delete();
			}
		}
		LOGGER.debug("Request service done.");
	}	

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void generateAndSendRequests() {
		LOGGER.debug("Request service started.");

		Set<Order<?>> commonOrders = Sets.newHashSet();
		Set<Order<?>> urgentOrders = Sets.newHashSet();

		List<Order<?>> orders = repository.loadTodaysPrintingOrders();
        if (CollectionUtils.isEmpty(orders)) {
            LOGGER.debug("Skip daily requests generation: no orders found");
            return;
        }
		
		for (Order<?> order : orders) {
			if (order.isUrgent()) {
				urgentOrders.add(order);
			} else {
			    commonOrders.add(order);
			}
		}

		int maxNumber = repository.getMaxRequestNumber();
		LOGGER.debug("Request max number: " + maxNumber);
		
		Date requestDate = DateUtils.truncate(new Date(), Calendar.HOUR);

		// generate common request
		Request commonRequest = null;
		TempFile printCommonReq = null;
		if (!CollectionUtils.isEmpty(commonOrders)) {
			commonRequest = generateRequest(maxNumber++, false, requestDate, commonOrders);
			printCommonReq = printRequest(commonRequest.getId());
			LOGGER.debug("Common request generated");
		}

		// generate urgent request
		Request urgentRequest = null;
		TempFile printUrgentReq = null;
		TempFile urgentReq = null;
		if (!CollectionUtils.isEmpty(urgentOrders)) {
			urgentRequest = generateRequest(maxNumber, true, requestDate, urgentOrders);
			printUrgentReq = printRequest(urgentRequest.getId());
			urgentReq = urgentRequest(urgentRequest.getId());
			LOGGER.debug("Urgent request generated");
		}

		// bookReq generation
		TempFile bookReq = null;
		if (!CollectionUtils.isEmpty(orders)) {
			bookReq = bookRequest(new Date(), orders, false);
			LOGGER.debug("Book request generated");
		}

		// request notification
		Locale locale = new Locale(Locales.RU);
		String dateText = DateFormat.getDateInstance().format(new Date());
		Vendor mainVendor = vendorService.getMainVendor();

		LOGGER.debug("Sending e-mails");
		try {
			if (printCommonReq != null || printUrgentReq != null) {
				printRequestNotification(printCommonReq, printUrgentReq, dateText, mainVendor, locale);
				LOGGER.debug("Sent print request e-mails");
			}

			int requestTotal = (commonRequest != null ? commonRequest.getTotal() : 0)
					+ (urgentRequest != null ? urgentRequest.getTotal() : 0);
			if (requestTotal > 0) {
				bookSubjectNotification(bookReq, dateText, mainVendor, locale);
				LOGGER.debug("Sent book request e-mails");
			}

			if (urgentRequest != null) {
				urgentRequestNotification(urgentReq, dateText, mainVendor, locale);
				LOGGER.debug("Sent urgent request e-mails");
			}
		} catch (Exception e) {
            LOGGER.debug("Sending request e-mails failed");
            ServiceLogger.log(e);
        } finally {
			if (commonRequest != null) {
				printCommonReq.getFile().delete();
			}
			if (urgentRequest != null) {
				printUrgentReq.getFile().delete();
				urgentReq.getFile().delete();
			}
			if (bookReq != null && bookReq.getFile() != null) {
				bookReq.getFile().delete();
			}
		}
		LOGGER.debug("Request service done.");
	}

	private Request generateRequest(int number, boolean urgentFlag, Date requestDate,
	                                Set<Order<?>> orders) {
		Request request = new Request();
		request.setNumber(number);
		request.setDate(requestDate);
		request.setUrgent(urgentFlag);
		for (Order<?> order : orders) {
			request.addOrder(order);
		}
		repository.saveRequest(request);
		setRequestTotals(request);

		return request;
	}

    private void printRequestNotification(TempFile printCommonRequest, TempFile printUrgentRequest,
		                                 String dateText, Vendor vendor, Locale locale) {
		String subject = messages.getMessage("requestSubject", new Object[] { dateText }, locale);
		Collection<String> recipients = StringUtil.split(config.getPrintRecipients(), ",");
        List<TempFile> files = Arrays.asList(printCommonRequest, printUrgentRequest);
        requestNotification(subject, vendor, recipients, files);
	}

    private void bookSubjectNotification(TempFile bookRequest, String dateText, Vendor vendor, Locale locale) {
		String subject = messages.getMessage("bookSubject", new Object[] { dateText }, locale);
		Collection<String> recipients = StringUtil.split(config.getBookRecipients(), ",");
        List<TempFile> files = Collections.singletonList(bookRequest);
        requestNotification(subject, vendor, recipients, files);
	}

    private void urgentRequestNotification(TempFile urgentRequest, String dateText, Vendor vendor, Locale locale) {
		String subject = messages.getMessage("urgentSubject", new Object[] { dateText }, locale);
		Collection<String> recipients = StringUtil.split(config.getUrgentRecipients(), ",");
        List<TempFile> files = Collections.singletonList(urgentRequest);
        requestNotification(subject, vendor, recipients, files);
	}

    private void requestNotification(String subject, Vendor vendor, Collection<String> recipients,
                                     List<TempFile> tempFiles) {
        try {
            JavaMailSender mailSender = mailSenderLocator.getMailSender(vendor);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

            messageHelper.setFrom(vendor.getEmail());
            messageHelper.setSubject(subject);
            messageHelper.setText(subject);

            for (TempFile tempFile : tempFiles) {
                if (tempFile != null) {
                    messageHelper.addAttachment(tempFile.getName(), tempFile.getFile());
                }
            }

            for (String email : recipients) {
                messageHelper.setTo(email);
                mailSender.send(message);
            }
        } catch (Exception e) {
            ServiceLogger.log(e);
        }
    }
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void exportLastRequestOrdersToXml() {
	    List<Order<?>> orders = repository.loadLastRequestOrders();
        exportQrdersToXml(orders);
	}
	
	private void exportQrdersToXml(List<Order<?>> orders) {
	    if (CollectionUtils.isEmpty(orders)) {
	        LOGGER.debug("Skip export to XML: no orders to export");
	        return;
	    }
	    
        XFtpClient ftpClient = ftpClientFactory.createClient();
        TempFile xmlFile = null;
        try {
            ftpClient.connect(printEffectConfig.getHost(), printEffectConfig.getUsername(),
                printEffectConfig.getPassword());
            
            ftpClient.mkdir(printEffectConfig.getPath());
            ftpClient.cd(printEffectConfig.getPath());
            
            OrderExpCollection orderCollection = prepareOrdersToExport(orders);
            xmlFile = generateOrdersXml(orderCollection);

            String tempFilePath = xmlFile.getFile().getPath();
            ftpClient.saveFile(tempFilePath, xmlFile.getId());
            ftpClient.deleteFileIfExists(xmlFile.getName());
            ftpClient.rename(xmlFile.getId(), xmlFile.getName());
        } catch (Exception ex) {
            Exceptions.rethrow(ex);
        } finally {
            ftpClient.disconnect();
            if (xmlFile != null && xmlFile.getFile() != null) {
                xmlFile.getFile().delete();
            }
        }
	}
	
	private OrderExpCollection prepareOrdersToExport(List<Order<?>> orders) {
	    String locale = Locales.RU;
	    
	    ru.imagebook.client.common.service.order.OrderService clientOrderService =
	        new ru.imagebook.client.common.service.order.OrderService(coreFactory);
	    
	    DateFormat dateInstance = DateFormat.getDateInstance();
	    
	    List<OrderExpItem> ordersToExport = new ArrayList<OrderExpItem>(orders.size());
        for (Order<?> order : orders) {
            OrderExpItem orderExp = new OrderExpItem();
            orderExp.setNumber(order.getNumber());
            orderExp.setArticle(clientOrderService.getProductArticle(order.getProduct()));
            orderExp.setCopies(order.getQuantity());
            orderExp.setnPages(order.getPageCount());
            
            Date deadlineDate = order.getPrintDate();
            if (!order.isUrgent()) {
                DateUtils.addDays(deadlineDate, 1);
            }
            orderExp.setDeadline(dateInstance.format(deadlineDate));
            
            String pageLamination = PageLamination.values.get(order.getPageLamination()).get(locale); 
            orderExp.setPageLamination(pageLamination);
            
            String coverLamination = CoverLamination.values.get(order.getCoverLamination()).get(locale);
            orderExp.setCoverLamination(coverLamination);
            
            String coverName = CoverUtil.getCoverName((AlbumOrder) order, locale);
            orderExp.setCoverType(coverName);
            ordersToExport.add(orderExp);
        }        
        OrderExpCollection orderCollection = new OrderExpCollection();
        orderCollection.setOrders(ordersToExport);
        return orderCollection;
	}
	
	private TempFile generateOrdersXml(OrderExpCollection orderCollection) throws JAXBException {
	    DateFormat df = new SimpleDateFormat(DATE_FORMAT);
	    String dateTimeText = df.format(new Date());
	    
        JAXBContext jaxbContext = JAXBContext.newInstance(OrderExpCollection.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        
        String id = UUID.randomUUID().toString();
        String path = fileConfig.getTempPath() + "/" + id;
        String filename = "orders_" + dateTimeText + ".xml";
        File outFile = new File(path);
        jaxbMarshaller.marshal(orderCollection, outFile);
        
        return new TempFile(id, filename, outFile);
	}
}