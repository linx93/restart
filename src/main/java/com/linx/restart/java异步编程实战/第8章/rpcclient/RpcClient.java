package com.linx.restart.java异步编程实战.第8章.rpcclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author linx
 * @since 2022/7/21 上午1:46
 */
public class RpcClient {
    //连接通道
    private volatile Channel channel;
    //请求id生成器
    private static final AtomicLong INVOKE_ID = new AtomicLong(0);
    //启动器
    private Bootstrap bootstrap;

    public RpcClient() {
        //1. 配置客户端
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        NettyClientHandler nettyClientHandler = new NettyClientHandler();
        try {
            bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline channelPipeline = socketChannel.pipeline();
                            //1.1 设置帧分隔符即解码器
                            ByteBuf delimiter = Unpooled.copiedBuffer("|".getBytes());
                            channelPipeline.addLast(new DelimiterBasedFrameDecoder(1000, delimiter));
                            //1.2 设置消息内容自动转换为String的解码器到管线
                            channelPipeline.addLast(new StringDecoder());
                            //1.3 设置字符串消息自动进行编码的编码器到管线
                            channelPipeline.addLast(new StringEncoder());
                            //1.4 添加业务handler到管线
                            channelPipeline.addLast(nettyClientHandler);
                        }
                    });
            //2. 发起链接请求，并同步等待链接完成
            ChannelFuture sync = bootstrap.connect("127.0.0.1", 18080).sync();
            if (sync.isDone() && sync.isSuccess()) {
                this.channel = sync.channel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(String msg) {
        channel.writeAndFlush(msg);
    }

    private void close() {
        if (null != bootstrap) {
            bootstrap.group().shutdownGracefully();
        }
        if (null != channel) {
            channel.close();
        }
    }

    /**
     * 根据消息内容和请求id。拼接消息帧
     *
     * @param msg
     * @param reqId
     * @return
     */
    private String generatorFrame(String msg, String reqId) {
        return msg + ":" + reqId + "|";
    }


    /**
     * 异步call
     * @param msg
     * @return
     */
    public CompletableFuture rpcAsyncCall(String msg) {
        //1.创建future
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        //2.创建消息id
        String reqId = String.valueOf(INVOKE_ID.getAndIncrement());
        //3. 根据消息、请求id来创建协议帧
        msg = generatorFrame(msg, reqId);
        //4. nio异步发起为网络请求,马上返回
        this.sendMsg(msg);
        //5. 保存future对象
        FutureMapUtil.put(reqId, completableFuture);
        return completableFuture;
    }


    /**
     * 同步call
     * @param msg
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public String rpcSyncCall(String msg) throws ExecutionException, InterruptedException {
        //1.创建future
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        //2.创建消息id
        String reqId = String.valueOf(INVOKE_ID.getAndIncrement());
        //3. 根据消息、请求id来创建协议帧
        msg = generatorFrame(msg, reqId);
        //4. nio异步发起为网络请求,马上返回
        this.sendMsg(msg);
        //5. 保存future对象
        FutureMapUtil.put(reqId, completableFuture);
        //6. 同步等待结果
        String result = completableFuture.get();

        return result;
    }
}
