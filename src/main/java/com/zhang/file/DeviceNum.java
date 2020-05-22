package com.zhang.file;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DeviceNum {
    public static void main(String[] args) {
        int size = 0;
        Map<String, String> map = new HashMap<>(size);
        int i = 0;
        int j = 0;
        while (i < size) {
            String uuid = random();
            if (map.containsKey(uuid)) {
                j++;
                System.out.println("第"+j+"次重复");
                continue;
            } else {
                map.put(uuid, uuid);
                i++;
            }
        }
        try {
            File file = new File("/Users/zhangsl/Downloads/2000.txt");
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
