package ru.imagebook.server.service.pdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import ru.imagebook.server.service.editor.EditorConst;

/**
 * Pdf sheet
 * 
 * @author liosha
 */
public class ImposerSheet {
    private List<ImposerPage> pages = new ArrayList<ImposerPage>();
    private Font font;

    public ImposerSheet() throws IOException, DocumentException {
        initFontForPdfTable();
    }

    private void initFontForPdfTable() throws DocumentException, IOException {
        BaseFont bf = BaseFont.createFont(
                BaseFont.TIMES_ROMAN,
                BaseFont.CP1252,
                BaseFont.EMBEDDED);
        this.font = new Font(bf, 4);
        this.font.setColor(BaseColor.WHITE);
        //font.setStyle(Font.BOLD);
    }

    public ImposerSheet(ImposerPage... pages) throws IOException, DocumentException {
        for (ImposerPage page : pages) {
            this.pages.add(page);
        }
        initFontForPdfTable();
    }

    /**
     * Impose all pages of sheet to document
     * 
     * @param document
     *            {@link Document}
     * @param pdfWriter
     *            {@link PdfWriter}
     * @param reader
     *            {@link PdfReader}
     * @param imposerConfig
     *            {@link ImposerConfig} configurator for drawing lines on sheet
     * @param pageNumber
     *            {@link Integer}
     * @return {@link Document}
     * @throws BadElementException
     * @throws DocumentException
     */
    public Document imposeSheet(Document document, PdfWriter pdfWriter, PdfReader reader, ImposerConfig imposerConfig)
            throws BadElementException, DocumentException, IOException {
        // Sheet
        for (ImposerPage page : pages) {
            document.add(page.imposePage(pdfWriter, reader));
        }

        // Draw lines
        List<IFigure> figures = imposerConfig.getLines();
        if (figures != null) {
            PdfContentByte cb = pdfWriter.getDirectContent();
            cb.setLineWidth(imposerConfig.getLineWidth());

            for (IFigure figure : figures) {
                figure.draw(cb);
            }
            cb.stroke();

            if (imposerConfig.isShowFormatColorScheme()) {
                drawFormatColorScheme(pdfWriter, imposerConfig, cb);
            }
        }
        return document;
    }

    private void drawFormatColorScheme(PdfWriter pdfWriter, ImposerConfig imposerConfig, PdfContentByte cb) throws IOException, DocumentException {
        float left, bottom;
        final boolean isOddPage = pdfWriter.getPageNumber() % 2 == 1;
        final boolean isAlbum = pdfWriter.getPageSize().getHeight() < pdfWriter.getPageSize().getWidth();
        if (isAlbum) {
            left =   isOddPage ? pdfWriter.getPageSize().getWidth() - (3 * PdfConst.MM_TO_PX) : 0;
            bottom = (40 * PdfConst.MM_TO_PX);
        } else {
            left =   isOddPage ? pdfWriter.getPageSize().getWidth() - (40 * PdfConst.MM_TO_PX) : 0;
            bottom = pdfWriter.getPageSize().getHeight();
        }
        final PdfPTable table = new PdfPTable(isAlbum ? 1 : 2);
        if (isAlbum) {
            table.setTotalWidth(3*PdfConst.MM_TO_PX);
        } else {
            table.setTotalWidth(40 * PdfConst.MM_TO_PX);
        }

        final PdfPCell cell1 = getPdfPCell(imposerConfig, isOddPage, isAlbum, isOddPage);
        table.addCell(cell1);

        final PdfPCell cell2 = getPdfPCell(imposerConfig, !isOddPage, isAlbum, isOddPage);
        table.addCell(cell2);

        table.completeRow();

        table.writeSelectedRows(0, -1, left, bottom, cb);

        if (isAlbum) {
            Rectangle rectangle;
            if (isOddPage)
                rectangle = new Rectangle(left - (20 * PdfConst.MM_TO_PX), 0,  pdfWriter.getPageSize().getWidth(), 3*PdfConst.MM_TO_PX);
            else
                rectangle = new Rectangle(0,  0, (20 * PdfConst.MM_TO_PX), 3*PdfConst.MM_TO_PX);
            rectangle.setBackgroundColor(cell2.getBackgroundColor());
            cb.rectangle(rectangle);
            cb.stroke();
        } else {
            Rectangle rectangle;
            if (isOddPage)
                rectangle = new Rectangle(pdfWriter.getPageSize().getWidth() - (3 * PdfConst.MM_TO_PX), pdfWriter.getPageSize().getHeight(),  pdfWriter.getPageSize().getWidth(), pdfWriter.getPageSize().getHeight() - (20*PdfConst.MM_TO_PX));
            else
                rectangle = new Rectangle(0,  pdfWriter.getPageSize().getHeight(), (3 * PdfConst.MM_TO_PX), pdfWriter.getPageSize().getHeight() - (20*PdfConst.MM_TO_PX));
            rectangle.setBackgroundColor(isOddPage ? cell2.getBackgroundColor() : cell1.getBackgroundColor());
            cb.rectangle(rectangle);
            cb.stroke();
        }
    }

    private PdfPCell getPdfPCell(ImposerConfig imposerConfig, final boolean isOddCell, final boolean isAlbum, final boolean isOddPage) throws IOException, DocumentException {
        final PdfPCell cell;

        //Float padding = 5f;
        
        if (!isAlbum) {
            Phrase content = new Phrase(String.format("%02d", isOddCell ? imposerConfig.getProductNumber() : imposerConfig.getProductType()), font);
            cell = new PdfPCell(content);
            /*Float fontSize = content.getFont().getSize();
            Float capHeight = content.getFont().getBaseFont().getFontDescriptor(BaseFont.CAPHEIGHT, fontSize);
            //cell.setPadding(padding);
            cell.setPaddingTop(capHeight - fontSize + padding);*/
            cell.setHorizontalAlignment(PdfContentByte.ALIGN_CENTER);
            cell.setUseBorderPadding(true);
            final ImposerColor imposerColor = ImposerColor.getImposerColorByProduct((isOddCell ? imposerConfig.getProductNumber() : imposerConfig.getProductType()), !isOddCell);
            if (imposerColor != null) {
                cell.setBackgroundColor(imposerColor.getBaseColor());
            }
        } else {
            int content;
            boolean isProductType;
            if (isOddPage) {
                content = isOddCell ? imposerConfig.getProductNumber() : imposerConfig.getProductType();
                isProductType = !isOddCell;
            } else {
                content = !isOddCell ? imposerConfig.getProductNumber() : imposerConfig.getProductType();
                isProductType = isOddCell;
            }
            Phrase phrase = new Phrase(String.format("%02d", content), font);
            cell = new PdfPCell(phrase);
            cell.setFixedHeight(20 * PdfConst.MM_TO_PX);
            cell.setHorizontalAlignment(PdfContentByte.ALIGN_CENTER);
            cell.setRotation(isOddPage ? 270 : 90);
            final ImposerColor imposerColor = ImposerColor.getImposerColorByProduct(content, isProductType);
            if (imposerColor != null) {
                cell.setBackgroundColor(imposerColor.getBaseColor());
            }
            /*cell.setPaddingLeft(2);
            cell.setPaddingRight(1);*/
            
        } 
        
        return cell;
    }

    public void addPage(ImposerPage page) {
        pages.add(page);
    }

    public void removePage(ImposerPage page) {
        pages.remove(page);
    }

    public List<ImposerPage> getPages() {
        return pages;
    }

    public void setPages(List<ImposerPage> pages) {
        this.pages = pages;
    }
}