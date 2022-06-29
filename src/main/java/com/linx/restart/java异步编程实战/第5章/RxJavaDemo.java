package com.linx.restart.java异步编程实战.第5章;

import com.linx.restart.utils.SleepUtil;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * RxJava的demo
 *
 * @author linx
 * @since 2022/6/29 下午9:59
 */
public class RxJavaDemo {
/*    public static void main(String[] args) {
        //过滤出18岁以上的人的名字
        List<Person> people = List.of(new Person(18, "linx"), new Person(17, "hwm"));
        Flowable.fromArray(people.toArray(new Person[0]))
                .filter(person -> person.getAge() >= 18)
                .map(Person::getName)
                .subscribe(System.out::println);
    }*/


/*
    public static void main(String[] args) {
        List<String> list = List.of("192.168.0.1", "192.168.0.2", "192.168.0.3", "192.168.0.4");
        long start = Instant.now().toEpochMilli();
        //1.由main线程依次顺序调用rpcCall
        Flowable.fromArray(list.toArray(new String[0]))
                .map(item -> rpcCall(item, item + "-param"))
                .subscribe(System.out::println);
        System.out.println("耗时：" + (Instant.now().toEpochMilli() - start) + "秒");
    }
*/

 /*   public static void main(String[] args) throws InterruptedException {
        //2.由main线程切换到IO线程调用rpcCall
        List<String> list = List.of("192.168.0.1", "192.168.0.2", "192.168.0.3", "192.168.0.4");
        Flowable.fromArray(list.toArray(new String[0]))
                .observeOn(Schedulers.io())//切换到IO线程执行
                .map(item -> rpcCall(item, item + "-param"))
                .subscribe(System.out::println);
        //由于Schedulers.io()指定的Io线程是Deamon线程
        //挂起main线程
        System.out.println("挂起main线程");
        Thread.currentThread().join();
        //总结：可以由于打印看出，虽然由mian线程切换到IO线程执行，但本质还是有一个线程依次执行多次rpcCall
        //打印结果如下：
        //挂起main线程
        //开始 rpcCall： ip=192.168.0.1 param=192.168.0.1-param
        //执行线程：RxCachedThreadScheduler-1
        //结束 rpcCall： ip=192.168.0.1 param=192.168.0.1-param
        //response:[192.168.0.1192.168.0.1-param]
        //开始 rpcCall： ip=192.168.0.2 param=192.168.0.2-param
        //执行线程：RxCachedThreadScheduler-1
        //结束 rpcCall： ip=192.168.0.2 param=192.168.0.2-param
        //response:[192.168.0.2192.168.0.2-param]
        //开始 rpcCall： ip=192.168.0.3 param=192.168.0.3-param
        //执行线程：RxCachedThreadScheduler-1
        //结束 rpcCall： ip=192.168.0.3 param=192.168.0.3-param
        //response:[192.168.0.3192.168.0.3-param]
        //开始 rpcCall： ip=192.168.0.4 param=192.168.0.4-param
        //执行线程：RxCachedThreadScheduler-1
        //结束 rpcCall： ip=192.168.0.4 param=192.168.0.4-param
        //response:[192.168.0.4192.168.0.4-param]
    }*/

   /* public static void main(String[] args) {
        //3.异步调用
        List<String> list = List.of("192.168.0.1", "192.168.0.2", "192.168.0.3", "192.168.0.4");
        long start = Instant.now().toEpochMilli();
        Flowable.fromArray(list.toArray(new String[0]))
                .flatMap(ip -> Flowable.just(ip).subscribeOn(Schedulers.io()).map(value -> rpcCall(value, value)))
                .blockingSubscribe(System.out::println);
        System.out.println("耗时：" + (Instant.now().toEpochMilli() - start));
        //打印结果：
        //开始 rpcCall： ip=192.168.0.4 param=192.168.0.4
        //开始 rpcCall： ip=192.168.0.3 param=192.168.0.3
        //开始 rpcCall： ip=192.168.0.1 param=192.168.0.1
        //开始 rpcCall： ip=192.168.0.2 param=192.168.0.2
        //执行线程：RxCachedThreadScheduler-4
        //执行线程：RxCachedThreadScheduler-1
        //执行线程：RxCachedThreadScheduler-3
        //执行线程：RxCachedThreadScheduler-2
        //结束 rpcCall： ip=192.168.0.4 param=192.168.0.4
        //结束 rpcCall： ip=192.168.0.1 param=192.168.0.1
        //结束 rpcCall： ip=192.168.0.2 param=192.168.0.2
        //结束 rpcCall： ip=192.168.0.3 param=192.168.0.3
        //response:[192.168.0.2 192.168.0.2]
        //response:[192.168.0.1 192.168.0.1]
        //response:[192.168.0.3 192.168.0.3]
        //response:[192.168.0.4 192.168.0.4]
        //耗时：2101
    }*/

