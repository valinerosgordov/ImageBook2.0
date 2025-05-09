package ru.imagebook.server.service2.pdf;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;

import static ru.imagebook.server.service.pdf.PdfUtil.isCoverPdf;
import static ru.imagebook.server.service.pdf.PdfUtil.isNonImposedPdf;
import ru.imagebook.server.repository2.PdfTransferRepository;
import ru.imagebook.server.service.pdf.PdfConfig;
import ru.imagebook.server.service.pdf.PdfUtil;
import ru.imagebook.server.service.pdf.VendorPdfConfig;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderState;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.server.ftp.XFtpClient;
import ru.minogin.core.server.ftp.XFtpClientFactory;

public class PdfTransferEngineImpl implements PdfTransferEngine {
	private static final Logger LOG = Logger.getLogger(PdfTransferService.class);
	private static final String DATE_FORMAT = "dd.MM.yyyy_HH.mm";

	protected PdfConfig config;
	protected PdfUtil util;

	@Autowired
	private PdfTransferRepository repository;

	@Autowired
	private VendorPdfConfig vendorPdfConfig;

	@Autowired
	private MessageSource messages;

    private XFtpClientFactory ftpClientFactory = new XFtpClientFactory();

	@Autowired
	public PdfTransferEngineImpl(PdfConfig config) {
		this.config = config;
		this.util = new PdfUtil(config);
	}

	@Transactional
	@Override
	public void transferOrder(int orderId) {
		Order<?> order = repository.getOrder(orderId);
        LOG.debug("Transferring order: " + order.getNumber());

        Path pdfDir = Paths.get(config.getPdfPath());
        String orderPdfFilesPattern = order.getNumber() + "{_*,-*_*}.pdf";

        List<File> mainFiles = new ArrayList<>();
        List<File> coverFiles = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(pdfDir, orderPdfFilesPattern)) {
			for (Path entry: stream) {
				File file = entry.toFile();

				if (isNonImposedPdf(file.getName())) {
				    continue;
                }

				if (isCoverPdf(file.getName())) {
					coverFiles.add(file);
				} else {
				    mainFiles.add(file);
                }
			}
		} catch (IOException e) {
			Exceptions.rethrow(e);
		}

		transferPdfToVendor(order, mainFiles, coverFiles);

		order.setState(OrderState.PRINTING);
		LOG.debug("Order transferred successfully: " + order.getNumber());
	}

    private void transferPdfToVendor(Order<?> order, List<File> mainFiles, List<File> coverFiles) {
        User user = order.getUser();
        Vendor vendor = user.getVendor();

        Album album = (Album) order.getProduct();
        String typeName = album.getName().get(Locales.RU);

        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        String orderMainDir = df.format(order.getPrintDate());
        String orderUrgentTypeDir = getOrderUrgentTypeDirName(order);

        if (vendor.isPrinter()) {
            File base = new File(vendorPdfConfig.getPath(), vendor.getKey());
            base = new File(base, orderMainDir);
            File urgentTypeDir = new File(base, orderUrgentTypeDir);
            base = new File(urgentTypeDir, typeName);
            base.mkdirs();

            transferFilesToFS(base, mainFiles);

            if (album.isSeparateCover()) {
                if (album.isPlotterCover()) {
                    base = new File(urgentTypeDir, getPlotterCoverDirName());
                } else if (album.isRicohCover()) {
                    base = new File(urgentTypeDir, getRicohCoverDirName());
                } else if (album.isLeatheretteCover()) {
                    base = new File(urgentTypeDir, getLeatheretteCoverDirName());
                }
                transferFilesToFS(base, coverFiles);
            }
        } else {
            XFtpClient client = ftpClientFactory.createClient();
            try {
                client.connect(config.getHost(), config.getUser(), config.getPassword());

                String toFolder = orderMainDir;
                client.mkdir(toFolder);
                client.cd(toFolder);

                toFolder = orderUrgentTypeDir;
                client.mkdir(toFolder);
                client.cd(toFolder);

                client.mkdir(typeName);
                client.cd(typeName);

                transferFilesToFTP(client, mainFiles);

                if (album.isSeparateCover()) {
                    if (album.isPlotterCover()) {
                        String parentDir = "/" + orderMainDir + "/" + orderUrgentTypeDir;
                        client.cd(parentDir);
                        client.mkdir(getPlotterCoverDirName());
                        client.cd(getPlotterCoverDirName());
                    } else if (album.isRicohCover()) {
                        String parentDir = "/" + orderMainDir + "/" + orderUrgentTypeDir;
                        client.cd(parentDir);
                        client.mkdir(getRicohCoverDirName());
                        client.cd(getRicohCoverDirName());
                    } else if (album.isLeatheretteCover()) {
                        String parentDir = "/" + orderMainDir + "/" + orderUrgentTypeDir;
                        client.cd(parentDir);
                        client.mkdir(getLeatheretteCoverDirName());
                        client.cd(getLeatheretteCoverDirName());
                    }
                    transferFilesToFTP(client, coverFiles);
                }
            } catch (Exception e) {
                Exceptions.rethrow(e);
            } finally {
                client.disconnect();
            }
        }

        order.setState(OrderState.PRINTING);
        LOG.debug("Order transferred succefully: " + order.getNumber());
    }

    private void transferFilesToFS(File base, List<File> files) {
	    try {
            for (File srcFile : files) {
                File tempFile = new File(base, UUID.randomUUID().toString());
                FileUtils.copyFile(srcFile, tempFile);
                File file = new File(base, srcFile.getName());
                file.delete();
                tempFile.renameTo(file);
            }
        } catch (IOException e) {
            Exceptions.rethrow(e);
        }
    }

    private void transferFilesToFTP(XFtpClient client, List<File> files) {
        for (File srcFile : files) {
            String tempFileName = UUID.randomUUID().toString();
            client.saveFile(srcFile.getPath(), tempFileName);
            client.deleteFileIfExists(srcFile.getName());
            client.rename(tempFileName, srcFile.getName());
        }
    }

    private String getOrderUrgentTypeDirName(Order<?> order) {
        return order.isUrgent()
                ? messages.getMessage("urgentOrders", null,  new Locale(Locales.RU))
                : messages.getMessage("commonOrders", null,  new Locale(Locales.RU));
    }

    private String getPlotterCoverDirName() {
		return messages.getMessage("plotterCoverDirName", null, new Locale(Locales.RU));
	}

	private String getRicohCoverDirName() {
        return messages.getMessage("ricohCoverDirName", null, new Locale(Locales.RU));
    }

    private String getLeatheretteCoverDirName() {
	    return messages.getMessage("leatheretteCoverDirName", null, new Locale(Locales.RU));
    }

	@Transactional
	@Override
	public void resetOrderState(int orderId) {
		Order<?> order = repository.getOrder(orderId);
		order.setState(OrderState.READY_TO_TRANSFER_PDF);
	}
}
