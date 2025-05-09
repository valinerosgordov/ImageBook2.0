package ru.imagebook.server.tools.xwpf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;

import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.oo.shared.IllegalPathException;

public class XWPFUtils {
    
    public static XWPFDocument getDocument(String docPath) {
        File file = new File(docPath);
        if (!file.exists()) {
            throw new IllegalPathException("The Word document " + file + " does not exist.");
        }

        XWPFDocument doc = null;
        FileInputStream fis = null;
        try {
        	fis = new FileInputStream(file);
        	doc = new XWPFDocument(fis);
        } catch (IOException ex) {
        	Exceptions.rethrow(ex);
        }
        return doc;
    }

    public static void setCellValue(XWPFTableRow row, int cellNum, String value, XWPFFormat format) {
        XWPFTableCell cell = row.getCell(cellNum);
        while (!cell.getParagraphs().isEmpty()) {
            cell.removeParagraph(0);
        }
        XWPFParagraph paragraph = cell.addParagraph();
        paragraph.createRun().setText(value);

        setCellFormat(cell, format);
    }
    
    public static void setCellFormat(XWPFTableCell cell, XWPFFormat format) {
        XWPFParagraph paragraph = cell.getParagraphs().get(0);
        XWPFRun run;
        if (!paragraph.getRuns().isEmpty()) {
            run = paragraph.getRuns().get(0);
        } else {
            run = paragraph.createRun();
        }
        
        // cell
        if (format.getVerticalAlignment() != null) { 
            cell.setVerticalAlignment(format.getVerticalAlignment());
        }
        if (StringUtils.isNotEmpty(format.getBackgroundColor())) { 
        	cell.setColor(format.getBackgroundColor());
        }	
        if (format.getCtTcBorders() != null) {
        	CTTcPr tcpr = cell.getCTTc().addNewTcPr();
            tcpr.setTcBorders(format.getCtTcBorders());
        }   
        
        // paragraph
        if (format.getAlign() != null) { 
        	paragraph.setAlignment(format.getAlign());
        }
        
        // run
        if (format.getFontFamily() != null) { 
	        run.setFontFamily(format.getFontFamily());
	        // https://issues.apache.org/bugzilla/show_bug.cgi?id=47148
	        run.getCTR().getRPr().getRFonts().setHAnsi(format.getFontFamily());
        }
        if (format.getFontSize() > 0) {
        	run.setFontSize(format.getFontSize());
        }
        run.setBold(format.isBold());
    }    
    
    public static void hMergeCells(XWPFTableRow row, int startCellIdx, int endCellIdx) {
        XWPFTableCell startCell = row.getCell(startCellIdx);

        CTTcPr tcpr = startCell.getCTTc().addNewTcPr();
        CTHMerge hMerge=tcpr.addNewHMerge();
        hMerge.setVal(STMerge.RESTART);

        for (int i = endCellIdx; i > startCellIdx; i--) {
            XWPFTableCell c = row.getCell(i);
            tcpr = c.getCTTc().addNewTcPr();
            hMerge=tcpr.addNewHMerge();
            hMerge.setVal(STMerge.CONTINUE);
        }
    }
    
    public static XWPFFormat[] getFormats(XWPFTable table, XWPFTableRow row) {
    	XWPFFormat[] formats = new XWPFFormat[row.getTableCells().size()];
        for (int i = 0; i < row.getTableCells().size(); i++) {
            formats[i] = XWPFUtils.getFormat(row.getCell(i));
        }
        return formats;
    }
    
    public static XWPFFormat getFormat(XWPFTableCell cell) {
    	 XWPFFormat format = new XWPFFormat();
         
         // cell
         format.setVerticalAlignment(cell.getVerticalAlignment());
         format.setCtTcBorders(cell.getCTTc().getTcPr().getTcBorders());
         format.setBackgroundColor(cell.getColor());

         // paragraph
         if (cell.getParagraphs().isEmpty()) {
        	 return format;
         }
         XWPFParagraph paragraph = cell.getParagraphs().get(0);
         format.setAlign(paragraph.getAlignment());
         
         // run
         if (paragraph.getRuns().isEmpty()) {
        	 return format;
         }
         XWPFRun run = paragraph.getRuns().get(0);
         format.setBold(run.isBold());
         format.setFontSize(run.getFontSize());
         format.setFontFamily(run.getFontFamily());
         
         return format;
    }

    public static void replaceTextInDocument(XWPFDocument document, String findText, String replaceText) {
    	replaceTextInHeader(document, findText, replaceText);
    	replaceTextInBody(document, findText, replaceText);
    	replaceTextInFooter(document, findText, replaceText);
    }

    public static void replaceTextInHeader(XWPFDocument document, String findText, String replaceText) {
        for (XWPFHeader header : document.getHeaderList()) {
            replaceTextInParagraphs(header.getParagraphs(), findText, replaceText);
        }
    }
    
    public static void replaceTextInFooter(XWPFDocument document, String findText, String replaceText) {
    	for (XWPFFooter footer : document.getFooterList()) {
    		replaceTextInParagraphs(footer.getParagraphs(), findText, replaceText);
    	}
    }
    
    public static void replaceTextInBody(XWPFDocument document, String findText, String replaceText) {
    	replaceTextInParagraphs(document.getParagraphs(), findText, replaceText);
    }
    
    private static void replaceTextInParagraphs(List<XWPFParagraph>  paragraphs, String findText, String replaceText) {
    	for (XWPFParagraph paragraph : paragraphs) {
            List<XWPFRun> runs = paragraph.getRuns();
            for (XWPFRun run : runs) {
                String text = run.getText(run.getTextPosition());
                if (StringUtils.isEmpty(text)) {
                    continue;
                }
                text = text.replace(findText, replaceText);
                run.setText(text, 0);
            }
        }
    }

    public static void saveDocument(String filePath, XWPFDocument document) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            document.write(out);
        } catch (IOException ex) {
        	Exceptions.rethrow(ex);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    private XWPFUtils() {
    }
}