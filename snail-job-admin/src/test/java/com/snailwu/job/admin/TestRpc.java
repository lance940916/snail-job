package com.snailwu.job.admin;

import com.snailwu.job.admin.utils.HttpUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * @author 吴庆龙
 * @date 2020/5/22 2:13 下午
 */
public class TestRpc {

    @Test
    public void sayHello() throws IOException {

        HttpUtil.post("http://localhost:9999/say-hello?name=Mike", "你好，RPC.");

    }

}
