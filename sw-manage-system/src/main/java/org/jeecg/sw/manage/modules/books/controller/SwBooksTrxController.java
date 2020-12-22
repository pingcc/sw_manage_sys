package org.jeecg.sw.manage.modules.books.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.base.controller.BaseSwController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.sw.manage.modules.books.entity.SwBooksTrx;
import org.jeecg.sw.manage.modules.books.service.ISwBooksTrxService;
import org.jeecg.sw.manage.modules.books.vo.SwBooksTrxVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 书籍交易
 * @Author: Andy
 * @Date: 2020-09-27
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "书籍交易")
@RestController
@RequestMapping("/books/swBooksTrx")
public class SwBooksTrxController extends BaseSwController<SwBooksTrx, ISwBooksTrxService> {
    @Autowired
    private ISwBooksTrxService swBooksTrxService;

    /**
     * 分页列表查询
     *
     * @param swBooksTrxVo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "书籍交易-分页列表查询")
    @ApiOperation(value = "书籍交易-分页列表查询", notes = "书籍交易-分页列表查询")
    @GetMapping(value = "/list")
    @PermissionData(pageComponent = "books/SwBooksTrxList")
    public Result<?> queryPageList(SwBooksTrxVo swBooksTrxVo,
        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        // 默认不查询已删除的数据
        if (swBooksTrxVo.getIsDeleted() == null) {
            swBooksTrxVo.setIsDeleted(0);
        }
        QueryWrapper<SwBooksTrxVo> queryWrapper = QueryGenerator.initQueryWrapper(swBooksTrxVo, req.getParameterMap());
        Page<SwBooksTrxVo> page = new Page<>(pageNo, pageSize);
        if (pageNo == -1) {
            //查询所有数据
            List<SwBooksTrxVo> list = swBooksTrxService.queryBooksTrxList(queryWrapper);
            page.setRecords(list).setTotal(list.size());
        } else {
            List<SwBooksTrxVo> pageList = swBooksTrxService.queryBooksTrxList(page, queryWrapper);
            page.setRecords(pageList);
        }
        return Result.ok(page);
    }

    /**
     * 添加
     *
     * @param swBooksTrx
     * @return
     */
    @AutoLog(value = "书籍交易-添加")
    @ApiOperation(value = "书籍交易-添加", notes = "书籍交易-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SwBooksTrx swBooksTrx) {
        swBooksTrxService.save(swBooksTrx);
        return Result.ok("添加成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "书籍交易-通过id查询")
    @ApiOperation(value = "书籍交易-通过id查询", notes = "书籍交易-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SwBooksTrx swBooksTrx = swBooksTrxService.getById(id);
        return Result.ok(swBooksTrx);
    }

}
