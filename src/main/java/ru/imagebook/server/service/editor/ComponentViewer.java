package ru.imagebook.server.service.editor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import static ru.imagebook.server.service.flash.EditorBarcodeRenderer.HEIGHT_MM;
import static ru.imagebook.server.service.flash.EditorBarcodeRenderer.HEIGHT_WITHOUT_VENDOR_MM;
import static ru.imagebook.server.service.flash.EditorBarcodeRenderer.WIDTH_MM;
import ru.imagebook.server.service.flash.EditorBarcodeRenderer;
import ru.imagebook.server.service.render.RenderUtil;
import ru.imagebook.shared.model.AlbumOrder;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.editor.Barcode;
import ru.imagebook.shared.model.editor.ComponentVisitor;
import ru.imagebook.shared.model.editor.Image;
import ru.imagebook.shared.model.editor.Position;
import ru.imagebook.shared.model.editor.SafeArea;
import ru.minogin.core.client.exception.Exceptions;

public class ComponentViewer implements ComponentVisitor {
	private final int userId;
	private final OutputStream outputStream;
	private final EditorUtil util;
	private final AlbumOrder order;

	public ComponentViewer(AlbumOrder order, int userId,
			OutputStream outputStream, EditorUtil util) {
		this.order = order;
		this.userId = userId;
		this.outputStream = outputStream;
		this.util = util;
	}

	@Override
	public void visit(Image image) {
		try {
			String screenPath = util.getScreenPath(userId, image.getId());
			BufferedImage bufferedImage = ImageIO.read(new File(screenPath));
			double imageWidth = bufferedImage.getWidth();
			double imageHeight = bufferedImage.getHeight();
			int x = (int) (image.getClipLeft() * imageWidth);
			int y = (int) (image.getClipTop() * imageHeight);
			int width = (int) (image.getClipWidth() * imageWidth);
			int height = (int) (image.getClipHeight() * imageHeight);

			BufferedImage clippedImage = bufferedImage.getSubimage(x, y, width,	height);
			ImageIO.write(clippedImage, "jpeg",	new BufferedOutputStream(outputStream));
		} catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}

	@Override
	public void visit(Barcode barcode) {
		try {
			// BarcodeBuilder builder = new BarcodeBuilder();
			// net.sourceforge.barbecue.Barcode bc =
			// builder.createBarcode(order.getNumber());
			// int width = bc.getWidth();
			// int height = bc.getHeight();

            User user = order.getUser();
            int width = RenderUtil.mmToPx(WIDTH_MM);
			int height = RenderUtil.mmToPx(user.isPhotographer() ? HEIGHT_WITHOUT_VENDOR_MM : HEIGHT_MM);
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = image.createGraphics();
			graphics.setBackground(Color.WHITE);
			graphics.clearRect(0, 0, width, height);

            EditorBarcodeRenderer barcodeRenderer = new EditorBarcodeRenderer(graphics, 0, 0, order);
			barcodeRenderer.render();

			// bc.draw(graphics, 0, 0);
			ImageIO.write(image, "jpeg", new BufferedOutputStream(outputStream));
		} catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}

	@Override
	public void visit(Position position) {}

	@Override
	public void visit(SafeArea safeArea) {}
}
