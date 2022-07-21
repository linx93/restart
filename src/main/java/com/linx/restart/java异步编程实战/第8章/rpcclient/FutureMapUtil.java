package com.linx.restart.java异步编程实战.第8章.rpcclient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author linx
 * @since 2022/7/21 下午9:16
 */
public class FutureMapUtil {
    /**
     * <请求id,对应的Future>
     */
    private static final ConcurrentHashMap<String, CompletableFuture> futureMap = new ConcurrentHashMap<>();

    public static void put(String id, CompletableFuture future) {
        futureMap.put(id, future);
    }

    public static CompletableFuture remove(String id) {
        return futureMap.remove(id);
    }
}
