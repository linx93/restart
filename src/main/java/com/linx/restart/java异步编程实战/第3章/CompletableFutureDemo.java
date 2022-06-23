package com.linx.restart.java异步编程实战.第3章;

import com.linx.restart.utils.SleepUtil;

import java.util.concurrent.*;

/**
 * completableFuture demo
 *
 * @author linx
 * @since 2022/6/23 下午10:25
 */
public class CompletableFutureDemo {
    private final static int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
    private final static ThreadPoolExecutor POOL_EXECUTOR = new ThreadPoolExecutor(AVAILABLE_PROCESSORS, AVAILABLE_PROCESSORS * 2, 1, TimeUnit.MINUTES, new LinkedBlockingDeque<>(5), new ThreadPoolExecutor.CallerRunsPolicy());

/*
    //1.
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1 默认情况下，支撑CompletableFuture异步运行的线程池是ForkJoinPool
        // ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();  ForkJoinPool.commonPool()在整个JVM中是唯一的
        long start = Instant.now().getEpochSecond();
        CompletableFuture<String> completableFutureA = new CompletableFuture<>();
        CompletableFuture<String> completableFutureB = new CompletableFuture<>();
        POOL_EXECUTOR.execute(() -> completableFutureA.complete(doA()));
        POOL_EXECUTOR.execute(() -> completableFutureB.complete(doB()));

        //等待结果
        String s = completableFutureA.get();
        String s1 = completableFutureB.get();
        System.out.println(s);
        System.out.println(s1);

        System.out.println("汇总结果：" + s + " and " + s1);
        System.out.println("耗时：" + (Instant.now().getEpochSecond() - start) + "秒");
    }*/

/*        //2. 纯异步，没有返回值的异步
        public static void main(String[] args) throws ExecutionException, InterruptedException {
            //默认使用的线程池是ForkJoinPool的ForkJoinPool.commonPool() 在整个JVM中是唯一的
            //CompletableFuture.runAsync(CompletableFutureDemo::doC);
            //手动指定在自定义线程池
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(CompletableFutureDemo::doC, POOL_EXECUTOR);
            //获取到的打印出来是null
            System.out.println(voidCompletableFuture.get());
        }*/


    //3. 有返回值的异步
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //默认使用的线程池是ForkJoinPool的ForkJoinPool.commonPool() 在整个JVM中是唯一的
        CompletableFuture<String> doA = CompletableFuture.supplyAsync(CompletableFutureDemo::doA);
        //手动指定在自定义线程池
        CompletableFuture<String> doB = CompletableFuture.supplyAsync(CompletableFutureDemo::doB, POOL_EXECUTOR);
        String doAResult = doA.get();
        String doBResult = doB.get();
        System.out.println(doAResult + " and " +doBResult);
    }


    private static String doA() {
        System.out.println("doA start");
        SleepUtil.seelp(3);
        System.out.println("doA end");
        return "doA sleep 3 seconds";
    }

    private static String doB() {
        System.out.println("doB start");
        SleepUtil.seelp(1);
        System.out.println("doB end");
        return "doB sleep 1 seconds";
    }

    private static void doC() {
        System.out.println("doC start");
        SleepUtil.seelp(2);
        System.out.println("doC end");
    }
}
