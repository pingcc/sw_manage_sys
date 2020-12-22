package org.jeecg.sw.manage.modules.score.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.sw.manage.modules.score.entity.SwStudentScore;
import org.jeecg.sw.manage.modules.score.mapper.SwStudentScoreMapper;
import org.jeecg.sw.manage.modules.score.service.ISwStudentScoreService;
import org.jeecg.sw.manage.modules.score.vo.SwStudentScoreEx;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 学生分数表
 * @Author: Andy
 * @Date: 2020-10-29
 * @Version: V1.0
 */
@Service
@DS("sw-manage-datasource")
public class SwStudentScoreServiceImpl extends ServiceImpl<SwStudentScoreMapper, SwStudentScore>
    implements ISwStudentScoreService {

    @Override
    public List<SwStudentScoreEx> selectByExportList(QueryWrapper<SwStudentScore> queryWrapper) {
        return this.baseMapper.selectByExportList(queryWrapper);
    }
}
