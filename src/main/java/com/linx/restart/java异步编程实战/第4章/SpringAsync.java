package com.linx.restart.java异步编程实战.第4章;

import com.linx.restart.utils.SleepUtil;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * spring框架中的异步任务
 *
 * @author linx
 * @since 2022/6/27 下午10:10
 */
@Component
public class SpringAsync {
    /**
     * 无返回的
     * 这个异步任务的调用在启动类里面
     */
    @Async//注解可以使用@Async("某个线程池")来指定线程池,默认使用的是spring的SimpleAsyncTaskExecutor线程池
    public void taskA() {
        for (int i = 0; i < 10; i++) {
            //最终会看到这个打印出来的是task-1
            System.out.println(Thread.currentThread().getName() + " taskA start-" + i);
            SleepUtil.sleep(1);
            System.out.println(Thread.currentThread().getName() + " async exec taskA");
            System.out.println(Thread.currentThread().getName() + " taskA end-" + i);
        }
    }


    /**
     * 无返回的
     * 这个异步任务的调用在启动类里面
     */
    @Async
    public void taskB() {
        for (int i = 0; i < 8; i++) {
            //最终会看到这个打印出来的是task-2
            System.out.println(Thread.currentThread().getName() + " taskB start-" + i);
            SleepUtil.sleep(1);
            System.out.println(Thread.currentThread().getName() + " async exec taskB");
            System.out.println(Thread.currentThread().getName() + " taskB end-" + i);
        }
    }

    /**
     * 同步任务
     */
    public void taskC() {

        for (int i = 0; i < 8; i++) {
            //最终会看到这个打印出来的是main线程
            System.out.println(Thread.currentThread().getName() + " taskC start-" + i);
            SleepUtil.sleep(1);
            System.out.println(Thread.currentThread().getName() + " sync exec taskB");
            System.out.println(Thread.currentThread().getName() + " taskC end-" + i);
        }
    }


    /**
     * 有返回的，
     * 返回类型范围：[ 1.java中的java.util.concurrent.Future
     * 2.spring中的springframework.util.concurrent.ListenableFuture
     * 3.jdk8+中的java.util.concurrent.CompletableFuture
     * 4.spring中的AsyncResult
     * <p>
     * 这个异步任务的调用在启动类里面
     */
    @Async
    public CompletableFuture<String> taskD() {
        System.out.println(Thread.currentThread().getName() + " taskD start");
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        SleepUtil.sleep(1);
        System.out.println(Thread.currentThread().getName() + " async exec taskD");
        System.out.println(Thread.currentThread().getName() + " taskD end");
        completableFuture.complete("async exec taskD");
        return completableFuture;
    }

    //总结
    //1.注解可以使用@Async("某个线程池")来指定线程池,默认使用的是spring的SimpleAsyncTaskExecutor线程池
    //2.加了@Async注解修饰的方法，被调用时候是会启动一个新的线程去执行的
    //3.要使用@Async注解记得先在启动类上加@EnableAsync注解
    //4.@Async如果修饰了方法，这个方法所在的类就会被spring做代理处理,比如@Async修饰的taskD方法本质是下面这样的
    public CompletableFuture<String> proxyTaskD() {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " taskD start");
            SleepUtil.sleep(1);
            System.out.println(Thread.currentThread().getName() + " async exec taskD");
            System.out.println(Thread.currentThread().getName() + " taskD end");
            return "async exec taskD";
        }, new SimpleAsyncTaskExecutor());
    }
    //5. spring默认使用Cglib对@Async修饰的方法进行代理


}
