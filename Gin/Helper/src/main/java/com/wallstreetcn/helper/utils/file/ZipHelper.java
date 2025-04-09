package com.wallstreetcn.helper.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Leif Zhang on 2016/11/25.
 * Email leifzhanggithub@gmail.com
 */

public class ZipHelper {


    public static boolean zipFile(String zipFilePath, String outputDirectory) {
        //创建解压目标目录
        File file = new File(outputDirectory);
        //如果目标目录不存在，则创建
        if (!file.exists()) {
            file.mkdirs();
        }
        //打开压缩文件
        try {
            InputStream inputStream = new FileInputStream(zipFilePath);
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            //读取一个进入点
            ZipEntry zipEntry = zipInputStream.getNextEntry();

            byte[] buffer = new byte[1024];
            //解压时字节计数
            int count = 0;
            //如果进入点为空说明已经遍历完所有压缩包中文件和目录
            while (zipEntry != null) {
                //如果是一个目录
                String name = zipEntry.getName();
                if (name.contains("../")) {
                    throw new Exception("发现不安全的zip文件解压路径！");
                }
                if (zipEntry.isDirectory()) {
                    //String name = zipEntry.getName();
                    //name = name.substring(0, name.length() - 1);
                    file = new File(outputDirectory + File.separator + name);
                    file.mkdir();
                } else {
                    //如果是文件
                    file = new File(outputDirectory + File.separator + name);
                    //创建该文件
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    while ((count = zipInputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, count);
                    }
                    fileOutputStream.close();
                }
                //定位到下一个文件入口
                zipEntry = zipInputStream.getNextEntry();
            }
            zipInputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
