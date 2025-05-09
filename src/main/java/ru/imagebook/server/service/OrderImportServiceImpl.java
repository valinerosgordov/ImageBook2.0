package ru.imagebook.server.service;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ru.imagebook.server.repository.OrderRepository;
import ru.imagebook.server.repository.ProductRepository;
import ru.imagebook.server.repository.UserRepository;
import ru.imagebook.server.service.flash.FlashConfig;
import ru.imagebook.server.service.flash.FlashPath;
import ru.imagebook.server.service.flash.FlashServiceImpl;
import ru.imagebook.server.servlet.orderImport.ImportErrorCodes;
import ru.imagebook.server.servlet.orderImport.ImportParams;
import ru.imagebook.server.tools.XmlUtils;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.AlbumOrder;
import ru.imagebook.shared.model.AlbumOrderImpl;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.InvitationState;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderState;
import ru.imagebook.shared.model.OrderType;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.service.admin.order.OrderImportException;
import ru.imagebook.shared.service.admin.vendor.VendorAuthException;
import ru.minogin.core.client.collections.XArrayList;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.client.text.StringUtil;
import ru.minogin.core.server.ServiceLogger;
import ru.minogin.core.server.ftp.XFtpClient;

public class OrderImportServiceImpl implements OrderImportService {
	private static Logger logger = Logger.getLogger(FlashServiceImpl.class);

	private static final int JPEG_FILE_WRITE_TIME_SEC = 3 * 60;
	private static final String COVER_JPEG_FILE_NAME = "c.jpg";

	private static final String INITIAL_EXTERNAL_ORDER_NUMBER = "30000000";

	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	private final OrderService orderService;
	private final UserService userService;
	private final VendorService vendorService;

	@Autowired
	private MessageSource messages;

	private FlashPath flashPath;

	public OrderImportServiceImpl(UserRepository userRepository,
			ProductRepository productRepository, OrderRepository orderRepository,
			OrderService orderService, UserService userService,
			VendorService vendorService, FlashConfig config) {
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.orderRepository = orderRepository;
		this.orderService = orderService;
		this.userService = userService;
		this.vendorService = vendorService;
		this.flashPath = new FlashPath(config);
	}

	@Transactional
	@Override
	public Vendor authenticateVendor(Map<ImportParams, String> paramsMap) throws OrderImportException {
		String vendorKey = paramsMap.get(ImportParams.VENDOR_KEY);
		String vendorPwd = paramsMap.get(ImportParams.VENDOR_PWD);

		try {
			return vendorService.authenticateVendor(vendorKey, vendorPwd);
		}
		catch (VendorAuthException aex) {
			throw new OrderImportException(ImportErrorCodes.ERR002, null);
		}
	}

	@Override
	public void parseAndValidateXmlReq(InputStream is, Map<ImportParams, String> paramsMap)
			throws OrderImportException {
		parseXmlReq(is, paramsMap);
		validateReqParams(paramsMap);
	}

	private void parseXmlReq(InputStream is, Map<ImportParams, String> paramsMap)
			throws OrderImportException {
		try {
			Document xml = XmlUtils.parseXml(IOUtils.toByteArray(is));

			Map<String, List<ImportParams>> nodesMap = ImportParams.getGroupedParamsMap();
			for (String nodeName : nodesMap.keySet()) {
				NodeList list = xml.getElementsByTagName(nodeName);
				if (list != null && list.getLength() > 0) {
					for (ImportParams param : nodesMap.get(nodeName)) {
						String nodeValue = XmlUtils.getTagValue(param.getValue(), (Element) list.item(0));
						if (nodeValue == null) {
							throw new OrderImportException(ImportErrorCodes.ERR004, param.getValue());
						}
						paramsMap.put(param, StringUtil.trim(nodeValue));
					}
				}
				else {
					throw new OrderImportException(ImportErrorCodes.ERR004, nodeName);
				}
			}
		}
		catch (OrderImportException iex) {
			throw iex;
		}
		catch (Exception ex) {
			throw new OrderImportException(ImportErrorCodes.ERR003, null);
		}
	}

