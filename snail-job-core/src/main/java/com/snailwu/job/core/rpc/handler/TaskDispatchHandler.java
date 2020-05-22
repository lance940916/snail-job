package com.snailwu.job.core.rpc.handler;

import com.snailwu.job.core.response.RpcBaseResponse;
import com.snailwu.job.core.utils.JsonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * 处理调用中心发送过来的调度请求
 *
 * @author 吴庆龙
 * @date 2020/5/22 1:55 下午
 */
@Slf4j
public class TaskDispatchHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
        String uri = msg.uri();
        HttpMethod method = msg.method();
        log.info("method: {} uri: {}", method.name(), uri);

        boolean keepAlive = HttpUtil.isKeepAlive(msg);
        log.info("keepAlive: {}", keepAlive);

        String requestContent = msg.content().toString(StandardCharsets.UTF_8);
        log.info("请求内容: {}", requestContent);

        // 处理请求
        RpcBaseResponse rpcBaseResponse = doService(ctx, msg);

        // 响应请求
        doResponse(ctx, msg, rpcBaseResponse);
    }

    /**
     * 处理客户端的请求，即进行任务调度
     */
    private RpcBaseResponse doService(ChannelHandlerContext ctx, FullHttpRequest msg) {
        return null;
    }

    /**
     * 对客户端进行响应
     */
    private void doResponse(ChannelHandlerContext ctx, FullHttpRequest msg, RpcBaseResponse rpcBaseResponse) {
        // 转 JSON
        String responseJson = JsonUtil.writeValueAsString(rpcBaseResponse);
        ByteBuf contentBuf;
        if (StringUtil.isNullOrEmpty(responseJson)) {
            log.warn("返回空数据");
            contentBuf = Unpooled.EMPTY_BUFFER;
        } else {
            contentBuf = Unpooled.copiedBuffer(responseJson, StandardCharsets.UTF_8);
        }

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
        log.info("响应结束");
    }

}
