/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-6-28
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.util;

import com.google.javascript.jscomp.CommandLineRunner;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CompressUtils {

    public static void main(String[] args) throws Exception {
        String allList = "G:\\Source\\glacier\\web\\src\\main\\webapp\\scripts\\src\\list_all.txt";
        String allPath = "G:\\Source\\glacier\\web\\src\\main\\webapp\\scripts\\athene-all.js";
        String cssList = "G:\\Source\\glacier\\web\\src\\main\\webapp\\styles\\src\\list_css.txt";
        String cssPath = "G:\\Source\\glacier\\web\\src\\main\\webapp\\styles\\src\\athene-all.css";
        mergerFiles(readList(cssList), cssPath);
        compressFiles(readList(allList), allPath);
    }

    private static void compressFiles(List<String> fileList, String filePath) throws Exception {
        String compressPath = filePath.substring(0, filePath.lastIndexOf(".")) + ".min.js";
        String line = null;
        StringBuilder sb = new StringBuilder();
        for (String fileName : fileList) {
            FileInputStream fis = new FileInputStream(new File(fileName));
            InputStreamReader read = new InputStreamReader(fis, "utf-8");
            BufferedReader br = new BufferedReader(read);
            while ((line = br.readLine()) != null) {
                String newline = line.trim();
                if (newline.startsWith("//")) {
                    System.out.println(newline);
                }
                sb.append(line).append("\n");
            }
            br.close();
            read.close();
            fis.close();
        }
        String content = sb.toString();
        FileOutputStream fos1 = new FileOutputStream(filePath);
        Writer out1 = new OutputStreamWriter(fos1, "UTF-8");
        out1.write(content);
        out1.close();
        fos1.close();
        String[] args = new String[8];
        args[0] = "--js";
        args[1] = filePath;
        args[2] = "--js_output_file";
        args[3] = compressPath;
        args[4] = "--compilation_level";
        args[5] = "SIMPLE_OPTIMIZATIONS";
        args[6] = "--charset";
        args[7] = "UTF-8";
        CommandLineRunner.main(args);
    }

    private static void mergerFiles(List<String> fileList, String filePath) throws Exception {
        String line = null;
        StringBuilder sb = new StringBuilder();
        for (String fileName : fileList) {
            FileInputStream fis = new FileInputStream(new File(fileName));
            InputStreamReader read = new InputStreamReader(fis, "utf-8");
            BufferedReader br = new BufferedReader(read);
            while ((line = br.readLine()) != null) {
                String newline = line.trim();
                if (newline.startsWith("//")) {
                    System.out.println(newline);
                }
                sb.append(line).append("\n");
            }
            br.close();
            read.close();
            fis.close();
        }
        String content = sb.toString();
        FileOutputStream fos1 = new FileOutputStream(filePath);
        Writer out1 = new OutputStreamWriter(fos1, "UTF-8");
        out1.write(content);
        out1.close();
        fos1.close();
    }

    private static List<String> readList(String listFile) throws Exception {
        String path = listFile.substring(0, listFile.lastIndexOf("\\"));
        List<String> rtnList = new ArrayList<String>();
        File file = new File(listFile);
        String line;
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader read = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(read);
        while ((line = br.readLine()) != null) {
            rtnList.add(path + "\\" + line);
        }
        br.close();
        read.close();
        fis.close();
        return rtnList;
    }
}
