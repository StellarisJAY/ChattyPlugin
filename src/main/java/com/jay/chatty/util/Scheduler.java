package com.jay.chatty.util;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  定时执行器
 * </p>
 *
 * @author Jay
 * @date 2022/02/03 20:39
 */
public class Scheduler {
    static final ScheduledThreadPoolExecutor SCHEDULER = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
        @Override
        public Thread newThread(@NotNull Runnable r) {
            Thread thread = new Thread(r, "scheduled-thread");
            thread.setDaemon(true);
            return thread;
        }
    });

    public static void schedule(Runnable task, long delay){
        SCHEDULER.schedule(task, delay, TimeUnit.MILLISECONDS);
    }
}
