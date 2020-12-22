package org.jeecg.modules.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 审核角色
 * @Author: softto
 * @Date: 2020-07-20
 * @Version: V1.0
 */
@Data
public class SwFlowApproverPage {

    private String id;

    private String levelName;

    private String roleCode;

    private String roleName;

    @Dict(dicCode = "yn")
    private Integer isStart;

    @Dict(dicCode = "flow_type")
    private String flowType;
    private String nextLevel;
    private String level;

    private Integer isDeleted;

    private String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
