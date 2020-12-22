package org.jeecg.modules.system.controller;

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
import org.jeecg.common.util.RequestParamsUtils;
import org.jeecg.modules.system.entity.SwWorkflow;
import org.jeecg.modules.system.service.ISwWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 工作流
 * @Author: Andy
 * @Date: 2020-09-27
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "工作流")
@RestController
@RequestMapping("/system/swWorkflow")
public class SwWorkflowController extends BaseSwController<SwWorkflow, ISwWorkflowService> {
    @Autowired
    private ISwWorkflowService swWorkflowService;

    /**
     * 分页列表查询
     *
     * @param swWorkflow
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "工作流-分页列表查询")
    @ApiOperation(value = "工作流-分页列表查询", notes = "工作流-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SwWorkflow swWorkflow,
        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        // 默认不查询已删除的数据
        if (swWorkflow.getIsDeleted() == null) {
            swWorkflow.setIsDeleted(0);
        }
        QueryWrapper<SwWorkflow> queryWrapper = QueryGenerator.initQueryWrapper(swWorkflow, req.getParameterMap());
        Page<SwWorkflow> page = new Page<SwWorkflow>(pageNo, pageSize);
        IPage<SwWorkflow> pageList = swWorkflowService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 审批
     *
     * @param swWorkflow
     * @return
     */
    @AutoLog(value = "工作流-审批")
    @ApiOperation(value = "工作流-审批", notes = "工作流-审批")
    @PostMapping(value = "/approval")
    public Result<?> approval(@RequestBody SwWorkflow swWorkflow) {
        RequestParamsUtils.validateEntityFields(swWorkflow, "id", "businessId", "flowTye", "status");
        try {
            swWorkflowService.approval(swWorkflow);
            return Result.ok("添加成功！");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

    }

    /**
     * 审批
     *
     * @param businessId , flowTye
     * @return
     */
    @AutoLog(value = "工作流-开启")
    @ApiOperation(value = "工作流-开启", notes = "工作流-开启")
    @PostMapping(value = "/start")
    public Result<?> start(@RequestParam(name = "applyNum", required = true) String applyNum,
        @RequestParam(name = "businessId", required = true) String businessId,
        @RequestParam(name = "flowTye", required = true) String flowTye) {
        try {
            swWorkflowService.start(applyNum, businessId, flowTye);
            return Result.ok("添加成功！");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "工作流-通过id查询")
    @ApiOperation(value = "工作流-通过id查询", notes = "工作流-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SwWorkflow swWorkflow = swWorkflowService.getById(id);
        return Result.ok(swWorkflow);
    }

}
