package com.snailwu.job.admin.utils;

import com.snailwu.job.admin.core.conf.AdminConfig;
import com.snailwu.job.core.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author 吴庆龙
 * @date 2020/6/17 10:24 上午
 */
public class I18nUtil {
    private static final Logger log = LoggerFactory.getLogger(I18nUtil.class);

    private static Properties prop;

    private static Properties loadProperties() {
        if (prop != null) {
            return prop;
        }

        String i18n = AdminConfig.getInstance().getProperties().getI18n();
        String i18nFile = MessageFormat.format("i18n/message_{0}.properties", i18n);

        // load prop
        Resource resource = new ClassPathResource(i18nFile);
        EncodedResource encodedResource = new EncodedResource(resource, StandardCharsets.UTF_8);
        try {
            prop = PropertiesLoaderUtils.loadProperties(encodedResource);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return prop;
    }

    public static String getString(String key) {
        return loadProperties().getProperty(key);
    }

    public static String getMultiString(String... keys) {
        Map<String, String> map = new HashMap<>();
        Properties prop = loadProperties();
        if (keys != null && keys.length > 0) {
            for (String key : keys) {
                map.put(key, prop.getProperty(key));
            }
        } else {
            Set<String> propertyNames = prop.stringPropertyNames();
            for (String propertyName : propertyNames) {
                map.put(propertyName, prop.getProperty(propertyName));
            }
        }
        return JsonUtil.writeValueAsString(map);
    }

}
