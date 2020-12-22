package org.jeecg.modules.oss.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.system.base.entity.BaseSwEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
@TableName("oss_file")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OSSFile extends BaseSwEntity {

    private static final long serialVersionUID = 1L;

    @Excel(name = "文件名称")
    private String fileName;

    @Excel(name = "文件地址")
    private String url;

}
