package ru.imagebook.server.service.load;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.server.repository.LoadRepository;
import ru.imagebook.server.repository.ProductRepository;
import ru.imagebook.server.repository.UserRepository;
import ru.imagebook.server.service.OrderService;
import ru.imagebook.server.service.UserService;
import ru.imagebook.server.service.VendorService;
import ru.imagebook.server.service.notify.NotifyService;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.AlbumOrder;
import ru.imagebook.shared.model.AlbumOrderImpl;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.Email;
import ru.imagebook.shared.model.InvitationState;
import ru.imagebook.shared.model.OrderState;
import ru.imagebook.shared.model.OrderType;
import ru.imagebook.shared.model.Phone;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.collections.XArrayList;
import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.client.security.PasswordGenerator;
import ru.minogin.core.client.text.StringUtil;
import ru.minogin.core.server.ServiceLogger;
import ru.minogin.core.server.ftp.XFtpClient;

public class LoadTxServiceImpl implements LoadTxService {
	public static final Logger LOGGER = Logger.getLogger(LoadTxServiceImpl.class);

	private static final String PHONE = "PHONE";
	private static final String CELLULAR = "CELLULAR";
	private static final String LAST_NAME = "LAST_NAME";
	private static final String FIRST_NAME = "FIRST_NAME";
	private static final String PRODUCT_DETAILS = "PRODUCT_DETAILS";
	private static final String EMAIL = "EMAIL";
	private static final String PAGE_COUNT = "PAGE_COUNT";
	private static final String CUSTOMER_ID = "CUSTOMER_ID";
	private static final String COMMENT_KEY = "COMMENT = ";
	private static final String FINISHING_KEY = "FINISHING = ";
	private static final String COLOR_CORRECT_KEY = "COLOR_CORRECT = ";
	private static final int INFO_FILE_WRITE_TIME_SEC = 60;

	private final LoadConfig config;
	private final LoadRepository repository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final Map<Integer, Map<Integer, Album>> albums = new HashMap<Integer, Map<Integer, Album>>();
	private final Map<Integer, Color> colors = new HashMap<Integer, Color>();
	private final OrderService orderService;
	private final CoreFactory coreFactory;
	private final UserService userService;
	private final VendorService vendorService;
	private final NotifyService notifyService;

	public LoadTxServiceImpl(LoadConfig config, LoadRepository repository, UserRepository userRepository,
			ProductRepository productRepository, OrderService orderService, CoreFactory coreFactory, UserService userService,
			VendorService agentService, NotifyService notifyService) {
		this.config = config;
		this.repository = repository;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
		this.orderService = orderService;
		this.coreFactory = coreFactory;
		this.userService = userService;
		this.vendorService = agentService;
		this.notifyService = notifyService;
	}

	@Transactional
	@Override
	public void init() {
		List<Album> albumsList = productRepository.loadAlbums();
		for (Album album : albumsList) {
			Integer type = album.getType();
			Map<Integer, Album> typeAlbums = albums.get(type);
			if (typeAlbums == null) {
				typeAlbums = new HashMap<Integer, Album>();
				albums.put(type, typeAlbums);
			}
			typeAlbums.put(album.getNumber(), album);
		}

		List<Color> colorsList = productRepository.loadColors();
		for (Color color : colorsList) {
			colors.put(color.getNumber(), color);
		}
	}

	@Transactional
	@Override
	public void load() {
		XFtpClient client = new XFtpClient();
		try {
			client.connect(config.getHost(), config.getUser(), config.getPassword());

			FTPFile files[] = client.listFiles();
			Collection<String> numbers = new ArrayList<String>();
			for (FTPFile file : files) {
				if (file.isDirectory())
					numbers.add(file.getName());
			}
			List<String> existingNumbers = repository.loadExistingNumbers(numbers);
			numbers.removeAll(existingNumbers);

			for (String number : numbers) {
				try {
					client.cd(number);
					try {
						loadOrder(client, number);
					}
					finally {
						client.cd("..");
					}
				}
				catch (Exception e) {
					ServiceLogger.log(e);
				}
			}
		}
		finally {
			client.disconnect();
		}
	}

