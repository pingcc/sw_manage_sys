package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.system.entity.SysWorkNoRule;
import org.jeecg.modules.system.mapper.SysWorkNoRuleMapper;
import org.jeecg.modules.system.service.ISysWorkNoRuleService;
import org.springframework.stereotype.Service;

/**
 * @Description: 学生工号规则表
 * @Author: Andy
 * @Date: 2020-10-10
 * @Version: V1.0
 */
@Service
public class SysWorkNoRuleServiceImpl extends ServiceImpl<SysWorkNoRuleMapper, SysWorkNoRule>
    implements ISysWorkNoRuleService {

    @Override
    public SysWorkNoRule getSysWorkNoRule(SysWorkNoRule sysWorkNoRule) {
        QueryWrapper<SysWorkNoRule> queryWrapper = QueryGenerator.initQueryWrapper(sysWorkNoRule, null);
        return this.getOne(queryWrapper);

    }
}
