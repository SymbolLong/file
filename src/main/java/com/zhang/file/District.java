package com.zhang.file;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangsl-877857078@qq.com 2019-04-19 14:33
 */
public class District {

    public static void main(String[] args) {
//        handleFile("/Users/zhangsl/Downloads/pcd.txt", "/Users/zhangsl/Downloads/pcd2.txt");
        createSql("/Users/zhangsl/Downloads/pcd2.txt", "/Users/zhangsl/Downloads/pcd.sql");
    }

    public static void createSql(String src, String target) {
        try {
            File srcFile = new File(src);
            File targetFile = new File(target);
            LineIterator lineIterator = FileUtils.lineIterator(srcFile);
            while (lineIterator.hasNext()) {
                String line = lineIterator.next();
                String[] array = line.split("\t");
                StringBuffer stringBuffer = new StringBuffer("INSERT INTO `District`(`code`, `name`, `codes`, `names`, `parent`, `level`, `pinyin`, `jianpin`, `initial`, `longitude`, `latitude`) VALUES (");
                for (int i = 0; i < array.length; i++) {
                    if (i == 5) {
                        stringBuffer.append(array[i]);
                    } else {
                        stringBuffer.append("'" + array[i] + "'");
                    }
                    if (i != array.length - 1){
                        stringBuffer.append(",");
                    }
                }
                stringBuffer.append(");\r\n");
                FileUtils.write(targetFile, stringBuffer.toString(), "UTF-8", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleFile(String src, String target) {
        try {
            String title = "code\tname\tcodes\tnames\tparent\tpinyin\tjianpin\tinitial\tlng\tlat";
            File srcFile = new File(src);
            File targetFile = new File(target);
            FileUtils.write(targetFile, title, "UTF-8", true);
            LineIterator lineIterator = FileUtils.lineIterator(srcFile);
            String provinceCode = null;
            String provinceName = "";
            String cityCode = null;
            String cityName = "";
            while (lineIterator.hasNext()) {

                String line = lineIterator.next();
                String[] array = line.split("\t");
                String code = array[0];
                String name = array[1];

                JSONObject baidu = new JSONObject();
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(code + "\t");
                stringBuffer.append(name + "\t");
                if (code.endsWith("0000")) {
                    provinceCode = code;
                    provinceName = name;
                    stringBuffer.append("\t");
                    stringBuffer.append("\t");
                    stringBuffer.append("\t");
                    stringBuffer.append("1\t");
                    baidu = getBaidu(provinceName);
                } else if (code.endsWith("00")) {
                    cityCode = code;
                    cityName = name;
                    stringBuffer.append(provinceCode + "\t");
                    stringBuffer.append(provinceName + "\t");
                    stringBuffer.append(provinceCode + "\t");
                    stringBuffer.append("2\t");
                    baidu = getBaidu(provinceName + cityName);
                } else {
                    stringBuffer.append(provinceCode + "," + cityCode + "\t");
                    stringBuffer.append(provinceName + "," + cityName + "\t");
                    stringBuffer.append(cityCode + "\t");
                    stringBuffer.append("3\t");
                    baidu = getBaidu(provinceName + cityName + name);
                }
                stringBuffer.append(toHanyuPinyinString(name) + "\t");
                stringBuffer.append(getPinYinHeadChar(name) + "\t");
                stringBuffer.append(getPinYinHeadChar(name).substring(0, 1) + "\t");

                stringBuffer.append(baidu.getString("lng") + "\t");
                stringBuffer.append(baidu.getString("lat"));
                stringBuffer.append("\r\n");
                FileUtils.write(targetFile, stringBuffer.toString(), "UTF-8", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String toHanyuPinyinString(String input) {
        if (input.length() > 2) {
            if (input.endsWith("省")) {
                input = input.replace("省", "");
            }
            if (input.endsWith("市")) {
                input = input.replace("市", "");
            }
            if (input.endsWith("自治县")) {
                input = input.replace("自治县", "");
            }
            if (input.endsWith("自治州")) {
                input = input.replace("自治州", "");
            }
            if (input.endsWith("自治旗")) {
                input = input.replace("自治旗", "");
            }
            if (input.endsWith("县")) {
                input = input.replace("县", "");
            }
            if (input.endsWith("回族区")) {
                input = input.replace("回族区", "");
            }
            if (input.endsWith("自治区")) {
                input = input.replace("自治区", "");
            }
            if (input.endsWith("新区")) {
                input = input.replace("新区", "");
            }
            if (input.endsWith("矿区")) {
                input = input.replace("矿区", "");
            }
            if (input.endsWith("林区")) {
                input = input.replace("林区", "");
            }
            if (input.endsWith("特别行政区")) {
                input = input.replace("特别行政区", "");
            }
            if (input.endsWith("区")) {
                input = input.replace("区", "");
            }
        }
        StringBuilder pinyin = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
            char c = input.charAt(i);
            String[] pinyinArray = null;
            try {
                pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat);
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
            if (pinyinArray != null) {
                pinyin.append(pinyinArray[0]);
            } else if (c != ' ') {
                pinyin.append(input.charAt(i));
            }
        }
        return pinyin.toString();
    }

    /**
     * 提取每个汉字的首字母
     *
     * @param input
     * @return
     */
    public static String getPinYinHeadChar(String input) {
        List<String> filter = new ArrayList<String>();
        filter.add("省");
        filter.add("市");
        filter.add("自治县");
        filter.add("自治州");
        filter.add("自治旗");
        filter.add("县");
        filter.add("回族区");
        filter.add("自治区");
        filter.add("新区");
        filter.add("矿区");
        filter.add("林区");
        filter.add("特别行政区");
        filter.add("区");
        if (input.length() > 2) {
            for (int i = 0; i < filter.size(); i++) {
                String key = filter.get(i);
                if (input.endsWith(key)) {
                    input = input.replace(key, "");
                }
            }
        }

        String convert = "";
        for (int i = 0; i < input.length(); i++) {
            char word = input.charAt(i);
            //提取汉字的首字母
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert.toUpperCase();
    }

    public static JSONObject getBaidu(String name) {

        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(2000);
        client.getHttpConnectionManager().getParams().setSoTimeout(2000);
        String res = null;
        // Create a method instance.
        GetMethod method = null;
        try {
            Thread.sleep(100);
            String url = "http://api.map.baidu.com/geocoder/v2/?output=json&ak=74b0639e2dd539200d6b937b9422ea1d&address=" + URLEncoder.encode(name, "UTF-8");
            method = new GetMethod(url);
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
            // Execute the method.
            int statusCode = client.executeMethod(method);
            if (statusCode == HttpStatus.SC_OK) {
                BufferedReader reader;
                reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                StringBuffer stringBuffer = new StringBuffer();
                String str = "";
                while ((str = reader.readLine()) != null) {
                    stringBuffer.append(str);
                }
                res = stringBuffer.toString();
            }
            System.out.println(name + ": " + res);
            JSONObject result = JSON.parseObject(res).getJSONObject("result");
            return result.getJSONObject("location");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }
        JSONObject result = new JSONObject();
        result.put("lng", "未知");
        result.put("lat", "未知");
        return result;


    }
}