	private void loadOrder(XFtpClient client, String number) {
		String fileName = number + ".txt";
		if (!client.isFileReady(fileName, INFO_FILE_WRITE_TIME_SEC))
			return;

		String info = client.getFileContent(fileName);

		info = info.substring(1); // Removing byte output mode character
		Properties properties = new Properties();
		try {
			properties.load(new StringReader(info));
		}
		catch (IOException e) {
			Exceptions.rethrow(e);
		}

		String productText = properties.getProperty(PRODUCT_DETAILS);
		int i1 = productText.indexOf("(");
		int i2 = productText.indexOf(")");
		String articleText = productText.substring(i1 + 1, i2);
		String parts[] = articleText.split("-");
		int xClass = new Integer(parts[0]);
		if (xClass != 1)
			throw new RuntimeException("Class should be 1, but it is: " + xClass);
		int type = new Integer(parts[1]);
		int productNumber = new Integer(parts[2]);

		// TODO [mphoto workaround] remove after 21.09.2011
		if (type == 8 && productNumber == 99)
			type = 9;
		if (type == 1 && productNumber == 2)
			productNumber = 8;
		if (type == 2 && productNumber == 2)
			productNumber = 8;

		Map<Integer, Album> typeAlbums = albums.get(type);
		Album album = typeAlbums.get(productNumber);
		AlbumOrder order = new AlbumOrderImpl(album);
		order.setType(OrderType.MPHOTO);
		order.setNumber(number);

		String userName = StringUtil.trim(properties.getProperty(EMAIL));
		if (userName == null)
			throw new RuntimeException("User name not specified.");
		boolean newUser = false;

		String customerId = StringUtil.trim(properties.getProperty(CUSTOMER_ID));
		Vendor vendor;
		if (customerId != null) {
			vendor = vendorService.getVendorByCustomerId(customerId);
			if (vendor == null)
				throw new RuntimeException("No vendor with customer id: " + customerId);
		}
		else {
			vendor = vendorService.getMainVendor();
		}

		User user = userRepository.getUser(userName, vendor);
		if (user == null) {
			newUser = true;

			user = new User();
			user.setActive(true);
			user.setUserName(userName);
			PasswordGenerator passwordGenerator = coreFactory.createPasswordGenerator();
			user.setPassword(passwordGenerator.generate());

			String name = StringUtil.trim(properties.getProperty(FIRST_NAME));
			user.setName(name);

			String lastName = StringUtil.trim(properties.getProperty(LAST_NAME));
			user.setLastName(lastName);

			if (user.getEmails().isEmpty()) {
				user.addEmail(new Email(null, false));
			}
			user.getFirstEmail().setEmail(userName);
			user.getFirstEmail().setActive(true);

			String phoneText = StringUtil.trim(properties.getProperty(PHONE));
			if (phoneText != null)
				user.addPhone(new Phone(phoneText));

			String cellularText = StringUtil.trim(properties.getProperty(CELLULAR));
			if (cellularText != null)
				user.addPhone(new Phone(cellularText));

			user.setVendor(vendor);
			user.setInvitationState(InvitationState.CONFIRMED);

			userService.addUser(user);
			userService.updateAccount(user);
		}
		order.setUser(user);
		order.setUrgent(user.isUrgentOrders());

		if (isOldVersion(client) && !album.isTrialAlbum()) {
			order.setState(OrderState.OLD_VERSION);

			String subject = "Заказ из старой версии";
			String text = "Получен заказ № " + number + " из старой версии программы.\n";
			text += "1. Обработайте заказ вручную.\n";
			text += "2. Установите статус заказа: Формирование макета.";
			notifyService.notifyAdmin(subject, text);
		}
		else
			order.setState(OrderState.FLASH_GENERATION);

		int pageCount = new Integer(properties.getProperty(PAGE_COUNT));
		order.setPageCount(pageCount);

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

		orderService.setFreeIfIsFirstTrial(order);

		int p1 = info.indexOf(COMMENT_KEY) + COMMENT_KEY.length();
		LOGGER.debug("p1 = " + p1);
		int p2 = info.indexOf(FINISHING_KEY) - 1;
		LOGGER.debug("p2 = " + p2);
		if (p2 < 0) { // New AlbumMaker version has no "FINISHING" in txt file.
			p2 = info.indexOf(COLOR_CORRECT_KEY) - 1;
			LOGGER.debug("p2 = " + p2);
		}
		String comment = info.substring(p1, p2);
		LOGGER.debug("comment = " + comment);
		order.setComment(comment != null ? comment.trim() : comment);

		orderService.addOrder(order);

		if (newUser) {
			userService.sendRegistrationMail(user, userName);
			orderService.notifyNewUserOrder(order.getId());
		}
		else {
			orderService.notifyNewOrder(new XArrayList<Integer>(order.getId()));
		}
		orderService.notifyAdmin(order);
        orderService.notifyOrderCommentSpecified(order);
	}

	private boolean isOldVersion(XFtpClient client) {
		String path = "last/ib-1.bmp";
		return client.fileExists(path);
	}
}
