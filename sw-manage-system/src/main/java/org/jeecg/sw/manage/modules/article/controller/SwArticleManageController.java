package org.jeecg.sw.manage.modules.article.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.base.controller.BaseSwController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysDepartModel;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.sw.manage.modules.article.constant.ArticleConstant;
import org.jeecg.sw.manage.modules.article.entity.SwArticleApproverR;
import org.jeecg.sw.manage.modules.article.entity.SwArticleManage;
import org.jeecg.sw.manage.modules.article.request.ApprovalReq;
import org.jeecg.sw.manage.modules.article.service.ISwArticleApproverRService;
import org.jeecg.sw.manage.modules.article.service.ISwArticleManageService;
import org.jeecg.sw.manage.modules.article.vo.SwArticleManageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @Description: 文章管理
 * @Author: Andy
 * @Date: 2020-09-27
 * @Version: V1.0
 */
@Slf4j
@Api(tags = " 文章管理")
@RestController
@RequestMapping("/article/swArticleManage")
public class SwArticleManageController extends BaseSwController<SwArticleManage, ISwArticleManageService> {
    @Autowired
    private ISwArticleManageService swArticleManageService;
    @Autowired
    private ISwArticleApproverRService swArticleApproverRService;
    @Autowired
    private ISysBaseAPI sysBaseAPI;

    /**
     * 分页列表查询
     *
     * @param swArticleManage
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = " 文章管理-分页列表查询")
    @ApiOperation(value = " 文章管理-分页列表查询", notes = " 文章管理-分页列表查询")
    @GetMapping(value = "/list")
    @PermissionData(pageComponent = "article/SwArticleManageList")
    public Result<?> list(SwArticleManage swArticleManage,
        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        return getArticleMangeList(swArticleManage, pageNo, pageSize, req);

    }

    /**
     * 分页列表查询
     *
     * @param swArticleManage
     * @param pageNo
     * @param pageSize
     * @param req
     * @return 无数据权限 社区接口
     */
    @AutoLog(value = " 文章管理-分页列表查询")
    @ApiOperation(value = " 文章管理-分页列表查询", notes = " 文章管理-分页列表查询")
    @GetMapping(value = "/queryPageList")
    public Result<?> queryPageList(SwArticleManage swArticleManage,
        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        return getArticleMangeList(swArticleManage, pageNo, pageSize, req);
    }

