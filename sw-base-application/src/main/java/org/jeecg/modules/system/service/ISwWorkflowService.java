package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SwWorkflow;

/**
 * @Description: 工作流
 * @Author: Andy
 * @Date: 2020-09-27
 * @Version: V1.0
 */
public interface ISwWorkflowService extends IService<SwWorkflow> {
    /**
     * 审批
     *
     * @param swWorkflow
     */
    void approval(SwWorkflow swWorkflow) throws Exception;

    /**
     * 提交
     *
     * @param applyNum
     * @param businessId
     * @param flowType
     */
    void start(String applyNum, String businessId, String flowType);

    /**
     * @param status
     * @param businessId
     * @param flowType
     * @throws Exception
     */
    void approval(String status, String businessId, String flowType) throws Exception;
}
