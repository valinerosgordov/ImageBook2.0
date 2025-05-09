package ru.imagebook.server.service.editor;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import static ru.imagebook.server.service.editor.EditorConst.FONT_LOCATION;
import ru.imagebook.client.editor.ctl.file.NonZipArchiveFormatMessage;
import ru.imagebook.client.editor.ctl.file.ResizingFailedMessage;
import ru.imagebook.client.editor.ctl.file.ResizingProgressMessage;
import ru.imagebook.client.editor.ctl.file.ResizingResultMessage;
import ru.imagebook.client.editor.ctl.file.UnpackingFailedMessage;
import ru.imagebook.client.editor.ctl.file.UnpackingResultMessage;
import ru.imagebook.client.editor.ctl.file.UnsupportedImageTypeMessage;
import ru.imagebook.client.editor.ctl.file.UploadFailedMessage;
import ru.imagebook.client.editor.ctl.file.UploadProgressMessage;
import ru.imagebook.client.editor.ctl.file.UploadResultMessage;
import ru.imagebook.client.editor.ctl.file.WrongFileTypeMessage;
import ru.imagebook.server.repository.EditorRepository;
import ru.imagebook.server.service.FileConfig;
import ru.imagebook.server.service.HeavyExecutorService;
import ru.imagebook.server.service.OrderService;
import ru.imagebook.server.service.UserService;
import ru.imagebook.server.service.auth.AuthService;
import ru.imagebook.server.service.auth.XSecurityService;
import ru.imagebook.server.service.flash.EditorBarcodeRenderer;
import ru.imagebook.server.service.flash.FlashConfig;
import ru.imagebook.server.service.render.RenderUtil;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.AlbumOrder;
import ru.imagebook.shared.model.AlbumOrderImpl;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderState;
import ru.imagebook.shared.model.OrderType;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.ProductType;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.editor.Barcode;
import ru.imagebook.shared.model.editor.BarcodeImpl;
import ru.imagebook.shared.model.editor.Component;
import ru.imagebook.shared.model.editor.ImageImpl;
import ru.imagebook.shared.model.editor.ImageLayoutType;
import ru.imagebook.shared.model.editor.Layout;
import ru.imagebook.shared.model.editor.Page;
import ru.imagebook.shared.model.editor.PageType;
import ru.imagebook.shared.model.editor.Position;
import ru.imagebook.shared.model.editor.PositionImpl;
import ru.imagebook.shared.model.editor.SafeArea;
import ru.imagebook.shared.model.editor.SafeAreaImpl;
import ru.minogin.core.client.common.AccessDeniedError;
import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.client.file.FileBean;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.server.ServiceLogger;
import ru.minogin.core.server.awt.ImageSize;
import ru.minogin.core.server.awt.ImageUtil;
import ru.minogin.core.server.file.FileComparator;
import ru.minogin.core.server.flow.remoting.MessageError;
import ru.minogin.core.server.upload.ProgressCallback;
import ru.minogin.core.server.upload.UploadFilter;
import ru.minogin.core.server.upload.Uploader;
import ru.minogin.core.server.zip.UnpackFilter;
import ru.minogin.core.server.zip.Unpacker;
import ru.minogin.gfx.GraphicsUtil;
import ru.minogin.gfx.GraphicsUtil.ImageType;

public class EditorServiceImpl implements EditorService {
	private static Logger logger = Logger.getLogger(EditorServiceImpl.class);

    private static final int PREVIEW_WIDTH = 100;
    private static final int SCREEN_WIDTH = 600;
    private static final String EDITOR_ORDER_ID = "editorOrderId";
    private static final int DELAY_SEC = 60;
    private static final float PAGE_THICKNESS_MM = 0.2f;
    private static final float MIN_SPINE_WIDTH_MM = 4;
    private static final int BARCODE_RIGHT_MARGIN_DEFAULT = 10;
    private static final int BARCODE_BOTTOM_MARGIN_DEFAULT = 5;
    private static final int BARCODE_LEFT_MARGIN_TRIAL = 10;
    private static final String UNSUPPORTED_PAGE_TYPE_MSG = "Unsupported page type";
    private static final int MAX_INDEX = 1000000;
    private static final int SAFE_AREA_INDEX = MAX_INDEX + 100;
    private static final int SAFE_AREA_INDEX2 = MAX_INDEX + 101;
    private static final int BARCODE_INDEX = MAX_INDEX + 200;

    private final EditorConfig config;
    private final EditorRepository repository;
    private final UserService userService;
    private final AuthService authService;
    private final EditorTxService txService;
    private final HeavyExecutorService executorService;
    private final FlashConfig flashConfig;
    private final FileConfig fileConfig;
    private final EditorUtil util;
    private final OrderService orderService;
    private final XSecurityService securityService;

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ExecutorService executor = Executors.newCachedThreadPool();
    // TODO mem
    private CopyOnWriteArraySet<Order<?>> processingOrders = new CopyOnWriteArraySet<Order<?>>();

    public EditorServiceImpl(EditorConfig config, EditorRepository repository,
                             UserService userService, AuthService authService,
                             EditorTxService txService, HeavyExecutorService executorService,
                             FlashConfig flashConfig, FileConfig fileConfig,
                             OrderService orderService, XSecurityService securityService) {
        this.config = config;
        this.repository = repository;
        this.userService = userService;
        this.authService = authService;
        this.txService = txService;
        this.executorService = executorService;
        this.flashConfig = flashConfig;
        this.fileConfig = fileConfig;
        this.orderService = orderService;
        this.securityService = securityService;
        util = new EditorUtil(config);
    }

    @Override
    public Map<Integer, List<Product>> loadProductsMap(int userId) {
        User user = userService.getUser(userId);
        List<Product> products = repository.loadProducts();

        Set<Product> accessedProducts = user.getAccessedProducts();

        Map<Integer, List<Product>> productsMap = new LinkedHashMap<Integer, List<Product>>();
        for (Product product : products) {
            Set<User> accessedUsers = product.getAccessedUsers();
            if (accessedUsers.contains(user)
                || (accessedUsers.isEmpty() && (accessedProducts.isEmpty() || accessedProducts.contains(product)))) {
                Integer type = product.getType();
                List<Product> typeProducts = productsMap.get(type);
                if (typeProducts == null) {
                    typeProducts = new ArrayList<Product>();
                    productsMap.put(type, typeProducts);
                }
                typeProducts.add(product);
            }
        }

        return productsMap;
    }

    @Override
    public FileBean loadFolders(int userId) {
        File userFolder = new File(getImagesPath(userId));
        if (!userFolder.exists())
            userFolder.mkdirs();

        if (userFolder.listFiles().length == 0)
            createFolder(userId, "Все изображения");

        FileBean fileBean = new FileBean();
        loadFolders(userFolder, fileBean);
        return fileBean;
    }

    private void createFolder(int userId, String path) {
        try {
            String imagesPath = getImagesPath(userId);
            File file = new File(imagesPath + "/" + path);
            String canonicalPath = file.getCanonicalPath();
            if (!canonicalPath.contains(imagesPath))
                throw new AccessDeniedError();
            file.mkdirs();

            String screenPath = util.getScreenPath(userId);
            new File(screenPath + "/" + path).mkdirs();

            String previewPath = util.getPreviewPath(userId);
            new File(previewPath + "/" + path).mkdirs();
        } catch (Exception e) {
            Exceptions.rethrow(e);
        }
    }

    private void loadFolders(File folder, FileBean fileBean) {
        for (File child : folder.listFiles()) {
            if (child.isDirectory()) {
                FileBean childBean = new FileBean();
                childBean.setName(child.getName());
                childBean.setParent(fileBean);
                fileBean.getChildren().add(childBean);

                loadFolders(child, childBean);
            }
        }
    }

    private String getImagesPath(int userId) {
        try {
            return new File(util.getStorageUserPath(userId) + "/images")
                    .getCanonicalPath();
        } catch (Exception e) {
            return Exceptions.rethrow(e);
        }
    }

    @Override
    public List<String> loadPreviews(int userId, String path) {
        try {
            String imagesPath = getImagesPath(userId);
            File f = new File(imagesPath + "/" + path);
            String canonicalPath = f.getCanonicalPath();
            if (!canonicalPath.contains(imagesPath)) {
                throw new AccessDeniedError();
            }

            String loadPath = util.getPreviewPath(userId) + "/" + path;
            File folder = new File(loadPath);
            List<String> paths = new ArrayList<String>();
            File[] files = folder.listFiles();
            if (files == null) {
                return paths;
            }
            Arrays.sort(files, new FileComparator());
            for (File file : files) {
                if (!file.isDirectory()) {
                    paths.add(file.getName());
                }
            }
            return paths;
        } catch (Exception e) {
            return Exceptions.rethrow(e);
        }
    }

