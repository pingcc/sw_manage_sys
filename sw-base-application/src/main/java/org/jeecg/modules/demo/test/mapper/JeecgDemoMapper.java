package org.jeecg.modules.demo.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.demo.test.entity.BaseSwDemo;

import java.util.List;

/**
 * @Description: jeecg 测试demo
 * @Author: Andy
 * @Date: 2018-12-29
 * @Version: V1.0
 */
public interface JeecgDemoMapper extends BaseMapper<BaseSwDemo> {

    public List<BaseSwDemo> getDemoByName(@Param("name") String name);

    /**
     * 查询列表数据 直接传数据权限的sql进行数据过滤
     *
     * @param page
     * @param permissionSql
     * @return
     */
    public IPage<BaseSwDemo> queryListWithPermission(Page<BaseSwDemo> page,
        @Param("permissionSql") String permissionSql);

}
