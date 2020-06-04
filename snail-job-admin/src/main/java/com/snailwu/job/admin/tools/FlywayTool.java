package com.snailwu.job.admin.tools;

import org.flywaydb.core.Flyway;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author 吴庆龙
 * @date 2020/6/4 3:30 下午
 */
public class FlywayTool {
    public static void main(String[] args) throws IOException {
        // 配置文件的文件名
        String propertyFileName = "db.properties";

        // 实例化 Properties
        String userDir = System.getProperty("user.dir");
        String filePath = "/snail-job-admin/src/main/resources/" + propertyFileName;
        File file = new File(userDir, filePath);
        if (!file.exists()) {
            throw new RuntimeException("Flyway 配置文件不存在.");
        }

//        FileInputStream fis = new FileInputStream(file);
//        Properties properties = new Properties();
//        properties.load(fis);
//
//        // 获取属性
//        String url = properties.getProperty("hikari.url");
//        String user = properties.getProperty("hikari.username");
//        String password = properties.getProperty("hikari.password");
//
//        // Sql 文件路径
//        String sqlFilePath = "classpath:sql";
//
//        // 创建Flyway
//        Flyway flyway = Flyway.configure()
//                .dataSource(url, user, password)
//                .encoding(StandardCharsets.UTF_8)
//                .locations(sqlFilePath)
//                .load();
//
//        // 清除数据库中的表
//        flyway.clean();
//
//        // 将SQL语句映射到数据库
//        flyway.migrate();

    }
}
