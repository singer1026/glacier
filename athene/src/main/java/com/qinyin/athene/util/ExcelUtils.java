/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-9-8
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelUtils {
    public static HSSFCellStyle getCellCCStyle(HSSFWorkbook workbook) {
        HSSFFont cellFont = workbook.createFont();
        cellFont.setFontName("宋体");
        cellFont.setFontHeightInPoints((short) 10);
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(cellFont);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
        cellStyle.setWrapText(true);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        return cellStyle;
    }

    public static HSSFCellStyle getCellCommonStyle(HSSFWorkbook workbook) {
        //设置普通单元格字体
        HSSFFont cellFont = workbook.createFont();
        cellFont.setFontName("宋体");
        cellFont.setFontHeightInPoints((short) 10);
        //设置普通单元格样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(cellFont);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 左右居中
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);// 上下居中
        cellStyle.setWrapText(true);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        return cellStyle;
    }
}
