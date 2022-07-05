package com.linx.restart.java异步编程实战.第7章;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * 基于函数式的WebFlux的配置
 *
 * @author linx
 * @since 2022/7/5 下午9:44
 */
@Configuration
public class WebFluxDemoHandlerConfig {

    @Bean
    public WebFluxDemoHandler handler() {
        return new WebFluxDemoHandler();
    }

    /**
     * 配置路由
     * @param handler
     * @return
     */
    @Bean
    public RouterFunction<ServerResponse> routerFunction(WebFluxDemoHandler handler) {
        return RouterFunctions.route()
                .GET("/func1", RequestPredicates.accept(MediaType.APPLICATION_JSON), handler::getList)
                .GET("/func2", RequestPredicates.accept(MediaType.APPLICATION_JSON), handler::getSingle)
                .build();
    }
}
