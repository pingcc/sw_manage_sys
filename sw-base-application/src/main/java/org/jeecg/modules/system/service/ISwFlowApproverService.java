package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SwFlowApprover;

import java.util.List;

/**
 * @Description: 订单审核人
 * @Author: Andy
 * @Date: 2020-07-20
 * @Version: V1.0
 */
public interface ISwFlowApproverService extends IService<SwFlowApprover> {

    List<SwFlowApprover> getSwFlowApprover(SwFlowApprover snowFlowApprover);
}
