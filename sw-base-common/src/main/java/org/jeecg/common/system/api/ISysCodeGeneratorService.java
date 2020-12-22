package org.jeecg.common.system.api;

/**
 * @Author Andy
 * @Date 2020/7/23
 * 系统编码生成服务，可扩展规则
 */
public interface ISysCodeGeneratorService {

    /**
     * 生成系统编码规则
     *
     * @param itemText
     * @param middlePrefix
     * @return pre+formastDate+middlePrefix+redis
     */
    String generateCodeFromLookup(String itemText, String middlePrefix);

    /**
     * 生成系统编码规则
     *
     * @param itemText
     * @return
     */
    String generateCodeFromLookup(String itemText);

}
