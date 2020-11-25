package com.snailwu.job.core.utils;

import com.snailwu.job.core.biz.model.ResultT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * 使用原生的 HTTPURLConnection 进行请求
 *
 * @author 吴庆龙
 * @date 2020/5/22 4:38 下午
 */
public class JobHttpUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobHttpUtil.class);

    /**
     * Header 中的Token
     */
    public static final String JOB_ACCESS_TOKEN = "SNAIL-JOB-ACCESS-TOKEN";

    private JobHttpUtil() {
    }

    /**
     * 发起 Http Post 请求
     * 返回值一定不是 null
     */
    public static <T> ResultT<T> postBody(String url, String accessToken, Object bodyObj, int timeout, Class<T> clazz) {
        if (url == null || !url.startsWith("http")) {
            return new ResultT<>(ResultT.FAIL_CODE, "请求地址不正确");
        }

        HttpURLConnection connection = null;
        try {
            URL realUrl = new URL(url);
            connection = (HttpURLConnection) realUrl.openConnection();

            // 信任所有主机
            boolean useHttps = url.startsWith("https");
            if (useHttps) {
                HttpsURLConnection https = (HttpsURLConnection) connection;
                trustAllHosts(https);
            }

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setReadTimeout(timeout * 1000);
            connection.setConnectTimeout(3 * 1000);
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Accept-Charset", "UTF-8");

            // Token
            if (accessToken != null && accessToken.trim().length() > 0) {
                connection.setRequestProperty(JOB_ACCESS_TOKEN, accessToken);
            }

            // 建立连接
            connection.connect();

            // 输入请求内容
            if (bodyObj != null) {
                // 默认是 UTF-8 编码
                byte[] bodyBytes = JobJsonUtil.writeValueAsByte(bodyObj);
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(bodyBytes);
                outputStream.flush();
                outputStream.close();
            }

            // 校验状态码
            int statusCode = connection.getResponseCode();
            if (statusCode != 200) {
                return new ResultT<>(ResultT.FAIL_CODE, "请求失败.statusCode:[" + statusCode + "],URL:[" + url + "]");
            }

            // 读取响应内容
            String resultJson;
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                resultJson = sb.toString();
            }

            // 解析 Json, Http返回的是 Result<?> 的json
            return JobJsonUtil.readValue(resultJson, ResultT.class, clazz);
        } catch (Exception e) {
            LOGGER.error("HTTP请求异常.URL:[{}],异常信息:{}", url, e.getMessage());
            return new ResultT<>(ResultT.FAIL_CODE, "HTTP请求异常");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    protected static final TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    // ignore
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    // ignore
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            }
    };

    private static void trustAllHosts(HttpsURLConnection connection) {
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            SSLSocketFactory factory = sc.getSocketFactory();
            connection.setSSLSocketFactory(factory);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        connection.setHostnameVerifier((hostname, session) -> true);
    }

}
