package org.jeecg.common.system.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
@Data
public class SysDepartGroupUserModel implements Serializable {

    private static final long serialVersionUID = 1135244826522091457L;
    /**
     * 登录账号
     */
    private String username;
    private String realname;
    private String orgCode;
    private String orgName;
    private String userId;
    private String departId;
    private String departParentId;

}
