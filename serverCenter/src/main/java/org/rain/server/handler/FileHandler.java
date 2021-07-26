package org.rain.server.handler;

import cn.hutool.core.io.FileUtil;
import org.rain.server.data.DataMessageQueue;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @Author: wcy
 * @Date: 2021/7/23
 */
public class FileHandler {

    private static final String dataPath = "/Users/rain/data/logs/data/";
    private static final String suffix = ".data";
    private static long fileCode = 000000001L;
    private static long fileSize = 1024 * 1024 *50;
    public static String nowFile = dataPath + fileCode + suffix;

    public static void appendFile(){
        fileCode ++;
        nowFile = dataPath + fileCode + suffix;
        FileUtil.touch(FileUtil.newFile(nowFile));
    }

    public static void saveDataToFile(List<Object> dataList){
        if (FileUtil.size(FileUtil.newFile(FileHandler.nowFile)) > fileSize){
            FileHandler.appendFile();
        }
        FileUtil.appendUtf8Lines(dataList,nowFile);
    }
    public static void init() {
        if(FileUtil.isNotEmpty(FileUtil.newFile(dataPath))){
            List<File> allFile = FileUtil.loopFiles(dataPath);
            allFile.forEach(line->{
                List<String> list = FileUtil.readLines(line.getPath(), Charset.defaultCharset());
                if (DataMessageQueue.size()>10){
                    return ;
                }else {
                    DataMessageQueue.addAll(list);
                }
            });
        }

    }

}
