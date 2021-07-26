package org.rain.server.data;

import cn.hutool.core.collection.ListUtil;
import org.rain.common.domain.vo.MessageBaseVO;
import org.rain.server.handler.LocalStoreHandler;

import java.util.*;

/**
 * @Author: wcy
 * @Date: 2021/7/23
 */
public class DataMessageQueue  {

    private static DataMessageQueue instance = null;

    private ServerConfig config = ServerConfig.getInstance();
    private LocalStoreHandler localhandler = LocalStoreHandler.getInstance();

    private  List<Object> dataQueue ;
    private  int tail ;
    private  int savePoint ;

    public static DataMessageQueue getInstance() {
        if (instance == null){
            instance = new DataMessageQueue();
            instance.dataQueue = new ArrayList<>(101);
            instance.tail = 0;
            instance.savePoint = 0;
        }
        return instance;
    }

    private DataMessageQueue(){ }
    /**
     * 仅在初始化时调用该方法，不考虑超容后落盘
     * @param c
     * @return
     */
    public void addAll(Collection<?> c) {
        instance.dataQueue.addAll(c);
        for (int i = 0; i < config.getDataQueueSize() +10; i++) {
            instance.dataQueue.add(new MessageBaseVO());
        }
        //c的数据量小于等于showCount
        instance.savePoint = c.size();
        instance.tail = c.size();
    }

    public  void add(Object o) {
        /**
         * 1 初始化时，加载数据量小于或等于限定值，tail=head+1，不落盘
         */
        if (instance.tail == config.getDataQueueSize() ){
            localhandler.saveDataToFile(instance.dataQueue.subList(instance.savePoint,config.getDataQueueSize()));
            instance.savePoint = tail = 0;
        }
        instance.dataQueue.set(instance.tail++,o);
    }

    public void scheduleSync(){
        /**
         * 当定时同步任务发生时，存在三种情况
         */
        localhandler.saveDataToFile(instance.dataQueue.subList(instance.savePoint,instance.tail));
        instance.savePoint = instance.tail;
    }

    public List<Object> showData(){
        if (instance.tail>config.getClientShowSize()){
            return instance.dataQueue.subList(0,config.getClientShowSize());
        }else {
            return ListUtil.toList(instance.dataQueue.subList(config.getDataQueueSize() - (config.getClientShowSize()-instance.tail),
                config.getDataQueueSize()),instance.dataQueue.subList(0,instance.tail));
        }
    }

    public int size() {
        return instance.dataQueue.size();
    }

//    public static void main(String[] args) {
//        DataMessageQueue fixed = new DataMessageQueue();
//        for (int i = 0; i < 101; i++) {
//            fixed.add(i);
//        }
//        for (int i = 0; i < 120; i++) {
//            fixed.offer(i);
//        }
//                for (int i = 0; i < 20; i++) {
//                    fixed.poll();
//                }
//                for (int i = 0; i < 20; i++) {
//                    fixed.offer(i);
//                }
//        //        System.out.println(fixed.head);
//        //        System.out.println(fixed.tail);
//        System.out.println(fixed.toString());;
//    }
}
