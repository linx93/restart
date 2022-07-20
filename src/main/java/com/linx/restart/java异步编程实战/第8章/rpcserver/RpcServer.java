package com.linx.restart.java异步编程实战.第8章.rpcserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 基于Netty的简单demo来模拟RpcServer，数据包格式 【消息体:请求id|】
 *
 * @author linx
 * @since 2022/7/21 上午1:04
 */
public class RpcServer {
    public static void main(String[] args) {
        //0. 配置创建两级线程池
        //boss组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //work组
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        //1. 创建业务处理handler
        NettyServerHandler nettyServerHandler = new NettyServerHandler();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 10240)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)//使用内存池
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)//使用内存池
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(128 * 1024, 256 * 1024))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            ChannelPipeline pipeline = sc.pipeline();
                            //1.1 分隔符
                            ByteBuf delimiter = Unpooled.copiedBuffer("|".getBytes());
                            pipeline.addLast(new DelimiterBasedFrameDecoder(1000, delimiter));
                            //1.2 设置消息内容自动转换为String的解码器到管线
                            pipeline.addLast(new StringDecoder());
                            //1.3 设置字符串消息自动进行编码的编码器到管线
                            pipeline.addLast(new StringEncoder());
                            //1.4 设置业务handler到管线
                            pipeline.addLast(nettyServerHandler);
                        }
                    });
            //2. 启动服务
            ChannelFuture sync = serverBootstrap.bind(18080).sync();
            //3. 等待服务监听socket关闭
            sync.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //4. 关闭
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }


    }
}
