package ru.minogin.gfx;

import org.apache.commons.io.IOUtils;
import ru.minogin.util.server.file.FileUtil;
import ru.minogin.util.server.io.IOUtil;
import ru.minogin.util.shared.exceptions.Exceptions;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.Iterator;

import static ru.minogin.util.shared.math.MathUtil.round;

/** Utilities for working with raster images.
 *
 * @author Andrey Minogin 2012 */
public class GraphicsUtil {
    public static final int INITIAL_BUFSIZE = 4096;

    public enum ImageType {
        JPEG, PNG;
    }

    public static BufferedImage createImage(int width, int height, ImageType type) {
        return new BufferedImage(width, height, convertImageType(type));
    }

    private static int convertImageType(ImageType type) {
        if (type == ImageType.JPEG)
            return BufferedImage.TYPE_INT_RGB;
        else if (type == ImageType.PNG)
            return BufferedImage.TYPE_INT_ARGB;
        else
            throw new RuntimeException("Unknown image type: " + type);
    }

    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (Exception e) {
            return Exceptions.rethrow(e);
        }
    }

    /** <p>This method should be used instead of
     * {@link Image#getScaledInstance(int, int, int)} as it is much faster giving
     * the same quality.</p>
     * <p>See <a href=
     * "http://today.java.net/pub/a/today/2007/04/03/perils-of-image-getscaledinstance.html"
     * >this link</a> for more information.</p> */
    public static BufferedImage resize(BufferedImage image, ImageType type, int newWidth, int newHeight) {
        BufferedImage result = image;
        int w = image.getWidth();
        int h = image.getHeight();

        do {
            if (w > newWidth) {
                w = w / 2;
                h = h / 2;
                if (w < newWidth) {
                    w = newWidth;
                    h = newHeight;
                }
            } else {
                w = newWidth;
                h = newHeight;
            }

            BufferedImage tmp = new BufferedImage(w, h, convertImageType(type));
            Graphics2D g = tmp.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(result, 0, 0, w, h, null);
            g.dispose();

            result = tmp;
        }
        while (w != newWidth);

        return result;
    }

    /** Scale image to the given width preserving aspect ratio. */
    public static BufferedImage scaleToNewWidth(BufferedImage image, ImageType type, int newWidth) {
        double w = image.getWidth();
        double h = image.getHeight();

        double newW = newWidth;
        double newH = newW * h / w;

        return resize(image, type, round(newW), round(newH));
    }

    /** Scales image to the given height preserving aspect ratio. */
    public static BufferedImage scaleToNewHeight(BufferedImage image, ImageType type, int newHeight) {
        double w = image.getWidth();
        double h = image.getHeight();

        double newH = newHeight;
        double newW = newH * w / h;

        return resize(image, type, round(newW), round(newH));
    }

    public static BufferedImage convertPngToJpeg(BufferedImage pngImage) {
        BufferedImage jpegImage = new BufferedImage(pngImage.getWidth(), pngImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = jpegImage.createGraphics();
        graphics.drawImage(pngImage, 0, 0, null);
        return jpegImage;
    }

    public static void saveJpeg(BufferedImage jpegImage, String path) {
        try {
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
            ImageWriter writer = writers.next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(1);

            FileImageOutputStream os = new FileImageOutputStream(new File(path));
            writer.setOutput(os);
            IIOImage iioImage = new IIOImage(jpegImage, null, null);
            writer.write(null, iioImage, param);
            os.flush();
            writer.dispose();
            os.close();
        } catch (Exception e) {
            Exceptions.rethrow(e);
        }
    }

    public static void savePng(BufferedImage pngImage, String path) {
        try {
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("png");
            ImageWriter writer = writers.next();
            ImageWriteParam param = writer.getDefaultWriteParam();

            FileImageOutputStream os = new FileImageOutputStream(new File(path));
            writer.setOutput(os);
            IIOImage iioImage = new IIOImage(pngImage, null, null);
            writer.write(null, iioImage, param);
            os.flush();
            writer.dispose();
            os.close();
        } catch (Exception e) {
            Exceptions.rethrow(e);
        }
    }

    public static void sendImage(String path, HttpServletResponse response) {
        try {
            String ext = FileUtil.getExtension(path);
            if (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg"))
                response.setContentType("image/jpeg");
            else if (ext.equalsIgnoreCase("png"))
                response.setContentType("image/png");
            else
                throw new RuntimeException("Unknown image format: " + ext);

            response.setContentLength((int) new File(path).length());
            IOUtil.copy(path, response.getOutputStream());
        } catch (Exception e) {
            Exceptions.rethrow(e);
        }
    }

    /** If possible use {@link #sendImage(String, HttpServletResponse)} instead of
     * this method due to overhead caused by determining image size. */
    public static void sendImage(BufferedImage image, ImageType type, HttpServletResponse response) {
        try {
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream(INITIAL_BUFSIZE);
            MemoryCacheImageOutputStream imageOutputStream = new MemoryCacheImageOutputStream(byteOutputStream);

            ImageWriter writer;
            ImageWriteParam param;
            if (type == ImageType.JPEG) {
                response.setContentType("image/jpeg");

                Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
                writer = writers.next();
                param = writer.getDefaultWriteParam();
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(1);
            } else if (type == ImageType.PNG) {
                response.setContentType("image/png");

                Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("png");
                writer = writers.next();
                param = writer.getDefaultWriteParam();
            } else
                throw new RuntimeException("Unknown image type: " + type);

            writer.setOutput(imageOutputStream);
            IIOImage iioImage = new IIOImage(image, null, null);
            writer.write(null, iioImage, param);
            byteOutputStream.flush();
            writer.dispose();

// ImageIO.write(image, getFormatName(type), byteOutputStream);

            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteOutputStream.toByteArray());
            OutputStream os = response.getOutputStream();
            response.setContentLength(byteOutputStream.size());
            IOUtils.copy(byteInputStream, os);

            byteOutputStream.close();
            byteInputStream.close();
        } catch (Exception e) {
            Exceptions.rethrow(e);
        }
    }
}
