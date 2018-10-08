package com.runchain.plugins.security;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

public class FileUtil {

    /**
     * 创建文件夹
     *
     * @param strDir 要创建的文件夹名称
     * @return 如果成功true;否则false
     */
    public static boolean mkdir(String strDir) {
        File fileNew = new File(strDir);
        if (!fileNew.exists()) {
            return fileNew.mkdirs();
        } else {
            return true;
        }
    }

    /**
     * 创建文件
     *
     * @param filePath
     * @return
     */
    public static File createFile(String filePath) {
        File file = new File(filePath);
        try {
            file.getParentFile().mkdirs();
            if (!file.exists())
                file.createNewFile();
        } catch (Exception e) {
            Log.e("FileUtil","file create error"+e.getMessage());
        }
        return file;
    }

    /**
     * 写文件
     *
     * @param path
     * @param content
     * @throws IOException
     */
    public static void write(String path, String content) throws IOException {
        Writer fw = new BufferedWriter(new FileWriter(new File(path)));
        fw.write(content);
        fw.close();
    }

    public static String read(String path) {
        String content = null;
        try {
            InputStream inputStream = new FileInputStream(path);
            StringBuffer sb = new StringBuffer();
            int c = 0;
            byte[] bytes = new byte[1024];

            while ((c = inputStream.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, c, "utf-8"));
            }
            content = sb.toString();
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }



}
