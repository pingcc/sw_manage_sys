package org.jeecg.sw.manage.modules.books.request;

import lombok.Data;

/**
 * @Author Andy
 * @Date 2020/11/18
 */
@Data
public class BooksTrxReq {
    private String username;
    private String realname;
    private String booksId;
    //还书状态
    private int isBookNormal;
    //状态1：未借出;借书，2已借出 还书
    private Integer bookSale;
    private String sysOrgCode;
    private String sysOrgName;
    private String sysBpOrgCode;
    private String sysBpOrgName;
}
