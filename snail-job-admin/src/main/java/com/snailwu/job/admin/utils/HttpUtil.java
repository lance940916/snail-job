package com.snailwu.job.admin.utils;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 封装的 HttpClient
 *
 * @author 吴庆龙
 * @date 2020/5/22 4:38 下午
 */
public class HttpUtil {
    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);

    private static final HttpClient httpClient;

    /**
     * 客户端与服务端建立连接的超时时间
     */
    private static final int CONNECTION_TIMEOUT = 3000;

    /**
     * 从连接池中获取连接的超时时间
     */
    private static final int CONNECTION_REQUEST_TIMEOUT = 3000;

    /**
     * 客户端读取服务端数据的超时时间
     */
    private static final int SOCKET_TIMEOUT = 3000;

    /**
     * 文本响应消息的处理器
     */
    private static final HttpTextResponseHandler HTTP_TEXT_RESPONSE_HANDLER = new HttpTextResponseHandler();

    /**
     * 请求配置
     */
    private static final RequestConfig REQUEST_CONFIG = RequestConfig.custom()
            .setConnectTimeout(CONNECTION_TIMEOUT)
            .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
            .setSocketTimeout(SOCKET_TIMEOUT)
            .setAuthenticationEnabled(false)
            .setContentCompressionEnabled(false)
            .setRedirectsEnabled(false)
            .setRelativeRedirectsAllowed(false)
            .build();

    /**
     * Socket 配置
     */
    private static final SocketConfig SOCKET_CONFIG = SocketConfig.custom()
            .setSoTimeout(SOCKET_TIMEOUT)
            .build();

    static {
        // 默认的 header
        List<Header> headerList = new ArrayList<>();
        headerList.add(new BasicHeader(HttpHeaders.CONNECTION, "Keep-Alive"));
        headerList.add(new BasicHeader(HttpHeaders.ACCEPT_CHARSET, "UTF-8"));
        headerList.add(new BasicHeader(HttpHeaders.CACHE_CONTROL, "no-cache"));

        httpClient = HttpClients.custom()
                .disableAuthCaching()
                .disableCookieManagement()
                .disableRedirectHandling()
                .disableAutomaticRetries()
                .setDefaultRequestConfig(REQUEST_CONFIG)
                .setDefaultSocketConfig(SOCKET_CONFIG)
                .setDefaultHeaders(headerList)
                .build();
    }

    /**
     * 使用 GET 请求获取数据
     *
     * @param uri 如 http://localhost:8080/index
     * @return 响应结果
     */
    public static String get(String uri) {
        // GET
        HttpGet get = new HttpGet(uri);
        try {
            return httpClient.execute(get, HTTP_TEXT_RESPONSE_HANDLER);
        } catch (IOException e) {
            log.error("[HTTP 请求]-请求异常. Msg:{}", e.getMessage());
        }
        return null;
    }

    /**
     * 使用 GET 请求获取数据
     *
     * @param uri 如 http://localhost:8080/index
     * @param urlParams 传入要放在 URI 中的参数
     * @param connectTimeout 客户端与服务端建立连接的超时时间
     * @return 响应结果
     */
    public static String get(String uri, Map<String, String> urlParams, int connectTimeout) {
        // 构建 URI
        URI uriObj = buildURI(uri, urlParams);

        // GET
        HttpGet get = new HttpGet(uriObj);
        get.addHeader(new BasicHeader(HttpHeaders.ACCEPT, "application/json, text/*"));
        get.setConfig(RequestConfig.copy(REQUEST_CONFIG).setConnectTimeout(connectTimeout).build());

        try {
            return httpClient.execute(get, HTTP_TEXT_RESPONSE_HANDLER);
        } catch (IOException e) {
            log.error("[HTTP 请求]-请求异常. Msg:{}", e.getMessage());
        }
        return null;
    }

    /**
     * 使用 Post 请求
     *
     * @param uri 如 http://localhost:8080/index
     * @param bodyJsonContent 请求体使用 JSON 编码后放在请求体内
     * @return 响应结果
     */
    public static String post(String uri, String bodyJsonContent) {
        return post(uri, null, bodyJsonContent);
    }

    /**
     * 使用 Post 请求，参数通过 URI 传递
     *
     * @param uri 如 http://localhost:8080/index
     * @param urlParams 传入要放在 URI 中的参数
     * @return 响应结果
     */
    public static String post(String uri, Map<String, String> urlParams) {
        return post(uri, urlParams, null);
    }

    /**
     * 使用 Post 请求，参数通过 URI 传递，并且使用请求体传递 JSON 数据
     *
     * @param uri 如 http://localhost:8080/index
     * @param urlParams 传入要放在 URI 中的参数
     * @param bodyJsonContent 请求体使用 JSON 编码后放在请求体内
     * @return 响应结果
     */
    public static String post(String uri, Map<String, String> urlParams, String bodyJsonContent) {
        // 构建 URI
        URI uriObj = buildURI(uri, urlParams);

        // POST
        HttpPost post = new HttpPost(uriObj);
        post.addHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8"));
        post.addHeader(new BasicHeader(HttpHeaders.ACCEPT, "application/json, text/*"));

        // 请求体
        if (bodyJsonContent != null && bodyJsonContent.length() > 0) {
            StringEntity body = new StringEntity(bodyJsonContent, StandardCharsets.UTF_8);
            post.setEntity(body);
        }

        // 请求
        try {
            return httpClient.execute(post, HTTP_TEXT_RESPONSE_HANDLER);
        } catch (IOException e) {
            log.error("[HTTP 请求]-请求异常. Msg:{}", e.getMessage());
        }
        return null;
    }

    /**
     * 构造 URI
     *
     * @param uri 如 http://localhost:8080/index
     * @param urlParams 传入要放在 URI 中的参数
     * @return 响应结果
     */
    private static URI buildURI(String uri, Map<String, String> urlParams) {
        URI uriObj;
        try {
            URIBuilder uriBuilder = new URIBuilder(uri);
            uriBuilder.setCharset(StandardCharsets.UTF_8);
            if (urlParams != null && !urlParams.isEmpty()) {
                for (Map.Entry<String, String> entry : urlParams.entrySet()) {
                    uriBuilder.addParameter(entry.getKey(), entry.getValue());
                }
            }
            uriObj = uriBuilder.build();
        } catch (URISyntaxException e) {
            log.error("[HTTP 请求]-构造 URI 异常. Msg:{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return uriObj;
    }

    /**
     * HTTP 对于文本的封装处理
     */
    public static class HttpTextResponseHandler implements ResponseHandler<String> {
        @Override
        public String handleResponse(HttpResponse response) throws IOException {
            StatusLine statusLine = response.getStatusLine();
            HttpEntity responseEntity = response.getEntity();
            Header contentType = responseEntity.getContentType();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                log.info("[HTTP 请求]-响应 StatusCode:{} ContentType:{}", statusCode, contentType);

                String responseContent = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
                log.info("[HTTP 请求]-响应消息: " + responseContent);
                return responseContent;
            } else {
                log.error("[HTTP 请求]-响应 StatusCode:{} ContentType:{}", statusCode, contentType);
            }

            return null;
        }
    }
}
