package com.linx.restart.java异步编程实战.第6章;

import com.linx.restart.utils.SleepUtil;
import org.springframework.stereotype.Component;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
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
@WebServlet(urlPatterns = "/servlet3.0-test", asyncSupported = true)
public class MyServlet extends HttpServlet {
    private final static int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
    private final static ThreadPoolExecutor POOL_EXECUTOR = new ThreadPoolExecutor(AVAILABLE_PROCESSORS, AVAILABLE_PROCESSORS * 2, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(5), new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        //开启异步，获取异步上下文
        System.out.println("----start servlet3.O");
        final AsyncContext asyncContext = httpServletRequest.startAsync();
        POOL_EXECUTOR.execute(() -> {
            System.out.println("--- async res start");
            System.out.println("--- async exec thread " + Thread.currentThread().getName() + " ---");
            SleepUtil.sleep(1);
            try {
                PrintWriter out = asyncContext.getResponse().getWriter();
                out.write("welcome this is my servlet3.O async handle demo " + new Random().nextInt());
                System.out.println("async res end");

            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
                //throw new RuntimeException(e);
            } finally {
                //异步完成通知
                asyncContext.complete();
            }
        });
        System.out.println("---end servlet3.O---");
    }

    /*//支持异步处理asyncSupported = true
    //重写doGet方法
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("主线程开始..... " + Thread.currentThread() + " start " + System.currentTimeMillis());
        AsyncContext startAsync = req.startAsync();

        startAsync.start(() -> {
            System.out.println("子线程开始..... " + Thread.currentThread() + " start " + System.currentTimeMillis());
            try {
                //业务处理逻辑;
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            AsyncContext asyncContext = req.getAsyncContext();
            ServletResponse response = asyncContext.getResponse();
            try {
                response.getWriter().write("order success...");
            } catch (IOException e) {
                e.printStackTrace();
            }
            startAsync.complete();
            System.out.println("子线程结束..... " + Thread.currentThread() + " end " + System.currentTimeMillis());
        });
        System.out.println("主线程结束..... " + Thread.currentThread() + " end " + System.currentTimeMillis());
        //主线程的资源断开
    }*/
}
