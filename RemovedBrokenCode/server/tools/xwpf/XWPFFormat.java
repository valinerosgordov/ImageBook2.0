package ru.imagebook.server.tools.xwpf;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;

public class XWPFFormat {
    private String fontFamily;
    private int fontSize;
    private boolean bold;
    private String backgroundColor;
    private ParagraphAlignment align;
    private XWPFTableCell.XWPFVertAlign verticalAlignment;
    private CTTcBorders ctTcBorders;

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public ParagraphAlignment getAlign() {
        return align;
    }

    public void setAlign(ParagraphAlignment align) {
        this.align = align;
    }

    public XWPFTableCell.XWPFVertAlign getVerticalAlignment() {
        return verticalAlignment;
    }

    public void setVerticalAlignment(XWPFTableCell.XWPFVertAlign verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public CTTcBorders getCtTcBorders() {
        return ctTcBorders;
    }

    public void setCtTcBorders(CTTcBorders ctTcBorders) {
        this.ctTcBorders = ctTcBorders;
    }
}