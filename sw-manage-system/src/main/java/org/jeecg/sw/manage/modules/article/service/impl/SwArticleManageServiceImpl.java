package org.jeecg.sw.manage.modules.article.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.sw.manage.modules.article.entity.SwArticleApproverR;
import org.jeecg.sw.manage.modules.article.entity.SwArticleManage;
import org.jeecg.sw.manage.modules.article.mapper.SwArticleManageMapper;
import org.jeecg.sw.manage.modules.article.request.ApprovalReq;
import org.jeecg.sw.manage.modules.article.service.ISwArticleApproverRService;
import org.jeecg.sw.manage.modules.article.service.ISwArticleManageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Description: 文章管理
 * @Author: Andy
 * @Date: 2020-09-27
 * @Version: V1.0
 */
@Service
@DS("sw-manage-datasource")
public class SwArticleManageServiceImpl extends ServiceImpl<SwArticleManageMapper, SwArticleManage>
    implements ISwArticleManageService {
    @Autowired
    private ISwArticleApproverRService swArticleApproverRService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void articleOperate(@RequestBody ApprovalReq approvalReq) {
        LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();

        SwArticleManage swArticleManage = this.getById(approvalReq.getArticleId());
        swArticleManage.setStatus(approvalReq.getStatus());
        swArticleManage.setGuideBy(sysUser.getUsername());
        swArticleManage.setArticleLevel(approvalReq.getArticleLevel());
        swArticleManage.setIsPublish(approvalReq.getIsPublish());
        swArticleManage.setGuideName(sysUser.getRealname());
        swArticleManage.setRemarks(approvalReq.getRemarks());
        this.updateById(swArticleManage);
        //获取登录用户信息
        SwArticleApproverR swArticleApproverR = new SwArticleApproverR();
        BeanUtils.copyProperties(approvalReq, swArticleApproverR);
        swArticleApproverR.setUserId(sysUser.getId());
        swArticleApproverR.setUserName(sysUser.getUsername());
        swArticleApproverR.setRealName(sysUser.getRealname());
        swArticleApproverRService.save(swArticleApproverR);

    }
}
