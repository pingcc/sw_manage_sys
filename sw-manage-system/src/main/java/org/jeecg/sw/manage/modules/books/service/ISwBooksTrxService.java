package org.jeecg.sw.manage.modules.books.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.jeecg.sw.manage.modules.books.entity.SwBooksTrx;
import org.jeecg.sw.manage.modules.books.vo.SwBooksTrxVo;

import java.util.List;

/**
 * @Description: 书籍交易
 * @Author: Andy
 * @Date: 2020-09-27
 * @Version: V1.0
 */
public interface ISwBooksTrxService extends IService<SwBooksTrx> {
    /**
     * @param booksId 书籍id
     * @return
     */
    SwBooksTrx getSwBookTrxRecord(String booksId);

    /**
     * 查询书籍管理列表
     *
     * @param page
     * @param wrapper
     * @return
     */
    List<SwBooksTrxVo> queryBooksTrxList(Page<SwBooksTrxVo> page,
        @Param(Constants.WRAPPER) Wrapper<SwBooksTrxVo> wrapper);

    List<SwBooksTrxVo> queryBooksTrxList(Wrapper<SwBooksTrxVo> wrapper);

}
