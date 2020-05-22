package com.snailwu.job.core.rpc;

import com.snailwu.job.core.rpc.handler.TaskDispatchHandler;
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
import lombok.extern.slf4j.Slf4j;

/**
 * 使用 Netty 发送请求与 snail-job-admin 进行任务调度通信
 * 每个客户端都会启动一个 Netty 服务，以供调度中心使用 Http 请求进行任务调度
 *
 * @author 吴庆龙
 * @date 2020/5/22 11:21 上午
 */
@Slf4j
public class RpcServer {

    private Thread rpcThread;

    /**
     * 启 RPC 服务与 admin 进行通信
     *
     * @param localPort Netty 服务对应的本机端口
     */
    public void start(Integer localPort) {
        rpcThread = new Thread(() -> {
            // 启动 Netty
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new IdleStateHandler(0, 0, 30))
                                    .addLast(new HttpServerCodec())
                                    .addLast(new HttpObjectAggregator(5 * 1024 * 1024))
                                    .addLast(new TaskDispatchHandler())
                            ;
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            try {
                // 执行完 bind 之后就可以接受连接了
                ChannelFuture future = bootstrap.bind(localPort).sync();
                log.info("Worker RPC 服务启动成功. 监听端口:{}", localPort);

                // 主线程 wait，等待服务端链路关闭，子线程开始监听接受请求
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                // 服务端主动进行中断，也就是 stop
                log.info("Worker RPC 服务停止运行");
            } finally {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            }
        });
        rpcThread.setDaemon(true);
        rpcThread.setName("job-worker-rpc-main");
        rpcThread.start();
    }

    /**
     * 停止 RPC 服务
     */
    public void stop() {
        if (rpcThread != null && rpcThread.isAlive()) {
            rpcThread.interrupt();
        }
    }

}
