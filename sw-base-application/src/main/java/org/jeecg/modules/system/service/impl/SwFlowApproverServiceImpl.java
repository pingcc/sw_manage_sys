package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.system.entity.SwFlowApprover;
import org.jeecg.modules.system.mapper.SwFlowApproverMapper;
import org.jeecg.modules.system.service.ISwFlowApproverService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @Description: 订单审核人
 * @Author: Andy
 * @Date: 2020-07-20
 * @Version: V1.0
 */
@Service
public class SwFlowApproverServiceImpl extends ServiceImpl<SwFlowApproverMapper, SwFlowApprover>
    implements ISwFlowApproverService {

    @Override
    public List<SwFlowApprover> getSwFlowApprover(SwFlowApprover snowFlowApprover) {
        snowFlowApprover.setIsDeleted(0);
        QueryWrapper<SwFlowApprover> queryWrapper = QueryGenerator.initQueryWrapper(snowFlowApprover, new HashMap<>());
        queryWrapper.orderByAsc("create_time");
        return this.list(queryWrapper);
    }
}
