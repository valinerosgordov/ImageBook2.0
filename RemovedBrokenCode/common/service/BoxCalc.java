package ru.imagebook.client.common.service;


public class BoxCalc {

    protected static final int ALBUM_HEIGHT_CM = 3;
    protected static final int PACK_WIDTH_CM = 5;

    public static int getBoxHeight(int ordersQuantity) {
        return ALBUM_HEIGHT_CM * ordersQuantity;
    }

    public static int getBoxWidth(int albumWidth) {
        return (int) (Math.ceil(albumWidth / 10) + PACK_WIDTH_CM);
    }

    public static int getBoxLength(int albumLength) {
        return (int) (Math.ceil(albumLength / 10) + PACK_WIDTH_CM);
    }
}
