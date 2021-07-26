package org.rain.server.handler;

import org.rain.server.data.DataMessageQueue;

import java.util.concurrent.*;

/**
 * @Author: wcy
 * @Date: 2021/7/23
 */
public class InitStartHandler {
    public static void initialize(){
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(DataMessageQueue::scheduleSync,
            5, 120,TimeUnit.SECONDS);
        LocalStoreHandler.init();
    }

}
