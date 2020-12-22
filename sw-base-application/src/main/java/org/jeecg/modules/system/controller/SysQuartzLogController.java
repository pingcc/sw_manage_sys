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
import org.jeecg.modules.system.entity.SysQuartzLog;
import org.jeecg.modules.system.service.ISysQuartzLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 系统调度日志表
 * @Author: Andy
 * @Date: 2020-08-24
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "系统调度日志表")
@RestController
@RequestMapping("/system/sysQuartzLog")
public class SysQuartzLogController extends BaseSwController<SysQuartzLog, ISysQuartzLogService> {
    @Autowired
    private ISysQuartzLogService sysQuartzLogService;

    /**
     * 分页列表查询
     *
     * @param sysQuartzLog
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "系统调度日志表-分页列表查询")
    @ApiOperation(value = "系统调度日志表-分页列表查询", notes = "系统调度日志表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SysQuartzLog sysQuartzLog,
        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        // 默认不查询已删除的数据
        QueryWrapper<SysQuartzLog> queryWrapper = QueryGenerator.initQueryWrapper(sysQuartzLog, req.getParameterMap());
        Page<SysQuartzLog> page = new Page<SysQuartzLog>(pageNo, pageSize);
        IPage<SysQuartzLog> pageList = sysQuartzLogService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param sysQuartzLog
     * @return
     */
    @AutoLog(value = "系统调度日志表-添加")
    @ApiOperation(value = "系统调度日志表-添加", notes = "系统调度日志表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SysQuartzLog sysQuartzLog) {
        sysQuartzLogService.save(sysQuartzLog);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param sysQuartzLog
     * @return
     */
    @AutoLog(value = "系统调度日志表-编辑")
    @ApiOperation(value = "系统调度日志表-编辑", notes = "系统调度日志表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SysQuartzLog sysQuartzLog) {
        sysQuartzLogService.updateById(sysQuartzLog);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "系统调度日志表-通过id删除")
    @ApiOperation(value = "系统调度日志表-通过id删除", notes = "系统调度日志表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        sysQuartzLogService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "系统调度日志表-批量删除")
    @ApiOperation(value = "系统调度日志表-批量删除", notes = "系统调度日志表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.sysQuartzLogService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "系统调度日志表-通过id查询")
    @ApiOperation(value = "系统调度日志表-通过id查询", notes = "系统调度日志表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SysQuartzLog sysQuartzLog = sysQuartzLogService.getById(id);
        return Result.ok(sysQuartzLog);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param sysQuartzLog
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysQuartzLog sysQuartzLog) {
        return super.exportXls(request, sysQuartzLog, SysQuartzLog.class, "系统调度日志表");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SysQuartzLog.class);
    }

}
