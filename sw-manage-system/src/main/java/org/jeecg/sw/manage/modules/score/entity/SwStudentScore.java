package org.jeecg.sw.manage.modules.score.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 学生分数表
 * @Author: Andy
 * @Date: 2020-10-29
 * @Version: V1.0
 */
@Data
@TableName("sw_student_score")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sw_student_score对象", description = "学生分数表")
public class SwStudentScore {

    /**
     * 主键
     */
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 学生id
     */
    @ApiModelProperty(value = "学生id")
    private String studentId;
    /**
     * 学生学号
     */
    @ApiModelProperty(value = "学生学号")
    private String studentNo;
    /**
     * 学生昵称
     */
    @ApiModelProperty(value = "学生昵称")
    private String studentBy;
    /**
     * 学生姓名
     */
    @ApiModelProperty(value = "学生姓名")
    private String studentName;
    /**
     * 考试分数
     */
    @ApiModelProperty(value = "考试分数")
    private java.math.BigDecimal examineScore;
    /**
     * 考试科目
     */
    @ApiModelProperty(value = "考试科目")
    private String examineSub;
    /**
     * 考试时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "考试时间")
    private Date examineTime;
    /**
     * 考试类型
     */
    @ApiModelProperty(value = "考试类型")
    private String examineType;
    /**
     * 班级编码
     */
    @ApiModelProperty(value = "班级编码")
    private String sysOrgCode;
    private String sysOrgName;
    /**
     * 年级编码
     */
    @ApiModelProperty(value = "年级编码")
    private String sysBpOrgCode;
    private String sysBpOrgName;
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
}
