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
 * @Description: 学生申请表
 * @Author: Andy
 * @Date: 2020-11-09
 * @Version: V1.0
 */
@Data
@TableName("sys_user_apply")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sys_user_apply对象", description = "学生申请表")
public class SysUserApply {

    /**
     * 主键id
     */
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "申请单号")
    private String applyNum;

    /**
     * 登录账号
     */
    @Excel(name = "登录账号", width = 15)
    @ApiModelProperty(value = "登录账号")
    private String username;
    /**
     * 真实姓名
     */
    @Excel(name = "真实姓名", width = 15)
    @ApiModelProperty(value = "真实姓名")
    private String realname;
    /**
     * 头像
     */
    @Excel(name = "头像", width = 15)
    @ApiModelProperty(value = "头像")
    private String avatar;
    /**
     * 籍贯
     */
    @Excel(name = "籍贯", width = 15)
    @ApiModelProperty(value = "籍贯")
    private String nativePlace;
    /**
     * 生日
     */
    @Excel(name = "生日", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "生日")
    private Date birthday;
    /**
     * 性别(0-默认未知,1-男,2-女)
     */
    @Excel(name = "性别(0-默认未知,1-男,2-女)", width = 15)
    @ApiModelProperty(value = "性别(0-默认未知,1-男,2-女)")
    @Dict(dicCode = "sex")
    private Integer sex;
    /**
     * 电子邮件
     */
    @Excel(name = "电子邮件", width = 15)
    @ApiModelProperty(value = "电子邮件")
    private String email;
    /**
     * 审批状态:DRAFT|草稿,APPROVAL|审批中,REFUSE|驳回,ALLOW|通过
     */
    @ApiModelProperty(value = "审批状态")
    @Dict(dicCode = "audit_status")
    private String status;
    /**
     * 电话
     */
    @Excel(name = "电话", width = 15)
    @ApiModelProperty(value = "电话")
    private String phone;
    /**
     * 班级编码
     */
    @ApiModelProperty(value = "班级编码")
    private String orgCode;

    @ApiModelProperty(value = "年级名称")
    private String bpOrgName;
    @ApiModelProperty(value = "年级编码")
    private String bpOrgCode;
    /**
     * 班级编码
     */
    @ApiModelProperty(value = "班级id")
    private String deptId;
    /**
     * 班级编码
     */
    @Excel(name = "班级名称", width = 15)
    @ApiModelProperty(value = "班级名称")
    private String deptName;

    /**
     * 同步工作流引擎(1-同步,0-不同步)
     */
    @Excel(name = "同步工作流引擎(1-同步,0-不同步)", width = 15)
    @ApiModelProperty(value = "同步工作流引擎(1-同步,0-不同步)")
    private Integer activitiSync;
    /**
     * 职务，关联职务表
     */
    @Excel(name = "职务，关联职务表", width = 15)
    @ApiModelProperty(value = "职务，关联职务表")
    private String post;
    /**
     * 座机号
     */
    @Excel(name = "座机号", width = 15)
    @ApiModelProperty(value = "座机号")
    private String telephone;
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
    /**
     * 身份（1普通成员 2上级）
     */
    @Excel(name = "身份（1普通成员 2上级）", width = 15)
    @ApiModelProperty(value = "身份（1普通成员 2上级）")
    private Integer userIdentity;
}
