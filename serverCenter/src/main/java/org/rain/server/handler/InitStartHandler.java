package org.rain.server.handler;

import cn.hutool.core.io.FileUtil;
import org.rain.server.data.DataMessageQueue;
import org.rain.server.data.ServerConfig;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Author: wcy
 * @Date: 2021/7/23
 */
public class InitStartHandler {
    private DataMessageQueue dataMessageQueue = DataMessageQueue.getInstance();
    private ServerConfig config = ServerConfig.getInstance();

    public  void initialize(){
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(dataMessageQueue::scheduleSync,
            5, 120, TimeUnit.SECONDS);
    }

    public void initData() {
        if(FileUtil.isNotEmpty(FileUtil.newFile(config.getDataPath()))){
            List<File> allFile = FileUtil.loopFiles(config.getDataPath());
            allFile.forEach(line->{
                List<String> list = FileUtil.readLines(line.getPath(), Charset.defaultCharset());
                if (dataMessageQueue.size()>config.getClientShowSize()){
                    return ;
                }else {
                    dataMessageQueue.addAll(list);
                }
            });
        }

    }

}
