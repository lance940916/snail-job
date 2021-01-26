package com.snailwu.job.core.server;

import com.snailwu.job.core.biz.NodeBiz;
import com.snailwu.job.core.biz.impl.NodeBizImpl;
import com.snailwu.job.core.biz.model.IdleBeatParam;
import com.snailwu.job.core.biz.model.KillParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.biz.model.TriggerParam;
import com.snailwu.job.core.thread.RegistryNodeThread;
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

import static com.snailwu.job.core.constants.JobCoreConstant.JOB_ACCESS_TOKEN;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(EmbedServer.class);

    /**
     * Node节点的处理操作
     */
    private NodeBiz nodeBiz;

    private Thread thread;

    /**
     * 启 Netty 服务与 admin 进行通信
     *
     * @param httpPort Netty 服务对应的本机端口
     */
    public void start(int httpPort, String nodeAddress, String appName, String accessToken) {
        nodeBiz = new NodeBizImpl();

        thread = new Thread(() -> {
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
//                                    .addLast(new HttpServerCodec())
//                                    .addLast(new HttpObjectAggregator(5 * 1024 * 1024))
                                    .addLast(new EmbedHttpServerHandler(nodeBiz, accessToken))
                            ;
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            try {
                // 执行完 bind 之后就可以接受连接了
                ChannelFuture future = bootstrap.bind(httpPort).sync();
                LOGGER.info("嵌入式Http服务启动成功。监听端口：{}", httpPort);

                // 启动注册节点线程
                RegistryNodeThread.start(appName, nodeAddress);

                // 主线程 wait，等待服务端链路关闭，子线程开始监听接受请求
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                // 服务端主动进行中断，也就是 stop
            } finally {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
                LOGGER.info("嵌入式Http服务停止运行");
            }
        });
        thread.setDaemon(true);
        thread.setName("job-embed-server");
        thread.start();
    }

    /**
     * 停止 Netty 服务
     */
    public void stop() {
        // 停止 Netty
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        // 停止注册节点线程
        RegistryNodeThread.stop();
    }

    /**
     * Netty 的 Http 请求处理器
     */
    private static class EmbedHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
        private static final Logger LOGGER = LoggerFactory.getLogger(EmbedHttpServerHandler.class);

        private final NodeBiz nodeBiz;
        private final String accessToken;

        public EmbedHttpServerHandler(NodeBiz nodeBiz, String accessToken) {
            this.nodeBiz = nodeBiz;
            this.accessToken = accessToken;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
            String uri = msg.uri();
            HttpMethod method = msg.method();
            String requestData = msg.content().toString(StandardCharsets.UTF_8);
            String headerAccessToken = msg.headers().get(JOB_ACCESS_TOKEN);

            // 请求处理
            ResultT<String> result = doService(method, uri, headerAccessToken, requestData);

            // 请求响应
            String responseJson = "";
            try {
                responseJson = JobJsonUtil.writeValueAsString(result);
            } catch (Exception e) {
                LOGGER.error("序列化JSON异常");
            }
            doResponse(ctx, msg, responseJson);
        }

        /**
         * 处理客户端的请求
         */
        private ResultT<String> doService(HttpMethod httpMethod, String uri, String headerAccessToken, String requestData) {
            // 校验
            if (HttpMethod.POST != httpMethod) {
                return new ResultT<>(ResultT.FAIL_CODE, "HttpMethod不支持");
            }
            if (uri == null || uri.trim().length() == 0) {
                return new ResultT<>(ResultT.FAIL_CODE, "请求地址为空");
            }
            if (accessToken != null && accessToken.trim().length() > 0 && !accessToken.equals(headerAccessToken)) {
                return new ResultT<>(ResultT.FAIL_CODE, "AccessToken不正确");
            }

            // 映射 uri
            try {
                switch (uri) {
                    case "/beat":  // 心跳监测
                        return nodeBiz.beat();
                    case "/idleBeat":  // 执行器是否忙碌
                        IdleBeatParam idleBeatParam = JobJsonUtil.readValue(requestData, IdleBeatParam.class);
                        return nodeBiz.idleBeat(idleBeatParam);
                    case "/run":  // 执行任务
                        TriggerParam triggerParam = JobJsonUtil.readValue(requestData, TriggerParam.class);
                        return nodeBiz.run(triggerParam);
                    case "/kill":  // 终止任务
                        KillParam killParam = JobJsonUtil.readValue(requestData, KillParam.class);
                        return nodeBiz.kill(killParam);
                    case "/log":  // TODO 执行器信息
                        return ResultT.SUCCESS;
                    default:
                        return new ResultT<>(ResultT.FAIL_CODE, "请求地址(" + uri + ")不存在。");
                }
            } catch (Exception e) {
                return new ResultT<>(ResultT.FAIL_CODE, "服务内部错误：" + e.getMessage());
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

}
