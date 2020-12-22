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
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 工作流
 * @Author: Andy
 * @Date: 2020-09-27
 * @Version: V1.0
 */
@Data
@TableName("sw_workflow")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sw_workflow对象", description = "工作流")
public class SwWorkflow {

    /**
     * 主键
     */
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 业务流程id
     */
    @Excel(name = "业务流程id", width = 15)
    @ApiModelProperty(value = "业务流程id")
    private String businessId;
    /**
     * 申请单号
     */
    private String applyNum;
    /**
     * 业务类型
     */
    @Excel(name = "业务类型", width = 15)
    @ApiModelProperty(value = "业务类型")
    @Dict(dicCode = "flow_type")
    private String flowType;
    /**
     * 审批备注
     */
    @Excel(name = "审批备注", width = 15)
    @ApiModelProperty(value = "审批备注")
    private String remarks;
    /**
     * 审批状态:DRAFT|草稿,APPROVAL|审批中,REFUSE|驳回,ALLOW|通过
     */
    @Excel(name = "审批状态:DRAFT|草稿,APPROVAL|审批中,REFUSE|驳回,ALLOW|通过", width = 15)
    @ApiModelProperty(value = "审批状态:DRAFT|草稿,APPROVAL|审批中,REFUSE|驳回,ALLOW|通过")
    @Dict(dicCode = "audit_status")
    private String status;
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
