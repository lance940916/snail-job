package com.snailwu.job.admin.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.TimeZone;

/**
 * @author 吴庆龙
 * @date 2020/5/22 3:11 下午
 */
public class JsonUtil {
    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * ObjectMapper
     */
    private static final ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .setTimeZone(TimeZone.getDefault());

    /**
     * 将对象转为JSON格式
     * @param value 带转换的对象
     * @return JSON格式数据
     */
    public static String writeValueAsString(Object value) {
        if (value == null) {
            log.error("传入的对象为 null");
            return null;
        }
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("序列化对象异常", e);
        }
        return null;
    }

    public static byte[] writeValueAsBytes(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return mapper.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            log.error("序列化对象异常", e);
        }
        return null;
    }

    public static void writeToFile(File resultFile, Object value) {
        if (value == null) {
            log.error("待序列化的对象为 null");
            return;
        }
        try {
            mapper.writeValue(resultFile, value);
        } catch (IOException e) {
            log.error("序列化对象异常", e);
        }
    }

    public static <T> T readValue(String content, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(content, typeReference);
        } catch (IOException e) {
            log.error("Fail to convert json[{}] to bean[{}]", content, typeReference, e);
        }
        return null;
    }

    public static <T> T readValue(String content, Class<T> valueType) {
        try {
            return mapper.readValue(content, valueType);
        } catch (IOException e) {
            log.error("反序列化对象异常", e);
        }
        return null;
    }

}
