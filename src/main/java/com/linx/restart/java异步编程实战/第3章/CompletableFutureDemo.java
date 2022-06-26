package com.linx.restart.java异步编程实战.第3章;

import com.linx.restart.utils.RSAUtils;
import com.linx.restart.utils.SleepUtil;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * completableFuture demo
 *
 * @author linx
 * @since 2022/6/23 下午10:25
 */
public class CompletableFutureDemo {
    private final static int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
    private final static ThreadPoolExecutor POOL_EXECUTOR = new ThreadPoolExecutor(AVAILABLE_PROCESSORS, AVAILABLE_PROCESSORS * 2, 1, TimeUnit.MINUTES, new LinkedBlockingDeque<>(5), new ThreadPoolExecutor.CallerRunsPolicy());

/*
    //1.
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1 默认情况下，支撑CompletableFuture异步运行的线程池是ForkJoinPool
        // ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();  ForkJoinPool.commonPool()在整个JVM中是唯一的
        long start = Instant.now().getEpochSecond();
        CompletableFuture<String> completableFutureA = new CompletableFuture<>();
        CompletableFuture<String> completableFutureB = new CompletableFuture<>();
        POOL_EXECUTOR.execute(() -> completableFutureA.complete(doA()));
        POOL_EXECUTOR.execute(() -> completableFutureB.complete(doB()));

        //等待结果
        String s = completableFutureA.get();
        String s1 = completableFutureB.get();
        System.out.println(s);
        System.out.println(s1);

        System.out.println("汇总结果：" + s + " and " + s1);
        System.out.println("耗时：" + (Instant.now().getEpochSecond() - start) + "秒");
    }*/

/*        //2. 纯异步，没有返回值的异步
        public static void main(String[] args) throws ExecutionException, InterruptedException {
            //默认使用的线程池是ForkJoinPool的ForkJoinPool.commonPool() 在整个JVM中是唯一的
            //CompletableFuture.runAsync(CompletableFutureDemo::doC);
            //手动指定在自定义线程池
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(CompletableFutureDemo::doC, POOL_EXECUTOR);
            //获取到的打印出来是null
            System.out.println(voidCompletableFuture.get());
        }*/


/*     //3. 有返回值的异步
   public static void main(String[] args) throws ExecutionException, InterruptedException {
        //默认使用的线程池是ForkJoinPool的ForkJoinPool.commonPool() 在整个JVM中是唯一的
        CompletableFuture<String> doA = CompletableFuture.supplyAsync(CompletableFutureDemo::doA);
        //手动指定在自定义线程池
        CompletableFuture<String> doB = CompletableFuture.supplyAsync(CompletableFutureDemo::doB, POOL_EXECUTOR);
        String doAResult = doA.get();
        String doBResult = doB.get();
        System.out.println(doAResult + " and " +doBResult);
    }*/


/*    //4. thenRun 顺序依赖，无返回值，且后置任务虽然等待前置任务执行完了才开始执行，但是拿不到前置任务的返回结构
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //异步执行doA
        CompletableFuture<String> completableFutureA = CompletableFuture.supplyAsync(CompletableFutureDemo::doA);
        //在doA执行完成后执行doB 默认thenRun是使用的线程池是ForkJoinPool.commonPool()中的同一个现存来执行doA和doB两个任务，
        // 这路因为是用一个线程来完成两个任务，所以耗时是doA+doB的耗时，这很重要的
        CompletableFuture<Void> completableFutureB = completableFutureA.thenRun(CompletableFutureDemo::doB);
        //使用自定义线程池方式，这里相当于指定可回调任务doB是使用POOL_EXECUTOR中的某个线程来执行的，但是耗时是依然是doA+doB，这很重要
        //CompletableFuture<Void> completableFutureB = completableFutureA.thenRunAsync(CompletableFutureDemo::doB, POOL_EXECUTOR);
        //等待doB执行完成 打印的是null，因为使用thenRun得到的completableFutureB是没有带返回值的
        System.out.println(completableFutureB.get());
    }*/

