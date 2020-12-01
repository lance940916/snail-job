package com.snailwu.job.admin;

import com.snailwu.job.admin.config.RootConfig;
import com.snailwu.job.admin.core.config.AdminConfig;
import com.snailwu.job.admin.mapper.JobNodeDynamicSqlSupport;
import com.snailwu.job.core.constants.RegistryConstant;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Date;

import static org.mybatis.dynamic.sql.SqlBuilder.deleteFrom;
import static org.mybatis.dynamic.sql.SqlBuilder.isLessThan;

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
        Date deadDate = DateUtils.addSeconds(new Date(), RegistryConstant.DEAD_TIME * -1);
        System.out.println(DateFormatUtils.format(deadDate, "yyyy-MM-dd HH:mm:ss,SSS"));

        AdminConfig.getInstance().getJobNodeMapper().delete(
                deleteFrom(JobNodeDynamicSqlSupport.jobNode)
                        .where(JobNodeDynamicSqlSupport.updateTime, isLessThan(deadDate))
                        .build().render(RenderingStrategies.MYBATIS3)
        );
    }

}
