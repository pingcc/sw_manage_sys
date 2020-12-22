package org.jeecg.common.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * http请求
 *
 * @author huangtao
 */
public class HttpClientUtil {
    private static final Logger log = LoggerFactory.getLogger(HttpClientUtil.class);
    protected static CloseableHttpClient httpClient = createHttpClient(100, 10, 600000, 2);

    public static CloseableHttpClient createHttpClient(int maxTotal, int maxPerRoute, int timeout,
        int retryExecutionCount) {
        try {
            SSLContext sslContext = SSLContexts.custom().useSSL().build();
            SSLConnectionSocketFactory sf =
                new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            PoolingHttpClientConnectionManager poolingHttpClientConnectionManager =
                new PoolingHttpClientConnectionManager();
            poolingHttpClientConnectionManager.setMaxTotal(maxTotal);
            poolingHttpClientConnectionManager.setDefaultMaxPerRoute(maxPerRoute);
            SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(timeout).build();
            poolingHttpClientConnectionManager.setDefaultSocketConfig(socketConfig);
            return HttpClientBuilder.create().setConnectionManager(poolingHttpClientConnectionManager)
                .setSSLSocketFactory(sf).setRetryHandler(new HttpRequestRetryHandlerImpl(retryExecutionCount)).build();
        } catch (KeyManagementException var8) {
            var8.printStackTrace();
        } catch (NoSuchAlgorithmException var9) {
            var9.printStackTrace();
        }

        return null;
    }

    public static String send(String url) throws IOException {
        return send(url, null, false, "utf-8");
    }

    public static String send(String url, Map paramMap) throws IOException {
        return send(url, paramMap, true, "utf-8");
    }

    public static String send(String url, Map paramMap, boolean post) throws IOException {
        return send(url, paramMap, post, "utf-8");
    }

    public static String send(String url, Map paramMap, boolean post, String charset) throws IOException {
        return send(url, paramMap, null, post, charset);
    }

    public static String send(String url, Map paramMap, Map<String, String> header, boolean post, String charset)
        throws IOException {
        if (url == null || "".equals(url.trim()))
            throw new IllegalArgumentException("url is empty");
        RequestBuilder requestBuilder = post ? RequestBuilder.post() : RequestBuilder.get();
        if (paramMap != null && paramMap.size() > 0) {
            if (post) {
                List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                for (Object key : paramMap.keySet()) {
                    formparams.add(new BasicNameValuePair(key.toString(), paramMap.get(key).toString()));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
                requestBuilder.setEntity(entity);
            } else {
                StringBuilder sb = new StringBuilder(url);
                sb = sb.indexOf("?") == -1 ? sb.append("?") : sb.indexOf("&") == sb.length() - 1 ? sb : sb.append("&");
                for (Object key : paramMap.keySet()) {
                    sb.append(URLEncoder.encode(key.toString(), "utf-8")).append("=")
                        .append(URLEncoder.encode(Objects.toString(paramMap.get(key), ""), "utf-8")).append("&");
                }
                url = sb.toString();
            }
        }
        //设置请求头
        if (header != null) {
            Set<String> headerKeys = header.keySet();
            for (String key : headerKeys) {
                requestBuilder.setHeader(key, header.get(key));
            }
        }
        HttpUriRequest httpUriRequest = requestBuilder.setUri(url).build();
        try (CloseableHttpResponse response = execute(httpUriRequest)) {
            if (response == null || response.getEntity() == null)
                return null;
            String result = EntityUtils.toString(response.getEntity(), charset);
            return result;
        }
    }

    public static String send(String url, Map paramMap, Map<String, String> header, boolean post) throws IOException {
        return send(url, paramMap, header, post, "utf-8");
    }

    public static String sendJsonRequest(String url, JSONObject requestParams, Map<String, String> header, boolean post,
        String charset) throws IOException {
        try (CloseableHttpResponse response = getResponse(url, requestParams, header, post, charset)) {
            if (response == null || response.getEntity() == null)
                throw new IllegalStateException(url + " request failed");
            if (StringUtils.isNotBlank(charset))
                charset = "utf-8";
            String result = EntityUtils.toString(response.getEntity(), charset);
            return result;
        }
    }

    public static InputStream getFileInputStreamRequest(String url) throws IOException {
        return getFileInputStreamRequest(url, null, null, false, null);
    }

    public static InputStream getFileInputStreamRequest(String url, JSONObject requestParams,
        Map<String, String> header, boolean post, String charset) throws IOException {
        CloseableHttpResponse response = getResponse(url, requestParams, header, post, charset);
        if (response == null || response.getEntity() == null)
            throw new IllegalStateException(url + " request failed");
        return response.getEntity().getContent();
    }

    private static CloseableHttpResponse getResponse(String url, JSONObject requestParams, Map<String, String> header,
        boolean post, String charset) throws IOException {
        if (url == null || "".equals(url.trim()))
            throw new IllegalArgumentException("url is empty");
        RequestBuilder requestBuilder = post ? RequestBuilder.post() : RequestBuilder.get();
        //设置请求头
        if (header != null) {
            Set<String> headerKeys = header.keySet();
            for (String key : headerKeys) {
                requestBuilder.setHeader(key, header.get(key));
            }
        }

        if (requestParams != null && requestParams.size() > 0) {
            if (post) {
                EntityBuilder entityBuilder = EntityBuilder.create().setContentType(ContentType.APPLICATION_JSON)
                    .setText(new JSONObject(requestParams).toJSONString());
                if (StringUtils.isNotBlank(charset))
                    entityBuilder.setContentEncoding(charset);
                requestBuilder.setEntity(entityBuilder.build());
            } else {
                StringBuilder sb = new StringBuilder(url);
                sb = sb.indexOf("?") == -1 ? sb.append("?") : sb.indexOf("&") == sb.length() - 1 ? sb : sb.append("&");
                for (Object key : requestParams.keySet()) {
                    sb.append(URLEncoder.encode(key.toString(), "utf-8")).append("=")
                        .append(URLEncoder.encode(Objects.toString(requestParams.get(key), ""), "utf-8")).append("&");
                }
                url = sb.toString();
            }
        }

        if (requestBuilder.getFirstHeader("Content-Type") == null) {
            requestBuilder.setHeader("Content-Type", "application/json;charset=UTF-8");
        }
        HttpUriRequest httpUriRequest = requestBuilder.setUri(url).build();
        return execute(httpUriRequest);
    }

    public static CloseableHttpResponse execute(HttpUriRequest request) {
        try {
            return httpClient.execute(request, HttpClientContext.create());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 自动重试
     */
    private static class HttpRequestRetryHandlerImpl implements HttpRequestRetryHandler {
        private int retryExecutionCount;

        public HttpRequestRetryHandlerImpl(int retryExecutionCount) {
            this.retryExecutionCount = retryExecutionCount;
        }

        @Override
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            log.warn("http请求自动重试,第{}次", executionCount);
            if (executionCount > this.retryExecutionCount) {
                return false;
            } else if (exception instanceof InterruptedIOException) {
                return false;
            } else if (exception instanceof UnknownHostException) {
                return false;
            } else if (exception instanceof SSLException) {
                return false;
            } else if (exception instanceof ConnectTimeoutException) {
                return true;
            } else if (exception instanceof SocketTimeoutException) {
                return true;
            } else {
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
                return idempotent;
            }
        }
    }

}
