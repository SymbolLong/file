package com.zhang.file;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;

import javax.imageio.*;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 二维码工类
 */
public class QRCodeUtil {

    public static void main(String[] args) {
        getQrCode("http://image.jiudandan.com/mp?uuid=LQ0DFJH9&num=0", 449, 118);
    }

    public static void getQrCode(String contents, int size, int margin) {
        try {

            // 画笔
            BufferedImage bi = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = bi.createGraphics();
            //画出白背景
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, size, size);
            //画出二维码
            // 生成二维码
            Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
            hints.put(EncodeHintType.MARGIN, margin);
            QRCode qrCode = Encoder.encode(contents, ErrorCorrectionLevel.L, hints);
            ByteMatrix matrix = qrCode.getMatrix();
            g.setColor(Color.BLACK);
            int detectCornerSize = 7;
            int multi = 213 / matrix.getWidth();
            for (int x = 0; x < matrix.getWidth(); x++) {
                for (int y = 0; y < matrix.getHeight(); y++) {
                    if (matrix.get(x, y) == 1) {
                        if (x < detectCornerSize && y < detectCornerSize // 左上角
                                || (x < detectCornerSize && y >= matrix.getHeight() - detectCornerSize) // 左下脚
                                || (x >= matrix.getWidth() - detectCornerSize && y < detectCornerSize)) { // 右上角
                            g.setColor(Color.RED);
                        } else {
                            g.setColor(Color.BLACK);
                        }
                        g.fillRect(multi * x + margin + 5, multi * y + 94, multi, multi);
                    }
                }
            }
            // 写入文字
            String uuid = "LQ0DFJH9 ";
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
            AttributedString as = new AttributedString(uuid);
            g.setColor(Color.BLACK);
            as.addAttribute(TextAttribute.SIZE, 24);
            g.drawString(as.getIterator(), 167, 340);
            g.dispose();
//            ImageIO.write(bi, "png", new File("/Users/power/Downloads/tmp.png"));
            // 设置输出分辨率
            byte[] bytes = process(bi, 300);
            bytes2File(bytes, "/Users/power/Downloads", uuid + ".png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //1英寸是2.54厘米
    private static final double INCH_2_CM = 2.54d;

    private static byte[] process(BufferedImage image, int dpi) throws MalformedURLException, IOException {
        for (Iterator<ImageWriter> iw = ImageIO.getImageWritersByFormatName("png"); iw.hasNext(); ) {
            ImageWriter writer = iw.next();
            ImageWriteParam writeParam = writer.getDefaultWriteParam();
            ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
            IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);
            if (metadata.isReadOnly() || !metadata.isStandardMetadataFormatSupported()) {
                continue;
            }
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageOutputStream stream = null;
            try {
                setDPI(metadata, dpi);
                stream = ImageIO.createImageOutputStream(output);
                writer.setOutput(stream);
                writer.write(metadata, new IIOImage(image, null, metadata), writeParam);
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
            return output.toByteArray();
        }

        return null;
    }

    private static void setDPI(IIOMetadata metadata, int dpi) throws IIOInvalidTreeException {
        // for PNG, it's dots per millimeter
        double dotsPerMilli = 1.0 * dpi / 10 / INCH_2_CM;
        IIOMetadataNode horiz = new IIOMetadataNode("HorizontalPixelSize");
        horiz.setAttribute("value", Double.toString(dotsPerMilli));

        IIOMetadataNode vert = new IIOMetadataNode("VerticalPixelSize");
        vert.setAttribute("value", Double.toString(dotsPerMilli));

        IIOMetadataNode dim = new IIOMetadataNode("Dimension");
        dim.appendChild(horiz);
        dim.appendChild(vert);

        IIOMetadataNode root = new IIOMetadataNode("javax_imageio_1.0");
        root.appendChild(dim);

        metadata.mergeTree("javax_imageio_1.0", root);
    }

    //将Byte数组转换成文件
    public static void bytes2File(byte[] bytes, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "/" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
