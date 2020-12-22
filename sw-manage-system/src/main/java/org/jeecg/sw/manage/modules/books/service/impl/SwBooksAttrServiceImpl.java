package org.jeecg.sw.manage.modules.books.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.sw.manage.modules.books.entity.SwBooksAttr;
import org.jeecg.sw.manage.modules.books.mapper.SwBooksAttrMapper;
import org.jeecg.sw.manage.modules.books.service.ISwBooksAttrService;
import org.springframework.stereotype.Service;

/**
 * @Description: 书籍属性
 * @Author: Andy
 * @Date: 2020-09-27
 * @Version: V1.0
 */
@Service
@DS("sw-manage-datasource")
public class SwBooksAttrServiceImpl extends ServiceImpl<SwBooksAttrMapper, SwBooksAttr> implements ISwBooksAttrService {

    @Override
    public SwBooksAttr getSwBookAttr(String booksId) {
        SwBooksAttr params = new SwBooksAttr();
        params.setBooksId(booksId);
        QueryWrapper<SwBooksAttr> queryWrapper = QueryGenerator.initQueryWrapper(params, null);
        return this.getOne(queryWrapper);
    }
}
