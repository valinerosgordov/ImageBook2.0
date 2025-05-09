package ru.imagebook.server.service.flash;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.server.repository.FlashRepository;
import ru.imagebook.server.service.HeavyExecutorService;
import ru.imagebook.server.service.OrderService;
import ru.imagebook.server.service.SecurityService;
import ru.imagebook.server.service.ServerConfig;
import ru.imagebook.server.service.UserService;
import ru.imagebook.server.service.VendorService;
import ru.imagebook.server.service.editor.EditorConfig;
import ru.imagebook.server.service.render.RenderUtil;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderType;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.Role;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.model.common.preview.FlashParams;
import ru.minogin.core.client.common.AccessDeniedError;
import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.server.ServiceLogger;
import ru.minogin.core.server.freemarker.FreeMarker;
import ru.minogin.core.server.ftp.XFtpClient;
import ru.minogin.gfx.GraphicsUtil;
import ru.minogin.gfx.GraphicsUtil.ImageType;

public class FlashServiceImpl implements FlashService {
	private static Logger LOGGER = Logger.getLogger(FlashServiceImpl.class);

	private static final int DELAY_SEC = 60;
	private static final DecimalFormat PAGE_FORMAT = new DecimalFormat("0000");
	private static final int JPEG_FILE_WRITE_TIME_SEC = 3 * 60;
	private static final float MM_TO_INCH = 0.0393700787f;
	private static final float MM_TO_PX = MM_TO_INCH * 300f;
	private static final int FLASH_SESSION_LIFETIME_SEC = 60 * 60;
	private static final int FLASH_SESSION_CLEAN_PERIOD_SEC = 60 * 5;

	@Autowired
	private FlashTxService service;

	@Autowired
	private UserService userService;

	@Autowired
	private FlashRepository repository;

	@Autowired
	private HeavyExecutorService executorService;

	@Autowired
	private ServerConfig serverConfig;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private EditorConfig editorConfig;

    @Autowired
    private OrderService orderService;

	@Autowired
	private VendorService vendorService;

	private FlashConfig config;

	private FlashPath flashPath;

	// TODO mem
	private Set<Order<?>> processingOrders = new CopyOnWriteArraySet<Order<?>>();
	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
	private ExecutorService executor = Executors.newCachedThreadPool();
	// TODO mem
	private Map<String, FlashSession> sessions = new ConcurrentHashMap<String, FlashSession>();

	@Autowired
	public FlashServiceImpl(FlashConfig config) {
		this.config = config;
		this.flashPath = new FlashPath(config);
	}

