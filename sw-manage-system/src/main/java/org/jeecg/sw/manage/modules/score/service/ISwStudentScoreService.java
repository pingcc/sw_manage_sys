package org.jeecg.sw.manage.modules.score.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.jeecg.sw.manage.modules.score.entity.SwStudentScore;
import org.jeecg.sw.manage.modules.score.vo.SwStudentScoreEx;

import java.util.List;

/**
 * @Description: 学生分数表
 * @Author: Andy
 * @Date: 2020-10-29
 * @Version: V1.0
 */
public interface ISwStudentScoreService extends IService<SwStudentScore> {
    List<SwStudentScoreEx> selectByExportList(@Param(Constants.WRAPPER) QueryWrapper<SwStudentScore> queryWrapper);
}