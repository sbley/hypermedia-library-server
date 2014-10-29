package de.saxsys.hypermedia.rest.hal;

public class HalUtil {

    public static Integer toInt(Object value) {
        if (value.getClass().isAssignableFrom(String.class)) {
            String valueStr = (String) value;
            return Integer.valueOf(valueStr);
        }
        return null;
    }

}
