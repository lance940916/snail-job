package com.snailwu.job.core.server;

import com.snailwu.job.core.biz.ExecutorBiz;
import com.snailwu.job.core.biz.model.IdleBeatParam;
import com.snailwu.job.core.biz.model.KillParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.biz.model.TriggerParam;
import com.snailwu.job.core.utils.JobJsonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import static com.snailwu.job.core.constants.CoreConstant.ACCESS_TOKEN;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Netty 的 Http 请求处理器
 *
 * @author WuQinglong
 * @date 2021/1/27 12:14
 */
public class EmbedHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final ExecutorBiz executorBiz;
    private final String accessToken;

    public EmbedHttpServerHandler(ExecutorBiz executorBiz, String accessToken) {
        this.executorBiz = executorBiz;
        this.accessToken = accessToken;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
        String uri = msg.uri();
        HttpMethod method = msg.method();
        String requestData = msg.content().toString(UTF_8);
        String headerAccessToken = msg.headers().get(ACCESS_TOKEN);

        // 请求处理
        ResultT<String> result = doService(method, uri, headerAccessToken, requestData);

        // 请求响应
        String respStr = JobJsonUtil.writeValueAsString(result);
        doResponse(ctx, msg, respStr);
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
        if (headerAccessToken == null || accessToken.trim().length() <= 0) {
            return new ResultT<>(ResultT.FAIL_CODE, "AccessToken缺失");
        }
        if (!accessToken.equals(headerAccessToken)) {
            return new ResultT<>(ResultT.FAIL_CODE, "AccessToken错误");
        }

        // 映射 uri
        try {
            switch (uri) {
                // 心跳监测
                case "/beat":
                    return executorBiz.beat();
                // 执行器是否忙碌
                case "/idleBeat":
                    IdleBeatParam idleBeatParam = JobJsonUtil.readValue(requestData, IdleBeatParam.class);
                    return executorBiz.idleBeat(idleBeatParam);
                // 执行任务
                case "/run":
                    TriggerParam triggerParam = JobJsonUtil.readValue(requestData, TriggerParam.class);
                    return executorBiz.run(triggerParam);
                // 终止任务
                case "/kill":
                    KillParam killParam = JobJsonUtil.readValue(requestData, KillParam.class);
                    return executorBiz.kill(killParam);
                default:
                    return new ResultT<>(ResultT.FAIL_CODE, "请求地址[" + uri + "]不存在。");
            }
        } catch (Exception e) {
            return new ResultT<>(ResultT.FAIL_CODE, "服务内部错误：" + e.getMessage());
        }
    }

    /**
     * 请求响应
     */
    private void doResponse(ChannelHandlerContext ctx, FullHttpRequest msg, String content) {
        // 构造 response
        ByteBuf contentBuf = Unpooled.copiedBuffer(content, UTF_8);
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, contentBuf);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
        response.headers().set(HttpHeaderNames.CONTENT_ENCODING, UTF_8.name());
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

        // 如果请求是 keepAlive，响应也是
        if (HttpUtil.isKeepAlive(msg)) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        ctx.writeAndFlush(response);
        ctx.close();
    }
}