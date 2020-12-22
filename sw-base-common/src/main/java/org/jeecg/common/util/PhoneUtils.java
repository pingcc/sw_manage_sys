package org.jeecg.common.util;

/**
 * @Author Andy
 * @Date 2020/7/30
 */
public class PhoneUtils {
    public static String phoneHide(String phone) {
        return phone.replaceAll("(\\d{3})\\d{6}(\\d{2})", "$1******$2");
    }
}

