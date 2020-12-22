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
 * @Description: 订单审核角色
 * @Author: Andy
 * @Date: 2020-07-20
 * @Version: V1.0
 */
@Data
@TableName("sw_flow_approver")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "snow_flow_approver对象", description = "审核角色")
public class SwFlowApprover {

    /**
     * 级别id
     */
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 级别名称
     */
    @Excel(name = "级别名称", width = 15)
    @ApiModelProperty(value = "级别名称")
    private String levelName;

    /**
     * 级别名称
     */
    @Excel(name = "角色code", width = 15)
    @ApiModelProperty(value = "角色code")
    private String roleCode;

    /**
     * 是否开起
     */
    @Excel(name = "是否开起", width = 15)
    @ApiModelProperty(value = "是否开起")
    @Dict(dicCode = "yn")
    private Integer isStart;

    @ApiModelProperty(value = "流程分类-flow_type")
    @Dict(dicCode = "flow_type")
    private String flowType;

    @ApiModelProperty(value = "下一个节点审核状态")
    private String nextLevel;

    @ApiModelProperty(value = "当前节点审核状态")
    private String level;
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
