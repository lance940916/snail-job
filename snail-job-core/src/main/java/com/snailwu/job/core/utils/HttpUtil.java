package com.snailwu.job.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 封装的 HttpClient
 *
 * @author 吴庆龙
 * @date 2020/5/22 3:50 下午
 */
public class HttpUtil {

    private static final HttpClient httpClient;

    private static final int CONNECTION_TIMEOUT = 3000;
    private static final int CONNECTION_REQUEST_TIMEOUT = 3000;
    private static final int SOCKET_TIMEOUT = 3000;

    static {
        RequestConfig rc = RequestConfig.custom()
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .setAuthenticationEnabled(false)
                .setContentCompressionEnabled(false)
                .setRedirectsEnabled(false)
                .setRelativeRedirectsAllowed(false)
                .build();

        SocketConfig sc = SocketConfig.custom()
                .setSoTimeout(SOCKET_TIMEOUT)
                .build();

        // 默认的 header
        List<Header> headerList = new ArrayList<>();
        headerList.add(new BasicHeader(HttpHeaders.CONNECTION, "Keep-Alive"));
        headerList.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8"));
        headerList.add(new BasicHeader(HttpHeaders.ACCEPT, "application/json"));
        headerList.add(new BasicHeader(HttpHeaders.ACCEPT_CHARSET, "UTF-8"));
        headerList.add(new BasicHeader(HttpHeaders.CACHE_CONTROL, "no"));

        httpClient = HttpClients.custom()
                .disableAuthCaching()
                .disableCookieManagement()
                .disableRedirectHandling()
                .disableAutomaticRetries()
                .setDefaultRequestConfig(rc)
                .setDefaultSocketConfig(sc)
                .setDefaultHeaders(headerList)
                .build();
    }

    public String post(String url, String bodyJson) {
        HttpPost post = new HttpPost(url);

        // 请求体
        StringEntity body = new StringEntity(bodyJson, StandardCharsets.UTF_8);
        post.setEntity(body);

        try {
            return httpClient.execute(post, new HttpResponseHandler());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 统一将 HttpResponse 转为字符串返回
     */
    @Slf4j
    public static class HttpResponseHandler implements ResponseHandler<String> {
        @Override
        public String handleResponse(HttpResponse response) throws IOException {
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                Header contentType = responseEntity.getContentType();
                log.info("ContentType: {}", contentType);
                String responseContent = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
                log.info("[HTTP 请求]-响应: " + responseContent);
                return responseContent;
            }
            log.error("");
            return null;
        }
    }

}
