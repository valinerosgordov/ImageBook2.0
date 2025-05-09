package ru.imagebook.shared.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DeliveryType {
    public static final Map<Integer, String> values = new LinkedHashMap<Integer, String>();
    public static final List<Integer> DELIVERY_DISCOUNT_TYPES = new ArrayList<Integer>();

    public static final int POST = 100;
    public static final int MAJOR = 200;
    public static final int EXW = 300;
    public static final int POSTAMATE = 400;
    public static final int TRIAL = 500;
    public static final int MULTISHIP = 600;
    @Deprecated
    public static final int DDELIVERY = 700;
    public static final int SDEK = 800;

    static {
        values.put(POST, "Почта РФ");
        values.put(MAJOR, "Major Express");
        values.put(EXW, "Самовывоз");
        values.put(POSTAMATE, "Постаматы");
        values.put(TRIAL, "Пробные альбомы");
        values.put(MULTISHIP, "Мультишип");
        values.put(DDELIVERY, "DDelivery");
        values.put(SDEK, "CDEK");

        DELIVERY_DISCOUNT_TYPES.add(POST);
        DELIVERY_DISCOUNT_TYPES.add(MAJOR);
        DELIVERY_DISCOUNT_TYPES.add(POSTAMATE);
        // DELIVERY_DISCOUNT_TYPES.add(DDELIVERY);
    }
}