	private void validateReqParams(Map<ImportParams, String> paramsMap) throws OrderImportException {
		// check vendorKey and vendorPwd
		if (StringUtils.isEmpty(paramsMap.get(ImportParams.VENDOR_KEY))
				|| StringUtils.isEmpty(paramsMap.get(ImportParams.VENDOR_PWD))) {
			throw new OrderImportException(ImportErrorCodes.ERR001, null);
		}

		// parameter has empty value
		for (ImportParams param : paramsMap.keySet()) {
			String paramValue = paramsMap.get(param);
			if (!param.isNullable() && StringUtils.isEmpty(paramValue)) {
				throw new OrderImportException(ImportErrorCodes.ERR005, param.getValue());
			}
		}

		// parameter has incorrect type
		isNumeric(paramsMap, ImportParams.PRODUCT_PRICE);
		isNumeric(paramsMap, ImportParams.PRODUCT_PRICE);
	}

	private void isNumeric(Map<ImportParams, String> paramsMap, ImportParams importParameter) {
		if (!StringUtils.isNumeric(paramsMap.get(importParameter))) {
			throw new OrderImportException(ImportErrorCodes.ERR005, importParameter.getValue());
		}
	}

	@Transactional
	@Override
	public void loadOrder(Vendor vendor, Map<ImportParams, String> paramsMap)
			throws OrderImportException {
		int productType = Integer.parseInt(paramsMap.get(ImportParams.PRODUCT_TYPE));
		int productNumber = Integer.parseInt(paramsMap.get(ImportParams.PRODUCT_NUMBER));

		Album album = productRepository.getAlbumByProductTypeAndNumber(productType, productNumber);
		if (album == null) {
			throw new OrderImportException(ImportErrorCodes.ERR007, null);
		}
		AlbumOrder order = new AlbumOrderImpl(album);

		Map<Integer, Color> colors = new HashMap<Integer, Color>();
		List<Color> colorsList = productRepository.loadColors();
		for (Color color : colorsList) {
			colors.put(color.getNumber(), color);
		}

		// Number - 30 ******, external orders are numbered sequence starting with
// 30000000;
		String number = orderRepository.getLastOrderNumberByType(OrderType.EXTERNAL);
		number = StringUtils.isEmpty(number) ? INITIAL_EXTERNAL_ORDER_NUMBER : String.valueOf(Integer
				.parseInt(number) + 1);
		order.setNumber(number);

		String userName = paramsMap.get(ImportParams.USER_LOGIN);
		boolean newUser = false;

		User user = userRepository.getUser(userName, vendor);
		String userEmail = paramsMap.get(ImportParams.USER_EMAIL);
		if (user == null) {
			newUser = true;

			user = new User();
			user.setActive(true);
			user.setUserName(userName);

			String password = paramsMap.get(ImportParams.USER_PWD);
			user.setPassword(password);

			String name = paramsMap.get(ImportParams.USER_NAME);
			user.setName(name);

			user.getFirstEmail().setEmail(userEmail);
			user.getFirstEmail().setActive(true);

			user.setVendor(vendor);
			user.setInvitationState(InvitationState.CONFIRMED);
			user.setInfo(messages.getMessage("createdByExternalRequest", null, new Locale(Locales.RU)));

			try {
				userService.addUser(user);
				userService.updateAccount(user);
			}
			catch (Exception ex) {
				throw new OrderImportException(ImportErrorCodes.ERR006, null);
			}
		}
		order.setUser(user);
		order.setUrgent(user.isUrgentOrders());

		try {
			int colorNumber = album.getColorRange().get(0);
			order.setColor(colors.get(colorNumber));
		}
		catch (NullPointerException e) {
			ServiceLogger.warn("COLOR RANGE PROBLEM: " + number);
			throw e;
		}

		List<Integer> coverLamRange = album.getCoverLamRange();
		order.setCoverLamination(coverLamRange.get(0));

		List<Integer> pageLamRange = album.getPageLamRange();
		order.setPageLamination(pageLamRange.get(0));

		order.setState(OrderState.FLASH_GENERATION);
		order.setType(OrderType.EXTERNAL);

		int pagesCount = Integer.parseInt(paramsMap.get(ImportParams.PRODUCT_NPAGES));
		order.setPageCount(pagesCount);

		Integer specialPrice = new Integer(paramsMap.get(ImportParams.PRODUCT_PRICE));
		order.setSpecialPrice(specialPrice);

		orderService.setFreeIfIsFirstTrial(order);

		try {
			orderService.addOrder(order);
		}
		catch (Exception ex) {
			throw new OrderImportException(ImportErrorCodes.ERR011, null);
		}

		downloadJpegs(order, paramsMap);

		if (newUser) {
			userService.sendRegistrationMail(user, userEmail);
			orderService.notifyNewUserOrder(order.getId());
		}
		else {
			orderService.notifyNewOrder(new XArrayList<Integer>(order.getId()));
		}
		orderService.notifyAdmin(order);
	}

