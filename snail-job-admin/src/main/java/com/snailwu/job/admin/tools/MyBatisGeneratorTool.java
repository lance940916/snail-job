package com.snailwu.job.admin.tools;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 吴庆龙
 * @date 2020/6/4 3:31 下午
 */
public class MyBatisGeneratorTool {
    private static final Logger log = LoggerFactory.getLogger(MyBatisGeneratorTool.class);

    public static void main(String[] args) throws Exception {
        // 配置文件路径
        String userDir = System.getProperty("user.dir");
        String filePath = "/snail-job-admin/src/main/resources/mybatis-generator-config.xml";
        File file = new File(userDir, filePath);
        if (!file.exists()) {
            throw new RuntimeException("MyBatisGenerator 配置文件不存在.");
        }

        // 解析 xml 文件为 Configuration
        List<String> warnings = new ArrayList<>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(file);

        // 覆盖
        DefaultShellCallback callback = new DefaultShellCallback(true);

        // 进行生成
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(new GeneratorCallback());
    }

    /**
     * 定义回调，打印执行过程
     */
    private static class GeneratorCallback implements ProgressCallback {
        @Override
        public void introspectionStarted(int totalTasks) {
            log.info("MBG-introspectionStarted. totalTasks:{}", totalTasks);
        }

        @Override
        public void generationStarted(int totalTasks) {
            log.info("MBG-generationStarted. totalTasks:{}", totalTasks);
        }

        @Override
        public void saveStarted(int totalTasks) {
            log.info("MBG-generationStarted. totalTasks:{}", totalTasks);
        }

        @Override
        public void startTask(String taskName) {
            log.info("MBG-startTask. taskName:{}", taskName);
        }

        @Override
        public void done() {
            log.info("MBG-done");
        }

        @Override
        public void checkCancel() {
//            log.info("MBG-checkCancel");
        }
    }

}
