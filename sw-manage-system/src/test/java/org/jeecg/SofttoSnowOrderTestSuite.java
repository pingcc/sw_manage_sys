package org.jeecg;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

/**
 * 信息系统单元测试套件
 *
 * @author liucong
 * @date 2020-9-27
 */
@RunWith(JUnitPlatform.class)
@SelectPackages({"org.jeecg.snow"})
public class SofttoSnowOrderTestSuite {
}
