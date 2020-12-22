package org.jeecg.common.system.vo;

import lombok.Data;

/**
 * @Description: 系统调度日志表
 * @Author: Andy
 * @Date: 2020-08-24
 * @Version: V1.0
 */
@Data
public class SysQuartzLogModel {

    private String jobClassName;

    private String sysQuartzJobId;

    private String logContent;

    private String requestParam;

    private Integer costTime;

}
