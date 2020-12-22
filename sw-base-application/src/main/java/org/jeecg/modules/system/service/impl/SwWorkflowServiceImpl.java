package org.jeecg.modules.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.system.entity.SwWorkflow;
import org.jeecg.modules.system.entity.SysRole;
import org.jeecg.modules.system.entity.SysUserApply;
import org.jeecg.modules.system.mapper.SwWorkflowMapper;
import org.jeecg.modules.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 工作流
 * @Author: Andy
 * @Date: 2020-09-27
 * @Version: V1.0
 */
@Service
public class SwWorkflowServiceImpl extends ServiceImpl<SwWorkflowMapper, SwWorkflow> implements ISwWorkflowService {
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysUserApplyService sysUserApplyService;
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysDepartService sysDepartService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void approval(SwWorkflow swWorkflow) throws Exception {
        String remarks = swWorkflow.getRemarks();
        swWorkflow.setRemarks(remarks == null ? "通过" : remarks);
        String flowTye = swWorkflow.getFlowType();
        if (StringUtils.equalsIgnoreCase(CommonConstant.BUSINESS_REGISTER, flowTye)) {
            //学生注册，走注册成功业务
            SysUserApply sysUserApply = sysUserApplyService.getById(swWorkflow.getBusinessId());
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(sysUserApply));
            SysRole sysRole = new SysRole();
            sysRole.setRoleCode("sw_student_role");
            List<SysRole> sysUserRolesList = sysRoleService.queryRoleList(sysRole);
            if (sysUserRolesList == null) {
                throw new Exception("未找到学生职责");
            }
            String roleId = sysUserRolesList.get(0).getId();
            jsonObject.fluentPut("selectedroles", roleId).fluentPut("selecteddeparts", sysUserApply.getDeptId());
            sysUserService.addUser(jsonObject);
        } else if (StringUtils.equalsIgnoreCase(CommonConstant.BUSINESS_ARTICLE_PUB, flowTye)) {
            //文章发布业务
        } else {
            throw new Exception("无法识别的业务类型");
        }
        this.updateById(swWorkflow);
    }

    @Override
    public void approval(String status, String businessId, String flowType) throws Exception {
        LambdaQueryWrapper<SwWorkflow> lambdaQueryWrapper =
            new LambdaQueryWrapper<SwWorkflow>().eq(SwWorkflow::getFlowType, flowType)
                .eq(SwWorkflow::getBusinessId, businessId);
        SwWorkflow swWorkflow = this.getOne(lambdaQueryWrapper);
        if (swWorkflow == null) {
            throw new Exception("无法识别的业务类型");
        }
        swWorkflow.setStatus(CommonConstant.FLOW_ALLOW);
        approval(swWorkflow);
    }

    @Override
    public void start(String applyNum, String businessId, String flowType) {
        SwWorkflow swWorkflow = new SwWorkflow();
        swWorkflow.setStatus(CommonConstant.FLOW_APPROVAL);
        swWorkflow.setBusinessId(businessId);
        swWorkflow.setApplyNum(applyNum);
        swWorkflow.setFlowType(flowType);
        swWorkflow.setRemarks("提交");
        this.save(swWorkflow);
    }
}
