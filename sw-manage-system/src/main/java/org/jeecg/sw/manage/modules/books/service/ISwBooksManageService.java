package org.jeecg.sw.manage.modules.books.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.jeecg.sw.manage.modules.books.entity.SwBooksManage;
import org.jeecg.sw.manage.modules.books.request.BooksTrxReq;
import org.jeecg.sw.manage.modules.books.vo.SwBooksManageVO;

import java.util.List;

/**
 * @Description: 书籍管理表
 * @Author: Andy
 * @Date: 2020-09-27
 * @Version: V1.0
 */
public interface ISwBooksManageService extends IService<SwBooksManage> {
    /**
     * 查询书籍管理列表
     *
     * @param page
     * @param wrapper
     * @return
     */
    List<SwBooksManageVO> queryBooksManageList(Page<SwBooksManageVO> page,
        @Param(Constants.WRAPPER) Wrapper<SwBooksManageVO> wrapper);

    /**
     * 无分页
     *
     * @param wrapper
     * @return
     */
    List<SwBooksManageVO> queryBooksManageList(@Param(Constants.WRAPPER) Wrapper<SwBooksManageVO> wrapper);

    /**
     * 保存信息；
     *
     * @param swBooksManageVO
     */
    void saveSwBooks(SwBooksManageVO swBooksManageVO);

    void editSwBooks(SwBooksManageVO swBooksManageVO);

    void batchExportSwBook(List<SwBooksManageVO> swBooksManageVOs);

    void saveOrUpdateOperate(BooksTrxReq booksTrxReq) throws Exception;
}
