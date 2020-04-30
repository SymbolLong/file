//package com.zhang.file;
//
//import sun.awt.windows.ThemeReader;
//
//import java.util.concurrent.locks.Condition;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantLock;
//
//public class ThreadTest {
//
//    public static void main(String[] args) {
//
//        final MyLoop loop = new MyLoop();
//        new Thread("A") {
//            @Override
//            public void run() {
//                for (int i = 0; i < 10; i++) {
//                    loop.loop1(i);
//                }
//            }
//        }.start();
//
//        new Thread("B") {
//            @Override
//            public void run() {
//                for (int i = 0; i < 10; i++) {
//                    loop.loop2(i);
//                }
//            }
//        }.start();
//
//        new Thread("C") {
//            @Override
//            public void run() {
//                for (int i = 0; i < 10; i++) {
//                    loop.loop3(i);
//                }
//            }
//        }.start();
//
//    }
//}
//
//class MyLoop {
//
//    private int current = 1;
//
//    public synchronized void loop1(int i) {
//
//        try {
//            while (current != 1) {
//                this.wait();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(Thread.currentThread().getName() + "-" + i);
//        current = 2;
//        this.notifyAll();
//    }
//
//    public synchronized void loop2(int i) {
//        try {
//            while (current != 2) {
//                this.wait();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(Thread.currentThread().getName() + "-" + i);
//        current = 3;
//        this.notifyAll();
//    }
//
//    public synchronized void loop3(int i) {
//        try {
//            while (current != 3) {
//                this.wait();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(Thread.currentThread().getName() + "-" + i);
//        current = 1;
//        this.notifyAll();
//    }
//
//}
//
//
//
//
//
//
//
