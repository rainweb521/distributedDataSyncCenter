package org.rain.server.data;

import cn.hutool.core.io.FileUtil;
import lombok.Data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @Author: wcy
 * @Date: 2021/7/26
 */
@Data
public class ServerConfig {
    private static ServerConfig instance = null;
    private boolean startFlag = false;

    protected Integer serverId;

    protected Integer serverPort;

    protected Integer dataQueueSize;
    protected Integer clientShowSize;
    protected String dataPath;
    private String suffix;
    private Long fileName ;
    private Integer fileSize ;

    public static ServerConfig getInstance() {
        if (instance ==null){
            instance = new ServerConfig();
        }
        return instance;
    }

    private ServerConfig() {
    }

    public void parse() throws IOException {
        if (startFlag) return;
        Properties cfg = new Properties();
        File file = FileUtil.newFile("application.properties");
        try (FileInputStream in = new FileInputStream(file)) {
            cfg.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        parseProperties(cfg);
    }
    public void parseProperties(Properties cfg)  {
        ServerConfig serverconfig = new ServerConfig();
        serverconfig.serverId = (Integer)cfg.getOrDefault("server.id",1);
        serverconfig.serverPort = (Integer)cfg.getOrDefault("server.port",8070);
        serverconfig.dataQueueSize = (Integer)cfg.getOrDefault("data.queue.size",100);
        serverconfig.clientShowSize = (Integer)cfg.getOrDefault("client.show.size",10);
        serverconfig.dataPath = (String)cfg.getOrDefault("data.path","/Users/rain/data/logs/data/");
        serverconfig.suffix = (String)cfg.getOrDefault("suffix",".data");
        serverconfig.fileName = (Long)cfg.getOrDefault("fileName",000000001L);
        serverconfig.fileSize = (Integer)cfg.getOrDefault("fileSize",1024 * 1024 * 50);
        serverconfig.startFlag = true;
        instance = serverconfig;
    }

}