    @Override
    public void showPreview(int userId, String path, OutputStream outputStream) {
        try {
            String imagesPath = getImagesPath(userId);
            File f = new File(imagesPath + "/" + path);
            String canonicalPath = f.getCanonicalPath();
            if (!canonicalPath.contains(imagesPath))
                throw new AccessDeniedError();

            String previewPath = util.getPreviewPath(userId);
            String imagePath = previewPath + "/" + path;

            FileInputStream in = new FileInputStream(imagePath);
            BufferedOutputStream os = new BufferedOutputStream(outputStream);
            IOUtils.copy(in, os);
            in.close();
            os.flush();
        } catch (Exception e) {
            Exceptions.rethrow(e);
        }
    }

    @Override
    public void upload(HttpServletRequest request, final String sessionId, int userId) {
        List<File> uploadedFiles = null;
        String imagesPath = getImagesPath(userId);

        try {
            Uploader uploader = new Uploader(request, fileConfig.getTempPath());
            uploader.setProgressCallback(new ProgressCallback() {
                @Override
                public void onUploaded(long uploaded, long total) {
                    authService.sendMessage(sessionId, new UploadProgressMessage(uploaded, total));
                }
            });
            uploader.upload();

            String path = uploader.getParameter("a");
            path = URLDecoder.decode(path, "UTF-8");

            final String uploadPath = imagesPath + "/" + path;
            File uploadFolder = new File(uploadPath);
            String canonicalPath = uploadFolder.getCanonicalPath();
            if (!canonicalPath.contains(imagesPath)) {
                throw new AccessDeniedError();
            }

            UploadFilter uploadFilter = new UploadFilter() {
                @Override
                public boolean isAllowed(File file) {
                    if (!isJpeg(file) && !isZip(file)) {
                        throw new WrongFileTypeException();
                    }
                    return true;
                }
            };
            uploadedFiles = uploader.writeAll(uploadPath, uploadFilter);

            authService.sendMessage(sessionId, new UploadResultMessage());
        } catch (WrongFileTypeException e) {
            handleUploadException(new WrongFileTypeMessage(), sessionId, e);
        } catch (MessageError e) {
            handleUploadException(e.getErrorMessage(), sessionId, e);
        } catch (Exception e) {
            handleUploadException(new UploadFailedMessage(), sessionId, e);
        }

        // Unpack Zip
        List<File> files = new ArrayList<File>();
        try {
            for (File file : uploadedFiles) {
                if (isZip(file)) {
                    Unpacker unpacker = new Unpacker(file);
                    UnpackFilter unpackFilter = new UnpackFilter() {
                        @Override
                        public boolean isAllowed(File file) {
                            if (!isJpeg(file)) {
                                logger.debug(String.format("Unpack Zip: skipping non jpeg file [%s]", file.getName()));
                                return false;
                            }
                            return true;
                        }
                    };
                    unpacker.setFilter(unpackFilter);
                    files.addAll(unpacker.unpack());
                } else {
                    files.add(file);
                }
            }

            authService.sendMessage(sessionId, new UnpackingResultMessage());
        } catch (MessageError e) {
            handleUploadException(e.getErrorMessage(), sessionId, e);
        } catch (Exception e) {
            if (EditorUtil.ARCHIVE_IS_NOT_A_ZIP.equals(e.getMessage())) {
                handleUploadException(new NonZipArchiveFormatMessage(), sessionId, e);
            } else {
                handleUploadException(new UnpackingFailedMessage(), sessionId, e);
            }
        }

        // Upload Images
        try {
            URI baseUri = new File(imagesPath).toURI();
            final String screenPath = util.getScreenPath(userId);
            final String previewPath = util.getPreviewPath(userId);

            int resized = 0;
            int total = files.size();
            for (File file : files) {
                URI uri = file.toURI();
                URI relativeUri = baseUri.relativize(uri);
                String relativePath = relativeUri.getPath();

                File screenFile = new File(screenPath, relativePath);
                File parentFile = screenFile.getParentFile();
                if (parentFile != null) {
                    parentFile.mkdirs();
                }

                BufferedImage image = readImageFromFile(file);
                int width = image.getWidth();
                int height = image.getHeight();
                if (width > SCREEN_WIDTH) {
                    int screenHeight = height * SCREEN_WIDTH / width;
                    BufferedImage screenImage = new BufferedImage(SCREEN_WIDTH, screenHeight,
                        BufferedImage.TYPE_INT_RGB);
                    Image scaledInstance = GraphicsUtil.resize(image, ImageType.JPEG, SCREEN_WIDTH, screenHeight);
                    screenImage.getGraphics().drawImage(scaledInstance, 0, 0, null);
                    ImageIO.write(screenImage, "jpeg", screenFile);
                    image = screenImage;
                } else {
                    ImageIO.write(image, "jpeg", screenFile);
                }

                width = image.getWidth();
                height = image.getHeight();

                File previewFile = new File(previewPath, relativePath);
                parentFile = previewFile.getParentFile();
                if (parentFile != null) {
                    parentFile.mkdirs();
                }
                if (width > PREVIEW_WIDTH) {
                    int previewHeight = height * PREVIEW_WIDTH / width;
                    BufferedImage previewImage = new BufferedImage(PREVIEW_WIDTH, previewHeight,
                        BufferedImage.TYPE_INT_RGB);
                    Image scaledInstance = GraphicsUtil.resize(image, ImageType.JPEG, PREVIEW_WIDTH, previewHeight);
                    previewImage.getGraphics().drawImage(scaledInstance, 0, 0, null);
                    ImageIO.write(previewImage, "jpeg", previewFile);
                } else {
                    ImageIO.write(image, "jpeg", previewFile);
                }

                authService.sendMessage(sessionId, new ResizingProgressMessage(resized, total));
                resized++;
            }

            authService.sendMessage(sessionId, new ResizingResultMessage());
        } catch (UnsupportedImageTypeException e) {
            handleUploadException(new UnsupportedImageTypeMessage(e.getFilename()), sessionId, e);
        } catch (MessageError e) {
            handleUploadException(e.getErrorMessage(), sessionId, e);
        } catch (Exception e) {
            handleUploadException(new ResizingFailedMessage(), sessionId, e);
        }
    }

    private static int NORMALIZE_LAYOUT_TYPE(final int layoutType) {
        switch (layoutType) {
            case ImageLayoutType.FOLDER_BACKGROUND_LEFT: {
                return ImageLayoutType.BACKGROUND_LEFT;
            }
            case ImageLayoutType.FOLDER_BACKGROUND_RIGHT: {
                return ImageLayoutType.BACKGROUND_RIGHT;
            }
            case ImageLayoutType.FOLDER_NORMAL: {
                return ImageLayoutType.NORMAL;
            }
            default:
                return ImageLayoutType.NORMAL;
        }
    }

