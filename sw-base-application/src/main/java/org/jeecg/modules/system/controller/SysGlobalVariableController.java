package org.jeecg.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.BaseSwController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.system.entity.SysGlobalVariable;
import org.jeecg.modules.system.service.ISysGlobalVariableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 全局缓存变量表
 * @Author: Andy
 * @Date: 2020-07-15
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "全局缓存变量表")
@RestController
@RequestMapping("/sys/globalVariable")
public class SysGlobalVariableController extends BaseSwController<SysGlobalVariable, ISysGlobalVariableService> {
    @Autowired
    private ISysGlobalVariableService sysGlobalVariableService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页列表查询
     *
     * @param sysGlobalVariable
     * @param req
     * @return
     */
    @AutoLog(value = "全局缓存变量表-分页列表查询")
    @ApiOperation(value = "全局缓存变量表-分页列表查询", notes = "全局缓存变量表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SysGlobalVariable sysGlobalVariable, HttpServletRequest req) {
        // 默认不查询已删除的数据
        if (sysGlobalVariable.getIsDeleted() == null) {
            sysGlobalVariable.setIsDeleted(0);
        }
        Map<Object, Object> map = redisUtil.hmget(CacheConstant.GLOBAL_REDIS_CACHE);
        if (map == null || map.size() == 0) {
            QueryWrapper<SysGlobalVariable> queryWrapper =
                QueryGenerator.initQueryWrapper(sysGlobalVariable, req.getParameterMap());
            Page<SysGlobalVariable> page = new Page<SysGlobalVariable>(1, -1);
            IPage<SysGlobalVariable> pageList = sysGlobalVariableService.page(page, queryWrapper);
            List<SysGlobalVariable> sysList = pageList.getRecords();
            if (sysList != null && sysList.size() > 0) {
                Map<String, Object> map1 = new HashMap<>();
                for (SysGlobalVariable item : sysList) {
                    map1.put(item.getRedisKey(), item);
                }
                redisUtil.hmset(CacheConstant.GLOBAL_REDIS_CACHE, map1);
                return Result.ok(sysList);
            }
        }
        List<SysGlobalVariable> list = new LinkedList<>();
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            SysGlobalVariable sysGlobalVariable1 = (SysGlobalVariable)entry.getValue();
            list.add(sysGlobalVariable1);
        }

        return Result.ok(list);
    }

    @AutoLog(value = "全局缓存变量表-刷新全局变量")
    @ApiOperation(value = "全局缓存变量表-刷新全局变量", notes = "全局缓存变量表-刷新全局变量")
    @GetMapping(value = "/refreshSysGlobalVariable")
    public Result<?> refreshSysGlobalVariable(HttpServletRequest req) {
        redisUtil.del(CacheConstant.GLOBAL_REDIS_CACHE);
        return queryPageList(new SysGlobalVariable(), req);
    }

    /**
     * 添加
     *
     * @param sysGlobalVariable
     * @return
     */
    @AutoLog(value = "全局缓存变量表-添加")
    @ApiOperation(value = "全局缓存变量表-添加", notes = "全局缓存变量表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SysGlobalVariable sysGlobalVariable) {
        sysGlobalVariableService.save(sysGlobalVariable);
        //全局缓存
        redisUtil.hset(CacheConstant.GLOBAL_REDIS_CACHE, sysGlobalVariable.getRedisKey(), sysGlobalVariable);

        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param sysGlobalVariable
     * @return
     */
    @AutoLog(value = "全局缓存变量表-编辑")
    @ApiOperation(value = "全局缓存变量表-编辑", notes = "全局缓存变量表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SysGlobalVariable sysGlobalVariable) {
        sysGlobalVariableService.updateById(sysGlobalVariable);
        //全局缓存
        redisUtil.hset(CacheConstant.GLOBAL_REDIS_CACHE, sysGlobalVariable.getRedisKey(), sysGlobalVariable);

        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "全局缓存变量表-通过id删除")
    @ApiOperation(value = "全局缓存变量表-通过id删除", notes = "全局缓存变量表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {

        SysGlobalVariable sysGlobalVariable = sysGlobalVariableService.getById(id);
        sysGlobalVariable.setIsDeleted(CommonConstant.DEL_FLAG_1);
        sysGlobalVariableService.updateById(sysGlobalVariable);
        //全局缓存
        redisUtil.hmdel(CacheConstant.GLOBAL_REDIS_CACHE, sysGlobalVariable.getRedisKey());
        //        sysGlobalVariableService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "全局缓存变量表-通过id查询")
    @ApiOperation(value = "全局缓存变量表-通过id查询", notes = "全局缓存变量表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SysGlobalVariable sysGlobalVariable = sysGlobalVariableService.getById(id);
        return Result.ok(sysGlobalVariable);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param sysGlobalVariable
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysGlobalVariable sysGlobalVariable) {
        return super.exportXls(request, sysGlobalVariable, SysGlobalVariable.class, "全局缓存变量表");
    }

}
