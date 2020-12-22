package org.jeecg.common.system.vo;

import lombok.Data;

/**
 * @Description:
 * @author: dw
 * @date: 2020-09-07
 */
@Data
public class SysUserDepartVO {

    /**
     * 主键id
     */
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 部门id
     */
    private String depId;

    /**
     * 原部门id
     */
    private String sourceDepId;

}
