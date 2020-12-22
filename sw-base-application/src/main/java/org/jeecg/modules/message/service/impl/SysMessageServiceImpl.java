package org.jeecg.modules.message.service.impl;

import org.jeecg.common.system.base.service.impl.BaseSwServiceImpl;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.mapper.SysMessageMapper;
import org.jeecg.modules.message.service.ISysMessageService;
import org.springframework.stereotype.Service;

/**
 * @Description: 消息
 * @Author: Andy
 * @Date: 2019-04-09
 * @Version: V1.0
 */
@Service
public class SysMessageServiceImpl extends BaseSwServiceImpl<SysMessageMapper, SysMessage>
    implements ISysMessageService {

}