	@Override
	public void startAsync() {
		scheduler.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				executor.submit(new Runnable() {
					@Override
					public void run() {
						try {
							LOGGER.debug("Flash started");
							generate();
							LOGGER.debug("Flash finished");
						}
						catch (Throwable t) {
							ServiceLogger.log(t);
						}
					}
				});
			}
		}, 0, DELAY_SEC, TimeUnit.SECONDS);

		scheduler.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				Date date = new Date();
				Iterator<FlashSession> iterator = sessions.values().iterator();
				while (iterator.hasNext()) {
					FlashSession session = iterator.next();
					if (date.after(session.getExpires()))
						iterator.remove();
				}
			}
		}, 0, FLASH_SESSION_CLEAN_PERIOD_SEC, TimeUnit.SECONDS);
	}

	public void generate() {
		List<Order<?>> orders = service.loadOrders();
		orders.removeAll(processingOrders);
		processingOrders.addAll(orders);

		for (final Order<?> order : orders) {
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					try {
						LOGGER.debug("Generating: " + order.getNumber());
						boolean generated = generate(order);
						LOGGER.debug("Generated: " + order.getNumber() + ", result: " + generated);
						if (generated) {
							service.setFlashGenerated(order.getId());
							LOGGER.debug("State changed: " + order.getNumber());
							service.notifyFlashGenerated(order);
							LOGGER.debug("Notification sent: " + order.getNumber());
						}
					}
					catch (Throwable t) {
						LOGGER.debug("Cannot generate flash for: " + order.getNumber());
						ServiceLogger.log(t);
					}
					finally {
						processingOrders.remove(order);
						LOGGER.debug("Removed: " + order.getNumber());
					}
				}
			});
		}
	}

	@Override
	public boolean generate(Order<?> order) {
		boolean downloaded = true;

		if (order.getType() == OrderType.MPHOTO || order.getType() == OrderType.MANUAL) {
            downloaded = downloadJpegs(order);
        }

		if (downloaded) {
			if (order.getType() != OrderType.EDITOR) {
                renderBarcode(order);
            }

			generateFlashImages(order);
			return true;
		} else {
			LOGGER.debug("Order " + order.getNumber() + " not ready for flash.");
			return false;
		}
	}

	private void renderBarcode(Order<?> order) {
		int lastPage = order.getPageCount();
		String lastPageJpeg = getJpegPath(order, lastPage);

		try {

            File imageFile = new File(lastPageJpeg);

            Product product = order.getProduct();
            Album album = (Album) product;
            if (album.getLastPageTemplate() != null && order.getType() != OrderType.EXTERNAL) {
                File trialFile = new File(editorConfig.getTemplatePath() + "/" + album.getLastPageTemplate());
                FileUtils.copyFile(trialFile, imageFile);
            }

            BufferedImage image = ImageIO.read(imageFile);
            Graphics2D graphics = image.createGraphics();

            float x, y;
            if (order.getType() == OrderType.EXTERNAL || order.getType() == OrderType.BOOK) {
				if (album.getBarcodeX() != null && album.getBarcodeY() != null) {
					x = album.getBarcodeX();
					y = product.getBlockHeight() - BarcodeRenderer.HEIGHT_MM - album.getBarcodeY();
				} else {
					float pageMargin = 5;
					float barcodeXMargin = 2;
					float barcodeYMargin = 2;
					x = pageMargin + product.getBlockWidth() - EditorBarcodeRenderer.WIDTH_MM - barcodeXMargin;
					y = pageMargin + product.getBlockHeight() - EditorBarcodeRenderer.HEIGHT_MM - barcodeYMargin;
				}
                EditorBarcodeRenderer barcodeRenderer = new EditorBarcodeRenderer(graphics, RenderUtil.mmToPx(x),
                    RenderUtil.mmToPx(y), order);
                barcodeRenderer.render();
            } else if (order.getType() == OrderType.MPHOTO
                       && (album.getMphotoBarcodeX() != null && album.getMphotoBarcodeY() != null)) {
				x = album.getMphotoBarcodeX();
				y = product.getBlockHeight() - BarcodeRenderer.HEIGHT_MM - album.getMphotoBarcodeY();
				BarcodeRenderer barcodeRenderer = new BarcodeRenderer(graphics, RenderUtil.mmToPx(x),
						RenderUtil.mmToPx(y), order);
				barcodeRenderer.render();
			} else {
				float margin = 5;
				float blockWidth = product.getBlockWidth();
				float blockHeight = product.getBlockHeight();
				if (album.isAddressPrinted()) {
                    x = 3 * margin;
                } else {
                    x = blockWidth - BarcodeRenderer.WIDTH_MM - margin;
                }
				y = blockHeight - BarcodeRenderer.HEIGHT_MM;
                BarcodeRenderer barcodeRenderer = new BarcodeRenderer(graphics, RenderUtil.mmToPx(x),
                    RenderUtil.mmToPx(y), order);
                barcodeRenderer.render();
			}

			ImageIO.write(image, "jpeg", imageFile);
		} catch (Exception e) {
            LOGGER.debug("Failed to render barcode [orderId=" + order.getId() + ",imageFile='" + lastPageJpeg + "']");
			Exceptions.rethrow(e);
		}
	}


    @Transactional
    @Override
    public FlashParams getFlashPreviewParams(int orderId, int userId) {
        User user = userService.getUser(userId);
        Order<?> order = repository.getOrder(orderId);
        checkAccess(order, user);

        return getFlashPreviewParams(order, null);
    }

    @Override
    public FlashParams getFlashPreviewParams(Order<?> order, String logo) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, FLASH_SESSION_LIFETIME_SEC);
        Date expires = calendar.getTime();
        String sessionId = UUID.randomUUID().toString();
        FlashSession session = new FlashSession(expires, order);
        session.setLogo(logo);
        sessions.put(sessionId, session);

        Album album = (Album) order.getProduct();
        FlashSize flashSize = new FlashSize(album);

        int pageCount = order.getPageCount();
        if (album.isSeparateCover()) {
        	pageCount += 4;
		}

		if (album.isTablet()) {
        	pageCount -= 2;
		}

        return new FlashParams(sessionId, flashSize.getFlashWidthPx(), flashSize.getFlashHeightPx(),
            serverConfig.getFlashContextUrl(), pageCount);
    }

	@Transactional
	@Override
	public String showWebFlash(String orderNumber) {
		Order<?> order = repository.findOrder(orderNumber);
		if (!order.isWebFlash()) {
			throw new AccessDeniedError();
		}

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, FLASH_SESSION_LIFETIME_SEC);
		Date expires = calendar.getTime();
		String sessionId = UUID.randomUUID().toString();
		FlashSession session = new FlashSession(expires, order);
