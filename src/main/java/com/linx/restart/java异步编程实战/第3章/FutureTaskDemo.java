package com.linx.restart.java异步编程实战.第3章;

import com.linx.restart.utils.SleepUtil;

import java.time.Instant;
import java.util.concurrent.*;

/**
 * futureTask的demo
 *
 * @author linx
 * @since 2022/6/23 下午9:50
 */
public class FutureTaskDemo {
    private final static int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
    private final static ThreadPoolExecutor POOL_EXECUTOR = new ThreadPoolExecutor(AVAILABLE_PROCESSORS, AVAILABLE_PROCESSORS * 2, 1, TimeUnit.MINUTES, new LinkedBlockingDeque<>(5), new ThreadPoolExecutor.CallerRunsPolicy());

/*    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long start = Instant.now().toEpochMilli();
        FutureTask<String> futureTaskA = new FutureTask<>(() -> doA(1));
        FutureTask<String> futureTaskB = new FutureTask<>(() -> doB(3));
        POOL_EXECUTOR.execute(futureTaskA);
        POOL_EXECUTOR.execute(futureTaskB);
        futureTaskA.get();
        futureTaskB.get();
        long end = Instant.now().toEpochMilli();
        long t = end - start;
        System.out.println("耗时" + t);
    }*/

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long start = Instant.now().toEpochMilli();
        //这里使用POOL_EXECUTOR的submit的写法，其实等价于上面的execute
        Future<String> futureTaskA = POOL_EXECUTOR.submit(() -> doA(1));
        Future<String> futureTaskB = POOL_EXECUTOR.submit(() -> doB(3));
        futureTaskA.get();
        futureTaskB.get();
        long end = Instant.now().toEpochMilli();
        long t = end - start;
        System.out.println("耗时" + t);
    }

    private static String doA(int seconds) {
        SleepUtil.seelp(seconds);
        System.out.println("doA");
        return "doA";
    }

    private static String doB(int seconds) {
        SleepUtil.seelp(seconds);
        System.out.println("doB");
        return "doB";
    }
}
