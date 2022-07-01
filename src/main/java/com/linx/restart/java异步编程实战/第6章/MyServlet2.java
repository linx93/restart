package com.linx.restart.java异步编程实战.第6章;

import com.linx.restart.utils.SleepUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * servlet3.1 支持异步操作demo
 *
 * @author linx
 * @since 2022/6/30 下午9:47
 */
@WebServlet(urlPatterns = "/servlet3.1-test-async-read-body", asyncSupported = true)
public class MyServlet2 extends HttpServlet {
    private final static int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
    private final static ThreadPoolExecutor POOL_EXECUTOR = new ThreadPoolExecutor(AVAILABLE_PROCESSORS, AVAILABLE_PROCESSORS * 2, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(5), new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //开启异步，获取异步上下文
        System.out.println("---start servlet3.1---");
        AsyncContext asyncContext = request.startAsync();
        //设置数据就绪的监听器
        ServletInputStream inputStream = request.getInputStream();
        inputStream.setReadListener(new ReadListener() {
            @Override
            public void onDataAvailable() throws IOException {
                //当数据就绪时，通知读数据
                ServletInputStream inputStream = asyncContext.getRequest().getInputStream();
                byte[] bytes = new byte[1024];
                while (inputStream.isReady() && !inputStream.isFinished()) {
                    inputStream.read(bytes);
                }
                inputStream.close();
                String readText = new String(bytes, Charset.defaultCharset());
                System.out.println("线程：" + Thread.currentThread().getName() + " ， read:" + readText);
            }

            @Override
            public void onAllDataRead() throws IOException {
                //当请求体的数据全部被读取完毕，通知进行业务处理
                POOL_EXECUTOR.execute(() -> {
                    System.out.println("---async res start");
                    SleepUtil.sleep(3);
                    int random = new Random().nextInt();
                    System.out.println(random);
                    response.setContentType("text/html;charset=utf-8");
                    try {
                        PrintWriter writer = asyncContext.getResponse().getWriter();
                        writer.write("<html><head><title>hello word</title></head><body><h1>welcome this is a servlet3.1 async demo </h1></body></html>");
                    } catch (IOException e) {
                        System.out.println(e.getLocalizedMessage());
                        throw new RuntimeException(e);
                    } finally {
                        asyncContext.complete();
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError:" + throwable.getLocalizedMessage());
            }
        });
        System.out.println("---end servlet3.1 ---");
    }


    //servlet3.0 同步阻塞IO
    //servlet3.1同步非塞IO
}
