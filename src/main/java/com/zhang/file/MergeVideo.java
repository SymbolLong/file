package com.zhang.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author zhangsl-877857078@qq.com 2019-05-08 13:38
 */
public class MergeVideo {

    public static void main(String[] args) {
        union(new File("/Users/zhangsl/Downloads/test"), "/Users/zhangsl/Downloads/test/abc.mp4");
//        splitFile("/Users/zhangsl/Downloads/小金子与鸽子.mp4");
    }


    public static void splitFile(String path) {
        try {
            File file = new File(path);
            byte[] buffers = new byte[1024*1024];
            FileInputStream reader = new FileInputStream(file);
            int i = 0;
            File result = null;
            FileOutputStream writer = null;
            while (reader.read(buffers) != -1) {
                i++;
                result = new File("/Users/zhangsl/Downloads/小金子与鸽子-"+i);
                writer = new FileOutputStream(result);
                writer.write(buffers);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void files(String dir) {
        try {
            File src = new File(dir);
            List<File> files = Arrays.asList(src.listFiles());
            Collections.sort(files);
            for (File file : files) {
                if (file.getName().endsWith("jpeg")) {
                    union(file, "/Users/zhangsl/Downloads/" + file.getName() + ".mp4");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void union(File dir, String toFilePath) {
        try {
            List<File> files = Arrays.asList(dir.listFiles());
            Collections.sort(files);
            File result = new File(toFilePath);
            FileOutputStream writer = new FileOutputStream(result);
            byte[] buffers = new byte[1024];
            for (File file : files) {
                if (file.getName().startsWith("小")) {
                    System.out.println(file.getName());
                    FileInputStream reader = new FileInputStream(file);
                    while (reader.read(buffers) != -1) {
                        writer.write(buffers);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
