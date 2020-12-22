package org.jeecg.modules.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.DataBaseConstant;
import org.jeecg.common.constant.WebsocketConst;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.*;
import org.jeecg.common.util.*;
import org.jeecg.common.util.oss.OssBootUtil;
import org.jeecg.modules.message.entity.SysMessageTemplate;
import org.jeecg.modules.message.service.ISysMessageTemplateService;
import org.jeecg.modules.message.websocket.WebSocket;
import org.jeecg.modules.quartz.entity.QuartzJob;
import org.jeecg.modules.quartz.service.IQuartzJobService;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.mapper.*;
import org.jeecg.modules.system.service.*;
import org.jeecg.modules.system.vo.SysDepartGroupUserVo;
import org.jeecg.modules.system.vo.SysUserDepartInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * @Description: 底层共通业务API，提供其他独立模块调用
 * @Author: scott
 * @Date:2019-4-20
 * @Version:V1.0
 */
@Slf4j
@Service
@DS("master")
public class SysBaseApiImpl implements ISysBaseAPI {
    /**
     * 当前系统数据库类型
     */
    private static String DB_TYPE = "";
    @Autowired
    private ISysMessageTemplateService sysMessageTemplateService;
    @Resource
    private SysLogMapper sysLogMapper;
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private ISysDictService sysDictService;
    @Resource
    private SysAnnouncementMapper sysAnnouncementMapper;
    @Resource
    private SysAnnouncementSendMapper sysAnnouncementSendMapper;
    @Resource
    private WebSocket webSocket;
    @Resource
    private SysRoleMapper roleMapper;
    @Resource
    private SysDepartMapper departMapper;
    @Resource
    private SysCategoryMapper categoryMapper;

    @Autowired
    private ISysDataSourceService dataSourceService;

    @Autowired
    private IQuartzJobService quartzJobService;

    @Autowired
    private ISysQuartzLogService sysQuartzLogService;

    @Override
    public void addLog(String LogContent, Integer logType, Integer operatetype) {
        SysLog sysLog = new SysLog();
        //注解上的描述,操作日志内容
        sysLog.setLogContent(LogContent);
        sysLog.setLogType(logType);
        sysLog.setOperateType(operatetype);

        //请求的方法名
        //请求的参数

        try {
            //获取request
            HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
            //设置IP地址
            sysLog.setIp(IPUtils.getIpAddr(request));
        } catch (Exception e) {
            sysLog.setIp("127.0.0.1");
        }

        //获取登录用户信息
        LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        if (sysUser != null) {
            sysLog.setUserid(sysUser.getUsername());
            sysLog.setUsername(sysUser.getRealname());

        }
        sysLog.setCreateTime(new Date());
        //保存系统日志
        sysLogMapper.insert(sysLog);
    }

    @Override
    @Cacheable(cacheNames = CacheConstant.SYS_USERS_CACHE, key = "#username")
    public LoginUser getUserByName(String username) {
        if (oConvertUtils.isEmpty(username)) {
            return null;
        }
        LoginUser loginUser = new LoginUser();
        SysUser sysUser = userMapper.getUserByName(username);
        if (sysUser == null) {
            return null;
        }
        BeanUtils.copyProperties(sysUser, loginUser);

        // Sw liuc 2020-07-22 start
        // 增加用户所属角色列表
        StringBuilder sb = new StringBuilder();
        List<String> roleList = this.getRolesByUsername(username);
        roleList.forEach((role) -> {
            sb.append(sb.length() > 0 ? "," : "");
            sb.append(role);
        });

        loginUser.setRoles(sb.toString());
        // Sw liuc 2020-07-22 end

        // Sw liuc 2020-07-24 start
        // 设置当前登录用户所属的业务平台
        SysDepartModel sysDepartModel = new SysDepartModel();
        sysDepartModel.setOrgCode(loginUser.getOrgCode());
        sysDepartModel.setDepType("2");     // 2-部门类型是平台
        List<SysDepartModel> supDepart = this.getSupDepart(sysDepartModel);

        if (supDepart != null && !supDepart.isEmpty()) {
            loginUser.setBpOrgCode(supDepart.get(0).getOrgCode());
        }
        // Sw liuc 2020-07-24 start

        return loginUser;
    }

    @Override
    public LoginUser getUserById(String id) {
        if (oConvertUtils.isEmpty(id)) {
            return null;
        }
        LoginUser loginUser = new LoginUser();
        SysUser sysUser = userMapper.selectById(id);
        if (sysUser == null) {
            return null;
        }
        BeanUtils.copyProperties(sysUser, loginUser);
        return loginUser;
    }

