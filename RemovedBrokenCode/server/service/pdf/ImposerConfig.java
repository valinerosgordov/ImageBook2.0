package ru.imagebook.server.service.pdf;

import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.ProductType;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration class for imposing
 * 
 * @author liosha
 */
public class ImposerConfig {
    private float lineWidth = 0f;
    private List<IFigure> figures = new ArrayList<IFigure>();
    private int productType;
    private int productNumber;
    private boolean showFormatColorScheme = true;

    public ImposerConfig() {
    }

    public ImposerConfig(float lineWidth, final int productType, final int productNumber) {
        this.lineWidth = lineWidth;
        this.productNumber = productNumber;
        this.productType = productType;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    public List<IFigure> getLines() {
        return figures;
    }

    public void setLines(List<IFigure> lines) {
        this.figures = lines;
    }

    public void addFigure(IFigure line) {
        this.figures.add(line);
    }

    public void removeLine(IFigure line) {
        this.figures.remove(line);
    }

    public int getProductType() { return productType; }

    public int getProductNumber() { return productNumber; }

    public boolean isShowFormatColorScheme() {
        return showFormatColorScheme;
    }

    public void setShowFormatColorScheme(boolean showFormatColorScheme) {
        this.showFormatColorScheme = showFormatColorScheme;
    }
}