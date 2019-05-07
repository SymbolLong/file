package com.zhang.file;


import java.util.Scanner;

public class Fibonacci {

    private static Integer[] value;
    private static boolean[] used;

    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        int in = scanner.nextInt();
//        System.out.println(test(6));
        try{
            throw new NullPointerException();
        }catch (NullPointerException e){
            System.out.println("------");
//            e.printStackTrace();
            throw new RuntimeException("something wrong");
        }catch (Exception e){
            System.out.println("++++ ");
            e.printStackTrace();
        }
    }

    public static Integer test1(int n) {
        if (n <= 0) {
            return 0;
        }
        if (n == 1 || n == 2) {
            return 1;
        }
        return test1(n - 1) + test1(n - 2);
    }

    public static Integer test(int n) {
        //防止初始化数组大小不足
        if (n <= 0) {
            return 0;
        }
        if (n == 1 || n == 2) {
            return 1;
        }
        //初始化基本数据
        value = new Integer[n + 1];
        value[0] = 0;
        value[1] = 1;
        value[2] = 1;
        used = new boolean[n + 1];
        used[0] = true;
        used[1] = true;
        used[2] = true;
        return test3(n);
    }

    public static Integer test2(int n) {
        if (!used[n]) {
            Integer result = test2(n - 1) + test2(n - 2);
            value[n] = result;
            used[n] = true;
        }
        return value[n];
    }

    public static int test3(int n) {
        if (!used[n]) {
            for (int i = 3; i <= n; i++) {
                value[i] = test3(i - 1) + test3(i - 2);
                used[i] = true;
            }
        }
        return value[n];
    }


}
