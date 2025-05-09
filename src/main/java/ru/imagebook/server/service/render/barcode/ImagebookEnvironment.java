package ru.imagebook.server.service.render.barcode;

import java.awt.Font;

import net.sourceforge.barbecue.env.Environment;

public class ImagebookEnvironment implements Environment {
    /**
     * The default output resolution (in DPI) for barcodes in headless mode.
     */
    public static final int DEFAULT_RESOLUTION = 96;

    @Override
    public int getResolution() {
        return DEFAULT_RESOLUTION;
    }

    @Override
    public Font getDefaultFont() {
        return null;
    }
}
