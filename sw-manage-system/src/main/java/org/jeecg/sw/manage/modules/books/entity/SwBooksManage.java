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
 * @Description: 书籍管理表
 * @Author: Andy
 * @Date: 2020-09-27
 * @Version: V1.0
 */
@Data
@TableName("sw_books_manage")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sw_books_manage对象", description = "书籍管理表")
public class SwBooksManage {

    /**
     * 主键
     */
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 是否上家0-1
     */
    @Excel(name = "是否上架0-1", width = 15)
    @ApiModelProperty(value = "是否上家0-1")
    @Dict(dicCode = "yn")
    private Integer isSale;
    /**
     * 书籍名称
     */
    @Excel(name = "书籍名称", width = 15)
    @ApiModelProperty(value = "书籍名称")
    private String booksName;
    /**
     * 书籍描述
     */
    @Excel(name = "书籍描述", width = 15)
    @ApiModelProperty(value = "书籍描述")
    private String booksDesc;
    /**
     * 书籍条码
     */
    @Excel(name = "书籍条码", width = 15)
    @ApiModelProperty(value = "书籍条码")
    private String booksBarCode;
    /**
     * 书籍编码，系统自动生成
     */
    @Excel(name = "书籍编码，系统自动生成", width = 15)
    @ApiModelProperty(value = "书籍编码，系统自动生成")
    private String booksCode;
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
