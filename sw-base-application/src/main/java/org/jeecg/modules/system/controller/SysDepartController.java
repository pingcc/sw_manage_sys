package org.jeecg.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.ImportExcelUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.DepartIdModel;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 部门表 前端控制器
 * <p>
 *
 * @Author: Steve @Since： 2019-01-22
 */
@Slf4j
@Api(tags = "部门表")
@RestController
@RequestMapping("/sys/sysDepart")
public class SysDepartController {

    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    public RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysDictService sysDictService;

    /**
     * 查询数据 查出我的部门,并以树结构数据格式响应给前端
     *
     * @return
     */
    @ApiOperation(value = "查询我的部门数据返回树形结构", notes = "查询我的部门数据返回树形结构")
    @RequestMapping(value = "/queryMyDeptTreeList", method = RequestMethod.GET)
    public Result<List<SysDepartTreeModel>> queryMyDeptTreeList() {
        Result<List<SysDepartTreeModel>> result = new Result<>();
        LoginUser user = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        try {
            if (oConvertUtils.isNotEmpty(user.getUserIdentity()) && user.getUserIdentity()
                .equals(CommonConstant.USER_IDENTITY_2)) {
                List<SysDepartTreeModel> list = sysDepartService.queryMyDeptTreeList(user.getDepartIds());
                setTextCode(list);
                result.setResult(list);
                result.setMessage(CommonConstant.USER_IDENTITY_2.toString());
                result.setSuccess(true);
            } else {
                result.setMessage(CommonConstant.USER_IDENTITY_1.toString());
                result.setSuccess(true);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 查询数据 查出所有部门,并以树结构数据格式响应给前端
     *
     * @return
     */
    @ApiOperation(value = "查询部门数据返回树形结构", notes = "查询部门数据返回树形结构")
    @RequestMapping(value = "/queryTreeList", method = RequestMethod.GET)
    public Result<List<SysDepartTreeModel>> queryTreeList() {
        Result<List<SysDepartTreeModel>> result = new Result<>();
        try {
            // 从内存中读取
            //			List<SysDepartTreeModel> list =FindsDepartsChildrenUtil.getSysDepartTreeList();
            //			if (CollectionUtils.isEmpty(list)) {
            //				list = sysDepartService.queryTreeList();
            //			}
            List<SysDepartTreeModel> list = sysDepartService.queryTreeList();
            setTextCode(list);
            result.setResult(list);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    private void setTextCode(List<SysDepartTreeModel> list) { //遍历设置块码
        for (SysDepartTreeModel model : list) {
            if (model.getDepType() != null) {
                String depTypeText = sysDictService.queryDictTextByKey("dep_type", model.getDepType());
                model.setDepType_dictText(depTypeText);
            }
            List<SysDepartTreeModel> list1 = model.getChildren();
            if (list1 != null && list.size() > 0) {
                setTextCode(list1);
            }
        }
    }

    /**
     * 添加新数据 添加用户新建的部门对象数据,并保存到数据库
     *
     * @param sysDepart
     * @return
     */
    @ApiOperation(value = "添加部门", notes = "添加部门")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @CacheEvict(value = {CacheConstant.SYS_DEPARTS_CACHE, CacheConstant.SYS_DEPART_IDS_CACHE}, allEntries = true)
    public Result<SysDepart> add(@RequestBody SysDepart sysDepart, HttpServletRequest request) {
        Result<SysDepart> result = new Result<SysDepart>();
        String username = JwtUtil.getUserNameByToken(request);
        try {
            sysDepart.setCreateBy(username);
            sysDepartService.saveDepartData(sysDepart, username);
            //清除部门树内存
            // FindsDepartsChildrenUtil.clearSysDepartTreeList();
            // FindsDepartsChildrenUtil.clearDepartIdModel();
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 编辑数据 编辑部门的部分数据,并保存到数据库
     *
     * @param sysDepart
     * @return
     */
    @ApiOperation(value = "编辑部门", notes = "编辑部门")
    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    @CacheEvict(value = {CacheConstant.SYS_DEPARTS_CACHE, CacheConstant.SYS_DEPART_IDS_CACHE}, allEntries = true)
    public Result<SysDepart> edit(@RequestBody SysDepart sysDepart, HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        sysDepart.setUpdateBy(username);
        Result<SysDepart> result = new Result<SysDepart>();
        SysDepart sysDepartEntity = sysDepartService.getById(sysDepart.getId());
        if (sysDepartEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = sysDepartService.updateDepartDataById(sysDepart, username);
            // TODO 返回false说明什么？
            if (ok) {
                //清除部门树内存
                //FindsDepartsChildrenUtil.clearSysDepartTreeList();
                //FindsDepartsChildrenUtil.clearDepartIdModel();
                result.success("修改成功!");
            }
        }
        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除部门", notes = "删除部门")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @CacheEvict(value = {CacheConstant.SYS_DEPARTS_CACHE, CacheConstant.SYS_DEPART_IDS_CACHE}, allEntries = true)
    public Result<SysDepart> delete(@RequestParam(name = "id", required = true) String id) {

        Result<SysDepart> result = new Result<SysDepart>();
        SysDepart sysDepart = sysDepartService.getById(id);
        if (sysDepart == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = sysDepartService.delete(id);
            if (ok) {
                //清除部门树内存
                //FindsDepartsChildrenUtil.clearSysDepartTreeList();
                // FindsDepartsChildrenUtil.clearDepartIdModel();
                result.success("删除成功!");
            }
        }
        return result;
    }

    /**
     * 批量删除 根据前端请求的多个ID,对数据库执行删除相关部门数据的操作
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    @CacheEvict(value = {CacheConstant.SYS_DEPARTS_CACHE, CacheConstant.SYS_DEPART_IDS_CACHE}, allEntries = true)
    public Result<SysDepart> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {

        Result<SysDepart> result = new Result<SysDepart>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.sysDepartService.deleteBatchWithChildren(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }

    /**
     * 查询数据 添加或编辑页面对该方法发起请求,以树结构形式加载所有部门的名称,方便用户的操作
     *
     * @return
     */
    @ApiOperation(value = "id查询树形部门数据", notes = "id查询树形部门数据")
    @RequestMapping(value = "/queryIdTree", method = RequestMethod.GET)
    public Result<List<DepartIdModel>> queryIdTree() {
        //		Result<List<DepartIdModel>> result = new Result<List<DepartIdModel>>();
        //		List<DepartIdModel> idList;
        //		try {
        //			idList = FindsDepartsChildrenUtil.wrapDepartIdModel();
        //			if (idList != null && idList.size() > 0) {
        //				result.setResult(idList);
        //				result.setSuccess(true);
        //			} else {
        //				sysDepartService.queryTreeList();
        //				idList = FindsDepartsChildrenUtil.wrapDepartIdModel();
        //				result.setResult(idList);
        //				result.setSuccess(true);
        //			}
        //			return result;
        //		} catch (Exception e) {
        //			log.error(e.getMessage(),e);
        //			result.setSuccess(false);
        //			return result;
        //		}
        Result<List<DepartIdModel>> result = new Result<>();
        try {
            List<DepartIdModel> list = sysDepartService.queryDepartIdTreeList();
            result.setResult(list);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * <p>
     * 部门搜索功能方法,根据关键字模糊搜索相关部门
     * </p>
     *
     * @param keyWord
     * @return
     */
    @ApiOperation(value = "关键字模糊搜索相关部门", notes = "关键字模糊搜索相关部门")
    @RequestMapping(value = "/searchBy", method = RequestMethod.GET)
    public Result<List<SysDepartTreeModel>> searchBy(@RequestParam(name = "keyWord", required = true) String keyWord,
        @RequestParam(name = "myDeptSearch", required = false) String myDeptSearch) {
        Result<List<SysDepartTreeModel>> result = new Result<List<SysDepartTreeModel>>();
        //部门查询，myDeptSearch为1时为我的部门查询，登录用户为上级时查只查负责部门下数据
        LoginUser user = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        String departIds = null;
        if (oConvertUtils.isNotEmpty(user.getUserIdentity()) && user.getUserIdentity()
            .equals(CommonConstant.USER_IDENTITY_2)) {
            departIds = user.getDepartIds();
        }
        List<SysDepartTreeModel> treeList = this.sysDepartService.searhBy(keyWord, myDeptSearch, departIds);
        if (treeList == null || treeList.size() == 0) {
            result.setSuccess(false);
            result.setMessage("未查询匹配数据！");
            return result;
        }
        result.setResult(treeList);
        return result;
    }

    /**
     * 导出excel
     *
     * @param request
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(SysDepart sysDepart, HttpServletRequest request) {
        // Step.1 组装查询条件
        QueryWrapper<SysDepart> queryWrapper = QueryGenerator.initQueryWrapper(sysDepart, request.getParameterMap());
        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<SysDepart> pageList = sysDepartService.list(queryWrapper);
        //按字典排序
        Collections.sort(pageList, new Comparator<SysDepart>() {
            @Override
            public int compare(SysDepart arg0, SysDepart arg1) {
                return arg0.getOrgCode().compareTo(arg1.getOrgCode());
            }
        });
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "部门列表");
        mv.addObject(NormalExcelConstants.CLASS, SysDepart.class);
        LoginUser user = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("部门列表数据", "导出人:" + user.getRealname(), "导出信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    @CacheEvict(value = {CacheConstant.SYS_DEPARTS_CACHE, CacheConstant.SYS_DEPART_IDS_CACHE}, allEntries = true)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
        List<String> errorMessageList = new ArrayList<>();
        List<SysDepart> listSysDeparts1 = null;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                // orgCode编码长度
                int codeLength = 3;
                listSysDeparts1 = ExcelImportUtil.importExcel(file.getInputStream(), SysDepart.class, params);
                List<SysDepart> listSysDeparts = new ArrayList<>();
                for (SysDepart sysDepart : listSysDeparts1) {
                    if (StringUtils.isNotEmpty(sysDepart.getOrgCode())) {
                        listSysDeparts.add(sysDepart);
                    }
                }

                //按长度排序
                Collections.sort(listSysDeparts, new Comparator<SysDepart>() {
                    @Override
                    public int compare(SysDepart arg0, SysDepart arg1) {
                        System.out.println(arg0.getOrgCode() + "---" + arg1.getOrgCode());
                        return arg0.getOrgCode().length() - arg1.getOrgCode().length();
                    }
                });

                int num = 0;
                for (SysDepart sysDepart : listSysDeparts) {
                    String orgCode = sysDepart.getOrgCode();
                    if (orgCode.length() > codeLength) {
                        String parentCode = orgCode.substring(0, orgCode.length() - codeLength);
                        QueryWrapper<SysDepart> queryWrapper = new QueryWrapper<SysDepart>();
                        queryWrapper.eq("org_code", parentCode);
                        try {
                            SysDepart parentDept = sysDepartService.getOne(queryWrapper);
                            if (!parentDept.equals(null)) {
                                sysDepart.setParentId(parentDept.getId());
                            } else {
                                sysDepart.setParentId("");
                            }
                        } catch (Exception e) {
                            //没有查找到parentDept
                        }
                    } else {
                        sysDepart.setParentId("");
                    }
                    sysDepart.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
                    ImportExcelUtil.importDateSaveOne(sysDepart, ISysDepartService.class, errorMessageList, num,
                        CommonConstant.SQL_INDEX_UNIQ_DEPART_ORG_CODE);
                    num++;
                }
                //清空部门缓存
                Set keys3 = redisTemplate.keys(CacheConstant.SYS_DEPARTS_CACHE + "*");
                Set keys4 = redisTemplate.keys(CacheConstant.SYS_DEPART_IDS_CACHE + "*");
                redisTemplate.delete(keys3);
                redisTemplate.delete(keys4);
                return ImportExcelUtil
                    .imporReturnRes(errorMessageList.size(), listSysDeparts.size() - errorMessageList.size(),
                        errorMessageList);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("文件导入失败！");
    }

    /**
     * 查询所有部门信息
     *
     * @return
     */
    @ApiOperation(value = "id查询部门信息", notes = "id查询部门信息")
    @GetMapping("listAll")
    public Result<List<SysDepart>> listAll(@RequestParam(name = "id", required = false) String id) {
        Result<List<SysDepart>> result = new Result<>();
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
        query.orderByAsc(SysDepart::getOrgCode);
        if (oConvertUtils.isNotEmpty(id)) {
            String arr[] = id.split(",");
            query.in(SysDepart::getId, arr);
        }
        List<SysDepart> ls = this.sysDepartService.list(query);
        result.setSuccess(true);
        result.setResult(ls);
        return result;
    }

    /**
     * 查询所有部门信息
     *
     * @return
     */
    @ApiOperation(value = "查询所有部门信息", notes = "查询所有部门信息")
    @GetMapping("queryDepart")
    public Result<?> queryDepart(SysDepart sysDepart) {
        sysDepart.setDelFlag(0 + "");
        QueryWrapper<SysDepart> queryWrapper = QueryGenerator.initQueryWrapper(sysDepart, new HashMap<>());
        queryWrapper.orderByAsc("depart_order");
        List<SysDepart> ls = this.sysDepartService.list(queryWrapper);
        return Result.ok(ls);
    }

    /**
     * 查询数据 查出所有部门,并以树结构数据格式响应给前端
     *
     * @return
     */
    @ApiOperation(value = "关键字查询所有部门信息返回树形结构", notes = "关键字查询所有部门信息返回树形结构")
    @RequestMapping(value = "/queryTreeByKeyWord", method = RequestMethod.GET)
    public Result<Map<String, Object>> queryTreeByKeyWord(
        @RequestParam(name = "keyWord", required = false) String keyWord) {
        Result<Map<String, Object>> result = new Result<>();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            List<SysDepartTreeModel> list = sysDepartService.queryTreeByKeyWord(keyWord);
            //根据keyWord获取用户信息
            LambdaQueryWrapper<SysUser> queryUser = new LambdaQueryWrapper<SysUser>();
            queryUser.eq(SysUser::getDelFlag, 0);
            queryUser.and(i -> i.like(SysUser::getUsername, keyWord).or().like(SysUser::getRealname, keyWord));
            List<SysUser> sysUsers = this.sysUserService.list(queryUser);
            map.put("userList", sysUsers);
            map.put("departList", list);
            result.setResult(map);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 根据部门编码获取部门信息
     *
     * @param orgCode
     * @return
     */
    @GetMapping("/getDepartName")
    public Result<SysDepart> getDepartName(@RequestParam(name = "orgCode") String orgCode) {
        Result<SysDepart> result = new Result<>();
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
        query.eq(SysDepart::getOrgCode, orgCode);
        SysDepart sysDepart = sysDepartService.getOne(query);
        result.setSuccess(true);
        result.setResult(sysDepart);
        return result;
    }

    @ApiOperation(value = "查询下级部门数据返回树形结构", notes = "查询下级部门数据返回树形结构")
    @RequestMapping(value = "/queryTreeByOrgCode", method = RequestMethod.GET)
    public Result<?> queryTreeByOrgCode(@RequestParam(name = "orgCode", required = false) String orgCode) {
        Result<List<SysDepartTreeModel>> result = new Result<>();
        try {
            List<SysDepartTreeModel> list = sysDepartService.queryTreeByOrgCode(orgCode);
            return Result.ok(list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return result.error500(e.getMessage());
        }

    }

    @AutoLog(value = "查询上级部门")
    @ApiOperation(value = "查询上级部门", notes = "查询上级部门")
    @RequestMapping(value = "/getSupDepart", method = RequestMethod.GET)
    public Result<?> getSupDepart(SysDepart sysDepart) {
        Result<SysDepart> result = new Result<>();
        try {
            List<SysDepart> list = sysDepartService.getSupDepart(sysDepart);
            return Result.ok(list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return result.error500(e.getMessage());
        }
    }

}
