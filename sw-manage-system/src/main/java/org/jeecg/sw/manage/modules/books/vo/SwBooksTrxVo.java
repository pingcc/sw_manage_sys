package org.jeecg.sw.manage.modules.books.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 书籍交易
 * @Author: Andy
 * @Date: 2020-09-27
 * @Version: V1.0
 */
@Data
public class SwBooksTrxVo {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 书籍名称
     */
    @ApiModelProperty(value = "书籍名称")
    @Excel(name = "书籍名称", width = 15)
    private String booksName;

    /**
     * 书籍编码，系统自动生成
     */
    @Excel(name = "书籍编码", width = 15)
    @ApiModelProperty(value = "书籍编码")
    private String booksCode;

    /**
     * 书籍id
     */
    @ApiModelProperty(value = "书籍id")
    private String booksId;
    /**
     * 交易状态1正常|2超时
     */
    @ApiModelProperty(value = "交易状态1正常|2超时")
    @Dict(dicCode = "books_trx_type")
    private Integer trxType;
    /**
     * 书本是否正常，1|正常2|破损|3丢失
     */
    @ApiModelProperty(value = "书本是否正常，1|正常2|破损|3丢失")
    @Dict(dicCode = "books_status")
    private Integer isBookNormal;
    /**
     * 借书人
     */
    @ApiModelProperty(value = "借书人")
    private String borrowBy;

    @ApiModelProperty(value = "借书人")
    private String borrowName;

    /**
     * 借书时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "借书时间")
    private Date borrowTime;
    /**
     * 还书时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "还书时间")
    private Date returnTime;

    /**
     * 还书截止时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "还书截止时间")
    private Date deadlineTime;
    /**
     * 交易超时天数
     */
    @ApiModelProperty(value = "交易超时天数")
    private Integer trxDeadlineDays;
    /**
     * 书籍允许借出的天数
     */
    @ApiModelProperty(value = "书籍允许借出的天数")
    private Integer days;
    /**
     * 机构编码
     */
    @ApiModelProperty(value = "机构编码")
    private String sysOrgCode;
    private String sysOrgName;

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
