package com.linx.restart;

import com.linx.restart.java异步编程实战.第4章.SpringAsync;
import org.apache.logging.log4j.message.AsynchronouslyFormattable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ExecutionException;

/**
 * @author linx
 */
@EnableAsync
@ServletComponentScan
@SpringBootApplication
public class RestartApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RestartApplication.class, args);
        SpringAsync springAsync = context.getBean(SpringAsync.class);
        springAsync.taskA();
        springAsync.taskB();
        springAsync.taskC();
        //System.out.println("taskD complete 【"+springAsync.taskD().get()+"】");
        springAsync.taskD().whenComplete((result, throwable) -> {
            if (throwable == null) {
                System.out.println(Thread.currentThread().getName() + " taskD complete 【" + result + "】");
            } else {
                System.out.println(Thread.currentThread().getName() + "error:" + throwable.getLocalizedMessage());
            }
        });
    }

}
