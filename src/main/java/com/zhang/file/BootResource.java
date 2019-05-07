package com.zhang.file;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 生成 repository service impl 文件
 *
 * @author zhangsl-877857078@qq.com 2019-05-07 10:20
 */
public class BootResource {

    public static String baseDir = "/Users/zhangsl/Work/hp_modules/manage/src/main/java/";
    public static String encoding = StandardCharsets.UTF_8.displayName();
    public static String result = "\t import com.hpkj.common.result.ApiResult;\r\n";

    public static void main(String[] args) {
        String path = "com/hpkj/manager";
        List<String> classes = new ArrayList<String>();
        classes.add("Field");
//        create(path, classes);
        delete(path, classes);
    }

    public static void create(String path, List<String> classes) {
        File file = new File(baseDir + path);
        if (!file.exists()) {
            file.mkdir();
        }

        createRepository(path, classes);
        createService(path, classes);
        createServiceImpl(path, classes);
    }


    public static void createRepository(String path, List<String> classes) {

        try {
            String pack = path.replace("/", ".");
            File repository = new File(baseDir + path + "/repository");
            if (!repository.exists()) {
                repository.mkdir();
            }
            for (String clazz : classes) {
                File file = new File(baseDir + path + "/repository/" + clazz + "Repository.java");
                FileUtils.writeStringToFile(file, "package " + pack + ".repository;\r\n\r\n", encoding, true);

                FileUtils.writeStringToFile(file, "import " + pack + ".entity." + clazz + ";\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "import org.springframework.data.jpa.repository.JpaRepository;\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "import org.springframework.stereotype.Repository;\r\n\r\n", encoding, true);

                FileUtils.writeStringToFile(file, "/**\r\n", encoding, true);
                FileUtils.writeStringToFile(file, " * " + clazz + "数据访问\r\n", encoding, true);
                FileUtils.writeStringToFile(file, " *\r\n", encoding, true);
                FileUtils.writeStringToFile(file, " * @author zhangsl-877857078@qq.com " + DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + "\r\n", encoding, true);
                FileUtils.writeStringToFile(file, " */\r\n", encoding, true);

                FileUtils.writeStringToFile(file, "@Repository\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "public interface " + clazz + "Repository extends JpaRepository<" + clazz + ", Long> {\r\n\r\n", encoding, true);

                FileUtils.writeStringToFile(file, "}\r\n", encoding, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createService(String path, List<String> classes) {
        try {
            String pack = path.replace("/", ".");
            File service = new File(baseDir + path + "/service");
            if (!service.exists()) {
                service.mkdir();
            }
            for (String clazz : classes) {
                String lower = clazz.toLowerCase();
                File file = new File(baseDir + path + "/service/" + clazz + "Service.java");
                FileUtils.writeStringToFile(file, "package " + pack + ".service;\r\n\r\n", encoding, true);

                FileUtils.writeStringToFile(file, "import " + pack + ".entity." + clazz + ";\r\n", encoding, true);
                FileUtils.writeStringToFile(file, result + "\r\n", encoding, true);

                FileUtils.writeStringToFile(file, "/**\r\n", encoding, true);
                FileUtils.writeStringToFile(file, " * " + clazz + "服务接口\r\n", encoding, true);
                FileUtils.writeStringToFile(file, " *\r\n", encoding, true);
                FileUtils.writeStringToFile(file, " * @author zhangsl-877857078@qq.com " + DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + "\r\n", encoding, true);
                FileUtils.writeStringToFile(file, " */\r\n", encoding, true);

                FileUtils.writeStringToFile(file, "public interface " + clazz + "Service {\r\n\r\n", encoding, true);

                FileUtils.writeStringToFile(file, " \t/**\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t * 保存" + clazz + "对象\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t *\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t * @param " + lower + " 对象\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t * @return " + clazz + "\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t */\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t" + clazz + " save(" + clazz + " " + lower + ");\r\n\r\n", encoding, true);

                FileUtils.writeStringToFile(file, " \t/**\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t * 删除" + clazz + "对象\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t *\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t * @param " + clazz.toLowerCase() + " 对象\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t * @return ApiResult\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t */\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\tApiResult delete(" + clazz + " " + lower + ");\r\n\r\n", encoding, true);

                FileUtils.writeStringToFile(file, "\t/**\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t * 根据id查找" + clazz + "对象\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t *\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t * @param id " + lower + "对象id\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t * @return " + clazz + "\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t */\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t" + clazz + " findById(Long id);\r\n\r\n", encoding, true);

                FileUtils.writeStringToFile(file, "}\r\n", encoding, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createServiceImpl(String path, List<String> classes) {
        try {
            String pack = path.replace("/", ".");
            File impl = new File(baseDir + path + "/service/impl");
            if (!impl.exists()) {
                impl.mkdir();
            }
            for (String clazz : classes) {
                String lower = clazz.toLowerCase();
                File file = new File(baseDir + path + "/service/impl/" + clazz + "ServiceImpl.java");
                FileUtils.writeStringToFile(file, "package " + pack + ".service.impl;\r\n\r\n", encoding, true);

                FileUtils.writeStringToFile(file, result, encoding, true);
                FileUtils.writeStringToFile(file, "import " + pack + ".entity." + clazz + ";\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "import " + pack + ".repository." + clazz + "Repository;\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "import " + pack + ".service." + clazz + "Service;\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "import org.springframework.stereotype.Service;\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "import org.springframework.transaction.annotation.Transactional;\r\n\r\n", encoding, true);

                FileUtils.writeStringToFile(file, "/**\r\n", encoding, true);
                FileUtils.writeStringToFile(file, " * " + clazz + "服务实现类\r\n", encoding, true);
                FileUtils.writeStringToFile(file, " *\r\n", encoding, true);
                FileUtils.writeStringToFile(file, " * @author zhangsl-877857078@qq.com " + DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + "\r\n", encoding, true);
                FileUtils.writeStringToFile(file, " */\r\n", encoding, true);

                FileUtils.writeStringToFile(file, "@Service\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "@Transactional(rollbackFor = Exception.class)\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "public class " + clazz + "ServiceImpl implements " + clazz + "Service {\r\n\r\n", encoding, true);

                FileUtils.writeStringToFile(file, "\tprivate final " + clazz + "Repository " + lower + "Repository;\r\n\r\n", encoding, true);

                FileUtils.writeStringToFile(file, "\tpublic " + clazz + "ServiceImpl(" + clazz + "Repository " + lower + "Repository) {\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t\tthis." + lower + "Repository = " + lower + "Repository;\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t}\r\n\r\n", encoding, true);

                FileUtils.writeStringToFile(file, "\t@Override\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\tpublic " + clazz + " save(" + clazz + " " + lower + ") {\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t\treturn " + lower + "Repository.saveAndFlush(" + lower + ");\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t}\r\n\r\n", encoding, true);

                FileUtils.writeStringToFile(file, "\t@Override\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\tpublic ApiResult delete(" + clazz + " " + lower + ") {\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t\tif (" + lower + "Repository.existsById(" + lower + ".getId())) {\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t\t\t" + lower + "Repository.deleteById(" + lower + ".getId());\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t\t}\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t\treturn ApiResult.success();\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t}\r\n\r\n", encoding, true);

                FileUtils.writeStringToFile(file, "\t@Override\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\tpublic " + clazz + " findById(Long id) {\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t\treturn " + lower + "Repository.findById(id).orElse(null);\r\n", encoding, true);
                FileUtils.writeStringToFile(file, "\t}\r\n", encoding, true);

                FileUtils.writeStringToFile(file, "}\r\n", encoding, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void delete(String path, List<String> classes) {
        for (String clazz : classes) {
            File repository = new File(baseDir + path + "/repository/" + clazz + "Repository.java");
            if (repository.exists()) {
                repository.delete();
            }
            File service = new File(baseDir + path + "/service/" + clazz + "Service.java");
            if (service.exists()) {
                service.delete();
            }
            File impl = new File(baseDir + path + "/service/impl/" + clazz + "ServiceImpl.java");
            if (impl.exists()) {
                impl.delete();
            }
        }
    }

}
