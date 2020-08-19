package com.zhang.file;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.xml.internal.ws.util.StringUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class Archive {

    public static void main(String[] args) {

        //1 处理数据11-12
        String src = "/Users/power/Downloads/aiche/21.txt";
        String target = "/Users/power/Downloads/aiche/22.txt";
//        handleData(src, target);
        //2 处理抵扣项目12-13
        src = "/Users/power/Downloads/aiche/22.txt";
        target = "/Users/power/Downloads/aiche/23.txt";
//        handleDeducts(src, target);
        // 3 处理门店13-14
        src = "/Users/power/Downloads/aiche/23.txt";
        target = "/Users/power/Downloads/aiche/24.txt";
//        handleShop(src, target);
        // 3 生成sql14-15
        src = "/Users/power/Downloads/aiche/24.txt";
        target = "/Users/power/Downloads/aiche/25.sql";
        generateSQL(src, target);

    }

    public static void generateSQL(String src, String target) {
        try {
            File srcFile = new File(src);
            File targetFile = new File(target);
            LineIterator lineIterator = FileUtils.lineIterator(srcFile);
            while (lineIterator.hasNext()) {
                String line = lineIterator.next();
                String[] array = line.split("\t");
                StringBuffer sb = new StringBuffer();
                sb.append("INSERT INTO `Archive` (");
                sb.append("`createTime`,");
                sb.append("`updateTime`,");
                sb.append("`remark`,");
                sb.append("`shopId`,");//3
                sb.append("`shopName`,");
                sb.append("`taxNum`,");
                sb.append("`invoiceAddress`,");
                sb.append("`invoiceBank`,");
                sb.append("`invoiceCardNum`,");
                sb.append("`invoiceMobile`,");
                sb.append("`invoiceName`,");
                sb.append("`brandId`,");//11
                sb.append("`brand`,");
                sb.append("`departmentId`,");//13
                sb.append("`department`,");
                sb.append("`deducts`, ");
                sb.append("`creator`,");
                sb.append("`audit`,");
                sb.append("`auditTime`,");
                sb.append("`checker`,");
                sb.append("`checkTime`,");
                sb.append("`number`,");
                sb.append("`fresh`,");//22
                sb.append("`payType`,");
                sb.append("`startTime`,");//24
                sb.append("`endTime`,");//25
                sb.append("`delayStart`,");//26
                sb.append("`delayPart`,");
                sb.append("`max`,");//28
                sb.append("`salePrice`,");//29
                sb.append("`totalAmount`,");//30
                sb.append("`qualitySeq`,");//31
                sb.append("`maintainSeq`,");//32
                sb.append("`name`,");
                sb.append("`idNum`,");
                sb.append("`mobile`,");
                sb.append("`address`,");
                sb.append("`vin`,");
                sb.append("`engineNum`,");
                sb.append("`carNum`,");
                sb.append("`categoryId`,");//40
                sb.append("`categoryName`,");
                sb.append("`displacement`,");
                sb.append("`buyDate`,");
                sb.append("`factoryStart`,");
                sb.append("`factoryEnd`,");
                sb.append("`usedMonth`,");
                sb.append("`usedMileage`,");
                sb.append("`totalMileage`,");
                sb.append("`belong`,");
                sb.append("`emergency`,");//50

                sb.append("`enabled`,");//51
                sb.append("`aiCheStatus`,");
                sb.append("`balance`,");//53
                sb.append("`bank`,");
                sb.append("`cardNum`,");
                sb.append("`firstMaintain`,");
                sb.append("`invoiceNum`,");
                sb.append("`invoiceStatus`,");
                sb.append("`sendTime`");
                sb.append(") VALUES (");


                for (int i = 0; i < array.length; i++) {
                    boolean flag = i == 3 || i == 11
                            || i == 13 || i == 22
                            || i == 28 || i == 29
                            || i == 30 || i == 31
                            || i == 32 || i == 40
                            || i == 50 || i == 51
                            || i == 53;
                    if (flag) {
                        sb.append(array[i] + ",");
                    } else if (i == 24 || i == 25 || i == 26) {
                        sb.append("'" + array[i].substring(0, 10) + "',");
                    } else {
                        String value = array[i];
                        sb.append("'" + value + "',");
                    }
                }

                sb.append("null);\r\n");
                FileUtils.write(targetFile, sb.toString(), "utf-8", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleShop(String src, String target) {
        try {
            Map<String, JSONObject> shopMap = getShopMap();
            Map<String, Integer> depMap = getDepMap();
            Map<String, Integer> categoryMap = getCategoryMap();
            File srcFile = new File(src);
            File targetFile = new File(target);
            LineIterator lineIterator = FileUtils.lineIterator(srcFile);
            while (lineIterator.hasNext()) {
                String line = lineIterator.next();
                if (line.startsWith("创建时间")) {
                    continue;
                }
                String[] array = line.split("\t");
                String shopName = array[2].substring(2, 5);
                JSONObject shopInfo = shopMap.get(shopName);
//                sb.append("`createTime`,");
                FileUtils.write(targetFile, array[0] + "\t", "utf-8", true);
//                sb.append("`updateTime`,");
                FileUtils.write(targetFile, array[0] + "\t", "utf-8", true);
//                sb.append("`remark`,");
                FileUtils.write(targetFile, array[1] + "\t", "utf-8", true);
//                sb.append("`shopId`,");
                FileUtils.write(targetFile, shopInfo.getLong("id") + "\t", "utf-8", true);
//                sb.append("`shopName`,");
                FileUtils.write(targetFile, shopName + "\t", "utf-8", true);
//                sb.append("`taxNum`,");
                FileUtils.write(targetFile, shopInfo.getString("taxNum") + "\t", "utf-8", true);
//                sb.append("`invoiceAddress`,");
                FileUtils.write(targetFile, shopInfo.getString("invoiceAddress") + "\t", "utf-8", true);
//                sb.append("`invoiceBank`,");
                FileUtils.write(targetFile, shopInfo.getString("invoiceBank") + "\t", "utf-8", true);
//                sb.append("`invoiceCardNum`,");
                FileUtils.write(targetFile, shopInfo.getString("invoiceCardNum") + "\t", "utf-8", true);
//                sb.append("`invoiceMobile`,");
                FileUtils.write(targetFile, shopInfo.getString("invoiceMobile") + "\t", "utf-8", true);
//                sb.append("`invoiceName`,");
                FileUtils.write(targetFile, shopInfo.getString("invoiceName") + "\t", "utf-8", true);
//                sb.append("`brandId`,");
                FileUtils.write(targetFile, shopInfo.getLong("categoryId") + "\t", "utf-8", true);
//                sb.append("`brand`,");
                FileUtils.write(targetFile, shopInfo.getString("categoryName") + "\t", "utf-8", true);
//                sb.append("`departmentId`,");
                String department = array[3];
                FileUtils.write(targetFile, depMap.get(department) + "\t", "utf-8", true);
//                sb.append("`department`,");
                FileUtils.write(targetFile, department + "\t", "utf-8", true);
//                sb.append("`deducts`, ");
                FileUtils.write(targetFile, array[4] + "\t", "utf-8", true);
//                sb.append("`creator`,");
                FileUtils.write(targetFile, array[5] + "\t", "utf-8", true);
//                sb.append("`audit`,");
                FileUtils.write(targetFile, array[6] + "\t", "utf-8", true);
//                sb.append("`auditTime`,");
                FileUtils.write(targetFile, array[7] + "\t", "utf-8", true);
//                sb.append("`checker`,");
                FileUtils.write(targetFile, array[8] + "\t", "utf-8", true);
//                sb.append("`checkTime`,");
                FileUtils.write(targetFile, array[9] + "\t", "utf-8", true);
//                sb.append("`number`,");
                FileUtils.write(targetFile, array[10] + "\t", "utf-8", true);
//                sb.append("`fresh`,");
                int fresh = array[11] == "新增" ? 1 : 0;
                FileUtils.write(targetFile, fresh + "\t", "utf-8", true);
//                sb.append("`payType`,");
                FileUtils.write(targetFile, "SHOP\t", "utf-8", true);
//                sb.append("`startTime`,");
                FileUtils.write(targetFile, array[12] + "\t", "utf-8", true);
//                sb.append("`endTime`,");
                FileUtils.write(targetFile, array[13] + "\t", "utf-8", true);
//                sb.append("`delayStart`,");
                FileUtils.write(targetFile, array[14] + "\t", "utf-8", true);
//                sb.append("`delayPart`,");
                String delayPart = array[15] == "三大件质保" ? "PART" : "CAR";
                FileUtils.write(targetFile, delayPart + "\t", "utf-8", true);
//                sb.append("`max`,");
                FileUtils.write(targetFile, array[16] + "\t", "utf-8", true);
//                sb.append("`salePrice`,");
                FileUtils.write(targetFile, array[17] + "\t", "utf-8", true);
//                sb.append("`totalAmount`,");
                FileUtils.write(targetFile, array[18] + "\t", "utf-8", true);
//                sb.append("`qualitySeq`,");
                FileUtils.write(targetFile, "0\t", "utf-8", true);
//                sb.append("`maintainSeq`,");
                FileUtils.write(targetFile, "0\t", "utf-8", true);
//                sb.append("`name`,");
                FileUtils.write(targetFile, array[19] + "\t", "utf-8", true);
//                sb.append("`idNum`,");
                FileUtils.write(targetFile, array[20] + "\t", "utf-8", true);
//                sb.append("`mobile`,");
                FileUtils.write(targetFile, array[21] + "\t", "utf-8", true);
//                sb.append("`address`,");
                FileUtils.write(targetFile, array[22] + "\t", "utf-8", true);
//                sb.append("`vin`,");
                FileUtils.write(targetFile, array[23] + "\t", "utf-8", true);
//                sb.append("`engineNum`,");
                FileUtils.write(targetFile, array[24] + "\t", "utf-8", true);
//                sb.append("`carNum`,");
                FileUtils.write(targetFile, array[25] + "\t", "utf-8", true);
//                sb.append("`categoryId`,");
                String categoryName = array[26];
                FileUtils.write(targetFile, categoryMap.get(categoryName) + "\t", "utf-8", true);
//                sb.append("`categoryName`,");
                FileUtils.write(targetFile, categoryName + "\t", "utf-8", true);
//                sb.append("`displacement`,");
                FileUtils.write(targetFile, array[27] + "\t", "utf-8", true);
//                sb.append("`buyDate`,");
                FileUtils.write(targetFile, array[28] + "\t", "utf-8", true);
//                sb.append("`factoryStart`,");
                FileUtils.write(targetFile, array[29] + "\t", "utf-8", true);
//                sb.append("`factoryEnd`,");
                FileUtils.write(targetFile, array[30] + "\t", "utf-8", true);
//                sb.append("`usedMonth`,");
                FileUtils.write(targetFile, array[31] + "\t", "utf-8", true);
//                sb.append("`usedMileage`,");
                FileUtils.write(targetFile, array[32] + "\t", "utf-8", true);
//                sb.append("`totalMileage`,");
                FileUtils.write(targetFile, array[32] + "\t", "utf-8", true);

//                sb.append("`belong`,");
                FileUtils.write(targetFile, "SHOP\t", "utf-8", true);
//                sb.append("`emergency`,");
                FileUtils.write(targetFile, "0\t", "utf-8", true);
//                sb.append("`enabled`,");
                FileUtils.write(targetFile, "1\t", "utf-8", true);
//                sb.append("`aiCheStatus`,");
                FileUtils.write(targetFile, "FINISH\t", "utf-8", true);
//                sb.append("`balance`,");
                FileUtils.write(targetFile, "0\t", "utf-8", true);
//                sb.append("`bank`,");
                FileUtils.write(targetFile, "\t", "utf-8", true);
//                sb.append("`cardNum`,");
                FileUtils.write(targetFile, "\t", "utf-8", true);
//                sb.append("`firstMaintain`,");
                FileUtils.write(targetFile, "\t", "utf-8", true);
//                sb.append("`invoiceNum`,");
                FileUtils.write(targetFile, "\t", "utf-8", true);
//                sb.append("`invoiceStatus`,");
                FileUtils.write(targetFile, "APPLY\t", "utf-8", true);
//                sb.append("`sendTime`");
                FileUtils.write(targetFile, "\t", "utf-8", true);
                FileUtils.write(targetFile, "\r\n", "utf-8", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleDeducts(String src, String target) {
        try {
            Map<String, Integer> deducts = new HashMap<String, Integer>();
            deducts.put("保养类", 1);
            deducts.put("余额类", 2);
            deducts.put("赠送类", 3);
            File srcFile = new File(src);
            File targetFile = new File(target);
            File rubbish = new File("/Users/power/Downloads/aiche/999.txt");
            LineIterator lineIterator = FileUtils.lineIterator(srcFile);
            while (lineIterator.hasNext()) {
                String line = lineIterator.next();
                if (line.startsWith("创建时间")) {
                    FileUtils.write(targetFile, line + "\r\n", "utf-8", true);
                    continue;
                }

                StringBuffer sb = new StringBuffer();
                String[] array = line.split("\t");
                BigDecimal price = new BigDecimal(array[18]);
                BigDecimal total = new BigDecimal(array[19]);
                BigDecimal deduct = BigDecimal.ZERO;


                for (int i = 0; i < array.length; i++) {
                    String s = array[i];
                    if (i == 4) {
                        if (s.trim().length() > 0) {
                            String name = s.substring(0, 3);
                            String amount = s.substring(3);
                            if (!amount.equals("")) {
                                JSONObject tmp = new JSONObject();
                                tmp.put("id", deducts.get(name));
                                tmp.put("name", name);
                                tmp.put("num", "");
                                tmp.put("amount", amount);
                                deduct = deduct.add(new BigDecimal(amount));
                                sb.append("[" + tmp.toJSONString());
                            } else {
                                sb.append("[");
                            }
                        } else {
                            sb.append("[");
                        }
                    } else if (i == 5) {
                        if (s.trim().length() > 0) {
                            String name = s.substring(0, 3);
                            String amount = s.substring(3);
                            if (!amount.equals("")) {
                                JSONObject tmp = new JSONObject();
                                tmp.put("id", deducts.get(name));
                                tmp.put("name", name);
                                tmp.put("num", "");
                                tmp.put("amount", amount);
                                deduct = deduct.add(new BigDecimal(amount));
                                sb.append("," + tmp.toJSONString());
                            }
                        }
                        sb.append("]\t");
                    } else {
                        sb.append(s + "\t");
                    }
                }
                BigDecimal check = price.subtract(deduct);
                if (check.compareTo(total) != 0){
                    FileUtils.write(rubbish, line + "\r\n", "utf-8", true);
                    continue;
                }
                FileUtils.write(targetFile, sb.toString()+"\r\n", "utf-8", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleData(String src, String target) {
        try {
            File srcFile = new File(src);
            File targetFile = new File(target);
            File rubbish = new File("/Users/power/Downloads/aiche/9999.txt");
            int count = 0;
            LineIterator lineIterator = FileUtils.lineIterator(srcFile);
            while (lineIterator.hasNext()) {
                String line = lineIterator.next().replace("'", "");
                String[] array = line.split("\t");
                String carNum = array[26];//
                if (carNum.equals("0")) {
                    // 垃圾数据
                    FileUtils.write(rubbish, line + "\r\n", "utf-8", true);
                    continue;
                }
                String category = array[27];
                if (category.endsWith(" ")) {
                    category = category.substring(0, category.length() - 2);
                }
                if (category.equals("Axel") || category.equals("Axela") || category.equals("Axela 三厢") || category.equals("Axela 1.5L") || category.equals("Axela 2.0L")) {
                    category = "AXELA";
                } else if (category.equals("CX-") || category.equals("CX-5 2.0L") || category.equals("CX-5 2.5L")) {
                    category = "CX-5";
                } else if (category.equals("CX-30  2.0L")) {
                    category = "CX-30";
                } else if (category.startsWith("次世代")) {
                    category = "次世代";
                } else if (category.equals("-")) {
                    FileUtils.write(rubbish, line + "\r\n", "utf-8", true);
                    continue;
                }
                for (int i = 0; i < array.length; i++) {
                    if (i == 27) {
                        FileUtils.write(targetFile, category + "\t", "utf-8", true);
                        continue;
                    }
                    String s = array[i];
                    if (s.contains("/")) {
                        s = s.replace(" 0:00", "");
                        String[] dates = s.split("/");
                        String string = "20" + dates[2] + "-" + dates[0] + "-" + dates[1];
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = sdf.parse(string);
                        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String format = sdf.format(date);
                        FileUtils.write(targetFile, format + "\t", "utf-8", true);
                    } else {
                        FileUtils.write(targetFile, s + "\t", "utf-8", true);
                    }
                }
                FileUtils.write(targetFile, "\r\n", "utf-8", true);
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static Map<String, JSONObject> getShopMap() {
        Map<String, JSONObject> map = new HashMap<String, JSONObject>();
        JSONObject first = new JSONObject();
        first.put("id", 1);
        first.put("name", "古田店");
        first.put("invoiceAddress", "杭州市古墩路669号");
        first.put("invoiceBank", "建行杭州市绍兴路支行");
        first.put("invoiceCardNum", "33001616580053000082");
        first.put("invoiceMobile", "88948016");
        first.put("taxNum", "91330106747183378A");
        first.put("invoiceName", "杭州古田汽车销售服务有限公司");
        first.put("categoryId", 30);
        first.put("categoryName", "广本");
        map.put("古田店", first);
        JSONObject second = new JSONObject();
        second.put("id", 2);
        second.put("name", "骏田店");
        second.put("invoiceAddress", "杭州市西湖区古墩路669号");
        second.put("invoiceBank", "建行杭州市之江支行");
        second.put("invoiceCardNum", "33001616527053000179");
        second.put("invoiceMobile", "0571-88946009");
        second.put("taxNum", "913301066798759450");
        second.put("invoiceName", "杭州骏田汽车有限公司");
        second.put("categoryId", 54);
        second.put("categoryName", "长马");
        map.put("骏田店", second);
        JSONObject third = new JSONObject();
        third.put("id", 3);
        third.put("name", "骏兴店");
        third.put("invoiceAddress", "杭州市拱墅区东教路611号");
        third.put("invoiceBank", "杭州银行市延中支行");
        third.put("invoiceCardNum", "78208100232909");
        third.put("invoiceMobile", "28007607");
        third.put("taxNum", "9133010566521525XC");
        third.put("invoiceName", "杭州骏兴汽车有限公司");
        third.put("categoryId", 54);
        third.put("categoryName", "长马");
        map.put("骏兴店", third);
        JSONObject forth = new JSONObject();
        forth.put("id", 4);
        forth.put("name", "裕菲店");
        forth.put("invoiceAddress", "萧山区宁围街道宁税社区");
        forth.put("invoiceBank", "浙江萧山农村商业银行宁围支行");
        forth.put("invoiceCardNum", "201000010188893");
        forth.put("invoiceMobile", "22812222");
        forth.put("taxNum", "91330109770842423D");
        forth.put("invoiceName", "浙江裕菲汽车销售服务有限公司");
        forth.put("categoryId", 54);
        forth.put("categoryName", "长马");
        map.put("裕菲店", forth);
        return map;
    }

    private static Map<String, Integer> getDepMap() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("销售部", 1);
        map.put("售后部", 2);
        return map;
    }

    private static Map<String, Integer> getCategoryMap() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("雅阁", 31);
        map.put("混动雅阁", 32);
        map.put("缤智", 35);
        map.put("飞度", 36);
        map.put("凌派", 37);
        map.put("冠道", 39);
        map.put("皓影", 40);
        map.put("锋范", 42);
        map.put("AXELA", 55);
        map.put("CX-5", 56);
        map.put("CX-30", 57);
        map.put("次世代", 190);
        map.put("奥德赛", 191);
        return map;
    }
}
