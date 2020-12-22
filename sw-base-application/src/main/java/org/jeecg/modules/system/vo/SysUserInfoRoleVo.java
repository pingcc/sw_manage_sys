package org.jeecg.modules.system.vo;

import lombok.Data;

/**
 * @Author Andy
 * @Date 2020/8/3
 */
@Data
public class SysUserInfoRoleVo {
    /**
     * 角色id
     */
    private String roleId;
    private String delFlag;
    private String roleCode;
    private String username;
    private String realname;
    private String id;
    private String departId;
    private String departName;
    private String orgCode;
    private String bpOrgCode;
    private String bpOrgName;
}
