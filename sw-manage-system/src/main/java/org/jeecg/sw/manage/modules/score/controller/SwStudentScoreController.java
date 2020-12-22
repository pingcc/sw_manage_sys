package org.jeecg.sw.manage.modules.score.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.base.controller.BaseSwController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysDepartModel;
import org.jeecg.common.system.vo.SysUserDepartInfoModel;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.sw.manage.modules.score.entity.SwStudentScore;
import org.jeecg.sw.manage.modules.score.service.ISwStudentScoreService;
import org.jeecg.sw.manage.modules.score.vo.SwStudentScoreEx;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @Description: 学生分数表
 * @Author: Andy
 * @Date: 2020-10-29
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "学生分数表")
@RestController
@RequestMapping("/score/swStudentScore")
public class SwStudentScoreController extends BaseSwController<SwStudentScore, ISwStudentScoreService> {
    @Autowired
    private ISwStudentScoreService swStudentScoreService;
    @Autowired
    private ISysBaseAPI sysBaseAPI;

    /**
     * 分页列表查询
     *
     * @param swStudentScore
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "学生分数表-分页列表查询")
    @ApiOperation(value = "学生分数表-分页列表查询", notes = "学生分数表-分页列表查询")
    @GetMapping(value = "/list")
    @PermissionData(pageComponent = "score/SwStudentScoreList")
    public Result<?> queryPageList(SwStudentScore swStudentScore,
        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        // 默认不查询已删除的数据
        if (swStudentScore.getIsDeleted() == null) {
            swStudentScore.setIsDeleted(0);
        }
        QueryWrapper<SwStudentScore> queryWrapper =
            QueryGenerator.initQueryWrapper(swStudentScore, req.getParameterMap());
        Page<SwStudentScore> page = new Page<SwStudentScore>(pageNo, pageSize);
        IPage<SwStudentScore> pageList = swStudentScoreService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param swStudentScore
     * @return
     */
    @AutoLog(value = "学生分数表-添加")
    @ApiOperation(value = "学生分数表-添加", notes = "学生分数表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SwStudentScore swStudentScore) {
        try {
            setSwStudentScore(swStudentScore);
            swStudentScoreService.save(swStudentScore);
            return Result.ok("添加成功！");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 编辑
     *
     * @param swStudentScore
     * @return
     */
    @AutoLog(value = "学生分数表-编辑")
    @ApiOperation(value = "学生分数表-编辑", notes = "学生分数表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SwStudentScore swStudentScore) {
        try {
            setSwStudentScore(swStudentScore);
            swStudentScoreService.updateById(swStudentScore);
            return Result.ok("编辑成功!");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    private void setSwStudentScore(SwStudentScore swStudentScore) throws Exception {
        //获取登录用户信息
        LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        String bpOrgCode = sysUser.getBpOrgCode();
        List<String> studentNoList = new ArrayList<>();
        studentNoList.add(swStudentScore.getStudentNo());
        List<SysUserDepartInfoModel> sysUserDepartInfoModels = sysBaseAPI.getUserBySysDeparts(studentNoList);
        if (sysUserDepartInfoModels == null || sysUserDepartInfoModels.size() == 0) {
            throw new Exception("班级找不到对应的学号");
        }
        //查询年级信息
        List<SysDepartModel> sysDepartBpModelList =
            sysBaseAPI.getDepartsByCode(new SysDepartModel().setDepType(2 + "").setOrgCode(bpOrgCode));
        if (sysDepartBpModelList == null || sysDepartBpModelList.size() == 0) {
            throw new Exception("账号必须切换对应年级的负责人");
        }
        SysUserDepartInfoModel sysUserDepartInfoModel = sysUserDepartInfoModels.get(0);
        swStudentScore.setSysOrgCode(sysUserDepartInfoModel.getOrgCode());
        swStudentScore.setSysOrgName(sysUserDepartInfoModel.getDepartName());
        swStudentScore.setSysBpOrgCode(bpOrgCode);
        SysDepartModel bpSysDepartModel = sysDepartBpModelList.get(0);
        swStudentScore.setSysBpOrgName(bpSysDepartModel.getDepartName());
        swStudentScore.setStudentBy(sysUserDepartInfoModel.getUserName());
        swStudentScore.setStudentName(sysUserDepartInfoModel.getRealName());
        swStudentScore.setStudentId(sysUserDepartInfoModel.getUserId());
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "学生分数表-通过id删除")
    @ApiOperation(value = "学生分数表-通过id删除", notes = "学生分数表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        swStudentScoreService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "学生分数表-通过id查询")
    @ApiOperation(value = "学生分数表-通过id查询", notes = "学生分数表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SwStudentScore swStudentScore = swStudentScoreService.getById(id);
        return Result.ok(swStudentScore);
    }

    /**
     * 导出excel
     *
     * @param
     * @param swStudentScore
     */
    @RequestMapping(value = "/exportXls")
    @PermissionData(pageComponent = "score/SwStudentScoreList")
    public ModelAndView exportXls(HttpServletRequest req, SwStudentScore swStudentScore) {

        // 默认不查询已删除的数据
        if (swStudentScore.getIsDeleted() == null) {
            swStudentScore.setIsDeleted(0);
        }
        QueryWrapper<SwStudentScore> queryWrapper =
            QueryGenerator.initQueryWrapper(swStudentScore, req.getParameterMap());
        List<SwStudentScore> list = swStudentScoreService.list(queryWrapper);
        List<SwStudentScoreEx> swStudentScoreExportList = new ArrayList<>();
        for (SwStudentScore item : list) {
            SwStudentScoreEx swStudentScoreExport = new SwStudentScoreEx();
            BeanUtils.copyProperties(item, swStudentScoreExport);
            swStudentScoreExportList.add(swStudentScoreExport);
        }
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "学生成绩表");
        mv.addObject(NormalExcelConstants.CLASS, SwStudentScoreEx.class);
        ExportParams exportParams = new ExportParams();
        mv.addObject(NormalExcelConstants.PARAMS, exportParams);
        mv.addObject(NormalExcelConstants.DATA_LIST, swStudentScoreExportList);
        return mv;
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @PostMapping(value = "/importExcel")
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;

        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(0);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<SwStudentScoreEx> list =
                    ExcelImportUtil.importExcel(file.getInputStream(), SwStudentScoreEx.class, params);
                if (list == null || list.isEmpty()) {
                    return Result.ok("文件导入失败！没有找到对应的导入数据");
                }
                Set<String> studentNoSet = new HashSet<>();
                for (int i = 0; i < list.size(); i++) {
                    SwStudentScoreEx swStudentScoreExport = list.get(i);
                    if (oConvertUtils.isEmpty(swStudentScoreExport.getStudentNo())) {
                        throw new Exception("第" + (i + 1) + "行，学号必填");
                    }
                    studentNoSet.add(swStudentScoreExport.getStudentNo());
                }
                List<String> studentNoList = new ArrayList<>(studentNoSet);
                //班级信息
                List<SysUserDepartInfoModel> sysUserDepartInfoModels = sysBaseAPI.getUserBySysDeparts(studentNoList);
                //成功的学生学号
                Set<String> userSet = new HashSet<>();
                for (SysUserDepartInfoModel item : sysUserDepartInfoModels) {
                    userSet.add(item.getWorkNo());
                }
                //验证导入的数据是否学号填错
                for (String s : studentNoList) {
                    if (!userSet.contains(s)) {
                        throw new Exception(s + "学号为找到对应的学生");
                    }
                }
                //获取登录用户信息
                LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
                String bpOrgCode = sysUser.getBpOrgCode();

                Map<String, SysUserDepartInfoModel> map = new HashMap<>();
                for (SysUserDepartInfoModel item : sysUserDepartInfoModels) {
                    //该年级下存在这个班级
                    if (bpOrgCode.indexOf(item.getOrgCode()) != -1) {
                        throw new Exception("账号所在的年级不存在该班级，请确认信息");
                    }
                    map.put(item.getWorkNo(), item);
                }
                //查询年级信息
                List<SysDepartModel> sysDepartBpModelList =
                    sysBaseAPI.getDepartsByCode(new SysDepartModel().setDepType(2 + "").setOrgCode(bpOrgCode));
                if (sysDepartBpModelList == null || sysDepartBpModelList.size() == 0) {
                    throw new Exception("导入账号必须切换对应年级的负责人");
                }
                List<SwStudentScore> swStudentScoreList = new ArrayList<>();
                for (SwStudentScoreEx item : list) {
                    SwStudentScore swStudentScore = new SwStudentScore();
                    BeanUtils.copyProperties(item, swStudentScore);
                    SysUserDepartInfoModel sysUserDepartInfoModel = map.get(item.getStudentNo());
                    swStudentScore.setSysOrgCode(sysUserDepartInfoModel.getOrgCode());
                    swStudentScore.setSysOrgName(sysUserDepartInfoModel.getDepartName());
                    swStudentScore.setSysBpOrgCode(bpOrgCode);
                    SysDepartModel bpSysDepartModel = sysDepartBpModelList.get(0);
                    swStudentScore.setSysBpOrgName(bpSysDepartModel.getDepartName());
                    swStudentScore.setStudentBy(sysUserDepartInfoModel.getUserName());
                    swStudentScore.setStudentName(sysUserDepartInfoModel.getRealName());
                    swStudentScore.setStudentId(sysUserDepartInfoModel.getUserId());
                    swStudentScoreList.add(swStudentScore);
                }
                //只能是操作的账号到对应的班级信息
                this.swStudentScoreService.saveBatch(swStudentScoreList);
                return Result.ok("文件导入成功！数据行数:" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error(e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.ok("文件导入失败！");
    }

}
