package com.snailwu.job.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.snailwu.job.core.exception.SnailJobException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.TimeZone;

/**
 * 使用 Jackson 记性 JSON 数据的序列化反序列化
 *
 * @author 吴庆龙
 * @date 2020/5/22 3:11 下午
 */
public class JobJsonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobJsonUtil.class);

    /**
     * ObjectMapper
     */
    private static final ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .enable(SerializationFeature.CLOSE_CLOSEABLE)
            .setTimeZone(TimeZone.getDefault());

    /**
     * 将对象转为JSON格式
     * @param value 带转换的对象
     * @return JSON格式数据
     */
    public static String writeValueAsString(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            LOGGER.error("序列化对象异常", e);
            throw new SnailJobException(e);
        }
    }

    public static byte[] writeValueAsBytes(Object value) {
        try {
            return mapper.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            LOGGER.error("序列化对象异常", e);
            throw new SnailJobException(e);
        }
    }

    public static void writeToFile(File resultFile, Object value) {
        try {
            mapper.writeValue(resultFile, value);
        } catch (IOException e) {
            LOGGER.error("序列化对象异常", e);
            throw new SnailJobException(e);
        }
    }

    public static <T> T readValue(String content, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(content, typeReference);
        } catch (IOException e) {
            LOGGER.error("反序列化对象异常", e);
            throw new SnailJobException(e);
        }
    }

    public static <T> T readValue(String content, Class<T> valueType) {
        try {
            return mapper.readValue(content, valueType);
        } catch (IOException e) {
            LOGGER.error("反序列化对象异常", e);
            throw new SnailJobException(e);
        }
    }

    /**
     * 反序列化Json，动态泛型
     */
    public static <T> T readValue(String content, Class<T> classOfT, Class<?> argClassOfT) {
        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(classOfT, argClassOfT);
            return mapper.readValue(content, javaType);
        } catch (IOException e) {
            LOGGER.error("反序列化对象异常", e);
            throw new SnailJobException(e);
        }
    }

}
