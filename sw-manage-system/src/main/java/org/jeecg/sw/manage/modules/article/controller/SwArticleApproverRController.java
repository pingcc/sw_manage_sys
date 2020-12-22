package org.jeecg.sw.manage.modules.article.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.BaseSwController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.sw.manage.modules.article.entity.SwArticleApproverR;
import org.jeecg.sw.manage.modules.article.service.ISwArticleApproverRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 文章审批记录表
 * @Author: Andy
 * @Date: 2020-11-19
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "文章审批记录表")
@RestController
@RequestMapping("/article/swArticleApproverR")
public class SwArticleApproverRController extends BaseSwController<SwArticleApproverR, ISwArticleApproverRService> {
    @Autowired
    private ISwArticleApproverRService swArticleApproverRService;

    /**
     * 分页列表查询
     *
     * @param swArticleApproverR
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "文章审批记录表-分页列表查询")
    @ApiOperation(value = "文章审批记录表-分页列表查询", notes = "文章审批记录表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SwArticleApproverR swArticleApproverR,
        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        // 默认不查询已删除的数据
        if (swArticleApproverR.getIsDeleted() == null) {
            swArticleApproverR.setIsDeleted(0);
        }
        QueryWrapper<SwArticleApproverR> queryWrapper =
            QueryGenerator.initQueryWrapper(swArticleApproverR, req.getParameterMap());
        Page<SwArticleApproverR> page = new Page<SwArticleApproverR>(pageNo, pageSize);
        IPage<SwArticleApproverR> pageList = swArticleApproverRService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

}
