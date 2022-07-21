package com.linx.restart.java异步编程实战.第8章.rpcclient;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.CompletableFuture;

/**
 * @author linx
 * @since 2022/7/21 下午9:12
 */
@ChannelHandler.Sharable
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String[] split = ((String) msg).split(":");
        //根据请求id获取对应的future
        CompletableFuture future = FutureMapUtil.remove(split[1]);
        //如果存在，则设置对应的future
        if (null != future) {
            future.complete(split[0]);
        }
    }
}
