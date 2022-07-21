package com.linx.restart.java异步编程实战.第8章.test;

import com.linx.restart.java异步编程实战.第8章.rpcclient.RpcClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author linx
 * @since 2022/7/21 下午9:52
 */
public class TestModelAsyncRpc {
    private static final RpcClient rpcClient = new RpcClient();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1. 同步调用
        System.out.println("服务端响应的结果:" + rpcClient.rpcSyncCall("who are you"));

        //2. 异步调用
        /*CompletableFuture future = rpcClient.rpcAsyncCall("who are you");
        future.whenComplete((v, t) -> {
            if (t != null) {
                System.out.println("t=null");
            } else {
                System.out.println("-------->rpcSyncCall<--------:" + v);
            }
        });*/
    }
}
