package com.zhang.file;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;

public class DelJPEG {

    public static void main(String[] args) {
        handleFile();
    }

    public static void handleFile() {
        try {
            File src = new File("/Users/zhangsl/MIJIA_RECORD_VIDEO");
            File[] files = src.listFiles();
            for (File file : files) {
                System.out.println(file.getName());
                if (file.isDirectory()){
                    File[] subFiles = readDirectory(file);
                    for (File subFile : subFiles) {
                        System.out.println(subFile.getName());
                        delJpeg(subFile);
                    }
                }else {
                    delJpeg(file);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File[] readDirectory(File file){
        if (file.isDirectory()){
            return file.listFiles();
        }
        return null;
    }

    public static void delJpeg(File file){
        if (file.getName().endsWith(".jpeg")){
            file.delete();
        }
    }
}
