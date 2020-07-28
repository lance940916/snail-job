package com.snailwu.job.core.server;

import com.snailwu.job.core.biz.ExecutorBiz;
import com.snailwu.job.core.biz.impl.ExecutorBizImpl;
import com.snailwu.job.core.biz.model.IdleBeatParam;
import com.snailwu.job.core.biz.model.KillParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.biz.model.TriggerParam;
import com.snailwu.job.core.thread.ExecutorRegistryThread;
import com.snailwu.job.core.utils.JobJsonUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * 使用 Netty 发送请求与 snail-job-admin 进行任务调度通信
 * 每个客户端都会启动一个 Netty 服务，以供调度中心使用 Http 请求进行任务调度
 *
 * @author 吴庆龙
 * @date 2020/5/22 11:21 上午
 */
public class EmbedServer {
    public static final Logger LOGGER = LoggerFactory.getLogger(EmbedServer.class);

    private ExecutorBiz executorBiz;
    private Thread serverThread;

    /**
     * 启 Netty 服务与 admin 进行通信
     *
     * @param httpPort Netty 服务对应的本机端口
     */
    public void start(int httpPort, String executorAddress, String groupName) {
        executorBiz = new ExecutorBizImpl();
        serverThread = new Thread(() -> {
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
                                    .addLast(new EmbedHttpServerHandler(executorBiz))
                            ;
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            try {
                // 执行完 bind 之后就可以接受连接了
                ChannelFuture future = bootstrap.bind(httpPort).sync();
                LOGGER.info("[SnailJob]-嵌入式Http服务启动成功. 监听端口:{}", httpPort);

                // 启动注册节点线程
                startRegistry(groupName, executorAddress);

                // 主线程 wait，等待服务端链路关闭，子线程开始监听接受请求
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                // 服务端主动进行中断，也就是 stop
                LOGGER.info("[SnailJob]-嵌入式Http服务停止运行");
            } finally {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            }
        });
        serverThread.setDaemon(true);
        serverThread.setName("JobEmbedServer");
        serverThread.start();
    }

    /**
     * 停止 Netty 服务
     */
    public void stop() {
        // 停止 Netty
        if (serverThread != null && serverThread.isAlive()) {
            serverThread.interrupt();
        }

        // 停止注册节点线程
        stopRegistry();
    }

    /**
     * Netty 的 Http 请求处理器
     */
    public static class EmbedHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
        private static final Logger LOGGER = LoggerFactory.getLogger(EmbedHttpServerHandler.class);

        private final ExecutorBiz executorBiz;

        public EmbedHttpServerHandler(ExecutorBiz executorBiz) {
            this.executorBiz = executorBiz;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
            String uri = msg.uri();
            HttpMethod method = msg.method();
            String requestData = msg.content().toString(StandardCharsets.UTF_8);
            LOGGER.info("[SnailJob]-请求方法: {} 请求地址:{} 请求体:{}", method.name(), uri, requestData);

            // 请求处理
            ResultT<String> result = doService(method, uri, requestData);

            // 请求响应
            String responseJson = "";
            try {
                responseJson = JobJsonUtil.writeValueAsString(result);
            } catch (Exception e) {
                LOGGER.error("[SnailJob]-序列化JSON异常");
            }
            doResponse(ctx, msg, responseJson);
            LOGGER.info("[SnailJob]-请求响应: {}", responseJson);
        }

        /**
         * 处理客户端的请求
         */
        private ResultT<String> doService(HttpMethod httpMethod, String uri, String requestData) {
            // 校验
            if (HttpMethod.POST != httpMethod) {
                return new ResultT<>(ResultT.FAIL_CODE, "错误的请求,HttpMethod不支持.");
            }
            if (uri == null || uri.trim().length() == 0) {
                return new ResultT<>(ResultT.FAIL_CODE, "错误的请求,请求地址为空.");
            }

            // 映射 uri
            try {
                switch (uri) {
                    case "/beat":  // 心跳监测
                        return executorBiz.beat();
                    case "/idleBeat":  // 执行器是否忙碌
                        IdleBeatParam idleBeatParam = JobJsonUtil.readValue(requestData, IdleBeatParam.class);
                        return executorBiz.idleBeat(idleBeatParam);
                    case "/run":  // 执行任务
                        TriggerParam triggerParam = JobJsonUtil.readValue(requestData, TriggerParam.class);
                        return executorBiz.run(triggerParam);
                    case "/kill":  // 终止任务
                        KillParam killParam = JobJsonUtil.readValue(requestData, KillParam.class);
                        return executorBiz.kill(killParam);
                    case "/log":  // 执行器信息
                        return ResultT.SUCCESS;
                    default:
                        return new ResultT<>(ResultT.FAIL_CODE, "错误的请求,请求地址(" + uri + ")不存在.");
                }
            } catch (Exception e) {
                return new ResultT<>(ResultT.FAIL_CODE, "错误的请求.错误信息:" + e.getMessage());
            }
        }

        /**
         * 请求响应
         */
        private void doResponse(ChannelHandlerContext ctx, FullHttpRequest msg, String content) {
            ByteBuf contentBuf = Unpooled.copiedBuffer(content, StandardCharsets.UTF_8);

            // 构造 response
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, contentBuf);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
            response.headers().set(HttpHeaderNames.CONTENT_ENCODING, StandardCharsets.UTF_8.name());
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

            // 如果请求是 keepAlive，响应也是
            boolean keepAlive = HttpUtil.isKeepAlive(msg);
            if (keepAlive) {
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }
            ctx.writeAndFlush(response);
            ctx.close();
        }
    }

    /**
     * 启动注册线程
     */
    private void startRegistry(String groupName, String executorAddress) {
        ExecutorRegistryThread.start(groupName, executorAddress);
    }

    /**
     * 停止注册线程
     */
    private void stopRegistry() {
        ExecutorRegistryThread.stop();
    }

}