	private void downloadJpegs(Order<?> order, Map<ImportParams, String> paramsMap)
			throws OrderImportException {
		logger.debug("Downloading JPEGs: " + order.getNumber());

		Album album = (Album) order.getProduct();
		int lastPage = order.getPageCount();

		XFtpClient client = new XFtpClient();
		try {
			// connect
			try {
				logger.debug("Connecting");
				String ftpHost = paramsMap.get(ImportParams.FTP_HOST);
				String ftpUser = paramsMap.get(ImportParams.FTP_USER);
				String ftpPws = paramsMap.get(ImportParams.FTP_PWD);

				client.connect(ftpHost, ftpUser, ftpPws);
				logger.debug("Connected");
			}
			catch (Exception ex) {
				throw new OrderImportException(ImportErrorCodes.ERR008, null);
			}

			// Check
			logger.debug("Checking: " + order.getNumber());
			String ftpFolderPath = paramsMap.get(ImportParams.FTP_FOLDER_PATH);
			client.cd(ftpFolderPath);
			for (int page = 1; page <= lastPage; page++) {
				String fileName = getFileName(page);
				if (!client.isFileReady(fileName, JPEG_FILE_WRITE_TIME_SEC)) {
					throw new OrderImportException(ImportErrorCodes.ERR009, fileName);
				}
			}
			if (album.isSeparateCover()) {
				if (!client.isFileReady(COVER_JPEG_FILE_NAME, JPEG_FILE_WRITE_TIME_SEC)) {
					throw new OrderImportException(ImportErrorCodes.ERR009, COVER_JPEG_FILE_NAME);
				}
			}

			// Download
			try {
				logger.debug("Downloading: " + order.getNumber());
				String jpegDir = flashPath.getJpegDir(order);
				new File(jpegDir).mkdirs();
				for (int page = 1; page <= lastPage; page++) {
					String fileName = getFileName(page);
					String jpegPath = getJpegPath(order, page);
					client.loadFile(fileName, jpegPath);
				}
				if (album.isSeparateCover()) {
					client.loadFile(COVER_JPEG_FILE_NAME, getJpegCoverPath(order));
				}
			}
			catch (Exception ex) {
				throw new OrderImportException(ImportErrorCodes.ERR010, null);
			}
		}
		finally {
			client.disconnect();
		}

		logger.debug("JPEGs Downloaded: " + order.getNumber());
	}

	private String getJpegPath(Order<?> order, int page) {
		return flashPath.getJpegDir(order) + "/" + page + ".jpg";
	}

	private String getFileName(int page) {
		return page + ".jpg";
	}

	private String getJpegCoverPath(Order<?> order) {
		return flashPath.getJpegDir(order) + "/" + "c.jpg";
	}
}
