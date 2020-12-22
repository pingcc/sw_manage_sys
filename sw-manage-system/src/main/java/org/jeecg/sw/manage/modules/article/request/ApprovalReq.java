package org.jeecg.sw.manage.modules.article.request;

import lombok.Data;

/**
 * @Author Andy
 * @Date 2020/11/19
 */
@Data
public class ApprovalReq {

    private String articleId;
    private Integer articleLevel;
    private Integer isPublish;
    private String status;
    private String remarks;

}
