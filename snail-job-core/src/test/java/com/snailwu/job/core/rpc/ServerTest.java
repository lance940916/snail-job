package com.snailwu.job.core.rpc;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author 吴庆龙
 * @date 2020/5/22 2:07 下午
 */
public class ServerTest {

    @Test
    public void testStart() throws InterruptedException {
        RpcServer server = new RpcServer();
        server.start(9999);

        TimeUnit.SECONDS.sleep(20);
        server.stop();
    }

}
