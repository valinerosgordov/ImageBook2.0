package ru.imagebook.server.service.flash;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.AlbumImpl;
import ru.imagebook.shared.model.AlbumOrder;
import ru.imagebook.shared.model.AlbumOrderImpl;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;

public class BarcodeRendererRunner {
	public static void main(String[] args) throws Exception {
		BufferedImage image = new BufferedImage(800, 800,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();

		Vendor vendor = new Vendor();
		User user = new User();
		user.setVendor(vendor);

		Album album = new AlbumImpl();
		AlbumOrder order = new AlbumOrderImpl(album);
		order.setNumber("123456-78");
		order.setUser(user);

		EditorBarcodeRenderer renderer = new EditorBarcodeRenderer(graphics, 100, 100, order);
		renderer.render();
//		ImageIO.write(image, "jpeg", new File("d:/temp/bc.jpg"));
		ImageIO.write(image, "png", new File("d:/temp/bc.png"));
		System.out.println("DONE");
	}
}
