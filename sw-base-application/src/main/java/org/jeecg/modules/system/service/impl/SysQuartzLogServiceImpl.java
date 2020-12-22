package org.jeecg.modules.system.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.system.entity.SysQuartzLog;
import org.jeecg.modules.system.mapper.SysQuartzLogMapper;
import org.jeecg.modules.system.service.ISysQuartzLogService;
import org.springframework.stereotype.Service;

/**
 * @Description: 系统调度日志表
 * @Author: Andy
 * @Date: 2020-08-24
 * @Version: V1.0
 */
@Service
@DS("master")
public class SysQuartzLogServiceImpl extends ServiceImpl<SysQuartzLogMapper, SysQuartzLog>
    implements ISysQuartzLogService {

}
