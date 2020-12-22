package org.jeecg.modules.demo.test.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.system.base.service.BaseSwService;
import org.jeecg.modules.demo.test.entity.BaseSwDemo;

/**
 * @Description: jeecg 测试demo
 * @Author: Andy
 * @Date: 2018-12-29
 * @Version: V1.0
 */
public interface IBaseSwDemoService extends BaseSwService<BaseSwDemo> {

    public void testTran();

    public BaseSwDemo getByIdCacheable(String id);

    /**
     * 查询列表数据 在service中获取数据权限sql信息
     *
     * @param pageSize
     * @param pageNo
     * @return
     */
    IPage<BaseSwDemo> queryListWithPermission(int pageSize, int pageNo);
}
