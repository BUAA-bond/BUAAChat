package com.BUAAChat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * @author 西西弗
 * @Description:
 * @date 2023/12/16 11:36
 */
public class test {
    final Object lock = new Object();
    boolean isLogin=true;
    boolean isPaused=false;
    public static void main(String[] args) {
        test test = new test();
        test.run();
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        test.pauseThread();
        //test.resumeThread();
    }
    public  void run(){
        new Thread(()->{
            synchronized (lock) {
                while (isLogin) {
                    try {
                        while (isPaused) {
                            //System.out.println("lock");
                            lock.wait(); // 当 isPaused 为 true 时，线程进入等待状态
                        }
                        System.out.println(isPaused);
                        System.out.println(1);
                        //Message message = receiveMessage();
                        System.out.println(2);
                        //if(message!=null)
                        //handleMessage(message);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    private void pauseThread() {
        System.out.println("============");
        synchronized (lock) {
            isPaused = true; // 设置为暂停状态
            // 其他暂停前的操作可以放在这里
        }
    }
    private void resumeThread() {
        synchronized (lock) {
            isPaused = false; // 设置为运行状态
            lock.notify(); // 唤醒等待中的线程
        }
    }
}
