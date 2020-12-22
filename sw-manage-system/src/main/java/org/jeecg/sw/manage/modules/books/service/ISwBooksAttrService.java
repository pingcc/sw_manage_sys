package org.jeecg.sw.manage.modules.books.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.sw.manage.modules.books.entity.SwBooksAttr;

/**
 * @Description: 书籍属性
 * @Author: Andy
 * @Date: 2020-09-27
 * @Version: V1.0
 */
public interface ISwBooksAttrService extends IService<SwBooksAttr> {
    SwBooksAttr getSwBookAttr(String booksId);
}
