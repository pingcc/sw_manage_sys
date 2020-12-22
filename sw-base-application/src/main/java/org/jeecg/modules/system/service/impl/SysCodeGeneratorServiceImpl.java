package org.jeecg.modules.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.api.ISysCodeGeneratorService;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author Andy
 * @Date 2020/7/23
 */
@Service
public class SysCodeGeneratorServiceImpl implements ISysCodeGeneratorService {
    private static Logger logger = LoggerFactory.getLogger(SysCodeGeneratorServiceImpl.class);
    @Autowired
    private ISysBaseAPI sysBaseAPI;
    @Autowired
    @Lazy
    private RedisUtil redisUtil;

    @Override
    public String generateCodeFromLookup(String itemText, String middlePrefix) {
        JSONObject jsonObject = generateSimpleCode(itemText);
        String prefixCode = jsonObject.getString("prefixCode");
        String formatStr = jsonObject.getString("formatStr");
        String redisValue = jsonObject.getString("redisValue");
        return prefixCode + formatStr + middlePrefix + redisValue;
    }

    @Override
    public String generateCodeFromLookup(String itemText) {
        JSONObject jsonObject = generateSimpleCode(itemText);
        String prefixCode = jsonObject.getString("prefixCode");
        String formatStr = jsonObject.getString("formatStr");
        String redisValue = jsonObject.getString("redisValue");
        return prefixCode + formatStr + redisValue;
    }

    private JSONObject generateSimpleCode(String itemText) {
        //        str:{"pattern":"yyyyMM",prefixCode:"","generateCode":"auto_work_code",generateLength:"7"}
        String str = sysBaseAPI.queryDictItemsByValue("sys_generate_Rule", itemText);
        if (!verifyObj(itemText, str)) {
            return null;
        }
        JSONObject object = JSONObject.parseObject(str);
        String pattern = object.getString("pattern");
        String prefixCode = object.getString("prefixCode");
        String generateCode = object.getString("generateCode");
        Integer generateLength = object.getInteger("generateLength");
        String formatStr = "";
        if (oConvertUtils.isNotEmpty(pattern)) {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            formatStr = format.format(new Date());
        }
        if (oConvertUtils.isEmpty(pattern)) {
            prefixCode = "";
        }
        Integer redisValue = getRedisValue(generateCode, generateLength);
        JSONObject obj = new JSONObject().fluentPut("prefixCode", prefixCode).fluentPut("formatStr", formatStr)
            .fluentPut("redisValue", redisValue.toString());

        return obj;
    }

    private boolean verifyObj(String itemText, String str) {
        JSONObject object = JSONObject.parseObject(str);
        String generateCode = object.getString("generateCode");
        Integer generateLength = object.getInteger("generateLength");
        if (oConvertUtils.isEmpty(str)) {
            logger.error("字典sysGenerateCode,未找到itemText={}列", itemText);
            return false;
        }
        if (oConvertUtils.isEmpty(generateCode)) {
            logger.error("字典sysGenerateCode列,缺少sysGenerateCode字段");
            return false;
        }
        if (oConvertUtils.isEmpty(generateLength)) {
            logger.error("字典sysGenerateCode列,缺少generateLength字段");
            return false;
        }
        return true;
    }

    public Integer getRedisValue(String key, int generateLength) {
        if (redisUtil.get(key) == null) {
            setInitValue(key, generateLength);
        } else {
            Integer value = (Integer)redisUtil.get(key);
            //重置为0
            if (value.toString().length() > generateLength) {
                setInitValue(key, generateLength);
            }
        }
        Integer value = (Integer)redisUtil.get(key);
        //redis自增
        redisUtil.incr(key, 1);
        return value;
    }

    private void setInitValue(String key, int length) {
        StringBuilder sb = new StringBuilder();
        sb.append(1);
        for (int i = 0; i < length - 1; i++) {
            sb.append(0);
        }
        String str = sb.toString();
        redisUtil.set(key, Integer.parseInt(str));
    }
}
