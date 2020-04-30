package com.zhang.file;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BarcodeUtil {
    public static void getBarCode(String msg, String path) {
        try {
            File file = new File(path);
            file.createNewFile();
            OutputStream ous = new FileOutputStream(file);
            if (msg.equals("") || ous == null) {
                return;
            }
            //选择条形码类型(好多类型可供选择)
            Code128Bean bean = new Code128Bean();
            //设置长宽
            final double moduleWidth = 0.2;
            final int resolution = 300;
            bean.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);
            bean.setModuleWidth(moduleWidth);
            bean.doQuietZone(true);
            String format = "image/png";
            // 输出流
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(ous, format,
                    resolution, BufferedImage.TYPE_BYTE_BINARY, false, 0);
            //生成条码
            bean.generateBarcode(canvas, msg);

            canvas.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void getQrCode(String uuid, String num, int width) {
        try {
            // 生成二维码
            Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
//            hints.put(EncodeHintType.MARGIN, 1);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode("http://image.jiudandan.com/mp?uuid=" + uuid + "&num=" + num, BarcodeFormat.QR_CODE, width, width, hints);
            String tmp = "/Users/zhangsl/Downloads/tmp/" + uuid + "-" + num + ".png";
            Path path = FileSystems.getDefault().getPath(tmp);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
            // 二维码有问题，中转 输出图片
//            String filePath = "/Users/zhangsl/Downloads/tmp/" + uuid + "-" + num + ".png";
//            BufferedImage background = resizeImagePng(400, 400, ImageIO.read(new File(tmp)));
//            background = transparentImage(background, 10);
//            ImageIO.write(background, "png", new File(filePath));
            //添加红色框
            overlapImage(tmp, "/Users/zhangsl/Downloads/fg.png", uuid, num, width);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String overlapImage(String bgPath, String fgPath, String uuid, String num, int width) {
        try {
            //设置图片大小
            BufferedImage background = resizeImagePng(width, width, ImageIO.read(new File(bgPath)));
            BufferedImage frontgroud = resizeImagePng(width, width, ImageIO.read(new File(fgPath)));
            System.out.println(bgPath);
            System.out.println(fgPath);
            //在背景图片中添加入需要写入的信息，
            Graphics2D g = background.createGraphics();
            //写入字符
//            AttributedString as = new AttributedString(uuid);
//            int length = uuid.length();
//            g.setColor(Color.red);
//            Font font = new Font("行楷-简", Font.PLAIN, 30);
//            as.addAttribute(TextAttribute.FONT, font, 0, length);
//            g.drawString(as.getIterator(), 25, 73);
//            g.drawString(as.getIterator(), 120, 395);
            //设置为透明覆盖
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
            //在背景图片上相框
            g.drawImage(frontgroud, 0, 0, frontgroud.getWidth(), frontgroud.getHeight(), null);

            g.dispose();
            //输出图片
            if (num == "0") {
                ImageIO.write(background, "png", new File("/Users/zhangsl/Downloads/left/" + uuid + ".png"));
            } else {
                ImageIO.write(background, "png", new File("/Users/zhangsl/Downloads/right/" + uuid + ".png"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static BufferedImage transparentImage(BufferedImage srcImage, int alpha) throws IOException {
        int imgHeight = srcImage.getHeight();//取得图片的长和宽
        int imgWidth = srcImage.getWidth();
        int c = srcImage.getRGB(3, 3);
        //防止越位
        if (alpha < 0) {
            alpha = 0;
        } else if (alpha > 10) {
            alpha = 10;
        }
        BufferedImage tmpImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_4BYTE_ABGR);//新建一个类型支持透明的BufferedImage
        for (int i = 0; i < imgWidth; ++i)//把原图片的内容复制到新的图片，同时把背景设为透明
        {
            for (int j = 0; j < imgHeight; ++j) {
                //把背景设为透明
                if (srcImage.getRGB(i, j) == c) {
                    tmpImg.setRGB(i, j, c & 0x00ffffff);
                }
                //设置透明度
                else {
                    int rgb = tmpImg.getRGB(i, j);
                    rgb = ((alpha * 255 / 10) << 24) | (rgb & 0x00ffffff);
                    tmpImg.setRGB(i, j, rgb);
                }
            }
        }
        return tmpImg;
    }

    public static BufferedImage resizeImagePng(int x, int y, BufferedImage bfi) {
        BufferedImage bufferedImage = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);
        bufferedImage.getGraphics().drawImage(bfi.getScaledInstance(x, y, Image.SCALE_AREA_AVERAGING), 0, 0, null);
        return bufferedImage;
    }

    public static void mainBar(String[] args) {
        try {
            List<String> ids = new ArrayList<String>();
            File hundred = new File("/Users/power/Downloads/5000/5000.txt");
            LineIterator iterator = FileUtils.lineIterator(hundred);
            while (iterator.hasNext()) {
                String id = iterator.nextLine();
                if (id.length() == 15) {
                    ids.add(id);
                }
            }
            int i = 1;
            for (String id : ids) {
                System.out.println("-------------------");
                System.out.println(i + "正在处理:" + id);
                i++;
                String path = "/Users/power/Downloads/5000/barcode/" + id + ".png";
                BarcodeUtil.getBarCode(id, path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        try {
            File file = new File("/Users/zhangsl/Downloads/7.txt");
            LineIterator iterator = FileUtils.lineIterator(file);
            int i = 0;
            while (iterator.hasNext()) {
                System.out.println("正在处理：" + ++i);
                String[] text = iterator.nextLine().split("\t");
                String uuid = text[0];
                String num = "1";
                getQrCode(uuid, num, 400);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