    @Override
    public Order<?> addFolderWithImages(final String sessionId, final int userId,
                                        final String path, final int layoutType, final int pageNumber) {
        try {
            Map<Integer, Page> changedPages = new HashMap<Integer, Page>();

            int orderId = (Integer) authService.getSessionData(sessionId,
                    EDITOR_ORDER_ID);
            AlbumOrder order = (AlbumOrder) repository.loadOrderWithLayout(orderId);
            Layout layout = order.getLayout();
            List<Page> pages = layout.getPages();
            Page page = pages.get(pageNumber);
            if (page.isBlocked())
                throw new RuntimeException("Cannot add image. Page is blocked.");

            String imagesPath = getImagesPath(userId);
            File imageFile = new File(getImagesPath(userId) + "/" + path);
            String canonicalPath = imageFile.getCanonicalPath();
            if (!canonicalPath.contains(imagesPath))
                throw new AccessDeniedError();
            if (!imageFile.exists())
                throw new EditorServiceCommonError("Папка не найдена!");
            if (imageFile.listFiles() == null || imageFile.listFiles().length == 0)
                throw new EditorServiceCommonError("Для размещения папки с изображениями на листе, папка должна содержать файлы!");
            final File[] files = imageFile.listFiles();
            for (File file : imageFile.listFiles()) {
                if (file.isDirectory())
                    throw new EditorServiceCommonError("Для размещения папки с изображениями на листе, папка не должна содержать подпапки!");
            }
            /*Делаем из обычного заказа пакетный*/
            final boolean isPackaged = order.isPackaged();
            if (!isPackaged) {
                order.setPackageNumber(order.getNumber());
            } else if (imageFile.listFiles().length != (order.getLayouts() == null ? 0 : order.getLayouts().size())) {
                throw new EditorServiceCommonError("Количество файлов в папке должно соответствовать"
                    + "количеству вариантов данного пакетного заказа");
            }

			/*сортируем по названию*/
            Arrays.sort(files, new Comparator<File>() {
                public int compare(File strA, File strB) {
                    return (strA.getName()).compareToIgnoreCase(strB.getName());
                }
            });

			/*Если заказ еще не пакетный, то создаем слои и к каждому слою добавляем список листов*/
            if (!isPackaged) {
                //final Set<Layout> newLayouts = new HashSet<Layout>(files.length);
                for (int index = 0; index < files.length; index++) {
                    final Layout _layout = new Layout();

                    //_layout.setPages(newPages);
                    //createLayout(_layout, order, pages.size());

                    //order.setLayout(_layout);

                    //repository.saveOrder(order);
                    //repository.saveLayout(_layout);
                    order.addLayout(_layout);
                }


				/*для удобства сбрасываем на первый вариант(слой)*/
                order.setLayout(order.getLayouts().iterator().next());
                repository.saveOrder(order);
                repository.flush();

				/*Добавляем в слои новые листы по количеству файлов в папке*/
                for (int index = 0; index < files.length; index++) {
                    final List<Page> newPages = new ArrayList<Page>(pages.size());
                    for (Page item : pages) {
                        final Page _page = new Page(item);
                        if (page.getId().equals(item.getId())) {
								/*Делаем из обычного листа индивидуальный*/
                            _page.setCommon(false);
                        }
                        //repository.flush();
                        newPages.add(_page);
                    }
                    order.getLayouts().get(index).getPages().addAll(newPages);
                    repository.saveOrder(order);
                    repository.flush();
                }
                for (Layout _layout : order.getLayouts()) {
                    int indx = 0;
                    for (Page _page : _layout.getPages()) {
                        if (indx != pageNumber) {
                            for (Component component : _page.getComponents()) {
                                if (component instanceof ru.imagebook.shared.model.editor.Image) {
                                    ru.imagebook.shared.model.editor.Image image = (ru.imagebook.shared.model.editor.Image) component;
                                    int imagePrototypeId = image.getPrototypeId();
                                    int imageId = image.getId();

                                    String prototypeImagePath = util.getImagePath(userId,
                                            imagePrototypeId);
                                    String imagePath = util.getImagePath(userId, imageId);
                                    FileUtils.copyFile(new File(prototypeImagePath),
                                            new File(imagePath));

                                    String prototypeScreenPath = util.getScreenPath(userId,
                                            imagePrototypeId);
                                    String screenPath = util.getScreenPath(userId, imageId);
                                    FileUtils.copyFile(new File(prototypeScreenPath), new File(
                                            screenPath));
                                }
                            }
                        }
                        indx++;
                    }
                }
				/*Добавляем в подготовленные листы изображения*/
                int ind = 0;
                for (File file : files) {
                    final String imagePath = path + "/" + file.getName();
					/*конвертируем тип слоя в обычный тип слоя*/
                    final int newLayoutType = NORMALIZE_LAYOUT_TYPE(layoutType);
                    doAddImage(order.getLayouts().get(ind).getPages().get(pageNumber), newLayoutType, userId, imagePath);
                    ind++;
                }
                changedPages.put(pageNumber, page);

            } else {
                int ind = 0;
                for (File file : files) {
                    final String imagePath = path + "/" + file.getName();
					/*конвертируем тип слоя в обычный тип слоя*/
                    final int newLayoutType = NORMALIZE_LAYOUT_TYPE(layoutType);
                    final Page _page = order.getLayouts().get(ind).getPages().get(pageNumber);
                    _page.setCommon(false);
                    doAddImage(_page, newLayoutType, userId, imagePath);
                    ind++;
                }
                changedPages.put(pageNumber, page);
            }
            return order;
        } catch (Exception e) {
            return Exceptions.rethrow(e);
        }
    }

