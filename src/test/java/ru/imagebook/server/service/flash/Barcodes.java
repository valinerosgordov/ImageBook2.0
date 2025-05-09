package ru.imagebook.server.service.flash;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import net.sourceforge.barbecue.Barcode;

import org.apache.commons.io.FileUtils;

import ru.imagebook.server.service.render.barcode.BarcodeBuilder;
import ru.minogin.oo.server.OOClient;
import ru.minogin.oo.server.text.Table;
import ru.minogin.oo.server.text.TextDoc;

public class Barcodes {
	public static void main(String[] args) throws Exception {
// Collection<String> billIds = new XArrayList<String>(
// "10681",
// "10686",
// "10695",
// "10696",
// "10701",
// "10718",
// "10705",
// "10707"
// );

		String inputPath = "d:/temp/barcodes.txt";
		String templatePath = "d:/temp/im/template";
		String tempPath = "d:/temp/im/temp";

		List<String> billIds = FileUtils.readLines(new File(inputPath));

		OOClient ooClient = new OOClient();
		ooClient.connect();
		TextDoc doc = ooClient.openTextDoc(templatePath + "/barcodes.doc");
		Table table = doc.getTable(0);

		int y = 0;
		int x = 0;
		for (String billId : billIds) {
			BarcodeBuilder barcodeBuilder = new BarcodeBuilder();
			Barcode barcode = barcodeBuilder.createBarcode(billId);
			int w = barcode.getWidth();
			int h = barcode.getHeight() + 50;
			BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = (Graphics2D) image.getGraphics();
			graphics.setBackground(Color.WHITE);
			graphics.clearRect(0, 0, w, h);
			barcode.draw(graphics, 0, 0);
			String imagePath = tempPath + "/" + UUID.randomUUID().toString() + ".jpg";
			ImageIO.write(image, "JPEG", new File(imagePath));

			int scale = 12;
			table.insertImage(y, x, imagePath, w * scale, h * scale);
			x++;
			if (x == 4) {
				x = 0;
				y++;
				table.addRow();
			}
		}

		String path = "d:/temp/barcodes.doc";
		doc.saveAsDoc(path);
		doc.close();

		System.out.println("DONE");
	}
}