   /* public static void main(String[] args) {
        //3.异步调用 自定义线程池
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10, 10, 1, TimeUnit.MINUTES, new LinkedBlockingDeque<>(5), new ThreadPoolExecutor.CallerRunsPolicy());
        List<String> list = List.of("192.168.0.1", "192.168.0.2", "192.168.0.3", "192.168.0.4");
        long start = Instant.now().toEpochMilli();
        Flowable.fromArray(list.toArray(new String[0]))
                .flatMap(ip -> Flowable.just(ip).subscribeOn(Schedulers.from(poolExecutor)).map(value -> rpcCall(value, value)))
                .blockingSubscribe(System.out::println);
        System.out.println("耗时：" + (Instant.now().toEpochMilli() - start));

        //由于poolExecutor内部默认的创建的是用户线程，所以jvm不会正常退出,调用shutdown销毁用户线程
        poolExecutor.shutdown();
        //打印结果：
        //开始 rpcCall： ip=192.168.0.4 param=192.168.0.4
        //开始 rpcCall： ip=192.168.0.2 param=192.168.0.2
        //开始 rpcCall： ip=192.168.0.1 param=192.168.0.1
        //开始 rpcCall： ip=192.168.0.3 param=192.168.0.3
        //执行线程：pool-1-thread-3
        //执行线程：pool-1-thread-1
        //执行线程：pool-1-thread-4
        //执行线程：pool-1-thread-2
        //结束 rpcCall： ip=192.168.0.3 param=192.168.0.3
        //结束 rpcCall： ip=192.168.0.2 param=192.168.0.2
        //结束 rpcCall： ip=192.168.0.4 param=192.168.0.4
        //结束 rpcCall： ip=192.168.0.1 param=192.168.0.1
        //response:[192.168.0.3 192.168.0.3]
        //response:[192.168.0.1 192.168.0.1]
        //response:[192.168.0.2 192.168.0.2]
        //response:[192.168.0.4 192.168.0.4]
        //耗时：2121
    }*/

    /*public static void main(String[] args) throws InterruptedException {
        long start = Instant.now().toEpochMilli();
        Flowable.fromCallable(() -> rpcCall("192.168.0.1", "param"))
                .subscribeOn(Schedulers.io())//让上面的元素的发射阶段异步
                .observeOn(Schedulers.single())//observeOn方法的作用让接收元素和处理元素的逻辑从main线程中切换到其他线程中，让main马上返回
                .subscribe(RxJavaDemo::subscribeProcess, Throwable::printStackTrace);
        //挂起main线程
        System.out.println("挂起main线程");
        Thread.currentThread().join();
        //总结：
        //1. subscribeOn(Schedulers.io())是让我上面() -> rpcCall("192.168.0.1", "param")【数据发射】从mian线程进入另外一个线程异步执行
        //2. observeOn(Schedulers.single())是当数据准备就绪【数据发射完毕】时切换其他线程上执行处理数据，就比如这里的RxJavaDemo::subscribeProcess就是在另外的线程上执行的
        //打印结果：
        //挂起main线程
        //开始 rpcCall： ip=192.168.0.1 param=param
        //执行线程：RxCachedThreadScheduler-1
        //结束 rpcCall： ip=192.168.0.1 param=param
        //开始处理： res=response:[192.168.0.1param]
        //处理线程：RxSingleScheduler-1
        //结束处理： res=response:[192.168.0.1param]
    }*/


    //defer的使用
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger();
        Observable.range(1, 10)
                .doOnNext(ignored -> atomicInteger.incrementAndGet())
                .ignoreElements()//忽虑发射出的元素
                .andThen(Single.just(atomicInteger.get()))
                .subscribe(System.out::println);
        //这里打印出的是0，因为Single.just(atomicInteger.get())是在数据尚未运行还在还编译时计算的，所以我们需要延迟它，等原始的流完毕后再执行，这时候就需要defer
        Observable.range(1, 10)
                .doOnNext(ignored -> atomicInteger.incrementAndGet())
                .ignoreElements()//忽虑发射出的元素
                .andThen(Single.defer(() -> Single.just(atomicInteger.get())))
                .subscribe(System.out::println);
        //这里打印


        //RxJava的的github地址http://github.com/ReactiveX/RxJava
    }


    /**
     * 模拟rpc请求
     *
     * @param ip    ip
     * @param param 参数
     * @return
     */
    private static String rpcCall(String ip, String param) {
        System.out.println("开始 rpcCall： ip=" + ip + " param=" + param);
        System.out.println("执行线程：" + Thread.currentThread().getName());
        SleepUtil.sleep(2);
        System.out.println("结束 rpcCall： ip=" + ip + " param=" + param);
        return "response:[" + ip + " " + param + "]";
    }

    private static void subscribeProcess(String res) {
        System.out.println("开始处理： res=" + res);
        System.out.println("处理线程：" + Thread.currentThread().getName());
        SleepUtil.sleep(1);
        System.out.println("结束处理： res=" + res);
    }

    public static class Person {
        public Person(int age, String name) {
            this.age = age;
            this.name = name;
        }

        private int age;
        private String name;

        public int getAge() {
            return age;
        }

        public String getName() {
            return name;
        }

    }
}
