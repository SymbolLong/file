package com.zhang.file;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;

public class FileLineIterator {

    public static void main(String[] args) {
        String src = "/Users/zhangsl/Downloads/src.txt";
        String pcd = "/Users/zhangsl/Downloads/province.json";
        String target = "/Users/zhangsl/Downloads/target.sql";
        readFile(src, pcd, target);
    }

    public static void readFile(String src, String pcd, String target) {
        try {
            File srcFile = new File(src);
            File pcdFile = new File(pcd);
            File targetFile = new File(target);
            JSONArray prvinces = JSONArray.parseArray(FileUtils.readFileToString(pcdFile, "utf-8"));

            LineIterator lineIterator = FileUtils.lineIterator(srcFile);
            while (lineIterator.hasNext()) {
                String line = lineIterator.next();
                String[] array = line.split("\t");
                String level = array[4].trim();
                if ("三".equals(level)) {
                    continue;
                }

                String province = array[1].replace("省", "").replace("市", "").replace("维吾尔", "").replace("回族", "").replace("自治区", "").trim();
                String city = array[2].trim();
                String district = array[3].trim();
//                for (int i = 0; i < prvinces.size(); i++) {
//                    JSONObject first = prvinces.getJSONObject(i);
//                    if (first.getString("name").equalsIgnoreCase(province)) {
//                        JSONArray cities = first.getJSONArray("city");
//                        boolean exist = false;
//                        for (int j = 0; j < cities.size(); j++) {
//                            JSONObject second = cities.getJSONObject(j);
//                            if (second.getString("name").equalsIgnoreCase(city)) {
//                                JSONArray districts = second.getJSONArray("area");
//                                for (int k = 0; k < districts.size(); k++) {
//                                    String third = districts.getString(k);
//                                    if (third.equals(district)) {
//                                        break;
//                                    }
//                                }
//                                exist = true;
//                                break;
//                            }
//                        }
//                        if (!exist) {
//                            System.out.println(province + city + "不存在！！！");
//                        }
//                        break;
//                    }
//                }

                 StringBuffer stringBuffer = new StringBuffer();
                 stringBuffer.append("UPDATE districts d");
                 stringBuffer.append(" SET d.districtLevel =");
                 if ("一".equals(level)){
                 stringBuffer.append(" 'FIRST'");
                 }else{
                 stringBuffer.append(" 'SECOND'");
                 }
                 stringBuffer.append(" WHERE d.NAME =");
                 stringBuffer.append(" '"+district+"'");
                 stringBuffer.append(" AND d.provinceId =");
                 stringBuffer.append(" (SELECT id FROM provinces p");
                 stringBuffer.append(" WHERE p.NAME = '"+province+"') ");
                 stringBuffer.append(" AND d.cityId =");
                 stringBuffer.append("(SELECT id FROM cities c WHERE c.NAME = '"+city+"' );");
                 stringBuffer.append(";\r\n");
                 FileUtils.write(targetFile, stringBuffer.toString(),"utf-8", true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
