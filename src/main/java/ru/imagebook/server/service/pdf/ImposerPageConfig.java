package ru.imagebook.server.service.pdf;

/**
 * Configuration class for page imposing
 * 
 * @author liosha
 */
public class ImposerPageConfig {
    private float blockWidth = 0;
    private float blockHeight = 0;
    private float gabx = 0;
    private float gaby = 0;
    private float x = 0;
    private float y = 0;

    public ImposerPageConfig() {
    }

    public ImposerPageConfig(float blockWidth, float blockHeight, float gabx, float gaby, float x, float y) {
        this.blockWidth = blockWidth;
        this.blockHeight = blockHeight;
        this.gabx = gabx;
        this.gaby = gaby;
        this.x = x;
        this.y = y;
    }

    public float getBlockWidth() {
        return blockWidth;
    }

    public void setBlockWidth(float blockWidth) {
        this.blockWidth = blockWidth;
    }

    public float getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(float blockHeight) {
        this.blockHeight = blockHeight;
    }

    public float getGabx() {
        return gabx;
    }

    public void setGabx(float gabx) {
        this.gabx = gabx;
    }

    public float getGaby() {
        return gaby;
    }

    public void setGaby(float gaby) {
        this.gaby = gaby;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "ImposerPageConfig{" +
                "blockWidth=" + blockWidth +
                ", blockHeight=" + blockHeight +
                ", gabx=" + gabx +
                ", gaby=" + gaby +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}