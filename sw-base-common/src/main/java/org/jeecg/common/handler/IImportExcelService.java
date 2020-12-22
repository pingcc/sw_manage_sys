package org.jeecg.common.handler;

import java.util.List;

/**
 * @Author Andy
 * @Date 2020/7/22
 * 导入表格业务扩展类
 */
public interface IImportExcelService<T> {
    void importBefore(List<T> list); //导入成功之前

    void importAfter(List<T> list); //导入成功之后
}
