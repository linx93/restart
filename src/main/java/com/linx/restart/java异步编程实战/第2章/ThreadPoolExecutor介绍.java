package com.linx.restart.java异步编程实战.第2章;

/**
 * ThreadPoolExecutor介绍
 *
 * @author linx
 * @since 2022/6/22 下午11:58
 */
public class ThreadPoolExecutor介绍 {



    /**
     // 线程池的状态
     1. RUNNING:接收新任务并且处理阻塞队列中的任务
     2. SHUTDOWN:拒绝新任务但是处理阻塞队列中的任务
     3. STOP: 拒绝新任务并且抛弃阻塞队列中的任务，同时中断正在执行的任务
     4. TIDYING:所有任务都执行完，（包含阻塞队列中的任务），当前线程次的活动线程数为0,准备要调用terminated方法
     5. TERMINATED：终止状态，terminated方法调用完成后的状态
     */


    /**
     * 线程池状态转换
    1. RUNNING--->SHUTDOWN：当显示调用shutdown()方法时候，或者隐式调用了finalize()时，finalize()调用shutdown()方法
    2. RUNNING或SHUTDOWN---->STOP：当显示调用shutdownNow()方法时候
    3. SHUTDOWN---->TIDYING：当线程池和任务队列都为空时
    4. STOP---->TIDYING：当线程池为空时
    5. TIDYING---->TERMINATED:当terminated() hook方法执行完时候
    */

}
enum State {
    //线程状态，todo 注意：线程池状态和线程状态得分清楚了
    /**
     * Thread state for a thread which has not yet started.
     * <p>
     *  尚未启动的线程的线程状态。
     *
     */
    NEW,

    /**
     * Thread state for a runnable thread.  A thread in the runnable
     * state is executing in the Java virtual machine but it may
     * be waiting for other resources from the operating system
     * such as processor.
     * <p>
     * 可运行线程的线程状态。处于可运行状态的线程正在Java虚拟机中执行,但它可能正在等待来自操作系统的其他资源,例如处理器。
     *
     */
    RUNNABLE,

    /**
     * Thread state for a thread blocked waiting for a monitor lock.
     * A thread in the blocked state is waiting for a monitor lock
     * to enter a synchronized block/method or
     * reenter a synchronized block/method after calling
     * {@link Object#wait() Object.wait}.
     * <p>
     *  线程阻塞等待监视器锁的线程状态。处于阻塞状态的线程在调用{@link Object#wait()Object.wait}后等待监视器锁进入同步块/方法或重新输入同步块/方法。
     *
     */
    BLOCKED,

    /**
     * Thread state for a waiting thread.
     * A thread is in the waiting state due to calling one of the
     * following methods:
     * <ul>
     *   <li>{@link Object#wait() Object.wait} with no timeout</li>
     *   <li>{@link #join() Thread.join} with no timeout</li>
     *   <li>{@link LockSupport#park() LockSupport.park}</li>
     * </ul>
     *
     * <p>A thread in the waiting state is waiting for another thread to
     * perform a particular action.
     *
     * For example, a thread that has called <tt>Object.wait()</tt>
     * on an object is waiting for another thread to call
     * <tt>Object.notify()</tt> or <tt>Object.notifyAll()</tt> on
     * that object. A thread that has called <tt>Thread.join()</tt>
     * is waiting for a specified thread to terminate.
     * <p>
     *  等待线程的线程状态。线程由于调用以下方法之一而处于等待状态：
     * <ul>
     *  <li> {@ link Object#wait()Object.wait}没有超时</li> <li> {@ link #join()Thread.join}没有超时</li> <li> {@ link LockSupport #park()LockSupport.park}
         *  </li>。
         * </ul>
         *
         *  <p>处于等待状态的线程正在等待另一个线程执行特定操作。
         *
         *  例如,在对象上调用<tt> Object.wait()</tt>的线程正在等待另一个线程调用<tt> Object.notify()</tt>或<tt> Object.notifyAll )</tt>
         * 。
         * 调用<tt> Thread.join()</tt>的线程正在等待指定的线程终止。
         *
         */
    WAITING,

    /**
     * Thread state for a waiting thread with a specified waiting time.
     * A thread is in the timed waiting state due to calling one of
     * the following methods with a specified positive waiting time:
     * <ul>
     *   <li>{@link #sleep Thread.sleep}</li>
     *   <li>{@link Object#wait(long) Object.wait} with timeout</li>
     *   <li>{@link #join(long) Thread.join} with timeout</li>
     *   <li>{@link LockSupport#parkNanos LockSupport.parkNanos}</li>
     *   <li>{@link LockSupport#parkUntil LockSupport.parkUntil}</li>
     * </ul>
     * <p>
     *  具有指定等待时间的等待线程的线程状态。由于调用指定正等待时间的以下方法之一,线程处于定时等待状态：
     * <ul>
     * <li> {@ link #sleep Thread.sleep} </li> <li> {@ link Object#wait(long)Object.wait} with timeout </li>
         *  <li> {@ link #join .join} with timeout </li> <li> {@ link LockSupport#parkNanos LockSupport.parkNanos}
         *  </li> <li> {@ link LockSupport#parkUntil LockSupport.parkUntil}。
         * </ul>
         */
    TIMED_WAITING,

    /**
     * Thread state for a terminated thread.
     * The thread has completed execution.
     * <p>
     *  终止线程的线程状态。线程已完成执行。
     *
     */
    TERMINATED;
}
