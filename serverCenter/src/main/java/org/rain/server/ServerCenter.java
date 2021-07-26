package org.rain.server;

/**
 * @Author: wcy
 * @Date: 2021/7/21
 */
import lombok.extern.slf4j.Slf4j;
import org.rain.server.center.ServerConnect;
import org.rain.server.data.ServerConfig;
import org.rain.server.handler.InitStartHandler;

import java.io.IOException;

@Slf4j
public class ServerCenter {

    public static void main(String[] args) throws IOException {
        log.info("加载配置");
        ServerConfig.getInstance().parse();

        InitStartHandler initStartHandler = new InitStartHandler();

        initStartHandler.initData();
        ServerConnect connect = new ServerConnect();
        connect.connect();

    }

}