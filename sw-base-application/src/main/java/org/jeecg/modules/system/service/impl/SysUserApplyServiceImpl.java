package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysCodeGeneratorService;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserApply;
import org.jeecg.modules.system.mapper.SysUserApplyMapper;
import org.jeecg.modules.system.service.ISysUserApplyService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 学生申请表
 * @Author: Andy
 * @Date: 2020-11-09
 * @Version: V1.0
 */
@Service
public class SysUserApplyServiceImpl extends ServiceImpl<SysUserApplyMapper, SysUserApply>
    implements ISysUserApplyService {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysCodeGeneratorService sysCodeGeneratorService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveUserApply(SysUserApply sysUserApply) throws Exception {
        SysUser sysUser = new SysUser();
        sysUser.setUsername(sysUserApply.getUsername());
        QueryWrapper<SysUser> queryWrapper = QueryGenerator.initQueryWrapper(sysUser, null);
        SysUser oneUser = sysUserService.getOne(queryWrapper);
        if (oneUser != null) {
            throw new Exception("用户名已经存在!");
        }
        String code = sysCodeGeneratorService.generateCodeFromLookup("registerNoRule");
        sysUserApply.setApplyNum(code);
        sysUserApply.setStatus(CommonConstant.FLOW_DRAFT);
        save(sysUserApply);
    }

}