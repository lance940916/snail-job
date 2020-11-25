package com.snailwu.job.admin.utils;

import com.snailwu.job.admin.exception.PropertiesNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 属性工具类
 *
 * @author 吴庆龙
 * @date 2020/11/25 下午4:22
 */
public class PropUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropUtil.class);

    private static final Map<String, String> PROP_MAP = new HashMap<>();

    static {
        // 加载系统属性
        Properties sysProperties = System.getProperties();
        fillPropMap(sysProperties);

        // 加载系统环境变量
        Map<String, String> envMap = System.getenv();
        PROP_MAP.putAll(envMap);
    }

    /**
     * 加载文件
     */
    public static synchronized void load(String... fileClassPaths) {
        for (String fileClassPath : fileClassPaths) {
            Properties properties = new Properties();
            InputStream is = ClassLoader.getSystemResourceAsStream(fileClassPath);
            if (is == null) {
                LOGGER.error("读取配置文件流失败。【{}】", fileClassPath);
                continue;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                properties.load(reader);
                fillPropMap(properties);
            } catch (IOException e) {
                LOGGER.error("加载配置文件失败。【{}】", fileClassPath);
            }
        }
    }

    /**
     * 使用 Properties 填充 PROP_MAP
     */
    private static void fillPropMap(Properties properties) {
        Set<Map.Entry<Object, Object>> entries = properties.entrySet();
        for (Map.Entry<Object, Object> entry : entries) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            PROP_MAP.put(String.valueOf(key), String.valueOf(value));
        }
    }

    /**
     * 获取一个属性值
     *
     * @param key 键
     * @return 值，键不存在是返回null
     */
    public static String get(String key) {
        return PROP_MAP.get(key);
    }

    /**
     * 获取一个属性值
     *
     * @param key 键
     * @return 值
     */
    public static Integer getInt(String key) {
        String val = get(key);
        return val == null ? null : Integer.valueOf(val);
    }

    /**
     * 获取一个属性值
     *
     * @param key 键
     * @return 值
     */
    public static Long getLong(String key) {
        String val = get(key);
        return val == null ? null : Long.valueOf(val);
    }

    /**
     * 获取一个属性值，如果不存在返回默认值
     *
     * @param key 键
     * @param defaultVal 默认值
     * @return 值
     */
    public static String getOrDefault(String key, String defaultVal) {
        String val = get(key);
        return val == null ? defaultVal : val;
    }

    /**
     * 获取一个属性值，如果不存在返回默认值
     *
     * @param key 键
     * @param defaultVal 默认值
     * @return 值
     */
    public static Integer getIntOrDefault(String key, Integer defaultVal) {
        Integer val = getInt(key);
        return val == null ? defaultVal : val;
    }

    /**
     * 获取一个属性值，如果不存在返回默认值
     *
     * @param key 键
     * @param defaultVal 默认值
     * @return 值
     */
    public static Long getLongOrDefault(String key, Long defaultVal) {
        Long val = getLong(key);
        return val == null ? defaultVal : val;
    }

    /**
     * 获取一个属性值，如果不存在抛出异常{@link PropertiesNotFoundException}
     *
     * @param key 键
     * @return 值
     */
    public static String getRequired(String key) {
        String val = get(key);
        if (val == null) {
            throw new PropertiesNotFoundException(notFoundMessage(key));
        }
        return val;
    }

    /**
     * 获取一个属性值，如果不存在抛出异常{@link PropertiesNotFoundException}
     *
     * @param key 键
     * @return 值
     */
    public static String getIntRequired(String key) {
        String val = get(key);
        if (val == null) {
            throw new PropertiesNotFoundException(notFoundMessage(key));
        }
        return val;
    }

    /**
     * 获取一个属性值，如果不存在抛出异常{@link PropertiesNotFoundException}
     *
     * @param key 键
     * @return 值
     */
    public static String getLongRequired(String key) {
        String val = get(key);
        if (val == null) {
            throw new PropertiesNotFoundException(notFoundMessage(key));
        }
        return val;
    }

    /**
     * 封装异常信息
     *
     * @param key 键
     * @return 异常信息
     */
    private static String notFoundMessage(String key) {
        return "属性不存在：" + key;
    }

}
