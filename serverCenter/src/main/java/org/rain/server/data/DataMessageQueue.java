package org.rain.server.data;

import cn.hutool.core.collection.ListUtil;
import org.rain.common.domain.vo.MessageBaseVO;
import org.rain.server.handler.FileHandler;

import java.util.*;

/**
 * @Author: wcy
 * @Date: 2021/7/23
 */
public class DataMessageQueue  {
    public static int dataCount = 100;
    public static int showCount = 10;

    private static final List<Object> dataQueue = new ArrayList<>(101);
    private static int tail = 0;
    private static int savePoint = 0;

    /**
     * 仅在初始化时调用该方法，不考虑超容后落盘
     * @param c
     * @return
     */
    public static void addAll(Collection<?> c) {
        dataQueue.addAll(c);
        for (int i = 0; i < dataCount +10; i++) {
            dataQueue.add(new MessageBaseVO());
        }
        //c的数据量小于等于showCount
        savePoint = c.size();
        tail = c.size();
    }

    public static void add(Object o) {
        /**
         * 1 初始化时，加载数据量小于或等于限定值，tail=head+1，不落盘
         */
        if (tail == dataCount ){
            FileHandler.saveDataToFile(dataQueue.subList(savePoint,dataCount));
            savePoint = tail = 0;
        }
        dataQueue.set(tail++,o);
    }

    public static void scheduleSync(){
        /**
         * 当定时同步任务发生时，存在三种情况
         */
        FileHandler.saveDataToFile(dataQueue.subList(savePoint,tail));
        savePoint = tail;
    }

    public static List<Object> showData(){
        if (tail>showCount){
            return dataQueue.subList(0,showCount);
        }else {
            return ListUtil.toList(dataQueue.subList(dataCount - (showCount-tail),dataCount),dataQueue.subList(0,tail));
        }
    }

    public static int size() {
        return dataQueue.size();
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
