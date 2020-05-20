package com.zhang.file;

import com.alibaba.fastjson.JSONObject;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.util.*;

/**
 * @author zhangsl-877857078@qq.com 2019-04-19 14:33
 */
public class District {

    public static void main(String[] args) {
        String prefix = "/Users/power/Downloads/";
        String src = prefix + "district";
        String mid = prefix + "mid.txt";
        String tar = prefix + "pcd.txt";
        String sql = prefix + "district.sql";
//        filterData(src);
//        writeFile(src, mid);
//        handleFile(mid, tar);
        createSql(tar, sql);
    }

    public static void filterData(String src) {
        try {
            File srcFile = new File(src);
            File[] provinces = srcFile.listFiles();
            for (File province : provinces) {
                if (province.getName().equals(".DS_Store")) {
                    province.deleteOnExit();
                    continue;
                }
                System.out.println(province.getName());
                File[] districts = province.listFiles();
                for (File file : districts) {
                    if (file.getName().equals(".DS_Store")) {
                        file.deleteOnExit();
                        continue;
                    }
                    JSONObject json = readJson(file);
                    if (json == null || json.keySet().size() == 1) {
                        System.out.println(json.getString("html"));
                    }
                }
                Thread.sleep(3000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(String src, String target) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            List<String> codes = new ArrayList<String>();
            File srcFile = new File(src);
            File[] provinces = srcFile.listFiles();
            for (File province : provinces) {
                System.out.println(province.getName());
                File[] districts = province.listFiles();
                for (File file : districts) {
                    JSONObject json = readJson(file);
                    for (Map.Entry<String, Object> entry : json.entrySet()) {
                        if (!entry.getKey().equals("html")) {
                            codes.add(entry.getKey());
                            map.put(entry.getKey(), entry.getValue().toString());
                        }
                    }
                }
                Thread.sleep(3000);
            }
            File tarFile = new File(target);
            Collections.sort(codes);
            for (String code : codes) {
                String data = code + "\t" + map.get(code) + "\r\n";
                FileUtils.write(tarFile, data, "UTF-8", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleFile(String src, String target) {
        try {
            String title = "level\tcode\tname\tcodes\tnames\tparent\tinitial\r\n";
            File srcFile = new File(src);
            File targetFile = new File(target);
//不写入title            FileUtils.write(targetFile, title, "UTF-8", true);
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
                if (!code.endsWith("000000")) {
                    // 仅保留省市区
                    continue;
                }
                code = code.substring(0, 6);
                name = name.replace("�", "X");
                System.out.println(name);
                JSONObject baidu;
                StringBuffer stringBuffer = new StringBuffer();
                if (code.endsWith("0000")) {
                    provinceCode = code;
                    provinceName = name;
                    stringBuffer.append("1\t");// level
                    stringBuffer.append(code + "\t");// code
                    stringBuffer.append(name + "\t"); // name
                    stringBuffer.append("\t"); // codes
                    stringBuffer.append("\t");// names
                    stringBuffer.append("\t");// parent
                    baidu = getBaidu(provinceName);
                } else if (code.endsWith("00")) {
                    cityCode = code;
                    cityName = name;
                    stringBuffer.append("2\t");// level
                    stringBuffer.append(code + "\t");// code
                    stringBuffer.append(name + "\t"); // name
                    stringBuffer.append(provinceCode + "\t"); // codes
                    stringBuffer.append(provinceName + "\t");// names
                    stringBuffer.append(provinceCode + "\t");// parent
                    baidu = getBaidu(provinceName + cityName);
                } else {
                    stringBuffer.append("3\t");// level
                    stringBuffer.append(code + "\t");// code
                    stringBuffer.append(name + "\t"); // name
                    stringBuffer.append(provinceCode + "," + cityCode + "\t"); // codes
                    stringBuffer.append(provinceName + "," + cityName + "\t");// names
                    stringBuffer.append(cityCode + "\t");// parent
                    baidu = getBaidu(provinceName + cityName + name);
                }
//                stringBuffer.append(toHanyuPinyinString(name) + "\t");// pinyin
//                stringBuffer.append(getPinYinHeadChar(name) + "\t");// jianpin
                stringBuffer.append(getPinYinHeadChar(name).substring(0, 1));// initial

//                stringBuffer.append(baidu.getString("lng") + "\t");// lng
//                stringBuffer.append(baidu.getString("lat"));// lat
                stringBuffer.append("\r\n");
                FileUtils.write(targetFile, stringBuffer.toString(), "UTF-8", true);
            }
            String tw = "1\t71000\t台湾省\t\t\t\tT\r\n";
            String hk = "1\t810000\t香港特别行政区\t\t\t\tX\r\n";
            String macao = "1\t820000\t澳门特别行政区\t\t\t\tA";
            FileUtils.write(targetFile, tw, "UTF-8", true);
            FileUtils.write(targetFile, hk, "UTF-8", true);
            FileUtils.write(targetFile, macao, "UTF-8", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject readJson(File file) {
        try {
            LineIterator lineIterator = FileUtils.lineIterator(file);
            StringBuffer stringBuffer = new StringBuffer();
            while (lineIterator.hasNext()) {
                stringBuffer.append(lineIterator.next());
            }
            return JSONObject.parseObject(stringBuffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void createSql(String src, String target) {
        try {
            File srcFile = new File(src);
            File targetFile = new File(target);
            LineIterator lineIterator = FileUtils.lineIterator(srcFile);
            while (lineIterator.hasNext()) {
                String line = lineIterator.next();
                String[] array = line.split("\t");
                StringBuffer stringBuffer = new StringBuffer("INSERT INTO `District`(`level`, `code`, `name`, `codes`, `names`, `parent`, `initial`) VALUES (");
                for (int i = 0; i < array.length; i++) {
                    if (i == 0) {
                        stringBuffer.append(array[i]);
                    } else {
                        stringBuffer.append("'" + array[i] + "'");
                    }
                    if (i != array.length - 1) {
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
            if (input.endsWith("经济技术开发区")) {
                input = input.replace("经济技术开发区", "");
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

//        HttpClient client = new HttpClient();
//        client.getHttpConnectionManager().getParams().setConnectionTimeout(2000);
//        client.getHttpConnectionManager().getParams().setSoTimeout(2000);
//        String res = null;
//        // Create a method instance.
//        GetMethod method = null;
//        try {
//            Thread.sleep(100);
//            String url = "http://api.map.baidu.com/geocoder/v2/?output=json&ak=74b0639e2dd539200d6b937b9422ea1d&address=" + URLEncoder.encode(name, "UTF-8");
//            method = new GetMethod(url);
//            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
//            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
//            // Execute the method.
//            int statusCode = client.executeMethod(method);
//            if (statusCode == HttpStatus.SC_OK) {
//                BufferedReader reader;
//                reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
//                StringBuffer stringBuffer = new StringBuffer();
//                String str = "";
//                while ((str = reader.readLine()) != null) {
//                    stringBuffer.append(str);
//                }
//                res = stringBuffer.toString();
//            }
//            System.out.println(name + ": " + res);
//            JSONObject result = JSON.parseObject(res).getJSONObject("result");
//            return result.getJSONObject("location");
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (method != null) {
//                method.releaseConnection();
//            }
//        }
        JSONObject result = new JSONObject();
        result.put("lng", "未知");
        result.put("lat", "未知");
        return result;


    }
}
