package org.rain.server.data;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wcy
 * @Date: 2021/7/21
 */
public class ClientChannel {
    private final Map<String, Channel> managers;

    public ClientChannel() {
        managers = new HashMap<>(10);
    }

    public  void putChannel(String clientId,Channel channel){
        managers.put(clientId,channel);
    }

    public  Channel getChannel(String clientId){
        return managers.get(clientId);
    }

    public  Map<String, Channel> getManagers() {
        return managers;
    }

    public  boolean containsKey(String clientId) {
        return managers.containsKey(clientId);
    }

}
