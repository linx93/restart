package com.linx.restart.java异步编程实战.第5章;

import com.linx.restart.utils.SleepUtil;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Instant;
import java.util.List;

/**
 * Reactor的demo
 *
 * @author linx
 * @since 2022/6/29 下午10:00
 */
public class Reactor {

/*    public static void main(String[] args) {
        List<Person> people = List.of(new Person(17, "沙皮狗"), new Person(18, "杨老五"), new Person(18, "大虫子"), new Person(18, "linx"));
        Flux.fromArray(people.toArray(new Person[0]))
                .filter(person -> person.getAge() >= 18)
                .map(Person::getName)
                .subscribe(System.out::println);
    }*/

    public static void main(String[] args) throws InterruptedException {
        long start = Instant.now().toEpochMilli();
        Flux.just("hello", "word")
                .publishOn(Schedulers.single())//让下面subscribe中的任务异步执行，指定一个新的线程single-1执行，多个任务排序执行的
                .subscribe(item -> {
                    System.out.println("执行线程：" + Thread.currentThread().getName());
                    System.out.println("执行元素：" + item);
                    SleepUtil.sleep(1);
                }, Throwable::printStackTrace);
        System.out.println("耗时：" + (Instant.now().toEpochMilli() - start));
        Thread.currentThread().join();
    }

    //总结：reactor和Rxjava其实很相似的，使用基本相似，后续的webFlux就是基于reactor构建的


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
