package org.rain.server.handler;

import org.rain.server.data.DataMessageQueue;

import java.util.concurrent.*;

/**
 * @Author: wcy
 * @Date: 2021/7/23
 */
public class Init {
    public static void initialize(){
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.schedule(() -> {
            DataMessageQueue.scheduleSync();
            return "call";
        }, 120, TimeUnit.SECONDS);

        FileHandler.init();
    }

}