    private Result<?> getArticleMangeList(SwArticleManage swArticleManage,
        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        // 默认不查询已删除的数据
        if (swArticleManage.getIsDeleted() == null) {
            swArticleManage.setIsDeleted(0);
        }
        HashSet<String> orgCodeSet = new HashSet<>();
        HashSet<String> bpOrgCodeSet = new HashSet<>();
        QueryWrapper<SwArticleManage> queryWrapper =
            QueryGenerator.initQueryWrapper(swArticleManage, req.getParameterMap());
        Page<SwArticleManage> page = new Page<>(pageNo, pageSize);
        IPage<SwArticleManage> pageList = swArticleManageService.page(page, queryWrapper);
        for (SwArticleManage item : pageList.getRecords()) {
            orgCodeSet.add(item.getSysOrgCode());
            bpOrgCodeSet.add(item.getSysBpOrgCode());
        }
        String orgCodes = StringUtils.join(orgCodeSet.toArray(), ",");
        String bpOrgCodes = StringUtils.join(bpOrgCodeSet.toArray(), ",");
        //查询班级
        List<SysDepartModel> sysDepartModelList = new ArrayList<>();
        if (oConvertUtils.isNotEmpty(orgCodes)) {
            sysDepartModelList = sysBaseAPI.getDepartsByCode(new SysDepartModel().setOrgCode(orgCodes));
        }
        //查询平台
        List<SysDepartModel> sysDepartBpModelList = new ArrayList<>();
        if (oConvertUtils.isNotEmpty(bpOrgCodes)) {
            sysDepartBpModelList =
                sysBaseAPI.getDepartsByCode(new SysDepartModel().setOrgCode(bpOrgCodes).setDepType(2 + ""));
        }
        for (SwArticleManage articleManage : pageList.getRecords()) {
            for (SysDepartModel item : sysDepartModelList) {
                if (StringUtils.isNotEmpty(articleManage.getSysOrgCode())) {
                    if (articleManage.getSysOrgCode().equals(item.getOrgCode())) {
                        articleManage.setSysOrgName(item.getDepartName());
                        break;
                    }
                }
            }
            for (SysDepartModel item : sysDepartBpModelList) {
                if (StringUtils.isNotEmpty(articleManage.getSysBpOrgCode())) {
                    if (articleManage.getSysBpOrgCode().equals(item.getOrgCode())) {
                        articleManage.setSysBpOrgName(item.getDepartName());
                        break;
                    }
                }
            }
        }
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param swArticleManage
     * @return
     */
    @AutoLog(value = " 文章管理-添加")
    @ApiOperation(value = " 文章管理-添加", notes = " 文章管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SwArticleManage swArticleManage) {
        LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        String status = swArticleManage.getStatus();
        if (oConvertUtils.isEmpty(status)) {
            swArticleManage.setStatus(ArticleConstant.STATUS_DRAFT);
        }
        swArticleManage.setAuthorName(sysUser.getRealname());
        swArticleManage.setIsPublish(0);
        swArticleManageService.save(swArticleManage);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param swArticleManage
     * @return
     */
    @AutoLog(value = " 文章管理-编辑")
    @ApiOperation(value = " 文章管理-编辑", notes = " 文章管理-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SwArticleManage swArticleManage) {
        try {
            SwArticleManage entity = swArticleManageService.getById(swArticleManage.getId());
            String status = entity.getStatus();
            if (!status.equals(ArticleConstant.STATUS_ALLOW)) {
                BeanUtils.copyProperties(swArticleManage, entity);
                // 文章审核状态不能修改
                entity.setStatus(status);
                swArticleManageService.updateById(swArticleManage);
            } else {
                throw new Exception("审批通过的文章不能删除！");
            }

            return Result.ok("编辑成功!");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

    }

    /**
     * 文章审批
     *
     * @param approvalReq
     * @return
     */
    @AutoLog(value = " 文章审批")
    @ApiOperation(value = "文章审批", notes = " 文章审批")
    @PostMapping(value = "/articleApproval")
    public Result<?> articleOperate(@RequestBody ApprovalReq approvalReq) {
        try {
            swArticleManageService.articleOperate(approvalReq);
            return Result.ok("操作成功!");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

    }

    /**
     * 文章审批
     *
     * @param approvalReq
     * @return
     */
    @AutoLog(value = "文章提交审核")
    @ApiOperation(value = "文章提交审核", notes = "文章提交审核")
    @PostMapping(value = "/articleCommit")
    public Result<?> articleCommit(String id) {
        try {
            SwArticleManage swArticleManage = swArticleManageService.getById(id);
            swArticleManage.setStatus(ArticleConstant.STATUS_APPROVAL);
            swArticleManageService.updateById(swArticleManage);
            return Result.ok("操作成功!");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = " 文章管理-通过id删除")
    @ApiOperation(value = " 文章管理-通过id删除", notes = " 文章管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        SwArticleManage swArticleManage = swArticleManageService.getById(id);
        swArticleManage.setIsDeleted(1);
        swArticleManageService.updateById(swArticleManage);
        return Result.ok("删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = " 文章管理-通过id查询")
    @ApiOperation(value = " 文章管理-通过id查询", notes = " 文章管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SwArticleManage swArticleManage = swArticleManageService.getById(id);
        List<SwArticleApproverR> list = swArticleApproverRService.getSwArticleApproverR(id);
        SwArticleManageVo swArticleManageVo = new SwArticleManageVo();
        BeanUtils.copyProperties(swArticleManage, swArticleManageVo);
        swArticleManageVo.setSwArticleApproverRList(list);
        return Result.ok(list);
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
        return super.importExcel(request, response, SwArticleManage.class);
    }

}
