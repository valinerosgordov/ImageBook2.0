package ru.imagebook.server.service.pdf;

import com.itextpdf.text.pdf.PdfContentByte;

/**
 * @author liosha Interface for figures
 */
public interface IFigure {
    void draw(PdfContentByte contentByte);
}