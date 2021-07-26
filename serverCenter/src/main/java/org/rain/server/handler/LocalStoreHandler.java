package org.rain.server.handler;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.rain.server.data.DataMessageQueue;
import org.rain.server.data.ServerConfig;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @Author: wcy
 * @Date: 2021/7/23
 */
@Slf4j
public class LocalStoreHandler {

    private ServerConfig config = ServerConfig.getInstance();

    private static LocalStoreHandler instance;

    public  String nowFile = config.getDataPath() + config.getFileName() + config.getSuffix();

    private LocalStoreHandler() {
    }

    public static LocalStoreHandler getInstance(){
        if (instance == null){
            instance = new LocalStoreHandler();
        }
        return instance;
    }

    public  void appendFile(){
        config.setFileName(config.getFileName()+1);
        nowFile = config.getDataPath() + config.getFileName() + config.getSuffix();
        FileUtil.touch(FileUtil.newFile(nowFile));
    }

    public  void saveDataToFile(List<Object> dataList){
        if (FileUtil.size(FileUtil.newFile(nowFile)) > config.getFileSize()){
            appendFile();
        }
        FileUtil.appendUtf8Lines(dataList,nowFile);
    }


}