  /*  //5. thenAccept
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFutureA = CompletableFuture.supplyAsync(CompletableFutureDemo::doA);
        //doB的入参就是doA的返参，doB前置依赖就是doA,默认使用的也是使用ForkJoinPool.commonPool()的同一个线程，耗时也是doA+doB
        CompletableFuture<Void> completableFutureB = completableFutureA.thenAccept(CompletableFutureDemo::doB);
         // 这里一样的，如果要自定义线程池就是用thenAcceptAsync方法，这里相当于指定可回调任务doB是使用POOL_EXECUTOR中的某个线程来执行的，但是耗时是依然是doA+doB，这很重要
        //CompletableFuture<Void> completableFutureB = completableFutureA.thenAcceptAsync(CompletableFutureDemo::doB,POOL_EXECUTOR);
        //completableFutureB是不携带任务返回结果的,所以打印的永远是null
        System.out.println(completableFutureB.get());
    }*/

/*    //6. thenApply
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //doA执行完毕后回调doD执行，doA的返回作为doD的入参数  ， 耗时：doA+doD
        long start = Instant.now().toEpochMilli();
//        doA->ForkJoinPoll.commonPoll() ,  doD->ForkJoinPoll.commonPoll()
//        CompletableFuture<String> completableFuture = CompletableFuture
//                .supplyAsync(CompletableFutureDemo::doA).
//                thenApply(CompletableFutureDemo::doD);

        //耗时：doA+doD
        CompletableFuture<String> completableFuture = CompletableFuture
                .supplyAsync(CompletableFutureDemo::doA).
                //使用自定义线程池，doA和doD是不同线程来运行的  doA->ForkJoinPoll.commonPoll() ,  doD->POOL_EXECUTOR ，
                        thenApplyAsync(CompletableFutureDemo::doD, POOL_EXECUTOR);
        System.out.println(completableFuture.get());
        System.out.println(Instant.now().toEpochMilli() - start);

    }*/
/*
    //7. whenComplete,第一个异步任务执行完后执行第二个任务，第二个任务的依赖第一个任务的执行结果
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //加密id
        String encryptId = "TeNivJpS3T/XeqRGWRVI0n1vsF9rhpSaFe0AIRStM6u/6fJi6DhGYF8i+XJmGCvqtIQnrmm81VUunIHxcfhkh88lK0kTSP76HiWUyJblaqlKlGNiFDDxflF1LL34TeoebImSd9662n17q9D/P9rAZz13wxlfNytNsX8nJFFo/QE=";
        //私钥
        String privatekey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJ8+I7KUwBukeaBuPtV3svrQn6fNQnYdY7dsk5creS9o8+jDRAPcRoPsyEZs3Q0P8xdU6HHoJMBEFMI1FuG+whzphEmAJh2v32hSETYE+lFsyzSq6LSV0+0+sbY6pzWzqFRdWmHU1RN1mMPdXx03bQYWWQW0Y5oU4YIYFAWoEgK7AgMBAAECgYAOLs2lISR/EcYXaNpFzvRs7Fnb6ycpN/LiqlP22dNgSpu2tnV/VoYdR+CKjTWe7TW8dT6CrqdfTHEA3xObpY7KOmy98VN5uVuR0ZWmqymil/mUGobrzpZidmi6jNfbIaHpzOgqu89UN/AFZ/qALs877cl67GmCX+QyciQ45IJV2QJBANOdCntujWKp66lI4eFRg1+qKWFDyK936WNtsckGkb42hYOpOT0aPito1HmejMxes0JkVr3fZxVOWY3FPMXQliUCQQDApPXcLHy/iMQNuS++xbfVEiD+KcwJWY6LhfAcybyV62hcnCqRPG7IF+38eGkQVsnm4nN+++W/H0U1abZMu69fAkAiQJkhwZNBFSAAFrv5LKiHI5PvGnmxbUdpwKe2Uknk8A5McWfCbC0D+cPqq68+pVV+uZ8QvMiCulvkhrh/jHPBAkAW5gTLbQZPBgS31OFV/c6CJyuAypsUKW8GKp+F7HzcHSVEjNOKe/J3GlERh4aFiKtrJFOyLmL6us7RMIWYzV5lAkA+n1KQA0Ez2JwFFrRRM+YZ7Y0+yuak2DfKb+4tKKLS11nuS4uTKdgxtgDD6ybzeY6dqT60fVPbhCsoE4AuN9gP";
        long start = Instant.now().toEpochMilli();
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            String decryptId = null;
            try {
                SleepUtil.sleep(1);
                System.out.println("encryptId=" + encryptId);
                decryptId = RSAUtils.decryptByPrivateKey(encryptId, RSAUtils.getPrivateKey(privatekey));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return decryptId;
        });
        CompletableFuture<String> objectCompletableFuture = completableFuture.thenCompose(decryptId -> CompletableFuture.supplyAsync(() -> {
            SleepUtil.sleep(2);
            System.out.println("decryptId=" + decryptId);
            System.out.println("执行findById(" + decryptId + ")");
            System.out.println("查询结果为：" + decryptId + ":findById(decryptId)");
            return decryptId + ":findById(decryptId)";
        }));
        System.out.println(objectCompletableFuture.get());
        System.out.println(Instant.now().toEpochMilli() - start);
        //打印耗时大概3秒多一点，耗时等于两个异步任务耗时的和，说明两个任务是限执行第一个完成后再执行第二个
    }*/

