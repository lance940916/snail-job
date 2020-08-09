package com.snailwu.job.admin;

import com.snailwu.job.admin.config.RootConfig;
import com.snailwu.job.admin.service.InfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author 吴庆龙
 * @date 2020/8/7 7:37 上午
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(
        classes = RootConfig.class
)
public class DataSourceTest {

    @Resource
    private DataSource dataSource;

    @Test
    public void testDB() throws SQLException {
        System.out.println(dataSource.getConnection());
    }

}
