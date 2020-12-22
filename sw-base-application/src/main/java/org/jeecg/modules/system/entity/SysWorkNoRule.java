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
 * @Description: 学生工号规则表
 * @Author: Andy
 * @Date: 2020-10-10
 * @Version: V1.0
 */
@Data
@TableName("sys_work_no_rule")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sys_work_no_rule对象", description = "学生工号规则表")
public class SysWorkNoRule {

    /**
     * 主键
     */
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 年级编码
     */
    @Excel(name = "年级编码", width = 15)
    @ApiModelProperty(value = "年级编码")
    private String sysBpOrgCode;
    /**
     * 年级名称
     */
    @Excel(name = "年级名称", width = 15)
    @ApiModelProperty(value = "年级名称")
    private String sysBpOrgName;
    /**
     * 班级编码
     */
    @Excel(name = "班级编码", width = 15)
    @ApiModelProperty(value = "班级编码")
    private String sysOrgCode;
    /**
     * 班级名称
     */
    @Excel(name = "班级名称", width = 15)
    @ApiModelProperty(value = "班级名称")
    private String sysOrgName;
    /**
     * 学号规则，学生学号：年度+规则code4位数+gener_work_code自增长7位数，15位
     */
    @Excel(name = "学号规则，学生学号：年度+规则code4位数+gener_work_code自增长7位数，15位", width = 15)
    @ApiModelProperty(value = "学号规则，学生学号=年度+规则code4位数+gener_work_code自增长7位数，15位")
    private String ruleCode;

    @ApiModelProperty(value = "排序")
    private Integer sortOrder;
    /**
     * 刪除标识0-1
     */
    @Excel(name = "刪除标识0-1", width = 15)
    @ApiModelProperty(value = "刪除标识0-1")
    private Integer isDeleted;
    /**
     * 创建人
     */
    @Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 更新人
     */
    @Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
