package com.zhang.file;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.util.*;

public class DeviceNum {
    public static void main3(String[] args) {
        try {
            int size = 10;
            Set<String> set = new HashSet<String>(size);
            int i = 0;
            while (i < size) {
                String uuid = random2();
                if (set.contains(uuid)) {
                    continue;
                }
                set.add(uuid);
                i++;
            }
            File file = new File("/Users/zhangsl/Downloads/test.txt");
            for (String uuid : set) {
                FileUtils.write(file, uuid + "\r\n", "utf-8", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String random2() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        String src = "1234567890ACDEFGHJKMNPQRSTWXY";
        for (int i = 0; i < 10; ++i) {
            int number = random.nextInt(src.length());
            sb.append(src.charAt(number));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            int size = 8000;
            File file = new File("/Users/power/Downloads/" + size + ".txt");
            if (!file.exists()){
                file.createNewFile();
            }
            Set<String> list = new HashSet<String>(size);
            LineIterator lineIterator = FileUtils.lineIterator(file);
            while (lineIterator.hasNext()) {
                String line = lineIterator.next().trim();
                list.add(line);
            }
            while (list.size() < size) {
                String uuid = random("B");
                if (list.contains(uuid)) {
                    System.out.println(uuid + "已存在");
                    continue;
                } else {
                    list.add(uuid);
                    FileUtils.write(file, uuid + "\r\n", "utf-8", true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String random(String prefix) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        String src = "1234567890";
        for (int i = 0; i < 7; ++i) {
            int number = random.nextInt(src.length());
            sb.append(src.charAt(number));
        }
        return prefix + sb.toString();
    }
}
