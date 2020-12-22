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
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.base.controller.BaseSwController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.SysRoleModel;
import org.jeecg.modules.system.entity.SwFlowApprover;
import org.jeecg.modules.system.service.ISwFlowApproverService;
import org.jeecg.modules.system.vo.SwFlowApproverPage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Description: 审核角色
 * @Author: Andy
 * @Date: 2020-07-20
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "审核角色")
@RestController
@RequestMapping("/system/snowFlowApprover")
public class SwFlowApproverController extends BaseSwController<SwFlowApprover, ISwFlowApproverService> {
    @Autowired
    private ISwFlowApproverService snowFlowApproverService;
    @Autowired
    private ISysBaseAPI sysBaseAPI;

    /**
     * 分页列表查询
     *
     * @param snowFlowApprover
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "审核角色-分页列表查询")
    @ApiOperation(value = "审核角色-分页列表查询", notes = "审核角色-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SwFlowApprover snowFlowApprover,
        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        // 默认不查询已删除的数据
        if (snowFlowApprover.getIsDeleted() == null) {
            snowFlowApprover.setIsDeleted(0);
        }

        QueryWrapper<SwFlowApprover> queryWrapper =
            QueryGenerator.initQueryWrapper(snowFlowApprover, req.getParameterMap());
        Page<SwFlowApprover> page = new Page<SwFlowApprover>(pageNo, pageSize);
        IPage<SwFlowApprover> pageList = snowFlowApproverService.page(page, queryWrapper);
        Page<SwFlowApproverPage> snowFlowApproverPagePage = new Page<SwFlowApproverPage>(pageNo, pageSize);
        List<SwFlowApproverPage> list = new LinkedList<SwFlowApproverPage>();
        for (SwFlowApprover sf : pageList.getRecords()) {
            SwFlowApproverPage SnowFlowApproverPage = new SwFlowApproverPage();
            BeanUtils.copyProperties(sf, SnowFlowApproverPage);
            list.add(SnowFlowApproverPage);
        }

        Set<String> rolCodeSet = new HashSet<>();

        for (SwFlowApprover item : pageList.getRecords()) {
            rolCodeSet.add(item.getRoleCode());
        }
        String rolCodes = StringUtils.join(rolCodeSet.toArray(), ",");

        if (StringUtils.isNotEmpty(rolCodes)) {
            SysRoleModel sysRoleModel = new SysRoleModel();
            sysRoleModel.setRoleCode(rolCodes);
            List<SysRoleModel> roleModelList = sysBaseAPI.queryRoleList(sysRoleModel);
            for (SwFlowApproverPage snowFlowApproverPage : list) {
                for (SysRoleModel roleModel : roleModelList) {
                    if (snowFlowApproverPage.getRoleCode().equals(roleModel.getRoleCode())) {
                        snowFlowApproverPage.setRoleName(roleModel.getRoleName());
                        break;
                    }
                }
            }
        }

        snowFlowApproverPagePage.setTotal(pageList.getTotal()).setRecords(list);

        return Result.ok(snowFlowApproverPagePage);
    }

    /**
     * 添加
     *
     * @param snowFlowApprover
     * @return
     */
    @AutoLog(value = "审核角色-添加")
    @ApiOperation(value = "审核角色-添加", notes = "审核角色-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SwFlowApprover snowFlowApprover) {
        snowFlowApproverService.save(snowFlowApprover);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param snowFlowApprover
     * @return
     */
    @AutoLog(value = "审核角色-编辑")
    @ApiOperation(value = "审核角色-编辑", notes = "审核角色-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SwFlowApprover snowFlowApprover) {
        snowFlowApproverService.updateById(snowFlowApprover);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "审核角色-通过id删除")
    @ApiOperation(value = "审核角色-通过id删除", notes = "审核角色-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        SwFlowApprover snowDelivery = snowFlowApproverService.getById(id);
        snowDelivery.setIsDeleted(CommonConstant.DEL_FLAG_1);
        snowFlowApproverService.updateById(snowDelivery);
        //        snowFlowApproverService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "审核角色-批量删除")
    @ApiOperation(value = "审核角色-批量删除", notes = "审核角色-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        List<SwFlowApprover> list = new LinkedList<>();
        for (String id : idList) {
            SwFlowApprover snowDelivery = snowFlowApproverService.getById(id);
            snowDelivery.setIsDeleted(CommonConstant.DEL_FLAG_1);
            snowFlowApproverService.updateById(snowDelivery);
            list.add(snowDelivery);
        }
        this.snowFlowApproverService.updateBatchById(list);
        //        this.snowFlowApproverService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "审核角色-通过id查询")
    @ApiOperation(value = "审核角色-通过id查询", notes = "审核角色-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SwFlowApprover snowFlowApprover = snowFlowApproverService.getById(id);
        return Result.ok(snowFlowApprover);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param snowFlowApprover
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SwFlowApprover snowFlowApprover) {
        return super.exportXls(request, snowFlowApprover, SwFlowApprover.class, "审核角色");
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
        return super.importExcel(request, response, SwFlowApprover.class);
    }

}
