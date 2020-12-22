package org.jeecg.sw.manage.modules.books.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 书籍管理表
 * @Author: Andy
 * @Date: 2020-09-27
 * @Version: V1.0
 */
@Data
public class SwBooksManageVO {

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
     * 书籍条码
     */
    @Excel(name = "书籍条码", width = 15)
    @ApiModelProperty(value = "书籍条码")
    private String booksBarCode;

    /**
     * 书籍摆放编号
     */
    @Excel(name = "书籍摆放编号", width = 15)
    @ApiModelProperty(value = "书籍摆放编号")
    private String booksPosCode;

    /**
     * 是否上架0-1
     */
    @Dict(dicCode = "yn")
    @Excel(name = "是否上架", width = 15, dicCode = "yn")
    private Integer isSale;

    /**
     * 书籍描述
     */
    @ApiModelProperty(value = "书籍描述")
    @Excel(name = "书籍描述", width = 15)
    private String booksDesc;

    /**
     * 书籍是否借出0-1
     */
    @Excel(name = "书籍状态", width = 15, dicCode = "book_sale")
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

    /**
     * 最近一次借书时间
     */
    @Excel(name = "最近一次借书时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最近一次借书时间")
    private Date borrowTime;

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
