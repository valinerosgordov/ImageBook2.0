package ru.imagebook.server.service.pdf;

import com.itextpdf.text.BaseColor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Created by Max Bogovic on 30.01.2016.
 */
public enum ImposerColor {    
    
    RED_01 (BaseColor.RED, 1, true),
    RED_02 (BaseColor.RED, 2, true),
    RED_04 (BaseColor.RED, 4, true),
    RED_05 (BaseColor.RED, 5, true),
    DARK_BLUE_07 (new BaseColor(153,204,255), 7, true),
    GREEN_08  (new BaseColor(0, 128, 0), 8, true),
    GREEN_09  (new BaseColor(0, 128, 0), 9, true),
    GREEN_10  (new BaseColor(0, 128, 0), 10, true),
    PURPLE_09 (new BaseColor(51, 51, 153), 9, false),
    PURPLE_10 (new BaseColor(51, 51, 153), 10, false),
    PURPLE_21 (new BaseColor(51, 51, 153), 21, false),
    PURPLE_24 (new BaseColor(51, 51, 153), 24, false),
    ORANGE_08 (new BaseColor(255, 102, 0), 8, false),
    YELLOW_06 (BaseColor.YELLOW, 6, false),
    YELLOW_07 (BaseColor.YELLOW, 7, false),
    YELLOW_12 (BaseColor.YELLOW, 12, false),
    YELLOW_13 (BaseColor.YELLOW, 13, false),
    YELLOW_19 (BaseColor.YELLOW, 19, false),
    YELLOW_20 (BaseColor.YELLOW, 20, false),
    YELLOW_27 (BaseColor.YELLOW, 27, false),
    YELLOW_31 (BaseColor.YELLOW, 31, false),
    VIOLET_04 (new BaseColor(128, 0, 128), 4, false),
    VIOLET_05 (new BaseColor(128, 0, 128), 5, false),
    VINOUS_03 (new BaseColor(153, 51, 0), 3, false),
    VINOUS_18 (new BaseColor(128, 0, 128), 18, false),
    VINOUS_11 (new BaseColor(128, 0, 128), 11, false),
    BLUE_01   (new BaseColor(153, 204, 255), 1, false),
    BLUE_02   (new BaseColor(153, 204, 255), 2, false),
    BLUE_16   (new BaseColor(153, 204, 255), 16, false),
    BLUE_17   (new BaseColor(153, 204, 255), 17, false),
    BLUE_32   (new BaseColor(153, 204, 255), 32, false),
    BLUE_33   (new BaseColor(153, 204, 255), 33, false),
    BLUE_34   (new BaseColor(153, 204, 255), 34, false),
    BLUE_35   (new BaseColor(153, 204, 255), 35, false);
    
    private final BaseColor baseColor;
    private final int productValue;
    private final boolean isTypeValue;
    
    private ImposerColor(final BaseColor baseColor, final int productValue, final boolean isTypeValue) {
        this.baseColor = baseColor;
        this.productValue = productValue;
        this.isTypeValue = isTypeValue;
    }

    public BaseColor getBaseColor() { return baseColor; }
    public int getProductValue() { return productValue; }
    public boolean isTypeValue() { return isTypeValue; }

    private static final List<ImposerColor> AS_LIST = Collections.unmodifiableList(Arrays.asList(values()));

    public static ImposerColor getImposerColorByProduct(final int value, final boolean isProductType) {
        for (final ImposerColor imposeColor : AS_LIST) {
            if (imposeColor.isTypeValue() == isProductType) {
                if (imposeColor.getProductValue() == value) return imposeColor;
            }
        }
        //throw new IllegalArgumentException("Doesn't exists impose color for: product data - " + value + " and product kind is product type - " + isProductType);
        return null;
    }
    
}
