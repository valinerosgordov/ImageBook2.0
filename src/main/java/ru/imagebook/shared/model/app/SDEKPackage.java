package ru.imagebook.shared.model.app;

import java.io.Serializable;

public class SDEKPackage implements Serializable {
    private int length;
    private int width;
    private int height;
    private double weight;

    public SDEKPackage() {
    }

    public SDEKPackage(int length, int width, int height, double weight) {
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }
}
