package org.jeecg.sw.manage.modules.books.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.sw.manage.modules.books.entity.SwBooksManage;
import org.jeecg.sw.manage.modules.books.vo.SwBooksManageVO;

import java.util.List;

/**
 * @Description: 书籍管理表
 * @Author: Andy
 * @Date: 2020-09-27
 * @Version: V1.0
 */
public interface SwBooksManageMapper extends BaseMapper<SwBooksManage> {
    /**
     * 查询书籍管理列表
     *
     * @param page
     * @param wrapper
     * @return
     */
    List<SwBooksManageVO> queryBooksManageList(Page<SwBooksManageVO> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 查询书籍管理列表
     *
     * @param wrapper
     * @return
     */
    List<SwBooksManageVO> queryBooksManageList(@Param(Constants.WRAPPER) Wrapper wrapper);

}
