package ru.imagebook.server.service.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfContentByte;

/**
 * Line on pdf sheet Same on all pdf sheets in document
 * 
 * @author liosha
 */
public class Line implements IFigure {
    private float startx;
    private float starty;
    private float endx;
    private float endy;
    private BaseColor color = BaseColor.BLACK;

    public Line() {
    }

    public Line(float startx, float starty, float endx, float endy) {
        this.startx = startx;
        this.starty = starty;
        this.endx = endx;
        this.endy = endy;
    }

    public Line(float startx, float starty, float endx, float endy, BaseColor color) {
        this.startx = startx;
        this.starty = starty;
        this.endx = endx;
        this.endy = endy;
        this.color = color;
    }

    @Override
    public void draw(PdfContentByte contentByte) {
        contentByte.setColorStroke(color);
        contentByte.moveTo(this.startx, this.starty);
        contentByte.lineTo(this.endx, this.endy);
        contentByte.closePathStroke();
    }

    /**
     * @return the startx
     */
    public float getStartx() {
        return startx;
    }

    /**
     * @param startx
     *            the startx to set
     */
    public void setStartx(float startx) {
        this.startx = startx;
    }

    /**
     * @return the starty
     */
    public float getStarty() {
        return starty;
    }

    /**
     * @param starty
     *            the starty to set
     */
    public void setStarty(float starty) {
        this.starty = starty;
    }

    /**
     * @return the endx
     */
    public float getEndx() {
        return endx;
    }

    /**
     * @param endx
     *            the endx to set
     */
    public void setEndx(float endx) {
        this.endx = endx;
    }

    /**
     * @return the endy
     */
    public float getEndy() {
        return endy;
    }

    /**
     * @param endy
     *            the endy to set
     */
    public void setEndy(float endy) {
        this.endy = endy;
    }
}