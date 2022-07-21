package com.linx.restart.java异步编程实战.第8章.rpcserver;

import com.linx.restart.utils.SleepUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 业务处理handler
 *
 * @author linx
 * @since 2022/7/21 上午1:32
 */
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 5. 根据消息内容和请求id。拼接消息帧
     *
     * @param msg
     * @param reqId
     * @return
     */
    public String generatorFrame(String msg, String reqId) {
        return msg + ":" + reqId + "|";
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //6. 处理请求
        try {
            System.out.println("客户端发给服务端的消息内容:" + msg);
            //6.1 获取消息体，并且解析出请求id
            String str = (String) msg;
            String reqId = str.split(":")[1];
            //6.2 拼接结果，请求id，协议帧分隔符（模拟服务端执行服务生产结果）
            String resp = generatorFrame("i am linx", reqId);
            SleepUtil.sleep(5);
            //6.3 写回结果
            ctx.channel().writeAndFlush(Unpooled.copiedBuffer(resp.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
