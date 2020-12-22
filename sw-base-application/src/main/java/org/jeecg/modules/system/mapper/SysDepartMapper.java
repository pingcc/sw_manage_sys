package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.vo.SysUserDepartInfoVo;

import java.util.List;

/**
 * <p>
 * 部门 Mapper 接口
 * <p>
 *
 * @Author: Steve
 * @Since： 2019-01-22
 */
public interface SysDepartMapper extends BaseMapper<SysDepart> {

    /**
     * 根据用户ID查询部门集合
     */
    public List<SysDepart> queryUserDeparts(@Param("userId") String userId);

    public List<SysUserDepartInfoVo> getUserBySysDeparts(@Param("workNos") List<String> workNos);

    public List<SysDepart> queryDepartsByUsername(@Param("username") String username);

    @Select("select id from sys_depart where org_code=#{orgCode}")
    public String queryDepartIdByOrgCode(@Param("orgCode") String orgCode);

    @Select("select id,parent_id from sys_depart where id=#{departId}")
    public SysDepart getParentDepartId(@Param("departId") String departId);

    /**
     * 根据部门Id查询,当前和下级所有部门IDS
     *
     * @param departId
     * @return
     */
    List<String> getSubDepIdsByDepId(@Param("departId") String departId);

    /**
     * 根据部门Id查询,当前和上级所有部门IDS
     *
     * @param departId
     * @return
     */
    List<String> getSupDepIdsByDepId(@Param("departId") String departId);

    List<String> getSupDepIdsByDepCode(@Param("departCode") String departCode);

    /**
     * 根据部门编码获取部门下所有IDS
     *
     * @param orgCodes
     * @return
     */
    List<String> getSubDepIdsByOrgCodes(@Param("orgCodes") String[] orgCodes);

    /**
     * 根据机构代码查询机构名称
     *
     * @param orgCode
     * @return
     */
    @Select("select depart_name from sys_depart where org_code=#{orgCode}")
    public String queryDepartNameByOrgCode(@Param("orgCode") String orgCode);
}
