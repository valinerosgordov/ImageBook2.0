package ru.imagebook.server.service.flash;

import static ru.imagebook.server.service.editor.EditorConst.FONT_LOCATION;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.InputStream;

import net.sourceforge.barbecue.Barcode;

import org.springframework.core.io.ClassPathResource;

import ru.imagebook.server.service.render.RenderUtil;
import ru.imagebook.server.service.render.barcode.BarcodeBuilder;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.exception.Exceptions;

/**
 * BarcodeRenderer for Editor and Online-editor
 */
public class EditorBarcodeRenderer {
    public static final float WIDTH_MM = 33;
    public static final float HEIGHT_MM = 10;
    public static final float HEIGHT_WITHOUT_VENDOR_MM = 6;
    public static final float BARCODE_SHIFT_Y_MM = 4.8f;

    private final Graphics2D graphics;
    private final int x;
    private final int y;
    private final String number;
    private String label;
    private Color labelColor;

    public EditorBarcodeRenderer(Graphics2D graphics, int x, int y, String number) {
        this.graphics = graphics;
        this.x = x;
        this.y = y;
        this.number = number;
    }

    public EditorBarcodeRenderer(Graphics2D graphics, int x, int y, Order<?> order) {
        this(graphics, x, y, order.getNumber());

        User user = order.getUser();
        if (!user.isPhotographer()) {
            Vendor vendor = order.getUser().getVendor();
            setLabel(vendor.getSite(), Color.BLACK);
        }
    }

    private void setLabel(String label, Color color) {
        this.label = label;
        this.labelColor = color;
    }

    public void render() {
        try {
            int width = RenderUtil.mmToPx(WIDTH_MM);
            int height = RenderUtil.mmToPx(HEIGHT_MM);

            graphics.setBackground(Color.WHITE);

            int shiftY = 0;
            if (label == null) {
                height = RenderUtil.mmToPx(HEIGHT_WITHOUT_VENDOR_MM);
                shiftY = (y > 0) ? RenderUtil.mmToPx(HEIGHT_MM) - RenderUtil.mmToPx(HEIGHT_WITHOUT_VENDOR_MM) : 0;
                //shiftY = RenderUtil.mmToPx(HEIGHT_MM) - RenderUtil.mmToPx(HEIGHT_WITHOUT_VENDOR_MM);
            }
            graphics.fillRect(x, y + shiftY, width, height);

            BarcodeBuilder barcodeBuilder = new BarcodeBuilder();
            Barcode barcode = barcodeBuilder.createEditorBarcode(number);
            int barcodeWidth = barcode.getWidth();
            int barcodeX = x + (width - barcodeWidth) / 2;
            int barcodeYShift = (label != null) ? RenderUtil.mmToPx(BARCODE_SHIFT_Y_MM)
                : (shiftY + height - barcode.getHeight());
            int barcodeY = y + barcodeYShift;
            barcode.draw(graphics, barcodeX, barcodeY);

            if (label != null) {
                Object antiAliasing = graphics.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ClassPathResource resource = new ClassPathResource(FONT_LOCATION);
                InputStream is = resource.getInputStream();
                Font font = Font.createFont(Font.TRUETYPE_FONT, is);
                is.close();
                font = font.deriveFont(40f);
                graphics.setFont(font);
                FontMetrics fontMetrics = graphics.getFontMetrics();
                int labelWidth = fontMetrics.stringWidth(label);
                graphics.setColor(labelColor);
                int labelX = x + (width - labelWidth) / 2;
                int labelY = y + RenderUtil.mmToPx(3.5f);
                graphics.drawString(label, labelX, labelY);
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAliasing);
            }
        } catch (Exception e) {
            Exceptions.rethrow(e);
        }
    }
}
