package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysWorkNoRule;

/**
 * @Description: 学生工号规则表
 * @Author: Andy
 * @Date: 2020-10-10
 * @Version: V1.0
 */
public interface ISysWorkNoRuleService extends IService<SysWorkNoRule> {
    /**
     * @param sysWorkNoRule
     * @return
     */
    SysWorkNoRule getSysWorkNoRule(SysWorkNoRule sysWorkNoRule);
}
