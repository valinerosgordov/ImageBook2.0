package ru.imagebook.server.service.pickbook;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.server.model.importing.XFtp;
import ru.imagebook.server.service.editor.EditorTxService;
import ru.imagebook.server.service.flash.FlashConfig;
import ru.imagebook.server.service.flash.FlashPath;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.Order;
import ru.minogin.core.server.ftp.XFtpClient;


@Service
public class PickbookHelperImpl implements PickbookHelper {
    private static final Logger LOG = Logger.getLogger(PickbookHelperImpl.class);

	@Autowired
    private FlashConfig flashConfig;

    @Autowired
    private EditorTxService editorTxService;

	@Transactional
	@Override
	public void downloadJpegs(Order order, XFtp ftp, String jpegFolder) {
		FlashPath flashPath = new FlashPath(flashConfig);

		XFtpClient client = new XFtpClient();
        LOG.debug("Connecting");
        client.connect(ftp.getFtpHost(), ftp.getFtpUser(), ftp.getFtpPassword());
        LOG.debug("Connected");

        try {
            LOG.debug(String.format("Downloading JPEGs [orderId=%s, importId=%s]", order.getId(), order.getImportId()));
            client.cd(jpegFolder);

            String jpegDir = flashPath.getJpegDir(order);
            new File(jpegDir).mkdirs();
            for (int page = 1; page <= order.getPageCount(); page++) {
                String fileName = page + ".jpg";
                String jpegPath = flashPath.getJpegDir(order) + "/" + page + ".jpg";
                client.loadFile(fileName, jpegPath);
            }

            Album album = (Album) order.getProduct();
            if (album.isSeparateCover()) {
                client.loadFile("c.jpg", flashPath.getJpegDir(order) + "/" + "c.jpg");
            }

            editorTxService.setJpegGenerated(order.getId());

            LOG.debug(String.format("JPEGs downloaded [orderId=%s, importId=%s]", order.getId(), order.getImportId()));
        } catch (Exception e) {
            String errorMessage = String.format("Failed to download JPEGs from ftp [orderId=%s, importId=%s]",
                order.getId(), order.getImportId());
            LOG.error(errorMessage, e);

            editorTxService.setJpegGenerationError(order.getId());
        } finally {
            client.disconnect();
        }
	}
}
