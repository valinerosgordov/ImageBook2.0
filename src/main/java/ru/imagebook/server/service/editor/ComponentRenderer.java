package ru.imagebook.server.service.editor;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import ru.imagebook.server.service.flash.EditorBarcodeRenderer;
import ru.imagebook.server.service.render.RenderUtil;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.editor.Barcode;
import ru.imagebook.shared.model.editor.ComponentVisitor;
import ru.imagebook.shared.model.editor.Image;
import ru.imagebook.shared.model.editor.ImageLayoutType;
import ru.imagebook.shared.model.editor.Page;
import ru.imagebook.shared.model.editor.Position;
import ru.imagebook.shared.model.editor.SafeArea;
import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.gfx.GraphicsUtil;
import ru.minogin.gfx.GraphicsUtil.ImageType;

public class ComponentRenderer implements ComponentVisitor {
	private final Page page;
	private final int side;
	private final EditorUtil util;
	private final int userId;
	private final BufferedImage pageImage;
	private final Order<?> order;

	public ComponentRenderer(Page page, int side, EditorUtil util, int userId,
			BufferedImage pageImage, Order<?> order) {
		this.page = page;
		this.side = side;
		this.util = util;
		this.userId = userId;
		this.pageImage = pageImage;
		this.order = order;
	}

	@Override
	public void visit(Image image) {
		try {
			if (page.isSpreadOrFlyLeafPage()) {
				if (side == 0 && image.getLayoutType() == ImageLayoutType.BACKGROUND_RIGHT) {
					return;
				}
				if (side == 1 && image.getLayoutType() == ImageLayoutType.BACKGROUND_LEFT) {
					return;
				}
			}

			String imagePath = util.getImagePath(userId, image.getId());
			BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
			float width = bufferedImage.getWidth();
			float height = bufferedImage.getHeight();

			int x = (int) (image.getClipLeft() * width);
			int y = (int) (image.getClipTop() * height);
			int w = (int) (image.getClipWidth() * width);
			int h = (int) (image.getClipHeight() * height);
			BufferedImage subimage = bufferedImage.getSubimage(x, y, w, h);

			w = RenderUtil.mmToPx(image.getWidth());
			h = RenderUtil.mmToPx(image.getHeight());
//			java.awt.Image scaledImage = subimage.getScaledInstance(w, h,
//					BufferedImage.SCALE_SMOOTH);
			java.awt.Image scaledImage = GraphicsUtil.resize(subimage, ImageType.JPEG, w, h);

			float pageWidthMm = page.getWidth();
			float dxMm = page.getXMargin();
			if (page.isSpreadOrFlyLeafPage() && side == 1) {
                dxMm = -pageWidthMm / 2 + 2 * page.getXMargin();
            }
			float dyMm = page.getYMargin();
			x = RenderUtil.mmToPx(image.getLeft() + dxMm);
			y = RenderUtil.mmToPx(image.getTop() + dyMm);
			pageImage.getGraphics().drawImage(scaledImage, x, y, null);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}

	@Override
	public void visit(Barcode barcode) {
		try {
			// BarcodeBuilder builder = new BarcodeBuilder();
			// net.sourceforge.barbecue.Barcode bc =
			// builder.createBarcode(order.getNumber());
			int x = RenderUtil.mmToPx(barcode.getLeft() + page.getXMargin());
			int y = RenderUtil.mmToPx(barcode.getTop() + page.getYMargin());
			Graphics2D graphics = pageImage.createGraphics();
			EditorBarcodeRenderer barcodeRenderer = new EditorBarcodeRenderer(graphics, x, y, order);
			barcodeRenderer.render();

			// bc.draw(graphics, x, y);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}

	@Override
	public void visit(Position position) {}

	@Override
	public void visit(SafeArea safeArea) {}
}