    /*//8. 使用whenComplete实现将两个异步任务的执行结果作为参数再执行一个异步任务
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long start = Instant.now().toEpochMilli();
        //task-a和task-b是并发执行的
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            SleepUtil.sleep(2);
            System.out.println("task-a");
            return "task-a";
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            SleepUtil.sleep(3);
            System.out.println("task-b");
            return "task-b";
        }), (oneResult, twoResult) -> {
            SleepUtil.sleep(1);
            System.out.println("task-compose");
            //System.out.println("compose[" + oneResult + "," + twoResult + "]");
            return "compose[" + oneResult + "," + twoResult + "]";
        });
        System.out.println(completableFuture.get());
        System.out.println(Instant.now().toEpochMilli() - start);
        //耗时大概4秒，耗时=max(task-a,task-b)+task-compose,
    }*/
/*
    //9. allOf实现等待多个并发的completableFuture任务执行完毕
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long start = Instant.now().toEpochMilli();
        //转换多个CompletableFuture为一个CompletableFuture
        CompletableFuture<Void> result = CompletableFuture.allOf(CompletableFuture.supplyAsync(() -> {
                    SleepUtil.sleep(1);
                    System.out.println("task-a");
                    return "task-a";
                }),
                CompletableFuture.supplyAsync(() -> {
                    SleepUtil.sleep(2);
                    System.out.println("task-b");
                    return "task-b";
                }),
                CompletableFuture.supplyAsync(() -> {
                    SleepUtil.sleep(3);
                    System.out.println("task-c");
                    return "task-c";
                }),
                CompletableFuture.supplyAsync(() -> {
                    SleepUtil.sleep(4);
                    System.out.println("task-d");
                    return "task-d";
                }),
                CompletableFuture.supplyAsync(() -> {
                    SleepUtil.sleep(5);
                    System.out.println("task-e");
                    return "task-e";
                }));
        System.out.println(result.get());
        System.out.println(Instant.now().toEpochMilli() - start);
        //耗时大概5秒，说明上面所以的任务都是异步执行的，耗时=max(task-a，task-b，task-c,task-d,task-e)
    }*/
/*
    //9. anyOf实现等待多个并发的completableFuture任务只要有任意一个完成就返回
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long start = Instant.now().toEpochMilli();
        //转换多个CompletableFuture为一个CompletableFuture
        CompletableFuture<Object> result = CompletableFuture.anyOf(CompletableFuture.supplyAsync(() -> {
                    SleepUtil.sleep(1);
                    System.out.println("task-a");
                    return "task-a";
                }),
                CompletableFuture.supplyAsync(() -> {
                    SleepUtil.sleep(2);
                    System.out.println("task-b");
                    return "task-b";
                }),
                CompletableFuture.supplyAsync(() -> {
                    SleepUtil.sleep(3);
                    System.out.println("task-c");
                    return "task-c";
                }),
                CompletableFuture.supplyAsync(() -> {
                    SleepUtil.sleep(4);
                    System.out.println("task-d");
                    return "task-d";
                }),
                CompletableFuture.supplyAsync(() -> {
                    SleepUtil.sleep(5);
                    System.out.println("task-e");
                    return "task-e";
                }));
        System.out.println(result.get());
        System.out.println(Instant.now().toEpochMilli() - start);
        //耗时大概1秒，耗时=min(task-a，task-b，task-c,task-d,task-e)，注意，这里的a到e的所以任务也都是异步的
    }*/

/*
    //10. completeExceptionally对于抛出异常的处理
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        new Thread(() -> {
            SleepUtil.sleep(1);
            try {
                //[0,10)的随机数
                int number = new Random().nextInt(10);
                System.out.println(number);
                if (number > 4) {
                    throw new RuntimeException("随机数不能大于4");
                }
                completableFuture.complete("ok");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                //设置异常给completableFuture内部，这样下面的completableFuture.get()就会抛出异常后终止，而不是一直阻塞等待
                completableFuture.completeExceptionally(e);
            }
        }, "thread-a").start();
        //这里阻塞等待completableFuture执行完成，但是上面的随机数如果是大于5的时候抛出异常后completableFuture.complete("ok")就不会被执行，这会导致这里一直阻塞
        //对于这类情况，提供了completeExceptionally将异常设置给completableFuture内部，这样下面的completableFuture.get()就会抛出异常后终止，而不是一直阻塞等待
        System.out.println(completableFuture.get());
    }*/


    private static String doA() {
        System.out.println("doA start");
        SleepUtil.sleep(3);
        System.out.println("doA end");
        return "doA sleep 3 seconds";
    }

    private static void doB(String event) {
        System.out.println("doB start");
        SleepUtil.sleep(1);
        System.out.println("doB end");
        System.out.println(event + " 在 " + "doB sleep 1 seconds 之前");
    }


    private static String doB() {
        System.out.println("doB start");
        SleepUtil.sleep(1);
        System.out.println("doB end");
        return "doB sleep 1 seconds";
    }

    private static void doC() {
        System.out.println("doC start");
        SleepUtil.sleep(2);
        System.out.println("doC end");
    }

    private static String doD(String event) {
        System.out.println("doD start");
        SleepUtil.sleep(1);
        System.out.println("doD end");
        return event + " 在 " + "doB sleep 1 seconds 之前";
    }

    private static String doE(String event) {
        System.out.println("doE start");
        SleepUtil.sleep(2);
        System.out.println("doE end");
        return event + " 在 " + "doB sleep 1 seconds 之前";
    }
}
