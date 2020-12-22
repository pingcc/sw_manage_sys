package org.jeecg.sw.manage.modules.books.entity;

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
 * @Description: 书籍交易
 * @Author: Andy
 * @Date: 2020-09-27
 * @Version: V1.0
 */
@Data
@TableName("sw_books_trx")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sw_books_trx对象", description = "书籍交易")
public class SwBooksTrx {

    /**
     * 主键
     */
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 书籍id
     */
    @Excel(name = "书籍id", width = 15)
    @ApiModelProperty(value = "书籍id")
    private String booksId;
    /**
     * 交易状态1正常|2超时
     */
    @Excel(name = "交易状态1正常|2超时", width = 15)
    @ApiModelProperty(value = "交易状态1正常|2超时")
    @Dict(dicCode = "books_trx_type")
    private Integer trxType;
    /**
     * 书本是否正常，1|正常2|破损|3丢失
     */
    @Excel(name = "书本是否正常，1|正常2|破损|3丢失", width = 15)
    @ApiModelProperty(value = "书本是否正常，1|正常2|破损|3丢失")
    @Dict(dicCode = "books_status")
    private Integer isBookNormal;
    /**
     * 借书人
     */
    @ApiModelProperty(value = "借书人")
    private String borrowBy;

    @Excel(name = "借书人", width = 15)
    @ApiModelProperty(value = "借书人")
    private String borrowName;

    /**
     * 借书时间
     */
    @Excel(name = "借书时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "借书时间")
    private Date borrowTime;
    /**
     * 还书时间
     */
    @Excel(name = "还书时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "还书时间")
    private Date returnTime;

    /**
     * 还书截止时间
     */
    @Excel(name = "还书截止时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "还书截止时间")
    private Date deadlineTime;
    /**
     * 交易超时天数
     */
    @Excel(name = "交易超时天数", width = 15)
    @ApiModelProperty(value = "交易超时天数")
    private Integer trxDeadlineDays;
    /**
     * 书籍允许借出的天数
     */
    @Excel(name = "书籍允许借出的天数", width = 15)
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
