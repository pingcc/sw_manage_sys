package org.jeecg.sw.manage.modules.article.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.sw.manage.modules.article.entity.SwArticleApproverR;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description: 文章管理
 * @Author: Andy
 * @Date: 2020-09-27
 * @Version: V1.0
 */
@Data
public class SwArticleManageVo {

    /**
     * 主键
     */

    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 文章标题
     */
    @ApiModelProperty(value = "文章标题")
    private String articleTitle;
    /**
     * 书籍内容
     */
    @ApiModelProperty(value = "书籍内容")
    private Object articleContent;
    /**
     * 是否允许发布0-1
     */
    @ApiModelProperty(value = "是否允许发布0-1")
    @Dict(dicCode = "yn")
    private Integer isPublish;
    /**
     * 审批状态:DRAFT|草稿,APPROVAL|审批中,REFUSE|驳回,ALLOW|通过
     */
    @ApiModelProperty(value = "审批状态:DRAFT|草稿,APPROVAL|审批中,REFUSE|驳回,ALLOW|通过")
    @Dict(dicCode = "audit_status")
    private String status;
    /**
     * 文章作者realname
     */
    @ApiModelProperty(value = "文章作者realname")
    private String authorName;
    /**
     * 指导人
     */
    @ApiModelProperty(value = "指导人")
    private String guideBy;
    /**
     * 指导人名称
     */
    @ApiModelProperty(value = "指导人名称")
    private String guideName;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;
    /**
     * 文章评优等级
     */
    @ApiModelProperty(value = "文章评优等级")
    @Dict(dicCode = "article_level")
    private Integer articleLevel;
    /**
     * 机构编码
     */
    @ApiModelProperty(value = "机构编码")
    private String sysOrgCode;

    /**
     * 机构年级编码
     */
    @ApiModelProperty(value = "机构年级编码")
    private String sysBpOrgCode;
    /**
     * 刪除标识0-1
     */
    @ApiModelProperty(value = "刪除标识0-1")
    private Integer isDeleted;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    private List<SwArticleApproverR> swArticleApproverRList;
}
