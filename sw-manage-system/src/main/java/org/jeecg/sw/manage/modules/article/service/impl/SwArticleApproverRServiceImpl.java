package org.jeecg.sw.manage.modules.article.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.sw.manage.modules.article.entity.SwArticleApproverR;
import org.jeecg.sw.manage.modules.article.mapper.SwArticleApproverRMapper;
import org.jeecg.sw.manage.modules.article.service.ISwArticleApproverRService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 文章审批记录表
 * @Author: Andy
 * @Date: 2020-11-19
 * @Version: V1.0
 */
@Service
@DS("sw-manage-datasource")
public class SwArticleApproverRServiceImpl extends ServiceImpl<SwArticleApproverRMapper, SwArticleApproverR>
    implements ISwArticleApproverRService {

    @Override
    public List<SwArticleApproverR> getSwArticleApproverR(String articleId) {
        SwArticleApproverR params = new SwArticleApproverR();
        params.setArticleId(articleId);
        QueryWrapper<SwArticleApproverR> queryWrapper = QueryGenerator.initQueryWrapper(params, null);
        queryWrapper.orderByDesc("create_time");
        return this.baseMapper.selectList(queryWrapper);
    }

}
