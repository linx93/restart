package com.linx.restart.java异步编程实战.第3章;

import com.linx.restart.utils.SleepUtil;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * CompletableFuture结合JDK8 Stream使用
 *
 * @author linx
 * @since 2022/6/26 下午11:10
 */
public class CompletableFutureAndJDK8Stream {
    /*
    //1. 传统处理
    public static void main(String[] args) {
        long start = Instant.now().toEpochMilli();
        //请求多个服务
        List<String> responseList = new ArrayList<>(12);
        List<String> serverList = List.of("server1", "server2", "server3", "server4");
        for (String server : serverList) {
            String response = request(server);
            responseList.add(response);
            System.out.println(response);
        }
        //处理响应结果
        System.out.println(responseList.toString());
        System.out.println("耗时：" + (Instant.now().toEpochMilli() - start));

    }*/

    //2. stream+completableFuture处理请求
    public static void main(String[] args) {

        long start = Instant.now().toEpochMilli();
        //请求多个服务
        List<String> serverList = List.of("server1", "server2", "server3", "server4");
        //stream+completableFuture处理请求
        List<CompletableFuture<String>> collect = serverList.stream().map(item -> CompletableFuture.supplyAsync(() -> request(item))).collect(Collectors.toList());
        List<String> responseList = collect.stream().map(CompletableFuture::join).collect(Collectors.toList());
        //处理响应结果
        System.out.println(responseList);
        System.out.println("耗时：" + (Instant.now().toEpochMilli() - start));
    }

    //总结：传统方式耗时是所有任务的耗时加起来，而stream+completableFuture的耗时就是max(所以任务耗时)，性能提升非常明显

    public static String request(String server) {
        SleepUtil.sleep(1);
        return "response server:" + server;
    }
}
