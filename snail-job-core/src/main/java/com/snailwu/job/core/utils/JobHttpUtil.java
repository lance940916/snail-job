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
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * HttpClient
 *
 * @author 吴庆龙
 * @date 2020/5/22 4:38 下午
 */
public class JobHttpUtil {
    private static final Logger log = LoggerFactory.getLogger(JobHttpUtil.class);

    /**
     * Header 中的Token
     */
    public static final String JOB_ACCESS_TOKEN = "SNAIL-JOB-ACCESS-TOKEN";

    /**
     * 发起 Http Post 请求
     */
    public static <T> ResultT<T> postBody(String url, String accessToken, Object bodyObj, int timeout, Class<T> clazz) {
        if (url == null || !url.startsWith("http")) {
            return new ResultT<>(ResultT.FAIL_CODE, "请求地址不正确");
        }

        HttpURLConnection connection = null;
        try {
            URL realUrl = new URL(url);
            connection = (HttpURLConnection) realUrl.openConnection();

            // 信任 Https
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
                byte[] bodyBytes = JobJsonUtil.writeValueAsBytes(bodyObj);
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(bodyBytes);
                outputStream.flush();
                outputStream.close();
            }

            // 校验状态码
            int statusCode = connection.getResponseCode();
            if (statusCode != 200) {
                return new ResultT<>(ResultT.FAIL_CODE, "Job发送Http请求失败,状态码:" + statusCode + ",URL:" + url);
            }

            // 读取响应内容
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            String resultJson = sb.toString();

            // 解析 Json
            try {
                T resultT = JobJsonUtil.readValue(resultJson, clazz);
                return new ResultT<>(resultT);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return new ResultT<>(ResultT.FAIL_CODE, "解析响应JSON异常");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResultT<>(ResultT.FAIL_CODE, "发起HTTP请求异常");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static final TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
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
            log.error(e.getMessage(), e);
        }
        connection.setHostnameVerifier((hostname, session) -> true);
    }

}
