package com.linx.restart.java异步编程实战.第6章;

import com.linx.restart.utils.SleepUtil;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * servlet3.O 支持异步操作demo
 *
 * @author linx
 * @since 2022/6/30 下午9:47
 */
@WebServlet(urlPatterns = "/servlet3.0-test1", asyncSupported = true)
public class MyServlet1 extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        //开启异步，获取异步上下文
        System.out.println("---start servlet3.O---");
        AsyncContext asyncContext = request.startAsync();

        //添加事件监听器
        asyncContext.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent asyncEvent) throws IOException {
                System.out.println("onComplete     asyncEvent=" + asyncEvent);
                PrintWriter writer = asyncEvent.getAsyncContext().getResponse().getWriter();
                writer.write("hello MyServlet1");
            }

            @Override
            public void onTimeout(AsyncEvent asyncEvent) throws IOException {
                System.out.println("onTimeout     asyncEvent=" + asyncEvent);
            }

            @Override
            public void onError(AsyncEvent asyncEvent) throws IOException {
                System.out.println("onError     asyncEvent=" + asyncEvent);
            }

            @Override
            public void onStartAsync(AsyncEvent asyncEvent) throws IOException {
                System.out.println("onStartAsync     asyncEvent=" + asyncEvent);
            }
        });

        //提交异步任务
        asyncContext.start(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("提交异步任务");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                asyncContext.complete();
            }
        });
        System.out.println("---end servlet3.O---");
    }
}
