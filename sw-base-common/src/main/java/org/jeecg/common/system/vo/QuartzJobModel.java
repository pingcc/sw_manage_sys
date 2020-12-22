package org.jeecg.common.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 定时任务在线管理
 * @Author: Andy
 * @Date: 2019-01-02
 * @Version: V1.0
 */
@Data
public class QuartzJobModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;
    /**
     * 删除状态
     */
    private Integer delFlag;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date updateTime;
    /**
     * 任务类名
     */
    private String jobClassName;
    /**
     * cron表达式
     */
    private String cronExpression;
    /**
     * 参数
     */
    private String parameter;
    /**
     * 描述
     */
    private String description;
    /**
     * 状态 0正常 -1停止
     */
    @Dict(dicCode = "quartz_status")
    private Integer status;

}
