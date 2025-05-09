package ru.imagebook.shared.util;

import ru.minogin.core.client.text.StringUtil;

public class PhoneNumberUtil {

    public static final String DEFAULT_COUNTRY_CODE = "7";
    public static final int PHONE_NUMBER_LENGTH = 10;

    private PhoneNumberUtil() {
    }

    public static String[] extractPhoneNumber(String str) {
        String[] result = new String[]{"", ""}; // [countryCode, phone]

        if (!StringUtil.isEmpty(str)) {
            String in = str.startsWith("+") ? str.substring(1) : str;
            if (StringUtil.isEmpty(in) || in.length() < PHONE_NUMBER_LENGTH) {
                result[1] = in;
            } else {
                result[0] = in.substring(0, in.length() - PHONE_NUMBER_LENGTH); // countryCode
                result[1] = in.substring(in.length() - PHONE_NUMBER_LENGTH); // phoneNumber
            }
        }

        return result;
    }

    public static String getPhoneNumber(String countryCode, String phone) {
        return "+" + countryCode + phone;
    }
}
