package org.jeecg.sw.manage.modules.article.entity;

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
 * @Description: 文章审批记录表
 * @Author: Andy
 * @Date: 2020-11-19
 * @Version: V1.0
 */
@Data
@TableName("sw_article_approver_r")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sw_article_approver_r对象", description = "文章审批记录表")
public class SwArticleApproverR {

    /**
     * id
     */
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 文章id
     */
    @Excel(name = "文章id", width = 15)
    @ApiModelProperty(value = "文章id")
    private String articleId;
    /**
     * 审批状态:DRAFT|草稿,APPROVAL|审批中,REFUSE|驳回,ALLOW|通过
     */
    @Excel(name = "审批状态:DRAFT|草稿,APPROVAL|审批中,REFUSE|驳回,ALLOW|通过", width = 15)
    @ApiModelProperty(value = "审批状态:DRAFT|草稿,APPROVAL|审批中,REFUSE|驳回,ALLOW|通过")
    @Dict(dicCode = "audit_status")
    private String status;
    /**
     * 审批人id
     */
    @Excel(name = "审批人id", width = 15)
    @ApiModelProperty(value = "审批人id")
    private String userId;
    /**
     * 审批人名称
     */
    @Excel(name = "审批人名称", width = 15)
    @ApiModelProperty(value = "审批人名称")
    private String userName;
    /**
     * 审批人名称
     */
    @Excel(name = "审批人名称", width = 15)
    @ApiModelProperty(value = "审批人名称")
    private String realName;
    /**
     * 点评
     */
    @Excel(name = "点评", width = 15)
    @ApiModelProperty(value = "点评")
    private String remark;
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
