package org.jeecg.sw.manage.modules.books.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.sw.manage.modules.books.entity.SwBooksTrx;
import org.jeecg.sw.manage.modules.books.mapper.SwBooksTrxMapper;
import org.jeecg.sw.manage.modules.books.service.ISwBooksTrxService;
import org.jeecg.sw.manage.modules.books.vo.SwBooksTrxVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 书籍交易
 * @Author: Andy
 * @Date: 2020-09-27
 * @Version: V1.0
 */
@Service
@DS("sw-manage-datasource")
public class SwBooksTrxServiceImpl extends ServiceImpl<SwBooksTrxMapper, SwBooksTrx> implements ISwBooksTrxService {

    @Override
    public SwBooksTrx getSwBookTrxRecord(String booksId) {
        LambdaQueryWrapper<SwBooksTrx> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SwBooksTrx::getBooksId, booksId).isNull(SwBooksTrx::getReturnTime);
        return this.getOne(wrapper);
    }

    @Override
    public List<SwBooksTrxVo> queryBooksTrxList(Page<SwBooksTrxVo> page, Wrapper<SwBooksTrxVo> wrapper) {
        return this.baseMapper.queryBooksTrxList(page, wrapper);
    }

    @Override
    public List<SwBooksTrxVo> queryBooksTrxList(Wrapper<SwBooksTrxVo> wrapper) {
        return this.baseMapper.queryBooksTrxList(wrapper);
    }
}
