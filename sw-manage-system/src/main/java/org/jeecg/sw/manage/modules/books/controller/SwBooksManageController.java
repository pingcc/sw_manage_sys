package org.jeecg.sw.manage.modules.books.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.base.controller.BaseSwController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.RequestParamsUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.sw.manage.modules.books.entity.SwBooksManage;
import org.jeecg.sw.manage.modules.books.request.BooksTrxReq;
import org.jeecg.sw.manage.modules.books.service.ISwBooksManageService;
import org.jeecg.sw.manage.modules.books.vo.SwBooksManageVO;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Description: 书籍管理表
 * @Author: Andy
 * @Date: 2020-09-27
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "书籍管理表")
@RestController
@RequestMapping("/books/swBooksManage")
public class SwBooksManageController extends BaseSwController<SwBooksManage, ISwBooksManageService> {
    @Autowired
    private ISwBooksManageService swBooksManageService;
    @Autowired
    private ISysBaseAPI sysBaseAPI;

    /**
     * 分页列表查询
     *
     * @param swBooksManageVO
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "书籍管理表-分页列表查询")
    @ApiOperation(value = "书籍管理表-分页列表查询", notes = "书籍管理表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SwBooksManageVO swBooksManageVO,
        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        // 默认不查询已删除的数据
        if (swBooksManageVO.getIsDeleted() == null) {
            swBooksManageVO.setIsDeleted(0);
        }
        QueryWrapper<SwBooksManageVO> queryWrapper =
            QueryGenerator.initQueryWrapper(swBooksManageVO, req.getParameterMap());
        Page<SwBooksManageVO> page = new Page<>(pageNo, pageSize);
        if (pageNo == -1) {
            //查询所有数据
            List<SwBooksManageVO> list = swBooksManageService.queryBooksManageList(queryWrapper);
            page.setRecords(list).setTotal(list.size());
        } else {
            List<SwBooksManageVO> pageList = swBooksManageService.queryBooksManageList(page, queryWrapper);
            page.setRecords(pageList);
        }
        return Result.ok(page);
    }

    /**
     * 添加
     *
     * @param swBooksManage
     * @return
     */
    @AutoLog(value = "书籍管理表-添加")
    @ApiOperation(value = "书籍管理表-添加", notes = "书籍管理表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SwBooksManageVO swBooksManageVO) {
        swBooksManageService.saveSwBooks(swBooksManageVO);
        return Result.ok("添加成功！");
    }

    /**
     * 添加
     *
     * @param booksTrxReq
     * @return
     */
    @AutoLog(value = "书籍交易-添加")
    @ApiOperation(value = "书籍交易-添加", notes = "书籍交易-添加")
    @PostMapping(value = "/operateBooks")
    public Result<?> saveOrUpdateOperate(@RequestBody BooksTrxReq booksTrxReq) {
        try {
            RequestParamsUtils.validateEntityFields(booksTrxReq, "booksId", "bookSale");
            if (booksTrxReq.getBookSale() == 1) {
                RequestParamsUtils.validateEntityFields(booksTrxReq, "username", "realname", "sysOrgCode", "sysOrgName",
                    "sysBpOrgCode", "sysBpOrgName");
            } else {
                RequestParamsUtils.validateEntityFields(booksTrxReq, "isBookNormal");
            }
            this.swBooksManageService.saveOrUpdateOperate(booksTrxReq);
            return Result.ok("操作成功!");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

    }

    /**
     * 编辑
     *
     * @param swBooksManage
     * @return
     */
    @AutoLog(value = "书籍管理表-编辑")
    @ApiOperation(value = "书籍管理表-编辑", notes = "书籍管理表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SwBooksManageVO swBooksManageVO) {
        swBooksManageService.editSwBooks(swBooksManageVO);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "书籍管理表-通过id查询")
    @ApiOperation(value = "书籍管理表-通过id查询", notes = "书籍管理表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SwBooksManage swBooksManage = swBooksManageService.getById(id);
        return Result.ok(swBooksManage);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param swBooksManage
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SwBooksManageVO swBooksManageVO) {
        // 默认不查询已删除的数据
        if (swBooksManageVO.getIsDeleted() == null) {
            swBooksManageVO.setIsDeleted(0);
        }
        QueryWrapper<SwBooksManageVO> queryWrapper =
            QueryGenerator.initQueryWrapper(swBooksManageVO, request.getParameterMap());
        List<SwBooksManageVO> list = swBooksManageService.queryBooksManageList(queryWrapper);
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "书籍管理表");
        mv.addObject(NormalExcelConstants.CLASS, SwBooksManageVO.class);
        ExportParams exportParams = new ExportParams();
        mv.addObject(NormalExcelConstants.PARAMS, exportParams);
        mv.addObject(NormalExcelConstants.DATA_LIST, list);
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
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        //书籍入库
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
                List<SwBooksManageVO> list =
                    ExcelImportUtil.importExcel(file.getInputStream(), SwBooksManageVO.class, params);
                if (list == null || list.isEmpty()) {
                    return Result.ok("文件导入失败！没有找到对应的入库数据");
                }
                for (int i = 0; i < list.size(); i++) {
                    SwBooksManageVO swBooksManageVO = list.get(i);
                    String bookName = swBooksManageVO.getBooksName();
                    String booksCode = swBooksManageVO.getBooksCode();
                    String booksBarCode = swBooksManageVO.getBooksBarCode();
                    String booksPosCode = swBooksManageVO.getBooksPosCode();
                    if (oConvertUtils.isEmpty(bookName)) {
                        throw new Exception("书籍入库,第" + (i + 1) + "行，书籍名称必填");
                    }
                    if (oConvertUtils.isEmpty(booksBarCode)) {
                        throw new Exception("书籍入库,第" + (i + 1) + "行，书籍条码必填");
                    }
                    if (oConvertUtils.isEmpty(booksCode)) {
                        throw new Exception("书籍入库,第" + (i + 1) + "行，书籍编码必填");
                    }
                    if (oConvertUtils.isEmpty(booksPosCode)) {
                        throw new Exception("书籍入库,第" + (i + 1) + "行，书籍入库位置必填");
                    }
                }
                swBooksManageService.batchExportSwBook(list);
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
