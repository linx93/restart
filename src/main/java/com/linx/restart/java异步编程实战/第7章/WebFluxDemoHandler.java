package com.linx.restart.java异步编程实战.第7章;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 函数式WebFlux Demo，函数式编程模型是基于注解的编程模型的替代方案
 * 注意：当项目同时引入spring-boot-starter-web和spring-boot-starter-webflux时，
 * 会导致函数式编写WebFlux的demo的方式一直404,需要去除pring-boot-starter-web依赖，但是基于注解的方式不会被影响
 *
 * @author linx
 * @since 2022/7/5 下午9:26
 */
public class WebFluxDemoHandler {
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
     * 获取list
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> getList(ServerRequest serverRequest) {
        Flux<String> flux = Flux.just("xiaohei", "xiaohuang", "xiaobai")
                .publishOn(Schedulers.fromExecutor(BIZ_POOL_EXECUTOR))
                .map(element -> {
                    System.out.println(Thread.currentThread().getName());
                    return element;
                });
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(flux, String.class);
    }

    /**
     * 获取Single
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> getSingle(ServerRequest serverRequest) {
        Mono<String> flux = Mono.just("xiaohei");
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(flux, String.class);
    }
}
