package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 系统调度日志表
 * @Author: Andy
 * @Date: 2020-08-24
 * @Version: V1.0
 */
@Data
@TableName("sys_quartz_log")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sys_quartz_log对象", description = "系统调度日志表")
public class SysQuartzLog {

    /**
     * 主键
     */
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 任务类名
     */
    @Excel(name = "任务类名", width = 15)
    @ApiModelProperty(value = "任务类名")
    private String jobClassName;
    /**
     * 系统定时id
     */
    @Excel(name = "系统定时id", width = 15)
    @ApiModelProperty(value = "系统定时id")
    private String sysQuartzJobId;
    /**
     * 日志内容
     */
    @Excel(name = "日志内容", width = 15)
    @ApiModelProperty(value = "日志内容")
    private String logContent;
    /**
     * 请求参数
     */
    @Excel(name = "请求参数", width = 15)
    @ApiModelProperty(value = "请求参数")
    private Object requestParam;
    /**
     * 耗时
     */
    @Excel(name = "耗时", width = 15)
    @ApiModelProperty(value = "耗时")
    private Integer costTime;
    /**
     * 创建人
     */
    @Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 更新人
     */
    @Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**
     * 更新时间
     */
    @Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
