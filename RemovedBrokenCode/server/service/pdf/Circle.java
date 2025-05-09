package ru.imagebook.server.service.pdf;

import com.itextpdf.text.pdf.PdfContentByte;

/**
 * Circle on pdf sheet Same on all pdf sheets in document
 * 
 * @author liosha
 */
public class Circle implements IFigure {
    private float x;
    private float y;
    private float r;

    public Circle() {
    }

    public Circle(float x, float y, float r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    @Override
    public void draw(PdfContentByte contentByte) {
        contentByte.circle(this.x, this.y, r);
    }

    /**
     * @return the x
     */
    public float getX() {
        return x;
    }

    /**
     * @param x
     *            the x to set
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public float getY() {
        return y;
    }

    /**
     * @param y
     *            the y to set
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * @return the r
     */
    public float getR() {
        return r;
    }

    /**
     * @param r
     *            the r to set
     */
    public void setR(float r) {
        this.r = r;
    }
}