package org.rain.server;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wcy
 * @Date: 2021/7/21
 */
public class ServerChannel {
    private static Map<String, Channel> managers = new HashMap<>();

    public static void putChannel(String clientId,Channel channel){
        managers.put(clientId,channel);
    }

    public static Channel getChannel(String clientId){
        return managers.get(clientId);
    }

    public static Map<String, Channel> getManagers() {
        return managers;
    }

    public static void setManagers(Map<String, Channel> managers) {
        ServerChannel.managers = managers;
    }
}
