package org.jeecg.sw.manage.modules.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.sw.manage.modules.article.entity.SwArticleApproverR;

import java.util.List;

/**
 * @Description: 文章审批记录表
 * @Author: Andy
 * @Date: 2020-11-19
 * @Version: V1.0
 */
public interface ISwArticleApproverRService extends IService<SwArticleApproverR> {
    List<SwArticleApproverR> getSwArticleApproverR(String articleId);
}
