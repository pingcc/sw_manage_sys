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
 * @Description: 书籍属性
 * @Author: Andy
 * @Date: 2020-09-27
 * @Version: V1.0
 */
@Data
@TableName("sw_books_attr")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sw_books_attr对象", description = "书籍属性")
public class SwBooksAttr {

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
     * 书籍摆放编号
     */
    @Excel(name = "书籍摆放编号", width = 15)
    @ApiModelProperty(value = "书籍摆放编号")
    private String booksPosCode;
    /**
     * 书籍是否借出0-1
     */
    @Excel(name = "书籍状态", width = 15)
    @ApiModelProperty(value = "书籍状态")
    @Dict(dicCode = "book_sale")
    private Integer bookSale;
    /**
     * 书籍允许借出天数
     */
    @Excel(name = "书籍允许借出天数", width = 15)
    @ApiModelProperty(value = "书籍允许借出天数")
    private Integer days;
    /**
     * 最近一次借书人
     */
    @Excel(name = "最近一次借书人", width = 15)
    @ApiModelProperty(value = "最近一次借书人")
    private String borrowBy;

    /**
     * 最近一次借书人
     */
    @Excel(name = "最近一次借书人", width = 15)
    @ApiModelProperty(value = "最近一次借书人")
    private String borrowName;

    @Excel(name = "最近一次借书时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最近一次借书时间")
    private Date borrowTime;

    /**
     * 机构编码
     */
    @Excel(name = "机构编码", width = 15)
    @ApiModelProperty(value = "机构编码")
    private String sysOrgCode;
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
