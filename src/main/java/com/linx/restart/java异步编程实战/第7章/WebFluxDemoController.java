package com.linx.restart.java异步编程实战.第7章;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 注解式webFlux demo
 *
 * @author linx
 * @since 2022/7/4 下午11:46
 */
@RestController
public class WebFluxDemoController {

    /**
     * @return Mono流，包含0个或1个对象的流
     */
    @GetMapping(value = "/get-one")
    public Mono<String> getOne() {
        return Mono.just("i am linx").map(element -> {
            //这里打印出来的是http-nio-8080-exec-1，后面的1是随机的不一定是1,
            //这里明显看到了执行任务的线程是nio的，这里的线程是webFlux默认使用的netty服务器中的Worker Group中的某个工作线程
            //这种写法存在的问题：如果这个接口中操作比较耗时【如存在耗时的网络IO】，而刚好这个接口存在大量并发会导致netty服务器的工作线程被耗尽，从而导致程序不能处理其他请求
            System.out.println(Thread.currentThread().getName());
            return element;
        });
    }

    /**
     * @return Flux流，包含0个或多个对象的流
     */
    @GetMapping(value = "/get-list")
    public Flux<String> getList() {
        return Flux.just("i am linx", ",i am hwm")
                //切换到异步线程执行
                .publishOn(Schedulers.boundedElastic())
                .map(element -> {
                    //打印出现的线程是boundedElastic-1，很明显这个时候就不存在耗尽netty服务器的Worker Group中的所以线程的情况
                    System.out.println(Thread.currentThread().getName());
                    return element;
                });
    }


    /**
     * 自定义线程池
     */
    private static final ThreadPoolExecutor BIZ_POOL_EXECUTOR = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 2,
            1,
            TimeUnit.MINUTES,
            new LinkedBlockingDeque<>(5),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    /**
     * @return Flux流，包含0个或多个对象的流
     */
    @GetMapping(value = "/get-list1")
    public Flux<String> getList1() {
        return Flux.just("i", " am", " linx")
                //切换到异步线程执行，使用自定义线程池
                .publishOn(Schedulers.fromExecutor(BIZ_POOL_EXECUTOR))
                .map(element -> {
                    //打印出现的线程如下：
                    //pool-3-thread-1
                    //pool-3-thread-2
                    //pool-3-thread-3
                    //很明显这个时候也不存在耗尽netty服务器的Worker Group中的所以线程的情况，而且使用了3个线程分别处理3个任务
                    System.out.println(Thread.currentThread().getName());
                    return element;
                });
    }
}