    @Override
    public List<LoginUser> getUserList(LoginUser loginUser) {
        SysUser user = new SysUser();
        BeanUtils.copyProperties(loginUser, user);
        Result<IPage<SysUser>> result = new Result<IPage<SysUser>>();
        QueryWrapper<SysUser> queryWrapper = QueryGenerator.initQueryWrapper(user, new HashMap<>());
        //TODO 外部模拟登陆临时账号，列表不显示
        queryWrapper.ne("username", "_reserve_user_external");
        List<SysUser> list = sysUserService.list(queryWrapper);
        List<LoginUser> loginUsers = new LinkedList<>();
        for (SysUser item : list) {
            LoginUser model = new LoginUser();
            BeanUtils.copyProperties(item, model);
            loginUsers.add(model);
        }
        return loginUsers;
    }

    @Override
    public List<SysUserDepartInfoModel> getUserBySysDeparts(List<String> workNos) {
        List<SysUserDepartInfoVo> sysUserDepartInfoVoList = this.sysDepartService.getUserBySysDeparts(workNos);
        List<SysUserDepartInfoModel> list = new ArrayList<>();
        for (SysUserDepartInfoVo item : sysUserDepartInfoVoList) {
            SysUserDepartInfoModel sysUserDepartInfoModel = new SysUserDepartInfoModel();
            BeanUtils.copyProperties(item, sysUserDepartInfoModel);
            list.add(sysUserDepartInfoModel);
        }
        return list;
    }

    @Override
    public List<String> getRolesByUsername(String username) {
        return sysUserRoleMapper.getRoleByUserName(username);
    }

    //查询该用户是否包含某些特定的职责
    @Override
    public boolean isUserContainRoles(String roleCodes) {
        if (roleCodes != null) {
            String[] roleList = roleCodes.split(",");
            for (String roleCode : roleList) {
                String roleCodeStr = JwtUtil.getUserSystemData(DataBaseConstant.SYS_ROLE_LIST, null);
                if (roleCodeStr == null) {
                    return false;
                }
                String[] roleCodeList = roleCodeStr.split(",");
                for (String role : roleCodeList) {
                    if (StringUtils.equalsIgnoreCase(role, roleCode)) {
                        return true;
                    }
                }
            }
        }

        return false;

    }

    //查询该用户的所有职责是否都属于某些特定的职责 用来区分职责权限
    @Override
    public boolean isUserContainAllRoles(String roleCodes) {
        String roleCodeStr = JwtUtil.getUserSystemData(DataBaseConstant.SYS_ROLE_LIST, null);
        if (roleCodeStr == null) {
            return false;
        }
        String[] roleList = roleCodeStr.split(",");
        for (String roleCode : roleList) {
            if (!roleCodes.contains(roleCode)) {
                return false;
            }
        }
        return true;

    }

    @Override
    public List<String> getDepartIdsByUsername(String username) {
        List<SysDepart> list = sysDepartService.queryDepartsByUsername(username);
        List<String> result = new ArrayList<>(list.size());
        for (SysDepart depart : list) {
            result.add(depart.getId());
        }
        return result;
    }

    @Override
    public List<String> getDepartNamesByUsername(String username) {
        List<SysDepart> list = sysDepartService.queryDepartsByUsername(username);
        List<String> result = new ArrayList<>(list.size());
        for (SysDepart depart : list) {
            result.add(depart.getDepartName());
        }
        return result;
    }

    @Override
    public String getDatabaseType() throws SQLException {
        if (oConvertUtils.isNotEmpty(DB_TYPE)) {
            return DB_TYPE;
        }
        DataSource dataSource = SpringContextUtils.getApplicationContext().getBean(DataSource.class);
        return getDatabaseTypeByDataSource(dataSource);
    }

    @Override
    @Cacheable(value = CacheConstant.SYS_DICT_CACHE, key = "#code")
    public List<DictModel> queryDictItemsByCode(String code) {
        return sysDictService.queryDictItemsByCode(code);
    }

    @Override
    public String queryDictItemsByCode(String code, String key) {
        List<DictModel> dictModels = sysDictService.queryDictItemsByCode(code);
        for (DictModel dictModel : dictModels) {
            if (dictModel.getValue().equals(key)) {
                return dictModel.getText();
            }
        }
        return null;
    }

    @Override
    public String queryDictItemsByValue(String code, String text) {
        List<DictModel> dictModels = sysDictService.queryDictItemsByCode(code);
        for (DictModel dictModel : dictModels) {
            if (text.equals(dictModel.getText())) {
                return dictModel.getValue();
            }
        }
        return null;
    }

