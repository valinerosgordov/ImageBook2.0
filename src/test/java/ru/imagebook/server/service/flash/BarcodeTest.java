package ru.imagebook.server.service.flash;

import java.awt.image.BufferedImage;

import net.sourceforge.barbecue.Barcode;
import ru.imagebook.server.service.render.barcode.BarcodeBuilder;
import ru.minogin.gfx.GraphicsUtil;
import ru.minogin.gfx.GraphicsUtil.ImageType;

public class BarcodeTest {
	public static void main(String[] args) throws Exception {
		BarcodeBuilder builder = new BarcodeBuilder();
		Barcode barcode = builder.createBarcode("352800-1");
		BufferedImage image = GraphicsUtil.createImage(600, 600, ImageType.JPEG);
		barcode.draw(image.createGraphics(), 100, 100);
		GraphicsUtil.saveJpeg(image, "d:/temp/barcode.jpg");
	}
}
