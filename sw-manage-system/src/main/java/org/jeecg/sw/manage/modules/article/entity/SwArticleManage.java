package org.jeecg.sw.manage.modules.article.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: 文章管理
 * @Author: Andy
 * @Date: 2020-09-27
 * @Version: V1.0
 */
@Data
@TableName("sw_article_manage")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sw_article_manage对象", description = " 文章管理")
public class SwArticleManage {

    /**
     * 主键
     */
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 文章标题
     */
    @Excel(name = "文章标题", width = 15)
    @ApiModelProperty(value = "文章标题")
    private String articleTitle;
    /**
     * 书籍内容
     */
    @Excel(name = "书籍内容", width = 15)
    @ApiModelProperty(value = "书籍内容")
    private Object articleContent;
    /**
     * 是否允许发布0-1
     */
    @Excel(name = "是否允许发布0-1", width = 15)
    @ApiModelProperty(value = "是否允许发布0-1")
    @Dict(dicCode = "yn")
    private Integer isPublish;
    /**
     * 审批状态:DRAFT|草稿,APPROVAL|审批中,REFUSE|驳回,ALLOW|通过
     */
    @Excel(name = "审批状态:DRAFT|草稿,APPROVAL|审批中,REFUSE|驳回,ALLOW|通过", width = 15)
    @ApiModelProperty(value = "审批状态:DRAFT|草稿,APPROVAL|审批中,REFUSE|驳回,ALLOW|通过")
    @Dict(dicCode = "audit_status")
    private String status;
    /**
     * 文章作者realname
     */
    @Excel(name = "文章作者realname", width = 15)
    @ApiModelProperty(value = "文章作者realname")
    private String authorName;
    /**
     * 指导人
     */
    @Excel(name = "指导人", width = 15)
    @ApiModelProperty(value = "指导人")
    private String guideBy;
    /**
     * 指导人名称
     */
    @Excel(name = "指导人名称", width = 15)
    @ApiModelProperty(value = "指导人名称")
    private String guideName;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remarks;
    /**
     * 文章评优等级
     */
    @Excel(name = "文章评优等级", width = 15)
    @ApiModelProperty(value = "文章评优等级")
    @Dict(dicCode = "article_level")
    private Integer articleLevel;
    /**
     * 机构编码
     */
    @Excel(name = "机构编码", width = 15)
    @ApiModelProperty(value = "机构编码")
    private String sysOrgCode;

    @TableField(exist = false)
    private String sysOrgName;

    @Excel(name = "机构年级编码", width = 15)
    @ApiModelProperty(value = "机构年级编码")
    private String sysBpOrgCode;
    
    @TableField(exist = false)
    private String sysBpOrgName;

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
