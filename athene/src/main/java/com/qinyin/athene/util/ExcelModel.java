/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-8-26
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.util;

import org.apache.poi.hssf.usermodel.*;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelModel {
    private HSSFWorkbook workbook = new HSSFWorkbook();
    private HSSFFont headFont;
    private HSSFCellStyle headStyle;
    private HSSFFont cellFont;
    private HSSFCellStyle cellStyle;
    private String[] columnTitle;
    private String[] columnKey;
    private int[] columnWidth;
    private List<String> sheetNames;

    public void init() {
        //设置列头字体
        this.headFont = workbook.createFont();
        headFont.setFontName("宋体");
        headFont.setFontHeightInPoints((short) 10);
        headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //设置列头样式
        this.headStyle = workbook.createCellStyle();
        headStyle.setFont(headFont);
        headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
        headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
        headStyle.setLocked(true);
        headStyle.setWrapText(true);
        //设置普通单元格字体
        this.cellFont = workbook.createFont();
        cellFont.setFontName("宋体");
        cellFont.setFontHeightInPoints((short) 10);
        //设置普通单元格样式
        this.cellStyle = workbook.createCellStyle();
        cellStyle.setFont(cellFont);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 左右居中
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);// 上下居中
        cellStyle.setWrapText(true);
    }

    public void setColumnTitle(String[] columnTitle) {
        this.columnTitle = columnTitle;
    }

    public void setColumnKey(String[] columnKey) {
        this.columnTitle = columnKey;
    }

    public void setColumnWidth(int[] columnWidth) {
        this.columnWidth = columnWidth;
    }

    public void setSheetNames(List<String> sheetNames) {
        this.sheetNames = sheetNames;
    }

    public void setSheetName(String sheetName) {
        List<String> list = new ArrayList<String>();
        list.add(sheetName);
        this.sheetNames = list;
    }

    public void processHead() {
        if (sheetNames == null || sheetNames.size() == 0) {
            HSSFSheet sheet = workbook.createSheet("无数据");
            buildHead(sheet);
        } else {
            for (String sheetName : sheetNames) {
                HSSFSheet sheet = workbook.createSheet(sheetName);
                buildHead(sheet);
            }
        }
    }

    public HSSFWorkbook getWorkbook() {
        return workbook;
    }

    private void buildHead(HSSFSheet sheet) {
        //设置列宽
        for (int i = 0; i < columnWidth.length; i++) {
            sheet.setColumnWidth(i, columnWidth[i]);
        }
//        //设置列样式
//        for (int i = 0; i <= columnTitle.length; i++) {
//            sheet.setDefaultColumnStyle(i, cellStyle);
//        }
        HSSFRow headRow = sheet.createRow(0);
        for (int i = 0; i < columnTitle.length; i++) {
            HSSFCell cell = headRow.createCell(i);
            cell.setCellValue(new HSSFRichTextString(columnTitle[i]));
            cell.setCellStyle(headStyle);
        }
    }

    public void write(OutputStream out) throws Exception {
        workbook.write(out);
    }
}