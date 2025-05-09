package ru.imagebook.server.service.render.barcode;

import java.awt.Font;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.env.EnvironmentFactory;

import static ru.imagebook.server.service.editor.EditorConst.EDITOR_BARCODE_BAR_HEIGHT;
import static ru.imagebook.server.service.editor.EditorConst.EDITOR_BARCODE_BAR_WIDTH;
import static ru.imagebook.server.service.editor.EditorConst.EDITOR_BARCODE_FONT;
import static ru.imagebook.server.service.editor.EditorConst.EDITOR_BARCODE_RESOLUTION;
import static ru.imagebook.server.service.editor.EditorConst.DEFAULT_BARCODE_BAR_HEIGHT;
import static ru.imagebook.server.service.editor.EditorConst.DEFAULT_BARCODE_BAR_WIDTH;
import static ru.imagebook.server.service.editor.EditorConst.DEFAULT_BARCODE_FONT;
import static ru.imagebook.server.service.editor.EditorConst.DEFAULT_BARCODE_RESOLUTION;
import static ru.imagebook.server.service.editor.EditorConst.FONT_LOCATION;
import ru.imagebook.server.service.render.RenderUtil;
import ru.minogin.core.client.exception.Exceptions;

public class BarcodeBuilder {
    /**
     * Create barcode with default settings
     *
     * @param number -- order number
     * @return barcode
     */
    public Barcode createBarcode(String number) {
        try {
            Barcode barcode = BarcodeFactory.createCode128(number);
            ClassPathResource resource = new ClassPathResource(FONT_LOCATION);
            InputStream is = resource.getInputStream();
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            is.close();
            font = font.deriveFont(DEFAULT_BARCODE_FONT);
            barcode.setFont(font);
            barcode.setBarWidth(DEFAULT_BARCODE_BAR_WIDTH);
            barcode.setBarHeight(RenderUtil.mmToPx(DEFAULT_BARCODE_BAR_HEIGHT));
            barcode.setResolution(DEFAULT_BARCODE_RESOLUTION);
            return barcode;
        } catch (Exception e) {
            return Exceptions.rethrow(e);
        }
    }

    /**
     * Create barcode for Editor and Online-editor
     *
     * @param number -- order number
     * @return barcode
     */
    public Barcode createEditorBarcode(String number) {
        try {
            EnvironmentFactory.setDefaultEnvironment(new ImagebookEnvironment());

            Barcode barcode = BarcodeFactory.createCode128(number);
            ClassPathResource resource = new ClassPathResource(FONT_LOCATION);
            InputStream is = resource.getInputStream();
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            is.close();
            font = font.deriveFont(EDITOR_BARCODE_FONT);
            barcode.setFont(font);
            barcode.setBarWidth(EDITOR_BARCODE_BAR_WIDTH);
            barcode.setBarHeight(RenderUtil.mmToPx(EDITOR_BARCODE_BAR_HEIGHT));
            barcode.setResolution(EDITOR_BARCODE_RESOLUTION);
            return barcode;
        } catch (Exception e) {
            return Exceptions.rethrow(e);
        }
    }
}
