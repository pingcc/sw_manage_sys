package org.jeecg.modules.message.service;

import org.jeecg.common.system.base.service.BaseSwService;
import org.jeecg.modules.message.entity.SysMessageTemplate;

import java.util.List;

/**
 * @Description: 消息模板
 * @Author: Andy
 * @Date: 2019-04-09
 * @Version: V1.0
 */
public interface ISysMessageTemplateService extends BaseSwService<SysMessageTemplate> {
    List<SysMessageTemplate> selectByCode(String code);
}
