package org.jeecg.common.util;

import lombok.extern.slf4j.Slf4j;

/**
 * minio文件上传工具类
 */
@Slf4j
public class SystemConfigUtil {
    private static String appUrl;
    private static String logisticAuthKey;
    private static String logisticCustomer;

    public static String getLogisticCustomer() {
        return logisticCustomer;
    }

    public static void setLogisticCustomer(String logisticCustomer) {
        SystemConfigUtil.logisticCustomer = logisticCustomer;
    }

    public static String getLogisticAuthKey() {
        return logisticAuthKey;
    }

    public static void setLogisticAuthKey(String logisticAuthKey) {
        SystemConfigUtil.logisticAuthKey = logisticAuthKey;
    }

    public static void setAppUrl(String appUrl) {
        SystemConfigUtil.appUrl = appUrl;
    }

    public static String getAppUrl() {
        return appUrl;
    }

}
