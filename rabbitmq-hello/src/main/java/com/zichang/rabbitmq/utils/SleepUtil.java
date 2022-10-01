package com.zichang.rabbitmq.utils;

/**
 * author: ZiChangHong
 * create-date: 2022/9/28 21:22
 **/

/**
 * 睡眠工具类
 */
public class SleepUtil {

    //让线程睡多少秒
    public static void sleep(int second){
        try {
            Thread.sleep(1000 * second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