//		session.set("name", name);
//		session.set("author", author);
		sessions.put(sessionId, session);

		Album album = (Album) order.getProduct();
		FlashSize flashSize = new FlashSize(album);

		int pageCount = order.getPageCount();
		if (album.isSeparateCover()) {
			pageCount += 4;
		}

		if (album.isTablet()) {
			pageCount -= 2;
		}

		String pageFlipId = UUID.randomUUID().toString();

		// TODO use renderOrderFlashFlipper
		FreeMarker freeMarker = new FreeMarker(getClass());
		freeMarker.set("id", pageFlipId);
		freeMarker.set("albumWidth", flashSize.getWebWidth() * 2); //pageWidth * 2);
		freeMarker.set("albumHeight", flashSize.getWebHeight()); //flashParams.getFlashHeight()-79); //pageHeight);
		freeMarker.set("nPages", pageCount);
		freeMarker.set("nInitialPages", 3);
		freeMarker.set("isSeparateCover", ((Album) order.getProduct()).isSeparateCover());
		freeMarker.set("hard", album.isHardOrEverflat());
		freeMarker.set("sessionId", sessionId);
		freeMarker.set("webPrefix", serverConfig.getWebPrefix());
		return freeMarker.process("webPageFlip.ftl", Locales.RU);
	}

	@Deprecated
	@Transactional
	@Override
	public String showWebFlash(String orderNumber, String name, String author, boolean small) {
		Order<?> order = repository.findOrder(orderNumber);
		if (order == null)
			return "";

		if (!order.isWebFlash())
			throw new AccessDeniedError();

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, FLASH_SESSION_LIFETIME_SEC);
		Date expires = calendar.getTime();
		String sessionId = UUID.randomUUID().toString();
		FlashSession session = new FlashSession(expires, order);
		session.set("name", name);
		session.set("author", author);
		session.set("small", small);
		sessions.put(sessionId, session);

		Album album = (Album) order.getProduct();
		FlashSize flashSize = new FlashSize(album);

		FreeMarker freeMarker = new FreeMarker(getClass());
		freeMarker.set("webPrefix", serverConfig.getWebPrefix());
		freeMarker.set("id", order.getId());
		freeMarker.set("number", order.getNumber());
		freeMarker.set("sessionId", sessionId);
		freeMarker.set("width", flashSize.getWebWidth(small));
		freeMarker.set("height", flashSize.getWebHeight(small));
		freeMarker.set("small", small);
		return freeMarker.process("webFlash.ftl", Locales.RU);
	}

	@Deprecated
	@Override
	public void showFlashXml(String sessionId, Writer writer) {
		FlashSession session = sessions.get(sessionId);
		if (session == null) {
			throw new AccessDeniedError();
		}

		Order<?> order = session.getOrder();

		Album album = (Album) order.getProduct();
		FlashSize flashSize = new FlashSize(album);
		FreeMarker freeMarker = new FreeMarker(getClass());
		freeMarker.set("flashContextUrl", serverConfig.getFlashContextUrl());
		freeMarker.set("normalWidth", flashSize.getNormalWidthPx());
		freeMarker.set("normalHeight", flashSize.getNormalHeightPx());
		freeMarker.set("smallWidth", flashSize.getSmallWidthPx());
		freeMarker.set("largeWidth", flashSize.getLargeWidthPx());
		freeMarker.set("largeHeight", flashSize.getLargeHeightPx());
		Integer pageCount = order.getPageCount();
		if (album.isSeparateCover())
			pageCount += 4;
		freeMarker.set("pageCount", pageCount);
		freeMarker.set("hardCover", album.isHardcover() ? "true" : "false");
		freeMarker.set("order", order);
		freeMarker.set("sessionId", sessionId);
		List<Page> pages = new ArrayList<Page>();
		if (album.isSeparateCover()) {
			pages.add(new Page(PageType.FRONT, 1));
			pages.add(new Page(PageType.FRONT, 2));
		}
		int pageStart = 1;
		pageCount = order.getPageCount();
		if (album.isTablet()) {
		    pageStart = 2;
		    pageCount--;
        }
		for (int i = pageStart; i <= pageCount; i++) {
			pages.add(new Page(PageType.NORMAL, album.isTablet() ? i - 1 : i));
		}
		if (album.isSeparateCover()) {
			pages.add(new Page(PageType.BACK, 1));
			pages.add(new Page(PageType.BACK, 2));
		}
		freeMarker.set("pages", pages);
		freeMarker.set("logo", session.getLogo());

		Vendor vendor = vendorService.getVendorByCurrentSite();
		freeMarker.set("flashUrl", vendor.getFlashUrl());
		freeMarker.process("flashXml.ftl", Locales.RU, writer);
	}

	@Override
	public void showWebFlashXml(String sessionId, Writer writer) {
		FlashSession session = sessions.get(sessionId);
		if (session == null)
			throw new AccessDeniedError();

		Order<?> order = session.getOrder();

		Album album = (Album) order.getProduct();
		FlashSize flashSize = new FlashSize(album);
		FreeMarker freeMarker = new FreeMarker(getClass());
		freeMarker.set("webPrefix", serverConfig.getWebPrefix());
		freeMarker.set("hardCover", album.isHardcover() ? "true" : "false");
		freeMarker.set("sessionId", sessionId);
		boolean small = (Boolean) session.get("small");
		freeMarker.set("width", flashSize.getWebWidth(small));
		freeMarker.set("height", flashSize.getWebHeight(small));
		freeMarker.set("fsize", small ? 14 : 16);
		List<Page> pages = new ArrayList<Page>();
		if (album.isSeparateCover()) {
			pages.add(new Page(PageType.FRONT, 1));
			pages.add(new Page(PageType.FRONT, 2));
		}
		for (int i = 1; i <= order.getPageCount(); i++) {
			pages.add(new Page(PageType.NORMAL, i));
		}
		if (album.isSeparateCover()) {
			pages.add(new Page(PageType.BACK, 1));
			pages.add(new Page(PageType.BACK, 2));
		}

		freeMarker.set("pages", pages);
		String name = session.get("name");
		String author = session.get("author");
		String title = name + "\n";
		if (!author.isEmpty())
			title += "Автор: " + author;
		freeMarker.set("title", title);
		freeMarker.process("webFlashXml.ftl", Locales.RU, writer);
	}

	@Override
	public void showFlashImage(String sessionId, int type, int size, int page,
			HttpServletResponse response) {
		try {
			FlashSession session = sessions.get(sessionId);
			if (session == null)
				throw new AccessDeniedError();

			Order<?> order = session.getOrder();
			String path = flashPath.getImagePath(order.getId(), type, size, page);

			GraphicsUtil.sendImage(path, response);
		}
		catch (Exception e) {
			LOGGER.debug(e);
// Exceptions.rethrow(e);
		}
	}

	@Override
	public void showWebFlashImage(String sessionId, int type, int size, int page, HttpServletResponse response) {
		try {
			FlashSession session = sessions.get(sessionId);
			if (session == null) {
				throw new AccessDeniedError();
			}

			// Deprecated
			//boolean small = (Boolean) session.get("small");

			Order<?> order = session.getOrder();
			String path = flashPath.getWebImagePath(order.getId(), type, size, page);
			GraphicsUtil.sendImage(path, response);
		} catch (Exception e) {
			LOGGER.debug(e);
			// Exceptions.rethrow(e);
		}
	}

	private void checkAccess(Order<?> order, User user) {
		if (securityService.hasRole(user, Role.OPERATOR)
				|| securityService.hasRole(user, Role.FINISHING_MANAGER))
			return;

		if (!order.getUser().equals(user))
			throw new AccessDeniedError();
	}

	private boolean downloadJpegs(Order<?> order) {
		LOGGER.debug("Downloading JPEGs: " + order.getNumber());

		Album album = (Album) order.getProduct();
		String orderNumber = order.getNumber();
		int lastPage = order.getPageCount();
		String jpegFolder = album.getJpegFolder();

		XFtpClient client = new XFtpClient();
		try {
			LOGGER.debug("Connecting");
			client.connect(config.getHost(), config.getUser(), config.getPassword());
			LOGGER.debug("Connected");

			// Check
			LOGGER.debug("Checking: " + order.getNumber());
			client.cd(jpegFolder);
			for (int page = 1; page <= lastPage; page++) {
				String fileName = getFileName(orderNumber, page);
				if (!client.isFileReady(fileName, JPEG_FILE_WRITE_TIME_SEC))
					return false;
			}
			client.cd("..");
			if (album.isSeparateCover()) {
				String jpegCoverFolder = album.getJpegCoverFolder();
				client.cd(jpegCoverFolder);
				if (album.isWhiteCover()) {
					String fileName = "Album_" + orderNumber + "_Cover_Front.jpg";
					if (!client.isFileReady(fileName, JPEG_FILE_WRITE_TIME_SEC))
						return false;
				}
				else {
					String fileName = "Album_" + orderNumber + "_Cover.jpg";
					if (!client.isFileReady(fileName, JPEG_FILE_WRITE_TIME_SEC))
						return false;
				}
				client.cd("..");
			}

			// Download
			LOGGER.debug("Downloading: " + order.getNumber());
			client.cd(jpegFolder);
			String jpegDir = flashPath.getJpegDir(order);
			new File(jpegDir).mkdirs();
			for (int page = 1; page <= lastPage; page++) {
				String fileName = getFileName(orderNumber, page);
				String jpegPath = getJpegPath(order, page);
				client.loadFile(fileName, jpegPath);
			}
			client.cd("..");

			if (album.isSeparateCover()) {
				String jpegCoverFolder = album.getJpegCoverFolder();
				client.cd(jpegCoverFolder);
				String jpegPath = getJpegCoverPath(order);
				if (album.isWhiteCover()) {
					String fileName = "Album_" + orderNumber + "_Cover_Front.jpg";
					client.loadFile(fileName, jpegPath);
				}
				else {
					String fileName = "Album_" + orderNumber + "_Cover.jpg";
					client.loadFile(fileName, jpegPath);
				}
				client.cd("..");
			}

			// Delete
			LOGGER.debug("Deleting: " + order.getNumber());
			client.cd(jpegFolder);
			for (int page = 1; page <= lastPage; page++) {
				String fileName = getFileName(orderNumber, page);
				client.deleteFile(fileName);
			}
			client.cd("..");
			if (album.isSeparateCover()) {
				String jpegCoverFolder = album.getJpegCoverFolder();
				client.cd(jpegCoverFolder);
				if (album.isWhiteCover()) {
					String fileName = "Album_" + orderNumber + "_Cover_Front.jpg";
					client.deleteFile(fileName);
				}
				else {
					String fileName = "Album_" + orderNumber + "_Cover.jpg";
					client.deleteFile(fileName);
				}
				client.cd("..");
			}
		}
		finally {
			client.disconnect();
		}

		LOGGER.debug("JPEGs Downloaded: " + order.getNumber());

		return true;
	}

	private String getJpegCoverPath(Order<?> order) {
		return flashPath.getJpegDir(order) + "/" + "c.jpg";
	}

	private void generateFlashImages(Order<?> order) {
		try {
			LOGGER.debug("Resizing: " + order.getNumber());
			Album album = (Album) order.getProduct();

			int orderId = order.getId();
			String flashDir = flashPath.getFlashDir(orderId);
			new File(flashDir).mkdirs();
			int pageCount = order.getPageCount();
			FlashSize flashSize = new FlashSize(album);
			int pageWidth = flashSize.getPageWidthPx();
			int pageHeight = flashSize.getPageHeightPx();
			float cropLeftMm, cropTopMm = 5;
			int cropTop = (int) (cropTopMm * MM_TO_PX);

			int nw = flashSize.getNormalWidthPx();
			int nh = flashSize.getNormalHeightPx();
			int lw = flashSize.getLargeWidthPx();
			int lh = flashSize.getLargeHeightPx();
			int sh = flashSize.getSmallHeightPx();
			int sw = flashSize.getSmallWidthPx();

			int startPage = 1;
			if (album.isTablet() && album.isSeparateCover()) {
                startPage = 2;
			    pageCount--;
            }

			for (int page = startPage; page <= pageCount; page++) {
				String jpegPath = getJpegPath(order, page);
				BufferedImage image = ImageIO.read(new File(jpegPath));

				if (album.isSeparateCover()) {
					if (page % 2 == 1)
						cropLeftMm = (float) album.getInnerCrop() * 0.1f;
					else
						cropLeftMm = 5;
				}
				else
					cropLeftMm = 5;
				int cropLeft = (int) (cropLeftMm * MM_TO_PX);

				BufferedImage cropped = image.getSubimage(cropLeft, cropTop, pageWidth, pageHeight);
				BufferedImage large = resize(cropped, lw, lh,
					flashPath.getImagePath(orderId, PageType.NORMAL, PageSize.LARGE, album.isTablet() ? page - 1 : page));
				BufferedImage normal = resize(large, nw, nh,
					flashPath.getImagePath(orderId, PageType.NORMAL, PageSize.NORMAL, album.isTablet() ? page - 1 : page));
				resize(normal, sw, sh,
					flashPath.getImagePath(orderId, PageType.NORMAL, PageSize.SMALL, album.isTablet() ? page - 1 : page));
			}

			if (album.isSeparateCover()) {
				String jpegCoverPath = getJpegCoverPath(order);
				BufferedImage image = ImageIO.read(new File(jpegCoverPath));

				int frontLeftCrop = dot1MmToPx(album.getFrontLeftCrop());
				int frontUpperCrop = dot1MmToPx(album.getFrontUpperCrop());

				// TODO to isTablet if clause
				int subimageWidth = (frontLeftCrop + pageWidth > image.getMinX() + image.getWidth())
					? image.getWidth() - frontLeftCrop : pageWidth;
				int subimageHeight = (frontUpperCrop + pageHeight > image.getMinY() + image.getHeight())
					? image.getHeight() - frontUpperCrop : pageHeight;

				BufferedImage front = image.getSubimage(frontLeftCrop, frontUpperCrop, subimageWidth, subimageHeight);
				BufferedImage large = resize(front, lw, lh, flashPath.getImagePath(orderId, PageType.FRONT, PageSize.LARGE, 1));
				BufferedImage normal = resize(large, nw, nh, flashPath.getImagePath(orderId, PageType.FRONT, PageSize.NORMAL, 1));
				resize(normal, sw, sh, flashPath.getImagePath(orderId, PageType.FRONT, PageSize.SMALL, 1));

				if (album.isTablet()) {
                    String jpegPath = getJpegPath(order, 1);
                    BufferedImage flyleafImage1 = ImageIO.read(new File(jpegPath));

                    cropLeftMm = (float) album.getInnerCrop() * 0.1f;
                    int cropLeft = (int) (cropLeftMm * MM_TO_PX);

                    BufferedImage cropped = flyleafImage1.getSubimage(cropLeft, cropTop, pageWidth, pageHeight);
                    large = resize(cropped, lw, lh, flashPath.getImagePath(orderId, PageType.FRONT, PageSize.LARGE, 2));
                    normal = resize(large, nw, nh, flashPath.getImagePath(orderId, PageType.FRONT, PageSize.NORMAL, 2));
                    resize(normal, sw, sh, flashPath.getImagePath(orderId, PageType.FRONT, PageSize.SMALL, 2));
                } else {
                    writeWhiteImage(nw, nh, flashPath.getImagePath(orderId, PageType.FRONT, PageSize.NORMAL, 2));
                    writeWhiteImage(sw, sh, flashPath.getImagePath(orderId, PageType.FRONT, PageSize.SMALL, 2));
                    writeWhiteImage(lw, lh, flashPath.getImagePath(orderId, PageType.FRONT, PageSize.LARGE, 2));
                }

				if (album.isTablet()) {
                    String jpegPath = getJpegPath(order, order.getPageCount());
                    BufferedImage flyleafImage2 = ImageIO.read(new File(jpegPath));

                    cropLeftMm = 5; //(float) album.getInnerCrop() * 0.1f;
                    int cropLeft = (int) (cropLeftMm * MM_TO_PX);

                    BufferedImage cropped = flyleafImage2.getSubimage(cropLeft, cropTop, pageWidth, pageHeight);
                    large = resize(cropped, lw, lh, flashPath.getImagePath(orderId, PageType.BACK, PageSize.LARGE, 1));
                    normal = resize(large, nw, nh, flashPath.getImagePath(orderId, PageType.BACK, PageSize.NORMAL, 1));
                    resize(normal, sw, sh, flashPath.getImagePath(orderId, PageType.BACK, PageSize.SMALL, 1));
                } else {
                    writeWhiteImage(nw, nh, flashPath.getImagePath(orderId, PageType.BACK, PageSize.NORMAL, 1));
                    writeWhiteImage(sw, sh, flashPath.getImagePath(orderId, PageType.BACK, PageSize.SMALL, 1));
                    writeWhiteImage(lw, lh, flashPath.getImagePath(orderId, PageType.BACK, PageSize.LARGE, 1));
                }

                if (album.isWhiteCover()) {
                    writeWhiteImage(nw, nh, flashPath.getImagePath(orderId, PageType.BACK, PageSize.NORMAL, 2));
                    writeWhiteImage(sw, sh, flashPath.getImagePath(orderId, PageType.BACK, PageSize.SMALL, 2));
                    writeWhiteImage(lw, lh, flashPath.getImagePath(orderId, PageType.BACK, PageSize.LARGE, 2));
                } else {
                    int backLeftCrop = dot1MmToPx(album.getBackLeftCrop());
                    int backUpperCrop = dot1MmToPx(album.getBackUpperCrop());
                    BufferedImage back = image.getSubimage(backLeftCrop, backUpperCrop, pageWidth, pageHeight);
                    large = resize(back, lw, lh, flashPath.getImagePath(orderId, PageType.BACK, PageSize.LARGE, 2));
                    normal = resize(large, nw, nh, flashPath.getImagePath(orderId, PageType.BACK, PageSize.NORMAL, 2));
                    resize(normal, sw, sh, flashPath.getImagePath(orderId, PageType.BACK, PageSize.SMALL, 2));
                }
			}

			LOGGER.debug("Resized: " + order.getNumber());
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}

	// private void deleteJpegs(Order<?> order) {
	// int pageCount = order.getPageCount();
	// for (int page = 1; page <= pageCount; page++) {
	// String jpegPath = getJpegPath(order, page);
	// new File(jpegPath).delete();
	// }
	// new File(getJpegDir(order)).delete();
	// }

	private BufferedImage resize(BufferedImage image, int w, int h, String path) {
		try {
			BufferedImage im = GraphicsUtil.resize(image, ImageType.JPEG, w, h);
			GraphicsUtil.saveJpeg(im, path);
			return im;
		}
		catch (Exception e) {
			return Exceptions.rethrow(e);
		}
	}

	private void writeWhiteImage(int w, int h, String path) {
		try {
			BufferedImage white = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			white.getGraphics().setColor(Color.WHITE);
			white.getGraphics().fillRect(0, 0, w, h);
			ImageIO.write(white, "JPEG", new File(path));
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}

	private String getJpegPath(Order<?> order, int page) {
		return flashPath.getJpegDir(order) + "/" + page + ".jpg";
	}

	private String getFileName(String orderNumber, int page) {
		String pageText = PAGE_FORMAT.format(page);
		return "Album_" + orderNumber + "_Page_" + pageText + ".jpg";
	}

	private int dot1MmToPx(float dot1Mm) {
		return (int) (dot1Mm * 0.1f * MM_TO_PX);
	}

	@Override
	public Order<?> loadOrder(int code, int userId) {
		Order<?> order = orderService.getOrderByPublishCode(code);
		User user = userService.getUser(userId);
		checkAccess(order, user);

		return order;
	}

	@Override
	public void publishFlash(int orderId, int userId) {
		Order<?> order = repository.getOrder(orderId);
		User user = userService.getUser(userId);
		checkPublishAccess(order, user);

		order.setPublishFlash(true);
	}

	@Override
	public void unpublishFlash(int orderId, int userId) {
		Order<?> order = repository.getOrder(orderId);
		User user = userService.getUser(userId);
		checkPublishAccess(order, user);

		order.setPublishFlash(false);
	}

	private void checkPublishAccess(Order<?> order, User user) {
		if (securityService.hasRole(user, Role.OPERATOR))
			return;

		if (!order.getUser().equals(user))
			throw new AccessDeniedError();
	}

	@Deprecated
	@Override
	public void generateWebImages(Order<?> order, boolean small) {
		try {
			Album album = (Album) order.getProduct();
			FlashSize flashSize = new FlashSize(album);
			int width = flashSize.getWebWidth(small);
			int height = flashSize.getWebHeight(small);

			int orderId = order.getId();
			File folder = new File(flashPath.getFlashDir(orderId));
			String toFolder = flashPath.getWebFlashDir(orderId, small);
			new File(toFolder).mkdirs();
			for (File file : folder.listFiles()) {
				String name = file.getName();
				if (name.contains("_s.") || name.contains("_l.")) {
                    continue;
                }

				BufferedImage from = ImageIO.read(file);
				BufferedImage to = GraphicsUtil.resize(from, ImageType.JPEG, width, height);
				GraphicsUtil.saveJpeg(to, toFolder + "/" + name);
			}
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}

	@Override
	public void generateWebImages(Order<?> order) {
		try {
			Album album = (Album) order.getProduct();
			FlashSize flashSize = new FlashSize(album);
			int width = flashSize.getWebWidth();
			int height = flashSize.getWebHeight();

			int orderId = order.getId();
			File folder = new File(flashPath.getFlashDir(orderId));
			String toFolder = flashPath.getWebFlashDir(orderId, true); // small = 'true' - for backward compatibility
			new File(toFolder).mkdirs();
			for (File file : folder.listFiles()) {
				String name = file.getName();

				if (name.contains("_l.")) {
					Files.copy(file.toPath(), Paths.get(toFolder + "/" + name), StandardCopyOption.REPLACE_EXISTING);
				} else if (!name.contains("_s")) {
					BufferedImage from = ImageIO.read(file);
					BufferedImage to = GraphicsUtil.resize(from, ImageType.JPEG, width, height);
					GraphicsUtil.saveJpeg(to, toFolder + "/" + name);
				}
			}
		} catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}
}