    private BufferedImage readImageFromFile(File file) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(file);
            ColorModel imageColorModel = image.getColorModel();
            if (imageColorModel != null && !imageColorModel.getColorSpace().isCS_sRGB()) {
                throw new UnsupportedImageTypeException(file.getName());
            }
        } catch (Exception e) {
            if (EditorUtil.UNSUPPORTED_IMAGE_TYPE.equals(e.getMessage())) {
                throw new UnsupportedImageTypeException(file.getName());
            }
            Exceptions.rethrow(e);
        }

        return image;
    }

    private void handleUploadException(Message message, String sessionId, Throwable th) {
        authService.sendMessage(sessionId, message);
        Exceptions.rethrow(th);
    }

    @Override
    public Map<Integer, Page> addImage(String sessionId, int userId,
                                       String relativePath, int layoutType, int pageNumber) {
        try {
            Map<Integer, Page> changedPages = new HashMap<Integer, Page>();

            int orderId = (Integer) authService.getSessionData(sessionId,
                    EDITOR_ORDER_ID);
            AlbumOrder order = (AlbumOrder) repository.getOrder(orderId);
            Layout layout = order.getLayout();
            List<Page> pages = layout.getPages();
            Page page = pages.get(pageNumber);
            if (page.isBlocked())
                throw new RuntimeException("Cannot add image. Page is blocked.");

            String imagesPath = getImagesPath(userId);
            File imageFile = new File(getImagesPath(userId) + "/" + relativePath);
            String canonicalPath = imageFile.getCanonicalPath();
            if (!canonicalPath.contains(imagesPath))
                throw new AccessDeniedError();

            doAddImage(page, layoutType, userId, relativePath);

            changedPages.put(pageNumber, page);

            return changedPages;
        } catch (Exception e) {
            return Exceptions.rethrow(e);
        }
    }

    private void doAddImage(Page page, int layoutType, int userId,
                            String relativePath) {
        try {
            File imageFile = new File(getImagesPath(userId) + "/" + relativePath);
            ImageSize size = ImageUtil.getImageSize(imageFile);
            float iw = size.getWidth();
            float ih = size.getHeight();

            Iterator<Component> iterator = page.getComponents().iterator();
            int lastIndex = -1;
            while (iterator.hasNext()) {
                Component component = iterator.next();
                if (component.getIndex() > lastIndex
                        && component.getIndex() < MAX_INDEX)
                    lastIndex = component.getIndex();
                if (component instanceof ru.imagebook.shared.model.editor.Image) {
                    ru.imagebook.shared.model.editor.Image image = (ru.imagebook.shared.model.editor.Image) component;
                    int ilt = image.getLayoutType();
                    if ((ilt != ImageLayoutType.BACKGROUND_LEFT || layoutType != ImageLayoutType.BACKGROUND_RIGHT)
                            && (ilt != ImageLayoutType.BACKGROUND_RIGHT || layoutType != ImageLayoutType.BACKGROUND_LEFT)) {
                        iterator.remove();
                        if (image.getId() != null) {
                            deleteImageFiles(userId, page, image);
                        }
                    }
                }
            }

            ru.imagebook.shared.model.editor.Image image = new ImageImpl();
            image.setIndex(lastIndex + 1);
            image.setLayoutType(layoutType);

            disposeImage(image, iw, ih, page, layoutType);

            page.getComponents().add(image);
            repository.flush();

            Integer imageId = image.getId();

            String imagePath = util.getImagePath(userId, imageId);
            FileUtils.copyFile(imageFile, new File(imagePath));

            File screenFile = new File(util.getScreenPath(userId) + "/"
                    + relativePath);
            String screenPath = util.getScreenPath(userId, imageId);
            FileUtils.copyFile(screenFile, new File(screenPath));
        } catch (Exception e) {
            Exceptions.rethrow(e);
        }
    }

    private void copyImage(Page page, int layoutType, int userId, ru.imagebook.shared.model.editor.Image _image) {
        try {
            if (_image.getId() == null) return;

            ru.imagebook.shared.model.editor.Image image = new ImageImpl(_image);
			/*image.setIndex(lastIndex + 1);
			image.setLayoutType(layoutType);

			disposeImage(image, iw, ih, page, layoutType);*/

            page.getComponents().add(image);
            repository.flush();

            int imagePrototypeId = image.getPrototypeId();
            int imageId = image.getId();

            String prototypeImagePath = util.getImagePath(userId,
                    imagePrototypeId);
            String imagePath = util.getImagePath(userId, imageId);
            FileUtils.copyFile(new File(prototypeImagePath),
                    new File(imagePath));

            String prototypeScreenPath = util.getScreenPath(userId,
                    imagePrototypeId);
            String screenPath = util.getScreenPath(userId, imageId);
            FileUtils.copyFile(new File(prototypeScreenPath), new File(
                    screenPath));
        } catch (Exception e) {
            Exceptions.rethrow(e);
        }
    }

    private void disposeImage(ru.imagebook.shared.model.editor.Image image,
                              float iw, float ih, Page page, int layoutType) {
        float pw = page.getWidth();
        float ph = page.getHeight();

        float ir = iw / ih;

        Position position = null;
        for (Component component : page.getComponents()) {
            if (component instanceof Position) {
                position = (Position) component;
                break;
            }
        }

        if (position == null) {
            if (layoutType == ImageLayoutType.NORMAL
                    || layoutType == ImageLayoutType.BACKGROUND_LEFT)
                image.setLeft(-page.getXMargin());
            if (layoutType == ImageLayoutType.BACKGROUND_RIGHT)
                image.setLeft(pw / 2 - 2 * page.getXMargin());
            image.setTop(-page.getYMargin());

            if (layoutType == ImageLayoutType.BACKGROUND_LEFT
                    || layoutType == ImageLayoutType.BACKGROUND_RIGHT)
                pw = pw / 2 + page.getXMargin();
        } else {
            image.setLeft(position.getLeft());
            image.setTop(position.getTop());
            pw = position.getWidth();
            ph = position.getHeight();
        }

        float pr = pw / ph;

        if (ir < pr) {
            image.setClipLeft(0);
            image.setClipWidth(1);
            image.setClipTop((ih * pw - ph * iw) / (2 * ih * pw));
            image.setClipHeight((ph * iw) / (ih * pw));
        } else {
            image.setClipLeft((iw * ph - pw * ih) / (2 * iw * ph));
            image.setClipWidth((pw * ih) / (iw * ph));
            image.setClipTop(0);
            image.setClipHeight(1);
        }
        image.setWidth(pw);
        image.setHeight(ph);
    }

    // private void removeComponents(int userId, Page page) {
    // Iterator<Component> iterator = page.getComponents().iterator();
    // while (iterator.hasNext()) {
    // Component component = iterator.next();
    // if (component instanceof ru.imagebook.client.model.editor.Image) {
    // removeImage(userId, page, (ru.imagebook.client.model.editor.Image)
    // component);
    // }
    // iterator.remove();
    // }
    // }

    private void deleteImageFiles(int userId, Page page,
                                  ru.imagebook.shared.model.editor.Image image) {
        new File(util.getImagePath(userId, image.getId())).delete();
        new File(util.getScreenPath(userId, image.getId())).delete();
    }

    @Override
    public Order<?> createOrder(String sessionId, int userId, int productId,
                                int pageCount) {
        User user = userService.getUser(userId);

        Album album = (Album) repository.getProduct(productId);
        AlbumOrder order = new AlbumOrderImpl(album);
        order.setType(OrderType.EDITOR);
        order.setNumber(repository.nextCounter() + "");
        order.setUser(user);
        order.setUrgent(user.isUrgentOrders());
        order.setPageCount(pageCount);

        int colorNumber = album.getColorRange().get(0);
        Color color = repository.getColor(colorNumber);
        order.setColor(color);

        order.setCoverLamination(album.getCoverLamRange().get(0));
        order.setPageLamination(album.getPageLamRange().get(0));

        Layout layout = new Layout();
        createLayout(layout, order, pageCount);

        order.setLayout(layout);

        orderService.setFreeIfIsFirstTrial(order);

        repository.saveOrder(order);

        createTrialAlbumImage(album, layout, userId);

        authService.setSessionData(sessionId, EDITOR_ORDER_ID, order.getId());

        return order;
    }

    @Override
    public Order<?> copyOrder(String sessionId, int userId, int prototypeId) {
        try {
            User user = userService.getUser(userId);

            AlbumOrder prototype = (AlbumOrder) repository.getOrder(prototypeId);
            if (!prototype.getUser().equals(user))
                throw new AccessDeniedError();

            Album album = prototype.getProduct();
            AlbumOrder order = new AlbumOrderImpl(album);
            order.setType(OrderType.EDITOR);
            order.setNumber(repository.nextCounter() + "");
            order.setUser(user);
            order.setUrgent(user.isUrgentOrders());
            order.setPageCount(prototype.getPageCount());
            order.setColor(prototype.getColor());
            order.setCoverLamination(prototype.getCoverLamination());
            order.setPageLamination(prototype.getPageLamination());

            Layout layout = prototype.getLayout().copy();
            order.setLayout(layout);

            addBarcode(album, layout.getPages());

            repository.saveOrder(order);
            repository.flush();

            for (Page page : layout.getPages()) {
                for (Component component : page.getComponents()) {
                    if (component instanceof ru.imagebook.shared.model.editor.Image) {
                        ru.imagebook.shared.model.editor.Image image = (ru.imagebook.shared.model.editor.Image) component;
                        int imagePrototypeId = image.getPrototypeId();
                        int imageId = image.getId();

                        String prototypeImagePath = util.getImagePath(userId,
                                imagePrototypeId);
                        String imagePath = util.getImagePath(userId, imageId);
                        FileUtils.copyFile(new File(prototypeImagePath),
                                new File(imagePath));

                        String prototypeScreenPath = util.getScreenPath(userId,
                                imagePrototypeId);
                        String screenPath = util.getScreenPath(userId, imageId);
                        FileUtils.copyFile(new File(prototypeScreenPath), new File(
                                screenPath));
                    }
                }
            }

            int orderId = order.getId();
            order = (AlbumOrder) repository.loadOrderWithLayout(orderId);
            authService.setSessionData(sessionId, EDITOR_ORDER_ID, order.getId());

            return order;
        } catch (Exception e) {
            return Exceptions.rethrow(e);
        }
    }

    private void copyOrdersFromPackage(String sessionId, int userId, int prototypeId, final Layout layout,
                                       final int suffixOrderName) {
        try {
            User user = userService.getUser(userId);

            AlbumOrder prototype = (AlbumOrder) repository.loadOrderWithLayout(prototypeId);
            if (!prototype.getUser().equals(user))
                throw new AccessDeniedError();

            Album album = prototype.getProduct();
            AlbumOrder order = new AlbumOrderImpl(album);
            order.setType(OrderType.EDITOR);
            order.setNumber(prototype.getNumber() + "-" + suffixOrderName);
            order.setUser(user);
            order.setPackageNumber(prototype.getNumber());
            order.setState(OrderState.JPEG_GENERATION);
            order.setUrgent(user.isUrgentOrders());
            order.setPageCount(prototype.getPageCount());
            order.setColor(prototype.getColor());
            order.setCoverLamination(prototype.getCoverLamination());
            order.setPageLamination(prototype.getPageLamination());

            Layout _layout = layout.copy();
            order.setLayout(_layout);

            addBarcode(album, _layout.getPages());

            repository.saveOrder(order);
            repository.flush();

            for (Page page : _layout.getPages()) {
                for (Component component : page.getComponents()) {
                    if (component instanceof ru.imagebook.shared.model.editor.Image) {
                        ru.imagebook.shared.model.editor.Image image = (ru.imagebook.shared.model.editor.Image) component;
                        int imagePrototypeId = image.getPrototypeId();
                        int imageId = image.getId();

                        String prototypeImagePath = util.getImagePath(userId,
                                imagePrototypeId);
                        String imagePath = util.getImagePath(userId, imageId);
                        FileUtils.copyFile(new File(prototypeImagePath),
                                new File(imagePath));

                        String prototypeScreenPath = util.getScreenPath(userId,
                                imagePrototypeId);
                        String screenPath = util.getScreenPath(userId, imageId);
                        FileUtils.copyFile(new File(prototypeScreenPath), new File(
                                screenPath));
                    }
                }
            }

            int orderId = order.getId();
            order = (AlbumOrder) repository.loadOrderWithLayout(orderId);
            authService.setSessionData(sessionId, EDITOR_ORDER_ID, order.getId());
        } catch (Exception e) {
            Exceptions.rethrow(e);
        }
    }

    private void createTrialAlbumImage(Album album, Layout layout, int userId) {
        try {
            if (album.isTrialAlbum()) {
                List<Page> pages = layout.getPages();

                Page lastPage = pages.get(pages.size() - 1);
                lastPage.setBlocked(true);

                ru.imagebook.shared.model.editor.Image image = new ImageImpl();
                image.setLayoutType(ImageLayoutType.NORMAL);
                image.setLeft(-5);
                image.setTop(-5);
                image.setWidth(215);
                image.setHeight(150);
                image.setClipLeft(0);
                image.setClipTop(0);
                image.setClipWidth(1);
                image.setClipHeight(1);
                lastPage.getComponents().add(image);

                repository.flush();

                int imageId = image.getId();

                String path = config.getTemplatePath() + "/"
                        + album.getLastPageTemplate();
                FileUtils.copyFile(new File(path),
                        new File(util.getImagePath(userId, imageId)));

                path = config.getTemplatePath() + "/trial_s.jpg";
                FileUtils.copyFile(new File(path),
                        new File(util.getScreenPath(userId, imageId)));
            }
        } catch (Exception e) {
            Exceptions.rethrow(e);
        }
    }

    private void createLayout(Layout layout, AlbumOrder order, int pageCount) {
        Album album = order.getProduct();

        List<Page> pages = layout.getPages();

        if (album.isSeparateCover()) {
            Page page = new Page();
            page.setType(PageType.NORMAL);

            float coverWidth = album.getCoverWidth() + getSpineWidth(pageCount);
            float coverHeight = album.getCoverHeight();
            page.setSize(coverWidth, coverHeight);

            if (album.isWhiteCover()) {
                Position position = new PositionImpl();
                position.setComponentId(Components.WHITE_COVER_POSITION);
                position.setBlocked(true);
                float x = getSpineWidth(pageCount) / 2 + 30;
                position.setLeft(coverWidth / 2 + x - 15);
                position.setTop(20);
                position.setWidth(coverWidth / 2 - x - 35);
                position.setHeight(coverHeight - 70);
                page.getComponents().add(position);
            }

            int margin = 15;
            page.setMargins(margin, margin);
            pages.add(page);

            if (!album.isWhiteCover()) {
                SafeArea safeArea = new SafeAreaImpl();
                safeArea.setComponentId(Components.FULL_COVER_SAFE_AREA);
                safeArea.setIndex(SAFE_AREA_INDEX);
                safeArea.setLeft(album.getLeftCoverSafeArea());
                safeArea.setTop(album.getUpperCoverSafeArea());
                safeArea.setWidth(coverWidth - margin * 2 - album.getLeftCoverSafeArea() - album.getRightCoverSafeArea());
                safeArea.setHeight(coverHeight - margin * 2 - album.getUpperCoverSafeArea() - album.getBottomCoverSafeArea());
                page.getComponents().add(safeArea);
            }
        }

        float margin = 5;
        float blockWidth = album.getBlockWidth();
        float blockHeight = album.getBlockHeight();
        boolean right = true;
        for (int i = 0; i < pageCount; i++) {
            Page page = new Page();

            // Если тип продукта - Планшет, то вместо первой и последней страницы отображается не страница, а разворот
            // поэтому первые 2 и последние 2 страницы представляются как разворот = [форзац + странца] и [страница + форзац]

            if (i == 0
                || i == pageCount - 1
                || i == pageCount - 2) {

                page.setType(album.isTablet() ? PageType.FLYLEAF : PageType.NORMAL);
                createLayoutPage(album, page, margin, blockWidth, blockHeight, right);
                right = !right;
            } else {
                page.setType(PageType.SPREAD);
                createLayoutPage(album, page, margin, blockWidth, blockHeight, right);
            }

            if (page.isSpreadOrFlyLeafPage()) {
                i++;
            }

            page.setMargins(margin, margin);
            pages.add(page);
        }

        addBarcode(album, pages);
    }

    private void createLayoutPage(Album album, Page page, float margin, float blockWidth, float blockHeight,
                                  boolean right) {
        if (page.getType() == PageType.NORMAL) {
            page.setSize(blockWidth + 2 * margin, blockHeight + 2 * margin);

            SafeArea safeArea = new SafeAreaImpl();
            safeArea.setIndex(SAFE_AREA_INDEX);
            if (right) {
                safeArea.setLeft(album.getInnerSafeArea());
                safeArea.setTop(album.getUpperSafeArea());
                safeArea.setWidth(blockWidth - album.getInnerSafeArea() - album.getOuterSafeArea());
                safeArea.setHeight(blockHeight - album.getUpperSafeArea() - album.getBottomSafeArea());
            } else {
                safeArea.setLeft(album.getOuterSafeArea());
                safeArea.setTop(album.getUpperSafeArea());
                safeArea.setWidth(blockWidth - album.getInnerSafeArea() - album.getOuterSafeArea());
                safeArea.setHeight(blockHeight - album.getUpperSafeArea() - album.getBottomSafeArea());
            }
            page.getComponents().add(safeArea);
        } else if (page.isSpreadOrFlyLeafPage()) {
            page.setSize(2 * blockWidth + 2 * margin, blockHeight + 2 * margin);

            SafeArea safeArea = new SafeAreaImpl();
            safeArea.setIndex(SAFE_AREA_INDEX);
            safeArea.setLeft(album.getOuterSafeArea());
            safeArea.setTop(album.getUpperSafeArea());
            safeArea.setWidth(blockWidth - album.getInnerSafeArea() - album.getOuterSafeArea());
            safeArea.setHeight(blockHeight - album.getUpperSafeArea() - album.getBottomSafeArea());
            page.getComponents().add(safeArea);

            safeArea = new SafeAreaImpl();
            safeArea.setIndex(SAFE_AREA_INDEX2);
            safeArea.setLeft(blockWidth + album.getInnerSafeArea());
            safeArea.setTop(album.getUpperSafeArea());
            safeArea.setWidth(blockWidth - album.getInnerSafeArea() - album.getOuterSafeArea());
            safeArea.setHeight(blockHeight - album.getUpperSafeArea() - album.getBottomSafeArea());
            page.getComponents().add(safeArea);
        } else {
            throw new RuntimeException(UNSUPPORTED_PAGE_TYPE_MSG);
        }
    }

    private void addBarcode(Album album, List<Page> pages) {
        // If tablet and isBarcodeOnTheLastSpread() == true put barcode on the last spread instead of cover
        int barcodePageNum = (album.isTablet() && !album.isBarcodeOnTheLastSpread()) ? 0 : pages.size() - 1;
        Page page = pages.get(barcodePageNum);
        addBarcode(album, page);
    }

    private void addBarcode(Album album, Page page) {
        ru.imagebook.shared.model.editor.Barcode barcode = new BarcodeImpl();
        barcode.setIndex(BARCODE_INDEX);
        barcode.setBlocked(true);
        float widthMm = EditorBarcodeRenderer.WIDTH_MM;
        float heightMm = EditorBarcodeRenderer.HEIGHT_MM;
        if (album.getBarcodeX() != null && album.getBarcodeY() != null) {
            barcode.setLeft(album.getBarcodeX());
            barcode.setTop(page.getHeight() - (page.getYMargin() * 2) - heightMm - album.getBarcodeY());
        } else {
            if (album.isTrialAlbum()) {
                barcode.setLeft(BARCODE_LEFT_MARGIN_TRIAL);
            } else if (page.isSpreadOrFlyLeafPage()) {
                barcode.setLeft(page.getWidth() / 2 - (page.getXMargin() * 2) - widthMm - BARCODE_RIGHT_MARGIN_DEFAULT);
            } else {
                barcode.setLeft(page.getWidth() - (page.getXMargin() * 2) - widthMm - BARCODE_RIGHT_MARGIN_DEFAULT);
            }
            barcode.setTop(page.getHeight() - (page.getYMargin() * 2) - heightMm - BARCODE_BOTTOM_MARGIN_DEFAULT);
        }
        barcode.setWidth(widthMm);
        barcode.setHeight(heightMm);
        page.getComponents().add(barcode);
    }

    private float getSpineWidth(int pageCount) {
        float spineWidth = PAGE_THICKNESS_MM * pageCount;
        if (spineWidth < MIN_SPINE_WIDTH_MM)
            spineWidth = MIN_SPINE_WIDTH_MM;
        return spineWidth;
    }

    @Override
    public List<Order<?>> loadOrders(int userId) {
        User user = userService.getUser(userId);
        return repository.loadOrders(user);
    }

    @Override
    public Order<?> loadingOrderWithLayoutsAndPages(String sessionId, int userId) {
        int orderId = (Integer) authService.getSessionData(sessionId,
                EDITOR_ORDER_ID);
        AlbumOrder order = (AlbumOrder) repository.loadOrderWithLayout(orderId);
        order.getLayouts().size();
        for (Layout layout : order.getLayouts()) {
            layout.getPages().size();
        }
        return order;
    }

    @Override
    public Order<?> openOrder(String sessionId, int userId, int orderId) {
        AlbumOrder order = (AlbumOrder) repository.loadOrderWithLayout(orderId);
        order.getLayouts().size();
        for (Layout layout : order.getLayouts()) {
            layout.getPages().size();
        }
        securityService.checkOrderOwner(order, userId);
        authService.setSessionData(sessionId, EDITOR_ORDER_ID, orderId);

        // Update order state
        if (order.getState() != OrderState.NEW) {
            order.setState(OrderState.NEW);
        }
        order.setModifiedDate(new Date());
        repository.saveOrder(order);

        return order;
    }

    @Override
    public void showComponent(String sessionId, int userId, int componentId,
                              OutputStream outputStream) {
        int orderId = (Integer) authService.getSessionData(sessionId, EDITOR_ORDER_ID);
        AlbumOrder order = (AlbumOrder) repository.getOrder(orderId);

        Component component = repository.getComponent(componentId);
        component.accept(new ComponentViewer(order, userId, outputStream, util));
    }

    @Override
    public void createFolder(int userId, String path, String name) {
        createFolder(userId, path + "/" + name);
    }

    @Override
    public void editFolder(int userId, String path, String name) {
        try {
            String imagesPath = getImagesPath(userId);

            File fromFile = new File(imagesPath + "/" + path);
            String fromPath = fromFile.getCanonicalPath();
            if (!fromPath.contains(imagesPath))
                throw new AccessDeniedError();

            File toFile = new File(fromFile.getParent() + "/" + name);
            String toPath = toFile.getCanonicalPath();
            if (!toPath.contains(imagesPath))
                throw new AccessDeniedError();

            fromFile.renameTo(toFile);

            String screenPath = util.getScreenPath(userId);
            fromFile = new File(screenPath + "/" + path);
            toFile = new File(fromFile.getParent() + "/" + name);
            fromFile.renameTo(toFile);

            String previewPath = util.getPreviewPath(userId);
            fromFile = new File(previewPath + "/" + path);
            toFile = new File(fromFile.getParent() + "/" + name);
            fromFile.renameTo(toFile);
        } catch (Exception e) {
            Exceptions.rethrow(e);
        }
    }

    @Override
    public void deleteFolder(int userId, String path) {
        try {
            String imagesPath = getImagesPath(userId);
            File file = new File(imagesPath + "/" + path);
            String canonicalPath = file.getCanonicalPath();
            if (!canonicalPath.contains(imagesPath))
                throw new AccessDeniedError();
            FileUtils.deleteDirectory(file);

            FileUtils.deleteDirectory(new File(util.getScreenPath(userId) + "/"
                    + path));
            FileUtils.deleteDirectory(new File(util.getPreviewPath(userId) + "/"
                    + path));
        } catch (Exception e) {
            Exceptions.rethrow(e);
        }
    }

    @Override
    public void processOrder(String sessionId, final int userId) {
        int orderId = (Integer) authService.getSessionData(sessionId, EDITOR_ORDER_ID);
        Order<?> order = repository.loadOrderWithLayout(orderId);
        if (order.isPackaged()) {
            for (int index = 0; index < order.getLayouts().size(); index++) {
                copyOrdersFromPackage(sessionId, userId, orderId, order.getLayouts().get(index), index + 1);
            }
            removeOrderFromSession(sessionId);
            repository.deleteOrder(order);
        } else {
            order.setState(OrderState.JPEG_GENERATION);
        }

    }

    @Override
    public boolean requiredShowNotification(final String sessionId, final int userId, final int typeId, final boolean checkOnlyForCommonOrder) {
        if (checkOnlyForCommonOrder) {
            final int orderId = (Integer) authService.getSessionData(sessionId, EDITOR_ORDER_ID);
            final Order<?> order = repository.getOrder(orderId);
            if (order.isPackaged()) return false;
        }
        return repository.requiredShowNotificationMessage(userId, typeId);
    }

    @Override
    public void cancelShowNotificationMessage(final int userId, final int typeId) {
        repository.cancelFromNotification(userId, typeId);
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
                            logger.debug("JPEG started");
                            generate();
                            logger.debug("JPEG finished");
                        } catch (Throwable t) {
                            ServiceLogger.log(t);
                        }
                    }
                });
            }
        }, 0, DELAY_SEC, TimeUnit.SECONDS);
    }

    private void generate() {
        List<Order<?>> orders = txService.loadOrders();
        orders.removeAll(processingOrders);
        processingOrders.addAll(orders);

        for (final Order<?> order : orders) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        doGenerate(order);
                        txService.setJpegGenerated(order.getId());
                    } catch (Throwable t) {
                        ServiceLogger.log(t);

                        txService.setJpegGenerationError(order.getId());
                    } finally {
                        processingOrders.remove(order);
                    }
                }
            });
        }
    }

    private void doGenerate(Order<?> order) {
        logger.debug("Generate: " + order.getNumber());

        String jpegPath = flashConfig.getJpegPath() + "/" + order.getId();
        new File(jpegPath).mkdirs();

        User user = order.getUser();
        int userId = user.getId();

        Album album = (Album) order.getProduct();

        Layout layout = order.getLayout();
        List<Page> pages = layout.getPages();

        int first = 0;
        if (album.isSeparateCover()) {
            first = 1;

            BufferedImage image = generatePage(pages.get(0), 0, userId, order);
            drawNumber(image, order.getNumber());
            if (!album.isWhiteCover())
                drawSpine(image, order);
            if (album.isWhiteCover())
                image = postProcessWhiteCover(image, order);
            savePage(image, jpegPath, "c");
        }

        int jpegNumber = 1;
        for (int i = first; i < pages.size(); i++) {
            Page page = pages.get(i);

            if (page.getType() == PageType.NORMAL) {
                BufferedImage image = generatePage(page, 0, userId, order);
                // if (i == pages.size() - 1)
                // drawBarcode(album, image, order.getNumber());
                image = postProcess(image, album, jpegNumber % 2 == 1);
                savePage(image, jpegPath, jpegNumber + "");
                jpegNumber++;
            } else if (page.isSpreadOrFlyLeafPage()) {
                BufferedImage image = generatePage(page, 0, userId, order);
                image = postProcess(image, album, jpegNumber % 2 == 1);
                savePage(image, jpegPath, jpegNumber + "");
                jpegNumber++;

                image = generatePage(page, 1, userId, order);
                image = postProcess(image, album, jpegNumber % 2 == 1);
                savePage(image, jpegPath, jpegNumber + "");
                jpegNumber++;
            } else {
                throw new RuntimeException(UNSUPPORTED_PAGE_TYPE_MSG);
            }
        }
    }

    private BufferedImage postProcessWhiteCover(BufferedImage image, Order<?> order) {
        Album album = (Album) order.getProduct();

        int width = image.getWidth();
        int height = image.getHeight();
        float widthMm = RenderUtil.pxToMm(width);
        float heightMm = RenderUtil.pxToMm(height);

        float newWidthMm = album.getPdfCoverWidth();
        int newWidth = RenderUtil.mmToPx(newWidthMm);
        float newHeightMm;
        if (heightMm > 320)
            newHeightMm = 320;
        else
            newHeightMm = heightMm;
        int newHeight = RenderUtil.mmToPx(newHeightMm);

        float xMm, yMm, wMm, hMm;

        hMm = newHeightMm;
        if (heightMm <= 320)
            yMm = 0;
        else
            yMm = (heightMm - 320) / 2;

        float marginMm;
        if (album.isSuperAlbum())
            marginMm = 5;
        else
            marginMm = 40;
        xMm = widthMm + marginMm - newWidthMm;
        wMm = newWidthMm - marginMm;

        int x = RenderUtil.mmToPx(xMm);
        int y = RenderUtil.mmToPx(yMm);
        int w = RenderUtil.mmToPx(wMm);
        int h = RenderUtil.mmToPx(hMm);

        BufferedImage subimage = image.getSubimage(x, y, w, h);
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = newImage.getGraphics();
        graphics.setColor(java.awt.Color.WHITE);
        graphics.fillRect(0, 0, newWidth, newHeight);
        graphics.drawImage(subimage, 0, 0, null);
        graphics.setColor(java.awt.Color.LIGHT_GRAY);
        graphics.drawLine(w, 0, w, newHeight);
        return newImage;
    }

    private BufferedImage generatePage(Page page, int side, int userId, Order<?> order) {
        try {
            float pageWidthMm = page.getWidth();
            float pageHeightMm = page.getHeight();

            float jpegWidthMm = pageWidthMm;
            if (page.isSpreadOrFlyLeafPage()) {
                jpegWidthMm = pageWidthMm / 2 + page.getXMargin();
            }
            float jpegHeightMm = pageHeightMm;

            int jpegWidthPx = RenderUtil.mmToPx(jpegWidthMm);
            int jpegHeightPx = RenderUtil.mmToPx(jpegHeightMm);

            BufferedImage pageImage;
            if (pageHasBarcodeOnly(page)) {
                pageImage = new BufferedImage(jpegWidthPx, jpegHeightPx, BufferedImage.TYPE_BYTE_GRAY);
            } else {
                pageImage = new BufferedImage(jpegWidthPx, jpegHeightPx, BufferedImage.TYPE_INT_RGB);
            }
            Graphics graphics = pageImage.getGraphics();
            graphics.setColor(java.awt.Color.WHITE);
            graphics.fillRect(0, 0, jpegWidthPx, jpegHeightPx);

            ComponentRenderer renderer = new ComponentRenderer(page, side, util, userId, pageImage, order);
            for (Component component : page.getComponents()) {
                // put barcode on the second page of spread or flyLeaf
                if (component instanceof BarcodeImpl && page.isSpreadOrFlyLeafPage() && side == 0) {
                    continue;
                }
                component.accept(renderer);
            }

            return pageImage;
        } catch (Exception e) {
            return Exceptions.rethrow(e);
        }
    }

    public boolean pageHasBarcodeOnly(Page page) {
        boolean hasImage = false;
        boolean hasBarcode = false;
        for (Component component : page.getComponents()) {
            if (component instanceof ru.imagebook.shared.model.editor.Image) {
                hasImage = true;
            } else if (component instanceof Barcode) {
                hasBarcode = true;
            }
        }
        return !hasImage && hasBarcode;
    }

    private void drawSpine(BufferedImage image, Order<?> order) {
        int pageCount = order.getPageCount();
        float spineWidthMm = getSpineWidth(pageCount);
        int spineWidthPx = RenderUtil.mmToPx(spineWidthMm);

        int h = image.getHeight();

        int x = (image.getWidth() - spineWidthPx) / 2;
        Graphics2D graphics = image.createGraphics();
        int margin = RenderUtil.mmToPx(8);
        graphics.setColor(java.awt.Color.BLACK);
        graphics.drawLine(x, 0, x, margin);
        graphics.drawLine(x, h - margin, x, h);
        graphics.setColor(java.awt.Color.WHITE);
        graphics.drawLine(x + 1, 0, x + 1, margin);
        graphics.drawLine(x + 1, h - margin, x + 1, h);

        x = (image.getWidth() + spineWidthPx) / 2;
        graphics.setColor(java.awt.Color.BLACK);
        graphics.drawLine(x, 0, x, margin);
        graphics.drawLine(x, h - margin, x, h);
        graphics.setColor(java.awt.Color.WHITE);
        graphics.drawLine(x + 1, 0, x + 1, margin);
        graphics.drawLine(x + 1, h - margin, x + 1, h);
    }

    private void drawNumber(BufferedImage image, String number) {
        try {
            Graphics2D graphics = image.createGraphics();

            int margin = RenderUtil.mmToPx(3);
            int height = RenderUtil.mmToPx(20);
            int x = image.getWidth() - margin;
            int y = image.getHeight() / 2;
            graphics.setColor(java.awt.Color.WHITE);
            graphics.fillRect(x, y, margin, height);

            ClassPathResource resource = new ClassPathResource(FONT_LOCATION);
            InputStream is = resource.getInputStream();
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            is.close();
            font = font.deriveFont(40f);
            graphics.setFont(font);
            graphics.translate(image.getWidth(), y + height);
            graphics.rotate(-Math.PI / 2.0);
            graphics.setColor(java.awt.Color.LIGHT_GRAY);
            graphics.drawString(number, 0, 0);
        } catch (Exception e) {
            Exceptions.rethrow(e);
        }
    }

    private BufferedImage postProcess(BufferedImage src, Album album, boolean odd) {
        float srcWidth = src.getWidth();
        float srcHeight = src.getHeight();

        int type = album.getType();
        int width = (int) srcWidth;
        int shift = 0;
        if (type == ProductType.HARD_COVER_FULL_PRINT
                || type == ProductType.TABLET
                || type == ProductType.HARD_COVER_WHITE_MARGINS) {
            shift = RenderUtil.mmToPx(10);
            width += shift;
        } else if (type == ProductType.EVERFLAT_FULL_PRINT
                || type == ProductType.EVERFLAT_WHITE_MARGINS) {
            shift = RenderUtil.mmToPx(2);
            width += shift;
        }

        int height = (int) srcHeight;

        // Preserve image type of original image
        BufferedImage image = new BufferedImage(width, height, src.getType());
        Graphics graphics = image.getGraphics();
        graphics.setColor(java.awt.Color.WHITE);
        if (type == ProductType.HARD_COVER_FULL_PRINT
                || type == ProductType.TABLET
                || type == ProductType.HARD_COVER_WHITE_MARGINS) {
            int margin = RenderUtil.mmToPx(10);
            if (odd) {
                graphics.drawImage(src, shift, 0, null);
                graphics.fillRect(0, 0, margin, height);
            } else {
                graphics.drawImage(src, 0, 0, null);
                graphics.fillRect(width - margin, 0, width, height);
            }
            return image;
        } else if (type == ProductType.EVERFLAT_FULL_PRINT
                || type == ProductType.EVERFLAT_WHITE_MARGINS) {
            int margin = RenderUtil.mmToPx(7);
            if (odd) {
                graphics.drawImage(src, shift, 0, null);
                graphics.fillRect(0, 0, margin, height);
            } else {
                graphics.drawImage(src, 0, 0, null);
                graphics.fillRect(width - margin, 0, width, height);
            }
            return image;
        } else
            return src;
    }

    private void savePage(BufferedImage image, String jpegPath, String name) {
        try {
            String path = jpegPath + "/" + name + ".jpg";
            File file = new File(path);

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
            ImageWriter writer = writers.next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(1);

            FileImageOutputStream os = new FileImageOutputStream(file);
            writer.setOutput(os);
            IIOImage iioImage = new IIOImage(image, null, null);
            writer.write(null, iioImage, param);
            os.flush();
            writer.dispose();
            os.close();

            // ImageIO.write(image, "jpeg", file);
        } catch (Exception e) {
            Exceptions.rethrow(e);
        }
    }

    @Override
    public void deleteImage(int userId, String path) {
        try {
            String imagesPath = getImagesPath(userId);
            File file = new File(imagesPath + "/" + path);
            String canonicalPath = file.getCanonicalPath();
            if (!canonicalPath.contains(imagesPath))
                throw new AccessDeniedError();

            file.delete();

            new File(util.getScreenPath(userId) + "/" + path).delete();
            new File(util.getPreviewPath(userId) + "/" + path).delete();
        } catch (Exception e) {
            Exceptions.rethrow(e);
        }
    }

    @Override
    public void removeOrderFromSession(String sessionId) {
        authService.removeSessionData(sessionId, EDITOR_ORDER_ID);
    }

    private void changePageCountInternal(final Layout layout, final AlbumOrder order, final int pageCount, final Album album, final int userId) {
        List<Page> pages = layout.getPages();
        int pagesSize = pages.size();

        Layout newLayout = new Layout();
        createLayout(newLayout, order, pageCount);
        List<Page> newPages = newLayout.getPages();
        int newPagesSize = newPages.size();

        if (newPagesSize < pagesSize) {
            for (int i = pagesSize - 1; i >= newPagesSize - 1; i--) {
                pages.remove(i);
            }
            Page lastPage = newPages.get(newPagesSize - 1);
            pages.add(lastPage);
        } else {
            pages.remove(pagesSize - 1);
            for (int i = pagesSize - 1; i < newPagesSize; i++) {
                pages.add(newPages.get(i));
            }
        }

        if (album.isSeparateCover()) {
            Page cover = pages.get(0);
            Page newCover = newPages.get(0);
            cover.setWidth(newCover.getWidth());

            if (album.isWhiteCover()) {
                Position position = cover.getComponent(Components.WHITE_COVER_POSITION);
                Position newPosition = newCover.getComponent(Components.WHITE_COVER_POSITION);
                position.setPosition(newPosition);
            } else {
                SafeArea safeArea = cover.getComponent(Components.FULL_COVER_SAFE_AREA);
                SafeArea newSafeArea = newCover.getComponent(Components.FULL_COVER_SAFE_AREA);
                safeArea.setPosition(newSafeArea);
            }

            for (Component component : cover.getComponents()) {
                if (component instanceof ru.imagebook.shared.model.editor.Image) {
                    ru.imagebook.shared.model.editor.Image image = (ru.imagebook.shared.model.editor.Image) component;
                    String imagePath = util.getImagePath(userId, image.getId());
                    ImageSize size = ImageUtil.getImageSize(new File(imagePath));
                    disposeImage(image, size.getWidth(), size.getHeight(), cover, ImageLayoutType.NORMAL);
                }
            }
        }
    }

    @Override
    public Order<?> changePageCount(String sessionId, int userId, int pageCount) {
        int orderId = (Integer) authService.getSessionData(sessionId, EDITOR_ORDER_ID);
        AlbumOrder order = (AlbumOrder) repository.loadOrderWithLayout(orderId);
        if (order.getPageCount() == pageCount) {
            return order;
        }

        Album album = order.getProduct();

        if (!order.isPackaged()) {
            Layout layout = order.getLayout();
            changePageCountInternal(layout, order, pageCount, album, userId);
        } else {
            for (Layout layout : order.getLayouts()) {
                changePageCountInternal(layout, order, pageCount, album, userId);
            }
        }

        order.setPageCount(pageCount);

        return order;
    }

    @Override
    public void changeCurrentOrderLayout(final String sessionId, final int userId, final Layout layout) {
        final int orderId = (Integer) authService.getSessionData(sessionId,
                EDITOR_ORDER_ID);
        repository.saveLayout(layout, orderId);
    }

    @Override
    public Order<?> changePageTypeToIndividual(final String sessionId, final int userId, final Page page, final String path, final int pageNumber) {
        final int orderId = (Integer) authService.getSessionData(sessionId, EDITOR_ORDER_ID);
        page.setCommon(false);
		/*копируем изображение на все слои под  тем же номером листа*/
        AlbumOrder order = (AlbumOrder) repository.loadOrderWithLayout(orderId);
        final List<Layout> layouts = order.getLayouts();
        final Set<ru.imagebook.shared.model.editor.Image> images = new HashSet<>();
        Page _page = order.getLayout().getPages().get(pageNumber);
        _page.setCommon(false);
        Iterator<Component> iterator = _page.getComponents().iterator();
        int layoutType = 1;
        while (iterator.hasNext()) {
            Component component = iterator.next();
            if (!component.isBlocked()) {
                if (component instanceof ru.imagebook.shared.model.editor.Image) {
                    ru.imagebook.shared.model.editor.Image image = (ru.imagebook.shared.model.editor.Image) component;
                    images.add(image);
                }
            }
        }

        for (final Layout layout : layouts) {
            if (layout.getId().equals(order.getLayout().getId())) continue;
            Page newPage = layout.getPages().get(pageNumber);
            newPage.setCommon(false);
			/*если до этого быи на листе изображения, то удаляем их*/
            Iterator<Component> iter = newPage.getComponents().iterator();
            while (iter.hasNext()) {
                Component component = iter.next();
                if (!component.isBlocked()) {
                    if (component instanceof ru.imagebook.shared.model.editor.Image) {
                        ru.imagebook.shared.model.editor.Image image = (ru.imagebook.shared.model.editor.Image) component;
                        iter.remove();
                        deleteImageFiles(userId, newPage, image);
                    }
                }
            }
            for (ru.imagebook.shared.model.editor.Image image : images) {
                copyImage(newPage, image.getLayoutType(), userId, image);
            }
        }
        order = (AlbumOrder) repository.loadOrderWithLayout(orderId);
        return order;
    }

    @Override
    public List<Order<?>> loadAllOrders(int userId) {
        User user = userService.getUser(userId);
        return repository.loadAllOrders(user);
    }

    @Override
    public Page clearSpread(String sessionId, int userId, int pageNumber) {
        int orderId = (Integer) authService.getSessionData(sessionId,
                EDITOR_ORDER_ID);
        AlbumOrder order = (AlbumOrder) repository.getOrder(orderId);
        Layout layout = order.getLayout();
        Page page = layout.getPages().get(pageNumber);

        if (page.isBlocked())
            throw new RuntimeException("Page is blocked");

        Iterator<Component> iterator = page.getComponents().iterator();
        while (iterator.hasNext()) {
            Component component = iterator.next();
            if (!component.isBlocked()) {
                iterator.remove();
                if (component instanceof ru.imagebook.shared.model.editor.Image) {
                    ru.imagebook.shared.model.editor.Image image = (ru.imagebook.shared.model.editor.Image) component;
                    deleteImageFiles(userId, page, image);
                }
            }
        }

        return page;
    }

    @Override
    public Order<?> dispose(String sessionId, int userId, String path) {
        try {
            int orderId = (Integer) authService.getSessionData(sessionId, EDITOR_ORDER_ID);
            AlbumOrder order = (AlbumOrder) repository.loadOrderWithLayout(orderId);

            String imagesPath = getImagesPath(userId);
            File folder = new File(imagesPath + "/" + path);
            String canonicalPath = folder.getCanonicalPath();
            if (!canonicalPath.contains(imagesPath))
                throw new AccessDeniedError();

            List<File> files = new ArrayList<File>();
            for (File file : folder.listFiles()) {
                files.add(file);
            }
            Collections.sort(files, new FileComparator());
            Iterator<File> iterator = files.iterator();
            while (iterator.hasNext()) {
                File file = iterator.next();
                if (!isJpeg(file))
                    iterator.remove();
            }

            int index = doDispose(userId, files, path, order);
            while (index < files.size()) {
                Album album = order.getProduct();
                int pageCount = order.getPageCount();
                if (pageCount >= album.getMaxPageCount())
                    break;

                int newPageCount = pageCount + album.getMultiplicity();
                order = (AlbumOrder) changePageCount(sessionId, userId, newPageCount);
                index = doDispose(userId, files, path, order);
            }

            if (order.isPackaged()) {
                order.getLayouts().size();
            }
            return order;
        } catch (Exception e) {
            return Exceptions.rethrow(e);
        }
    }

    private int doDispose(int userId, List<File> files, String path, AlbumOrder order) {
        try {
            Layout layout = order.getLayout();
            List<Page> pages = layout.getPages();

            Iterator<ImageReader> readers = ImageIO
                    .getImageReadersByFormatName("jpeg");
            ImageReader reader = readers.next();

            int i = 0;
            for (Page page : pages) {
                if (page.isBlocked()) {
                    continue;
                }

                if (i >= files.size()) {
                    break;
                }

                File file = files.get(i);
                String fileName = file.getName();
                String imagePath = path + "/" + fileName;

                if (page.getType() == PageType.NORMAL) {
                    doAddImage(page, ImageLayoutType.NORMAL, userId, imagePath);
                    i++;
                } else if (page.isSpreadOrFlyLeafPage()) {
                    ImageInputStream iis = ImageIO.createImageInputStream(file);
                    reader.setInput(iis, true);
                    float w = reader.getWidth(0);
                    float h = reader.getHeight(0);
                    float r = w / h;
                    iis.close();

                    float pw = page.getWidth();
                    float ph = page.getHeight();
                    float pr = 0.75f * pw / ph;

                    if (r > pr) {
                        doAddImage(page, ImageLayoutType.NORMAL, userId, imagePath);
                        i++;
                    } else {
                        doAddImage(page, ImageLayoutType.BACKGROUND_LEFT, userId, imagePath);
                        i++;
                        if (i < files.size()) {
                            file = files.get(i);
                            fileName = file.getName();
                            imagePath = path + "/" + fileName;

                            iis = ImageIO.createImageInputStream(file);
                            reader.setInput(iis, true);
                            w = reader.getWidth(0);
                            h = reader.getHeight(0);
                            r = w / h;
                            iis.close();

                            if (r < pr) {
                                doAddImage(page, ImageLayoutType.BACKGROUND_RIGHT, userId,
                                        imagePath);
                                i++;
                            }
                        }
                    }
                } else {
                    throw new RuntimeException(UNSUPPORTED_PAGE_TYPE_MSG);
                }
            }

            return i;
        } catch (Exception e) {
            return (Integer) Exceptions.rethrow(e);
        }
    }

    private boolean isJpeg(File file) {
        if (file.isDirectory())
            return false;

        String fileName = file.getName();
        if (fileName.startsWith("."))
            return false;

        return fileName.toLowerCase().endsWith(".jpg")
                || fileName.toLowerCase().endsWith(".jpeg");
    }

    private boolean isZip(File file) {
        if (file.isDirectory())
            return false;

        String fileName = file.getName();
        if (fileName.startsWith("."))
            return false;

        return fileName.toLowerCase().endsWith(".zip");
    }
}
