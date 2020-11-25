package com.snailwu.job.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.snailwu.job.core.exception.SnailJobException;

import java.io.File;
import java.io.IOException;
import java.util.TimeZone;

/**
 * 使用 Jackson 进行 JSON 数据的序列化、反序列化
 *
 * @author 吴庆龙
 * @date 2020/5/22 3:11 下午
 */
public class JobJsonUtil {

    /**
     * 实例化 ObjectMapper
     */
    private static final ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .enable(SerializationFeature.CLOSE_CLOSEABLE)
            .setTimeZone(TimeZone.getDefault());

    /**
     * 将对象转为 String 类型的 Json
     *
     * @param value 待转换的对象
     * @return JSON数据
     */
    public static String writeValueAsString(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new SnailJobException(e);
        }
    }

    /**
     * 将对象转为 byte 类型的 Json
     *
     * @param value 带转换的对象
     * @return JSON数据
     */
    public static byte[] writeValueAsByte(Object value) {
        try {
            return mapper.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            throw new SnailJobException(e);
        }
    }

    /**
     * 将对象转为 Json 并写入文件中
     *
     * @param value 带转换的对象
     */
    public static void writeToFile(File resultFile, Object value) {
        try {
            mapper.writeValue(resultFile, value);
        } catch (IOException e) {
            throw new SnailJobException(e);
        }
    }

    /**
     * 读取 Json 数据转为对象
     *
     * @param content Json数据
     * @param typeReference 对象类型
     * @param <T> 对象泛型
     * @return 对象
     */
    public static <T> T readValue(String content, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(content, typeReference);
        } catch (IOException e) {
            throw new SnailJobException(e);
        }
    }

    /**
     * 读取 Json 数据转为对象
     * @param content Json数据
     * @param valueType 对象类型
     * @param <T> 对象泛型
     * @return 对象
     */
    public static <T> T readValue(String content, Class<T> valueType) {
        try {
            return mapper.readValue(content, valueType);
        } catch (IOException e) {
            throw new SnailJobException(e);
        }
    }

    /**
     * 读取 Json 数据转为对象，类似于 ResultT<String> 格式的
     *
     * @param content Json数据
     * @param classOfT 外部类类型
     * @param argClassOfT 反省类类型
     * @param <T> 外部类
     * @return 对象
     */
    public static <T> T readValue(String content, Class<T> classOfT, Class<?> argClassOfT) {
        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(classOfT, argClassOfT);
            return mapper.readValue(content, javaType);
        } catch (IOException e) {
            throw new SnailJobException(e);
        }
    }

}
