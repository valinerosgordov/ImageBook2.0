package ru.minogin.core.server.awt;

import java.io.File;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import ru.minogin.core.client.exception.Exceptions;

public class ImageUtil {
	public static ImageSize getImageSize(File imageFile) {
		try {
			Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("jpeg");
			ImageReader reader = readers.next();
			ImageInputStream iis = ImageIO.createImageInputStream(imageFile);
			reader.setInput(iis, true);
			ImageSize size = new ImageSize(reader.getWidth(0), reader.getHeight(0));
			iis.close();

			return size;
		}
		catch (Exception e) {
			return Exceptions.rethrow(e);
		}
	}
}
