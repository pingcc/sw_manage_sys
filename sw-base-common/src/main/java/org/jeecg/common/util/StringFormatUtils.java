package org.jeecg.common.util;

/**
 * @Author Andy
 * @Date 2020/8/11
 */
public class StringFormatUtils {
    public static boolean isLetterDigit(String str) {
        String regex = "^[a-z0-9A-Z]+$";
        return str.matches(regex);
    }
}
