package ru.imagebook.server.service.pdf;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Page One sheet can contains few pages
 * 
 * @author liosha
 */
public class ImposerPage {
    private int rotationDegrees = 0;
    private int pageNumber = 0;
    private ImposerPageConfig pageConfig = null;

    public ImposerPage() {
    }

    public ImposerPage(int rotationDegrees, int pageNumber, ImposerPageConfig pageConfig) {
        this.rotationDegrees = rotationDegrees;
        this.pageNumber = pageNumber;
        this.pageConfig = pageConfig;
    }

    /**
     * 
     * @param pdfWriter
     * @param reader
     * @param pageNumber
     * @return
     * @throws BadElementException
     */
    public Image imposePage(PdfWriter pdfWriter, PdfReader reader) throws BadElementException {
        Image image = null;
        PdfImportedPage importedPage = pdfWriter.getImportedPage(reader, pageNumber);
        importedPage.setBoundingBox(new Rectangle(pageConfig.getGabx(), pageConfig.getGaby(), pageConfig
                .getBlockWidth(), pageConfig.getBlockHeight()));
        image = Image.getInstance(importedPage);
        image.setAbsolutePosition(pageConfig.getX(), pageConfig.getY());
        image.setRotationDegrees(rotationDegrees);
        return image;
    }

    public int getRotationDegrees() {
        return rotationDegrees;
    }

    public void setRotationDegrees(int rotationDegrees) {
        this.rotationDegrees = rotationDegrees;
    }

    public ImposerPageConfig getPageConfig() {
        return pageConfig;
    }

    public void setPageConfig(ImposerPageConfig pageConfig) {
        this.pageConfig = pageConfig;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}