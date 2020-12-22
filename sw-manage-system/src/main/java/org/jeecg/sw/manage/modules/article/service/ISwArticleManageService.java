package org.jeecg.sw.manage.modules.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.sw.manage.modules.article.entity.SwArticleManage;
import org.jeecg.sw.manage.modules.article.request.ApprovalReq;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Description: 文章管理
 * @Author: Andy
 * @Date: 2020-09-27
 * @Version: V1.0
 */
public interface ISwArticleManageService extends IService<SwArticleManage> {
    void articleOperate(@RequestBody ApprovalReq approvalReq);
}
