package com.zhang.file;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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

import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author zhangsl-877857078@qq.com 2019-04-19 14:33
 */
public class FbDistrict {

    public static void main(String[] args) {
        try {
            String prefix = "/Users/power/Downloads/";
            String src = prefix+"area.json";
            File target = new File(prefix+"area.sql");
            File file = new File(src);
            FileReader fileReader = new FileReader(file);
            Reader reader = new InputStreamReader(new FileInputStream(file),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            String sql = "INSERT INTO `FbDistrict`(`createTime`, `enabled`, `operator`, `operatorId`, `remark`, `updateTime`, `name`, `code`, `parent`) VALUES ('2020-07-27 14:26:06', b'1', '1', 1, '付呗数据', '2020-07-27 14:26:14', '";
            String jsonStr = sb.toString();
            JSONObject json = JSON.parseObject(jsonStr);
            JSONArray array = json.getJSONArray("data");
            for (int i = 0; i < array.size(); i++) {
                JSONObject province = array.getJSONObject(i);
                String pName = province.getString("name");
                String pCode = province.getString("code");
                String pParentCode = "";
                FileUtils.write(target, sql+ pName +"','"+pCode+"','"+pParentCode+"');\r\n", "UTF-8", true);
                JSONArray cities = province.getJSONArray("children");
                for (int j = 0; j < cities.size(); j++) {
                    JSONObject city = cities.getJSONObject(j);
                    String cName = city.getString("name");
                    String cCode = city.getString("code");
                    String cParentCode = city.getString("pcode");
                    FileUtils.write(target, sql+ cName +"','"+cCode+"','"+cParentCode+"');\r\n", "UTF-8", true);
                    JSONArray districts = city.getJSONArray("children");
                    for (int k = 0; k < districts.size(); k++) {
                        JSONObject district = districts.getJSONObject(k);
                        String dName = district.getString("name");
                        String dCode = district.getString("code");
                        String dParentCode = district.getString("pcode");
                        FileUtils.write(target, sql+ dName +"','"+dCode+"','"+dParentCode+"');\r\n", "UTF-8", true);
                    }
                }
            }
            System.out.println(array.size());
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public static JSONArray getJSON() {
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(20000);
        client.getHttpConnectionManager().getParams().setSoTimeout(200000);
        String res = null;
        // Create a method instance.
        GetMethod method = null;
        try {
            String url = "http://lifecircle-ark-public.oss-cn-hangzhou.aliyuncs.com/openapi/areas.json";
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
            return JSON.parseObject(res).getJSONArray("data");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }
        return new JSONArray();
    }
}
