package org.jeecg.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.BaseSwController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.RequestParamsUtils;
import org.jeecg.modules.system.entity.SysUserApply;
import org.jeecg.modules.system.service.ISysUserApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 学生申请表
 * @Author: Andy
 * @Date: 2020-11-09
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "学生申请表")
@RestController
@RequestMapping("/system/sysUserApply")
public class SysUserApplyController extends BaseSwController<SysUserApply, ISysUserApplyService> {
    @Autowired
    private ISysUserApplyService sysUserApplyService;

    /**
     * 分页列表查询
     *
     * @param sysUserApply
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "学生申请表-分页列表查询")
    @ApiOperation(value = "学生申请表-分页列表查询", notes = "学生申请表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SysUserApply sysUserApply,
        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<SysUserApply> queryWrapper = QueryGenerator.initQueryWrapper(sysUserApply, req.getParameterMap());
        Page<SysUserApply> page = new Page<SysUserApply>(pageNo, pageSize);
        IPage<SysUserApply> pageList = sysUserApplyService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param sysUserApply
     * @return
     */
    @AutoLog(value = "学生申请表-添加")
    @ApiOperation(value = "学生申请表-添加", notes = "学生申请表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SysUserApply sysUserApply) {
        try {
            RequestParamsUtils.validateEntityFields(sysUserApply, "username", "realname", "orgCode");
            sysUserApplyService.saveUserApply(sysUserApply);
            return Result.ok("添加成功！");
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof java.sql.SQLIntegrityConstraintViolationException) {
                String errMsg = cause.getMessage();
                if (StringUtils.isNotBlank(errMsg)) {
                    //根据约束名称定位是什么字段重复
                    if (errMsg.contains(ISysUserApplyService.UK_USERNAME)) {
                        return Result.error("用户名已经存在!");
                    } else if (errMsg.contains(ISysUserApplyService.UK_PHONE)) {
                        return Result.error("手机号已经存在!");
                    } else if (errMsg.contains(ISysUserApplyService.UK_EMAIL)) {
                        return Result.error("email已经存在!");
                    }
                }
            }
            return Result.error(e.getMessage());
        }
    }

    /**
     * 编辑
     *
     * @param sysUserApply
     * @return
     */
    @AutoLog(value = "学生申请表-编辑")
    @ApiOperation(value = "学生申请表-编辑", notes = "学生申请表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SysUserApply sysUserApply) {
        //保证用户名不能修改
        SysUserApply oldSys = sysUserApplyService.getById(sysUserApply.getId());
        sysUserApply.setUsername(oldSys.getUsername());
        sysUserApplyService.updateById(sysUserApply);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "学生申请表-通过id删除")
    @ApiOperation(value = "学生申请表-通过id删除", notes = "学生申请表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        sysUserApplyService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "学生申请表-通过id查询")
    @ApiOperation(value = "学生申请表-通过id查询", notes = "学生申请表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SysUserApply sysUserApply = sysUserApplyService.getById(id);
        return Result.ok(sysUserApply);
    }

}