    @Override
    public List<DictModel> queryTableDictItemsByCode(String table, String text, String code) {
        return sysDictService.queryTableDictItemsByCode(table, text, code);
    }

    @Override
    public List<DictModel> queryAllDepartBackDictModel() {
        return sysDictService.queryAllDepartBackDictModel();
    }

    @Override
    public List<JSONObject> queryAllDepart(Wrapper wrapper) {
        //noinspection unchecked
        return JSON.parseArray(JSON.toJSONString(sysDepartService.list(wrapper))).toJavaList(JSONObject.class);
    }

    @Override
    public void sendSysAnnouncement(String fromUser, String toUser, String title, String msgContent) {
        this.sendSysAnnouncement(fromUser, toUser, title, msgContent, CommonConstant.MSG_CATEGORY_2);
    }

    @Override
    public void sendSysAnnouncement(String fromUser, String toUser, String title, String msgContent,
        String setMsgCategory) {
        SysAnnouncement announcement = new SysAnnouncement();
        announcement.setTitile(title);
        announcement.setMsgContent(msgContent);
        announcement.setSender(fromUser);
        announcement.setPriority(CommonConstant.PRIORITY_M);
        announcement.setMsgType(CommonConstant.MSG_TYPE_UESR);
        announcement.setSendStatus(CommonConstant.HAS_SEND);
        announcement.setSendTime(new Date());
        announcement.setMsgCategory(setMsgCategory);
        announcement.setDelFlag(String.valueOf(CommonConstant.DEL_FLAG_0));
        sysAnnouncementMapper.insert(announcement);
        // 2.插入用户通告阅读标记表记录
        String userId = toUser;
        String[] userIds = userId.split(",");
        String anntId = announcement.getId();
        for (int i = 0; i < userIds.length; i++) {
            if (oConvertUtils.isNotEmpty(userIds[i])) {
                SysUser sysUser = userMapper.getUserByName(userIds[i]);
                if (sysUser == null) {
                    continue;
                }
                SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                announcementSend.setAnntId(anntId);
                announcementSend.setUserId(sysUser.getId());
                announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
                sysAnnouncementSendMapper.insert(announcementSend);
                JSONObject obj = new JSONObject();
                obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_USER);
                obj.put(WebsocketConst.MSG_USER_ID, sysUser.getId());
                obj.put(WebsocketConst.MSG_ID, announcement.getId());
                obj.put(WebsocketConst.MSG_TXT, announcement.getTitile());
                webSocket.sendOneMessage(sysUser.getId(), obj.toJSONString());
            }
        }

    }

    @Override
    public void sendSysAnnouncement(String fromUser, String toUser, String title, String msgContent,
        String setMsgCategory, String busType, String busId) {
        SysAnnouncement announcement = new SysAnnouncement();
        announcement.setTitile(title);
        announcement.setMsgContent(msgContent);
        announcement.setSender(fromUser);
        announcement.setPriority(CommonConstant.PRIORITY_M);
        announcement.setMsgType(CommonConstant.MSG_TYPE_UESR);
        announcement.setSendStatus(CommonConstant.HAS_SEND);
        announcement.setSendTime(new Date());
        announcement.setMsgCategory(setMsgCategory);
        announcement.setDelFlag(String.valueOf(CommonConstant.DEL_FLAG_0));
        announcement.setBusId(busId);
        announcement.setBusType(busType);
        announcement.setOpenType(SysAnnmentTypeEnum.getByType(busType).getOpenType());
        announcement.setOpenPage(SysAnnmentTypeEnum.getByType(busType).getOpenPage());
        sysAnnouncementMapper.insert(announcement);
        // 2.插入用户通告阅读标记表记录
        String userId = toUser;
        String[] userIds = userId.split(",");
        String anntId = announcement.getId();
        for (int i = 0; i < userIds.length; i++) {
            if (oConvertUtils.isNotEmpty(userIds[i])) {
                SysUser sysUser = userMapper.getUserByName(userIds[i]);
                if (sysUser == null) {
                    continue;
                }
                SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                announcementSend.setAnntId(anntId);
                announcementSend.setUserId(sysUser.getId());
                announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
                sysAnnouncementSendMapper.insert(announcementSend);
                JSONObject obj = new JSONObject();
                obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_USER);
                obj.put(WebsocketConst.MSG_USER_ID, sysUser.getId());
                obj.put(WebsocketConst.MSG_ID, announcement.getId());
                obj.put(WebsocketConst.MSG_TXT, announcement.getTitile());
                webSocket.sendOneMessage(sysUser.getId(), obj.toJSONString());
            }
        }
    }

    @Override
    public void updateSysAnnounReadFlag(String busType, String busId) {
        SysAnnouncement announcement = sysAnnouncementMapper
            .selectOne(new QueryWrapper<SysAnnouncement>().eq("bus_type", busType).eq("bus_id", busId));
        if (announcement != null) {
            LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
            String userId = sysUser.getId();
            LambdaUpdateWrapper<SysAnnouncementSend> updateWrapper = new UpdateWrapper().lambda();
            updateWrapper.set(SysAnnouncementSend::getReadFlag, CommonConstant.HAS_READ_FLAG);
            updateWrapper.set(SysAnnouncementSend::getReadTime, new Date());
            updateWrapper.last("where annt_id ='" + announcement.getId() + "' and user_id ='" + userId + "'");
            SysAnnouncementSend announcementSend = new SysAnnouncementSend();
            sysAnnouncementSendMapper.update(announcementSend, updateWrapper);
        }
    }

    @Override
    public String parseTemplateByCode(String templateCode, Map<String, String> map) {
        List<SysMessageTemplate> sysSmsTemplates = sysMessageTemplateService.selectByCode(templateCode);
        if (sysSmsTemplates == null || sysSmsTemplates.size() == 0) {
            throw new JeecgBootException("消息模板不存在，模板编码：" + templateCode);
        }
        SysMessageTemplate sysSmsTemplate = sysSmsTemplates.get(0);
        //模板内容
        String content = sysSmsTemplate.getTemplateContent();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String str = "${" + entry.getKey() + "}";
                content = content.replace(str, entry.getValue());
            }
        }
        return content;
    }

    @Override
    public void sendSysAnnouncement(String fromUser, String toUser, String title, Map<String, String> map,
        String templateCode) {
        List<SysMessageTemplate> sysSmsTemplates = sysMessageTemplateService.selectByCode(templateCode);
        if (sysSmsTemplates == null || sysSmsTemplates.size() == 0) {
            throw new JeecgBootException("消息模板不存在，模板编码：" + templateCode);
        }
        SysMessageTemplate sysSmsTemplate = sysSmsTemplates.get(0);
        //模板标题
        title = title == null ? sysSmsTemplate.getTemplateName() : title;
        //模板内容
        String content = sysSmsTemplate.getTemplateContent();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String str = "${" + entry.getKey() + "}";
                title = title.replace(str, entry.getValue());
                content = content.replace(str, entry.getValue());
            }
        }

        SysAnnouncement announcement = new SysAnnouncement();
        announcement.setTitile(title);
        announcement.setMsgContent(content);
        announcement.setSender(fromUser);
        announcement.setPriority(CommonConstant.PRIORITY_M);
        announcement.setMsgType(CommonConstant.MSG_TYPE_UESR);
        announcement.setSendStatus(CommonConstant.HAS_SEND);
        announcement.setSendTime(new Date());
        announcement.setMsgCategory(CommonConstant.MSG_CATEGORY_2);
        announcement.setDelFlag(String.valueOf(CommonConstant.DEL_FLAG_0));
        sysAnnouncementMapper.insert(announcement);
        // 2.插入用户通告阅读标记表记录
        String userId = toUser;
        String[] userIds = userId.split(",");
        String anntId = announcement.getId();
        for (int i = 0; i < userIds.length; i++) {
            if (oConvertUtils.isNotEmpty(userIds[i])) {
                SysUser sysUser = userMapper.getUserByName(userIds[i]);
                if (sysUser == null) {
                    continue;
                }
                SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                announcementSend.setAnntId(anntId);
                announcementSend.setUserId(sysUser.getId());
                announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
                sysAnnouncementSendMapper.insert(announcementSend);
                JSONObject obj = new JSONObject();
                obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_USER);
                obj.put(WebsocketConst.MSG_USER_ID, sysUser.getId());
                obj.put(WebsocketConst.MSG_ID, announcement.getId());
                obj.put(WebsocketConst.MSG_TXT, announcement.getTitile());
                webSocket.sendOneMessage(sysUser.getId(), obj.toJSONString());
            }
        }
    }

    @Override
    public void sendSysAnnouncement(String fromUser, String toUser, String title, Map<String, String> map,
        String templateCode, String busType, String busId) {
        List<SysMessageTemplate> sysSmsTemplates = sysMessageTemplateService.selectByCode(templateCode);
        if (sysSmsTemplates == null || sysSmsTemplates.size() == 0) {
            throw new JeecgBootException("消息模板不存在，模板编码：" + templateCode);
        }
        SysMessageTemplate sysSmsTemplate = sysSmsTemplates.get(0);
        //模板标题
        title = title == null ? sysSmsTemplate.getTemplateName() : title;
        //模板内容
        String content = sysSmsTemplate.getTemplateContent();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String str = "${" + entry.getKey() + "}";
                title = title.replace(str, entry.getValue());
                content = content.replace(str, entry.getValue());
            }
        }
        SysAnnouncement announcement = new SysAnnouncement();
        announcement.setTitile(title);
        announcement.setMsgContent(content);
        announcement.setSender(fromUser);
        announcement.setPriority(CommonConstant.PRIORITY_M);
        announcement.setMsgType(CommonConstant.MSG_TYPE_UESR);
        announcement.setSendStatus(CommonConstant.HAS_SEND);
        announcement.setSendTime(new Date());
        announcement.setMsgCategory(CommonConstant.MSG_CATEGORY_2);
        announcement.setDelFlag(String.valueOf(CommonConstant.DEL_FLAG_0));
        announcement.setBusId(busId);
        announcement.setBusType(busType);
        announcement.setOpenType(SysAnnmentTypeEnum.getByType(busType).getOpenType());
        announcement.setOpenPage(SysAnnmentTypeEnum.getByType(busType).getOpenPage());
        sysAnnouncementMapper.insert(announcement);
        // 2.插入用户通告阅读标记表记录
        String userId = toUser;
        String[] userIds = userId.split(",");
        String anntId = announcement.getId();
        for (int i = 0; i < userIds.length; i++) {
            if (oConvertUtils.isNotEmpty(userIds[i])) {
                SysUser sysUser = userMapper.getUserByName(userIds[i]);
                if (sysUser == null) {
                    continue;
                }
                SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                announcementSend.setAnntId(anntId);
                announcementSend.setUserId(sysUser.getId());
                announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
                sysAnnouncementSendMapper.insert(announcementSend);
                JSONObject obj = new JSONObject();
                obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_USER);
                obj.put(WebsocketConst.MSG_USER_ID, sysUser.getId());
                obj.put(WebsocketConst.MSG_ID, announcement.getId());
                obj.put(WebsocketConst.MSG_TXT, announcement.getTitile());
                webSocket.sendOneMessage(sysUser.getId(), obj.toJSONString());
            }
        }
    }

    /**
     * 获取数据库类型
     *
     * @param dataSource
     * @return
     * @throws SQLException
     */
    private String getDatabaseTypeByDataSource(DataSource dataSource) throws SQLException {
        if ("".equals(DB_TYPE)) {
            Connection connection = dataSource.getConnection();
            try {
                DatabaseMetaData md = connection.getMetaData();
                String dbType = md.getDatabaseProductName().toLowerCase();
                if (dbType.indexOf("mysql") >= 0) {
                    DB_TYPE = DataBaseConstant.DB_TYPE_MYSQL;
                } else if (dbType.indexOf("oracle") >= 0) {
                    DB_TYPE = DataBaseConstant.DB_TYPE_ORACLE;
                } else if (dbType.indexOf("sqlserver") >= 0 || dbType.indexOf("sql server") >= 0) {
                    DB_TYPE = DataBaseConstant.DB_TYPE_SQLSERVER;
                } else if (dbType.indexOf("postgresql") >= 0) {
                    DB_TYPE = DataBaseConstant.DB_TYPE_POSTGRESQL;
                } else {
                    throw new JeecgBootException("数据库类型:[" + dbType + "]不识别!");
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                connection.close();
            }
        }
        return DB_TYPE;

    }

    @Override
    public List<DictModel> queryAllDict() {
        // 查询并排序
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<SysDict>();
        queryWrapper.orderByAsc("create_time");
        List<SysDict> dicts = sysDictService.list(queryWrapper);
        // 封装成 model
        List<DictModel> list = new ArrayList<DictModel>();
        for (SysDict dict : dicts) {
            list.add(new DictModel(dict.getDictCode(), dict.getDictName()));
        }

        return list;
    }

    @Override
    public List<SysCategoryModel> queryAllDSysCategory() {
        List<SysCategory> ls = categoryMapper.selectList(null);
        List<SysCategoryModel> res = oConvertUtils.entityListToModelList(ls, SysCategoryModel.class);
        return res;
    }

    @Override
    public List<DictModel> queryFilterTableDictInfo(String table, String text, String code, String filterSql) {
        return sysDictService.queryTableDictItemsByCodeAndFilter(table, text, code, filterSql);
    }

    @Override
    public List<String> queryTableDictByKeys(String table, String text, String code, String[] keyArray) {
        return sysDictService.queryTableDictByKeys(table, text, code, Joiner.on(",").join(keyArray));
    }

    @Override
    public List<ComboModel> queryAllUser() {
        List<ComboModel> list = new ArrayList<ComboModel>();
        List<SysUser> userList = userMapper.selectList(new QueryWrapper<SysUser>().eq("status", 1).eq("del_flag", 0));
        for (SysUser user : userList) {
            ComboModel model = new ComboModel();
            model.setTitle(user.getRealname());
            model.setId(user.getId());
            model.setUsername(user.getUsername());
            list.add(model);
        }
        return list;
    }

    @Override
    public JSONObject queryAllUser(String[] userIds, int pageNo, int pageSize) {
        JSONObject json = new JSONObject();
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>().eq("status", 1).eq("del_flag", 0);
        List<ComboModel> list = new ArrayList<ComboModel>();
        Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
        IPage<SysUser> pageList = userMapper.selectPage(page, queryWrapper);
        for (SysUser user : pageList.getRecords()) {
            ComboModel model = new ComboModel();
            model.setUsername(user.getUsername());
            model.setTitle(user.getRealname());
            model.setId(user.getId());
            model.setEmail(user.getEmail());
            if (oConvertUtils.isNotEmpty(userIds)) {
                for (int i = 0; i < userIds.length; i++) {
                    if (userIds[i].equals(user.getId())) {
                        model.setChecked(true);
                    }
                }
            }
            list.add(model);
        }
        json.put("list", list);
        json.put("total", pageList.getTotal());
        return json;
    }

    @Override
    public List<JSONObject> queryAllUser(Wrapper wrapper) {
        //noinspection unchecked
        return JSON.parseArray(JSON.toJSONString(userMapper.selectList(wrapper))).toJavaList(JSONObject.class);
    }

    @Override
    public List<ComboModel> queryAllRole() {
        List<ComboModel> list = new ArrayList<ComboModel>();
        List<SysRole> roleList = roleMapper.selectList(new QueryWrapper<SysRole>());
        for (SysRole role : roleList) {
            ComboModel model = new ComboModel();
            model.setTitle(role.getRoleName());
            model.setId(role.getId());
            list.add(model);
        }
        return list;
    }

    @Override
    public List<ComboModel> queryAllRole(String[] roleIds) {
        List<ComboModel> list = new ArrayList<ComboModel>();
        List<SysRole> roleList = roleMapper.selectList(new QueryWrapper<SysRole>());
        for (SysRole role : roleList) {
            ComboModel model = new ComboModel();
            model.setTitle(role.getRoleName());
            model.setId(role.getId());
            model.setRoleCode(role.getRoleCode());
            if (oConvertUtils.isNotEmpty(roleIds)) {
                for (int i = 0; i < roleIds.length; i++) {
                    if (roleIds[i].equals(role.getId())) {
                        model.setChecked(true);
                    }
                }
            }
            list.add(model);
        }
        return list;
    }

    @Override
    public List<String> getRoleIdsByUsername(String username) {
        return sysUserRoleMapper.getRoleIdByUserName(username);
    }

    @Override
    public String getDepartIdsByOrgCode(String orgCode) {
        return departMapper.queryDepartIdByOrgCode(orgCode);
    }

    @Override
    public DictModel getParentDepartId(String departId) {
        SysDepart depart = departMapper.getParentDepartId(departId);
        DictModel model = new DictModel(depart.getId(), depart.getParentId());
        return model;
    }

    @Override
    public List<SysDepartModel> getAllSysDepart() {
        List<SysDepartModel> departModelList = new ArrayList<SysDepartModel>();
        List<SysDepart> departList = departMapper.selectList(new QueryWrapper<SysDepart>().eq("del_flag", "0"));
        for (SysDepart depart : departList) {
            SysDepartModel model = new SysDepartModel();
            BeanUtils.copyProperties(depart, model);
            departModelList.add(model);
        }
        return departModelList;
    }

    @Override
    public DynamicDataSourceModel getDynamicDbSourceById(String dbSourceId) {
        SysDataSource dbSource = dataSourceService.getById(dbSourceId);
        return new DynamicDataSourceModel(dbSource);
    }

    @Override
    public DynamicDataSourceModel getDynamicDbSourceByCode(String dbSourceCode) {
        SysDataSource dbSource =
            dataSourceService.getOne(new LambdaQueryWrapper<SysDataSource>().eq(SysDataSource::getCode, dbSourceCode));
        return new DynamicDataSourceModel(dbSource);
    }

    @Override
    public List<String> getDeptHeadByDepId(String deptId) {
        List<SysUser> userList = userMapper
            .selectList(new QueryWrapper<SysUser>().like("depart_ids", deptId).eq("status", 1).eq("del_flag", 0));
        List<String> list = new ArrayList<>();
        for (SysUser user : userList) {
            list.add(user.getUsername());
        }
        return list;
    }

    @Override
    public String upload(MultipartFile file, String bizPath, String uploadType) {
        String url = "";
        if (CommonConstant.UPLOAD_TYPE_MINIO.equals(uploadType)) {
            url = MinioUtil.upload(file, bizPath);
        } else {
            url = OssBootUtil.upload(file, bizPath);
        }
        return url;
    }

    @Override
    public String upload(MultipartFile file, String bizPath, String uploadType, String customBucket) {
        String url = "";
        if (CommonConstant.UPLOAD_TYPE_MINIO.equals(uploadType)) {
            url = MinioUtil.upload(file, bizPath, customBucket);
        } else {
            url = OssBootUtil.upload(file, bizPath, customBucket);
        }
        return url;
    }

    @Override
    public void viewAndDownload(String filePath, String uploadpath, String uploadType, HttpServletResponse response) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            if (filePath.startsWith("http")) {
                String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                if (CommonConstant.UPLOAD_TYPE_MINIO.equals(uploadType)) {
                    String bucketName = filePath.replace(MinioUtil.getMinioUrl(), "").split("/")[0];
                    String objectName = filePath.replace(MinioUtil.getMinioUrl() + bucketName, "");
                    inputStream = MinioUtil.getMinioFile(bucketName, objectName);
                    if (inputStream == null) {
                        bucketName = CommonConstant.UPLOAD_CUSTOM_BUCKET;
                        objectName = filePath.replace(OssBootUtil.getStaticDomain() + "/", "");
                        inputStream = OssBootUtil.getOssFile(objectName, bucketName);
                    }
                } else {
                    String bucketName = CommonConstant.UPLOAD_CUSTOM_BUCKET;
                    String objectName = filePath.replace(OssBootUtil.getStaticDomain() + "/", "");
                    inputStream = OssBootUtil.getOssFile(objectName, bucketName);
                    if (inputStream == null) {
                        bucketName = filePath.replace(MinioUtil.getMinioUrl(), "").split("/")[0];
                        objectName = filePath.replace(MinioUtil.getMinioUrl() + bucketName, "");
                        inputStream = MinioUtil.getMinioFile(bucketName, objectName);
                    }
                }
                response.addHeader("Content-Disposition",
                    "attachment;fileName=" + new String(fileName.getBytes("UTF-8"), "iso-8859-1"));
            } else {
                // 本地文件处理
                filePath = filePath.replace("..", "");
                if (filePath.endsWith(",")) {
                    filePath = filePath.substring(0, filePath.length() - 1);
                }
                String fullPath = uploadpath + File.separator + filePath;
                String downloadFilePath = uploadpath + File.separator + fullPath;
                File file = new File(downloadFilePath);
                inputStream = new BufferedInputStream(new FileInputStream(fullPath));
                response.addHeader("Content-Disposition",
                    "attachment;fileName=" + new String(file.getName().getBytes("UTF-8"), "iso-8859-1"));
            }
            response.setContentType("application/force-download");// 设置强制下载不打开
            outputStream = response.getOutputStream();
            if (inputStream != null) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }
                response.flushBuffer();
            }
        } catch (IOException e) {
            response.setStatus(404);
            log.error("预览文件失败" + e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public void sendWebSocketMsg(String[] userIds, String cmd) {
        JSONObject obj = new JSONObject();
        obj.put(WebsocketConst.MSG_CMD, cmd);
        webSocket.sendMoreMessage(userIds, obj.toJSONString());
    }

    @Override
    public List<LoginUser> queryAllUserByIds(String[] userIds) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>().eq("status", 1).eq("del_flag", 0);
        queryWrapper.in("id", userIds);
        List<LoginUser> loginUsers = new ArrayList<>();
        List<SysUser> sysUsers = userMapper.selectList(queryWrapper);
        for (SysUser user : sysUsers) {
            LoginUser loginUser = new LoginUser();
            BeanUtils.copyProperties(user, loginUser);
            loginUsers.add(loginUser);
        }
        return loginUsers;
    }

    /**
     * 推送签到人员信息
     *
     * @param userId
     */
    @Override
    public void meetingSignWebsocket(String userId) {
        JSONObject obj = new JSONObject();
        obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_SIGN);
        obj.put(WebsocketConst.MSG_USER_ID, userId);
        //TODO 目前全部推送，后面修改
        webSocket.sendAllMessage(obj.toJSONString());
    }

    @Override
    public List<LoginUser> queryUserByNames(String[] userNames) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>().eq("status", 1).eq("del_flag", 0);
        queryWrapper.in("username", userNames);
        List<LoginUser> loginUsers = new ArrayList<>();
        List<SysUser> sysUsers = userMapper.selectList(queryWrapper);
        for (SysUser user : sysUsers) {
            LoginUser loginUser = new LoginUser();
            BeanUtils.copyProperties(user, loginUser);
            loginUsers.add(loginUser);
        }
        return loginUsers;
    }

    @Override
    public List<String> getSubDepIdsByDepId(String departId) {
        return sysDepartService.getSubDepIdsByDepId(departId);
    }

    @Override
    public List<SysDepartModel> getSupDepart(SysDepartModel sysDepartModel) {
        SysDepart sysDepart = new SysDepart();
        BeanUtils.copyProperties(sysDepartModel, sysDepart);
        List<SysDepart> sysDepartList = sysDepartService.getSupDepart(sysDepart);
        List<SysDepartModel> departModelList = new ArrayList<SysDepartModel>();
        for (SysDepart depart : sysDepartList) {
            SysDepartModel model = new SysDepartModel();
            BeanUtils.copyProperties(depart, model);
            departModelList.add(model);
        }
        return departModelList;
    }

    @Override
    public List<SysDepartModel> getDepartsByCode(SysDepartModel sysDepartModel) {
        SysDepart sysDepart = new SysDepart();
        BeanUtils.copyProperties(sysDepartModel, sysDepart);
        List<SysDepart> sysDepartList = sysDepartService.getDepartsByCode(sysDepart);
        List<SysDepartModel> departModelList = new ArrayList<SysDepartModel>();
        for (SysDepart depart : sysDepartList) {
            SysDepartModel model = new SysDepartModel();
            BeanUtils.copyProperties(depart, model);
            departModelList.add(model);
        }
        return departModelList;
    }

    @Override
    public List<SysRoleModel> queryRoleList(SysRoleModel sysRoleModel) {
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(sysRoleModel, sysRole);
        List<SysRole> list = sysRoleService.queryRoleList(sysRole);
        List<SysRoleModel> sysRoleModelList = new ArrayList<SysRoleModel>();

        for (SysRole item : list) {
            SysRoleModel model = new SysRoleModel();
            BeanUtils.copyProperties(item, model);
            sysRoleModelList.add(model);
        }
        return sysRoleModelList;
    }

    @Override
    public String getDepartNameByOrgCode(String orgCode) {
        return departMapper.queryDepartNameByOrgCode(orgCode);
    }

    @Override
    public List<QuartzJobModel> findByJobClassName(String jobClassName) {
        List<QuartzJob> quartzJobList = quartzJobService.findByJobClassName(jobClassName);
        List<QuartzJobModel> list = new ArrayList<>();
        for (QuartzJob job : quartzJobList) {
            QuartzJobModel quartzJobModel = new QuartzJobModel();
            BeanUtils.copyProperties(job, quartzJobModel);
            list.add(quartzJobModel);
        }
        return list;
    }

    @Override
    public void insertSysQuartzLog(SysQuartzLogModel sysQuartzLogModel) {
        SysQuartzLog sysQuartzLog = new SysQuartzLog();
        BeanUtils.copyProperties(sysQuartzLogModel, sysQuartzLog);
        sysQuartzLogService.save(sysQuartzLog);
    }

    @Override
    public void updateDepart(SysUserDepartVO sysUserDepartVO) {
        sysDepartService.updateDepart(sysUserDepartVO);
    }

    @Override
    public String queryDepartCode(String code, String type) {
        return sysDepartService.queryDepartCode(code, type);
    }

    @Override
    public List<SysDepartGroupUserModel> querySysGroupUserList(List<String> orgCodes) {
        List<SysDepartGroupUserVo> sysDepartGroupUserVoList = sysUserService.querySysGroupUserList(orgCodes);
        List<SysDepartGroupUserModel> list = new ArrayList<>();
        for (SysDepartGroupUserVo item : sysDepartGroupUserVoList) {
            SysDepartGroupUserModel sysQuartzLogModel = new SysDepartGroupUserModel();
            BeanUtils.copyProperties(item, sysQuartzLogModel);
            list.add(sysQuartzLogModel);
        }
        return list;
    }
}