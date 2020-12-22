package org.jeecg.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jeecg.modules.shiro.authc.JwtToken;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 单元测试基类
 *
 * @author liucong
 * @date 2020-9-27
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class BaseTest implements IBaseTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Resource
    org.apache.shiro.mgt.SecurityManager securityManager;

    protected MockMvc mockMvc;

    MockHttpServletRequest mockHttpServletRequest;

    MockHttpServletResponse mockHttpServletResponse;
    private String token;

    @BeforeAll
    public static void beforeAll() {
        log.info("beforeAll...");
    }

    @AfterAll
    public static void afterAll() {
        log.info("afterAll...");
    }

    @BeforeEach
    public void beforeEach() {
        log.info("beforeEach...");
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        mockHttpServletRequest = new MockHttpServletRequest(webApplicationContext.getServletContext());
        mockHttpServletResponse = new MockHttpServletResponse();
        MockHttpSession mockHttpSession = new MockHttpSession(webApplicationContext.getServletContext());
        mockHttpServletRequest.setSession(mockHttpSession);
        SecurityUtils.setSecurityManager(securityManager);
    }

    @AfterEach
    public void afterEach() {
        log.info("afterEach...");
        loginOut();
    }

    /**
     * 获取用户token
     *
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    @Override
    public String getToken(String username, String password) {
        //用户登录
        token = login(username, password);
        log.info("username={}, password={}, token={}", username, password, token);

        return token;
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    private String login(String username, String password) {
        Map<String, String> param = new HashMap<>();
        param.put("username", username);
        param.put("password", password);
        JSONObject response = restTemplate.postForObject("/sys/login", param, JSONObject.class);
        String token = response.getJSONObject("result").getString("token");
        // 保存subject 并缓存->> AuthenticationInfo doGetAuthenticationInfo
        Subject subject = SecurityUtils.getSubject();
        JwtToken jwtToken = new JwtToken(token);
        subject.login(jwtToken);
        return token;
    }

    /**
     * 退出登录清空 subject对象 缓存信息
     * 请求结束后调用该接口
     *
     * @return
     */
    public void loginOut() {
        try {
            String url = "/sys/logout";
            JSONObject param = new JSONObject();
            request(url, param, token, "doPost", null);
        } catch (Exception e) {

        }

    }

    private JSONObject request(String url, JSONObject params, String token, String requestMethod,
        Map<String, String> headerMap) throws Exception {
        log.info("模拟测试请求参数:url={},token={},params={}", url, token, params);
        //断言，url是否为空
        Assert.assertNotNull(url);
        //断言，requestMethod是否为空
        Assert.assertNotNull(requestMethod);
        MockHttpServletRequestBuilder request = null;
        switch (requestMethod) {
            case "doPost":
                request = MockMvcRequestBuilders.post(url).content(params.toJSONString());
                break;
            case "doPut":
                request = MockMvcRequestBuilders.put(url).content(params.toJSONString());
                break;
            case "doDelete":
                request = MockMvcRequestBuilders.delete(url).content(params.toJSONString());
                break;
            default: //doGet请求
                MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
                Set<String> keySet = params.keySet();
                for (String key : keySet) {
                    String values = params.getString(key);
                    map.add(key, values);
                }
                request = MockMvcRequestBuilders.get(url).params(map);//传参
                break;
        }
        MockHttpServletRequestBuilder builder =
            request.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .header("X-Access-Token", token);
        if (headerMap != null) {
            for (String key : headerMap.keySet()) {
                builder.header(key, headerMap.get(key));
            }
        }
        MvcResult mvcResult = mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk())
            //.andDo(MockMvcResultHandlers.print())
            .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        //断言，判断http返回代码是否正确
        Assert.assertEquals(200, status);
        JSONObject content = JSONObject.parseObject(contentAsString);
        int code = (int)content.get("code");
        //判断接口返回码是否正确
        Assert.assertEquals(200, code);
        log.info("服务器返回结果:code={},content={}", code, content);
        return content;

    }

    @Override
    public JSONObject doPost(String url, JSONObject params, String token) throws Exception {
        return this.request(url, params, token, "doPost", null);
    }

    @Override
    public <T> T doPost(String url, JSONObject params, String token, TypeReference<T> typeReference) throws Exception {
        JSONObject jsonObject = this.request(url, params, token, "doPost", null);
        return JSON.parseObject(jsonObject.toJSONString(), typeReference.getType());
    }

    @Override
    public JSONObject doPut(String url, JSONObject params, String token) throws Exception {
        return this.request(url, params, token, "doPut", null);
    }

    @Override
    public <T> T doPut(String url, JSONObject params, String token, TypeReference<T> typeReference) throws Exception {
        JSONObject jsonObject = this.request(url, params, token, "doPut", null);
        return JSON.parseObject(jsonObject.toJSONString(), typeReference.getType());
    }

    @Override
    public JSONObject doGet(String url, JSONObject params, String token) throws Exception {
        return this.request(url, params, token, "doGet", null);
    }

    @Override
    public <T> T doGet(String url, JSONObject params, String token, TypeReference<T> typeReference) throws Exception {
        JSONObject jsonObject = this.request(url, params, token, "doGet", null);
        return JSON.parseObject(jsonObject.toJSONString(), typeReference.getType());
    }

    @Override
    public JSONObject doGet(String url, JSONObject params, String token, Map<String, String> headerMap)
        throws Exception {
        return this.request(url, params, token, "doGet", headerMap);
    }

    @Override
    public JSONObject doGet(String url, JSONObject params, Map<String, String> headerMap) throws Exception {
        String token = this.getToken("admin", "123123");
        return this.request(url, params, token, "doGet", headerMap);
    }

    @Override
    public JSONObject doPost(String url, JSONObject params) throws Exception {
        String token = this.getToken("admin", "123123");
        return doPost(url, params, token);
    }

    @Override
    public JSONObject doPut(String url, JSONObject params) throws Exception {
        String token = this.getToken("admin", "123123");
        return doPut(url, params, token);
    }

    @Override
    public JSONObject doGet(String url, JSONObject params) throws Exception {
        String token = this.getToken("admin", "123123");
        return doGet(url, params, token);
    }
}
