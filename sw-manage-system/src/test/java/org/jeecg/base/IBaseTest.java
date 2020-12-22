package org.jeecg.base;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.Map;

/**
 * @Author Andy
 * @Date 2020/9/30
 */
public interface IBaseTest {
    /**
     * 获取token
     *
     * @param username
     * @param password
     * @return
     */
    String getToken(String username, String password);

    /**
     * 客户端模拟测试请求 doPost
     *
     * @param url
     * @param params
     * @param token
     * @return
     * @throws Exception
     */
    JSONObject doPost(String url, JSONObject params, String token) throws Exception;

    /**
     * 客户端模拟测试请求 doPost，返回类型是参数typeReference中引用的类型
     *
     * @param url
     * @param params
     * @param token
     * @param typeReference
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T doPost(String url, JSONObject params, String token, TypeReference<T> typeReference) throws Exception;

    /**
     * default: admin账号 doPost
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    JSONObject doPost(String url, JSONObject params) throws Exception;

    /**
     * 客户端模拟测试请求 doPut
     *
     * @param url
     * @param params
     * @param token
     * @return
     * @throws Exception
     */
    JSONObject doPut(String url, JSONObject params, String token) throws Exception;

    /**
     * 客户端模拟测试请求 doPut，返回类型是参数typeReference中引用的类型
     *
     * @param url
     * @param params
     * @param token
     * @param typeReference
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T doPut(String url, JSONObject params, String token, TypeReference<T> typeReference) throws Exception;

    /**
     * default: admin账号 doPut
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    JSONObject doPut(String url, JSONObject params) throws Exception;

    /**
     * 客户端模拟测试请求 doGet
     *
     * @param url
     * @param params
     * @param token
     * @return
     * @throws Exception
     */
    JSONObject doGet(String url, JSONObject params, String token) throws Exception;

    /**
     * 客户端模拟测试请求 doGet，返回类型是参数typeReference中引用的类型
     *
     * @param url
     * @param params
     * @param token
     * @param typeReference
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T doGet(String url, JSONObject params, String token, TypeReference<T> typeReference) throws Exception;

    /**
     * default: admin账号 doGet
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    JSONObject doGet(String url, JSONObject params) throws Exception;

    /**
     * @param url
     * @param params
     * @param token
     * @param headerMap 请求头
     * @return
     */
    JSONObject doGet(String url, JSONObject params, String token, Map<String, String> headerMap) throws Exception;

    /**
     * default: admin账号 doGet
     *
     * @throws Exception
     */
    JSONObject doGet(String url, JSONObject params, Map<String, String> headerMap) throws Exception;
}
