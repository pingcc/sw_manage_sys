package org.jeecg.sw.manage.modules.score.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 学生分数表
 * @Author: Andy
 * @Date: 2020-10-29
 * @Version: V1.0
 */
@Data
@ApiModel(value = "SwStudentScoreEx对象", description = "学生分数excel导入、导出")
public class SwStudentScoreEx {

    @Excel(name = "年级", width = 15)
    @ApiModelProperty(value = "年级")
    private String sysBpOrgName;
    @Excel(name = "班级", width = 15)
    @ApiModelProperty(value = "班级")
    private String sysOrgName;
    /**
     * 学生学号
     */
    @Excel(name = "学号", width = 15)
    @ApiModelProperty(value = "学生学号")
    private String studentNo;
    /**
     * 学生昵称
     */
    private String studentBy;
    /**
     * 学生姓名
     */
    @Excel(name = "姓名", width = 15)
    @ApiModelProperty(value = "学生姓名")
    private String studentName;
    /**
     * 考试分数
     */
    @Excel(name = "考试成绩", width = 15)
    @ApiModelProperty(value = "考试成绩")
    private java.math.BigDecimal examineScore;
    /**
     * 考试科目
     */
    @Excel(name = "考试科目", width = 15)
    @ApiModelProperty(value = "考试科目")
    private String examineSub;
    /**
     * 考试时间
     */
    @Excel(name = "考试时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "考试时间")
    private Date examineTime;
    /**
     * 考试类型
     */
    @Excel(name = "考试类型", width = 15)
    @ApiModelProperty(value = "考试类型")
    private String examineType;

    private String sysOrgCode;
    private String sysBpOrgCode;
    private Integer isDeleted;
    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;

}
