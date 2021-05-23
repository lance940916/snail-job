package com.snailwu.job.core.server;

import com.snailwu.job.core.biz.ExecutorBiz;
import com.snailwu.job.core.biz.impl.ExecutorBizImpl;
import com.snailwu.job.core.thread.RegistryNodeThread;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用 Netty 发送请求与 snail-job-admin 进行任务调度通信
 * 每个客户端都会启动一个 Netty 服务，以供调度中心使用 Http 请求进行任务调度
 *
 * @author 吴庆龙
 * @date 2020/5/22 11:21 上午
 */
public class EmbedServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmbedServer.class);

    /**
     * Node节点的处理操作
     */
    private ExecutorBiz executorBiz;

    private Thread thread;

    /**
     * 启 Netty 服务与调度中心进行通信
     *
     * @param port Netty 服务对应的本机端口
     */
    public void start(int port, String nodeAddress, String appName, String accessToken) {
        executorBiz = new ExecutorBizImpl();

        thread = new Thread(() -> {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline()
                                    .addLast(new IdleStateHandler(0, 0, 30))
                                    .addLast(new HttpServerCodec())
                                    .addLast(new HttpObjectAggregator(5 * 1024 * 1024))
                                    .addLast(new EmbedHttpServerHandler(executorBiz, accessToken))
                            ;
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            try {
                // 执行完 bind 之后就可以接受连接了
                ChannelFuture future = bootstrap.bind(port).sync();
                LOGGER.info("JobHttp服务启动成功。监听端口：{}", port);

                // 启动注册执行器线程
                RegistryNodeThread.start(appName, nodeAddress);

                // 主线程 wait，等待服务端链路关闭，子线程开始监听接受请求
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                // 服务端主动进行中断，忽略
            } finally {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
                LOGGER.info("JobHttp服务停止成功");
            }
        });
        thread.setDaemon(true);
        thread.setName("job-server");
        thread.start();
    }

    /**
     * 停止 Netty 服务
     */
    public void stop() {
        // 停止 Netty
        try {
            thread.interrupt();
            thread.join();
            LOGGER.info("停止线程 {} 成功", thread.getName());
        } catch (Exception e) {
            LOGGER.error("停止线程 {} 异常", thread.getName(), e);
        }

        // 停止注册节点线程
        RegistryNodeThread.stop();
    }

}
