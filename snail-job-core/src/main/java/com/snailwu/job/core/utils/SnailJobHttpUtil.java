package com.snailwu.job.core.utils;

import com.snailwu.job.core.exception.JobHttpException;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static com.snailwu.job.core.constants.JobCoreConstant.JOB_ACCESS_TOKEN;

/**
 * 使用原生的 HTTPURLConnection 进行请求
 *
 * @author 吴庆龙
 * @date 2020/5/22 4:38 下午
 */
public class SnailJobHttpUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(SnailJobHttpUtil.class);

    private static final HttpClient HTTP_CLIENT;

    static {
        HTTP_CLIENT = HttpClientBuilder.create()
                .setMaxConnTotal(50)
                .setMaxConnPerRoute(50)
                .setDefaultRequestConfig(
                        RequestConfig.custom()
                                .setConnectionRequestTimeout(3000)
                                .setConnectTimeout(5000)
                                .setSocketTimeout(5000)
                                .setAuthenticationEnabled(false)
                                .setRedirectsEnabled(false)
                                .build()
                )
                .disableRedirectHandling()
                .disableCookieManagement()
                .disableAutomaticRetries()
                .disableAuthCaching()
                .evictIdleConnections(10, TimeUnit.MINUTES)
                .setDefaultConnectionConfig(
                        ConnectionConfig.custom()
                                .setCharset(StandardCharsets.UTF_8)
                                .build()
                )
                .build();
    }

    public static String post(String url, String accessToken, Object bodyObj) {
        // 序列化请求体
        String bodyContent = SnailJobJsonUtil.writeValueAsString(bodyObj);

        // 请求
        HttpPost post = new HttpPost(url);
        post.setHeader(JOB_ACCESS_TOKEN, accessToken);
        post.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        post.setEntity(new StringEntity(bodyContent, StandardCharsets.UTF_8));
        try {
            HttpResponse httpResponse = HTTP_CLIENT.execute(post);

            // 状态码
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                throw new JobHttpException("请求失败。Http状态码：[" + statusCode + "]，URL：[" + url + "]");
            }

            // 获取响应内容
            HttpEntity httpEntity = httpResponse.getEntity();
            return EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("请求异常。URL:[{}]", url);
            LOGGER.error("请求异常。", e);
        }
        return null;
    }

}
