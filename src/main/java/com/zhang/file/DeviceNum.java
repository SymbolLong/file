package com.zhang.file;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DeviceNum {
    public static void main(String[] args) {
        try {
            int size = 0;
            Map<String, String> map1 = new HashMap<>(size);
            LineIterator lineIterator = FileUtils.lineIterator(new File("/Users/zhangsl/Downloads/2000.txt"));
            while (lineIterator.hasNext()){
                String line = lineIterator.next().trim();
                map1.put(line, line);
            }
            Map<String, String> map = new HashMap<>(size);
            int i = 0;
            int j = 0;
            while (i < size) {
                String uuid = random();
                if (map1.containsKey(uuid)){
                    System.out.println("2000已存在");
                    continue;
                }
                if (map.containsKey(uuid)) {
                    j++;
                    System.out.println("第"+j+"次重复");
                    continue;
                } else {
                    map.put(uuid, uuid);
                    i++;
                }
            }
            File file = new File("/Users/zhangsl/Downloads/500.txt");
            for (Map.Entry<String, String> entry : map.entrySet()) {
                FileUtils.write(file, entry.getKey()+"\r\n", "utf-8", true);
            }
        }catch (Exception e){
           e.printStackTrace();
        }

    }

    private static String random() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        String src = "1234567890";
        for (int i = 0; i < 7; ++i) {
            int number = random.nextInt(src.length());
            sb.append(src.charAt(number));
        }
        return "A" + sb.toString();
    }
}
