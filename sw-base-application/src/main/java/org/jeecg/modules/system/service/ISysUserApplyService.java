package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysUserApply;

/**
 * @Description: 学生申请表
 * @Author: Andy
 * @Date: 2020-11-09
 * @Version: V1.0
 */
public interface ISysUserApplyService extends IService<SysUserApply> {
    public static final String UK_USERNAME = "sys_user_apply.uk_username";
    public static final String UK_PHONE = "sys_user_apply.uk_phone";
    public static final String UK_EMAIL = "sys_user_apply.uk_email";

    void saveUserApply(SysUserApply sysUserApply) throws Exception;
}
