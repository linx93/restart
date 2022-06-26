package com.linx.restart.java异步编程实战.第2章;

import com.linx.restart.utils.SleepUtil;

import java.time.Instant;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class 显示使用线程池实现异步编程 {
    /**
     * 本机子可以用的处理器数 12
     */
    private final static int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
    /**
     * 自定义线程池
     * corePoolSize:核心线程数大小 -> 12
     * maximumPoolSize:最大线程池大小 -> 12*2
     * keepAliveTime:保持活动时间 -> 1
     * TimeUnit unit：时间单位 -> 分钟
     * BlockingQueue<Runnable> workQueue:阻塞队列
     * RejectedExecutionHandler handler :拒绝执行处理程序
     */
    private final static ThreadPoolExecutor POOL_EXECUTOR = new ThreadPoolExecutor(
            AVAILABLE_PROCESSORS,
            AVAILABLE_PROCESSORS * 2,
            1, TimeUnit.MINUTES,
            new LinkedBlockingDeque<>(5),
            new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) throws InterruptedException {
        long start = Instant.now().toEpochMilli();
        //1 开启异步单元执行任务A
        POOL_EXECUTOR.execute(() -> {
            doSomethingA();
        });
        //2 执行任务B
        doSomethingB();
        //POOL_EXECUTOR.execute(显示使用线程池实现异步编程::doSomethingB);

        //3 计算耗时
        // 其实这里本质就是计算出了doSomethingB的耗时，因为doSomethingA异步执行，如果doSomethingA在doSomethingB之前执行完，
        // 这里计算出的就是总耗时，如果doSomethingA后于doSomethingB执行完，则总耗时就是doSomethingA的耗时
        // 总结：耗时=max(doSomethingA耗时,doSomethingB耗时)
        System.out.println(Instant.now().toEpochMilli() - start);

        //4 挂起的的当前线程,保证doSomethingA和doSomethingB都执行完
        Thread.currentThread().join();
    }

    public static String doSomethingA() {
        SleepUtil.sleep(4);
        System.out.println("exec doSomethingA  1 seconds");
        return " exec doSomethingA  4 seconds";
    }

    public static String doSomethingB() {
        SleepUtil.sleep(2);
        System.out.println("exec doSomethingB  2 seconds");
        return "exec doSomethingB  2 seconds";
    }
}
