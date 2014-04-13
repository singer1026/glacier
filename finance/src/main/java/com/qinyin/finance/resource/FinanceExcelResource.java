/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-6-10
 * @email zhaolie43@gmail.com
 */
package com.qinyin.finance.resource;

import com.qinyin.athene.annotation.RequestMap;
import com.qinyin.athene.annotation.Response;
import com.qinyin.athene.annotation.UrlMapping;
import com.qinyin.athene.comparator.DateMonthComparator;
import com.qinyin.athene.comparator.DateYearComparator;
import com.qinyin.athene.util.*;
import com.qinyin.finance.comparator.AccountStatisticsComparator;
import com.qinyin.finance.model.AccountStatistics;
import com.qinyin.finance.model.Expense;
import com.qinyin.finance.model.Income;
import com.qinyin.finance.proxy.AccountStatisticsProxy;
import com.qinyin.finance.proxy.ExpenseProxy;
import com.qinyin.finance.proxy.IncomeProxy;
import com.qinyin.finance.wrap.AccountStatisticsWrap;
import com.qinyin.finance.wrap.ExpenseWrap;
import com.qinyin.finance.wrap.IncomeWrap;
import org.apache.poi.hssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

public class FinanceExcelResource {
    public static Logger log = LoggerFactory.getLogger(FinanceExcelResource.class);
    private ExpenseProxy expenseProxy;
    private IncomeProxy incomeProxy;
    private AccountStatisticsProxy accountStatisticsProxy;

    @UrlMapping(value = "/expenseListExcel")
    public Map<String, Object> expenseList(@RequestMap Map<String, Object> requestMap, @Response HttpServletResponse response)
            throws Exception {
        OutputStream out = response.getOutputStream();
        try {
            List<String> ordering = new ArrayList<String>();
            ordering.add("e.expend_date ASC");
            requestMap.put("ordering", ordering);
            List<Object> expenseWrapList = expenseProxy.queryForList(requestMap);
            GroupListMap groupListMap = new GroupListMap();
            for (Object o : expenseWrapList) {
                ExpenseWrap wrap = (ExpenseWrap) o;
                Timestamp expendDate = wrap.getExpendDate();
                groupListMap.put(DateUtils.formatDate(expendDate, DateMonthComparator.DATE_FORMAT), o);
            }
            List<String> sheetNames = groupListMap.getKeys();
            Collections.sort(sheetNames, new DateMonthComparator());
            ExcelModel excelModel = new ExcelModel();
            excelModel.init();
            excelModel.setSheetNames(sheetNames);
            excelModel.setColumnTitle(new String[]{"支出日期", "分类", "标题", "描述", "金额"});
            excelModel.setColumnWidth(new int[]{4000, 4000, 8000, 12000, 3000});
            excelModel.processHead();
            HSSFCellStyle numberCellStyle = this.getNumberStyle(excelModel.getWorkbook());
            HSSFWorkbook workbook = excelModel.getWorkbook();
            for (String sheetName : sheetNames) {
                HSSFSheet sheet = workbook.getSheet(sheetName);
                List list = groupListMap.get(sheetName);
                List expenseList = this.transferList(list);
                String[] columns = {"expendDateDisplay", "categoryDisplay", "title", "description", "amountDisplay"};
                BigDecimal amountTotal = new BigDecimal(0);
                int size = expenseList.size();
                for (int i = 0; i < size; i++) {
                    Map<String, Object> expenseMap = (Map<String, Object>) expenseList.get(i);
                    BigDecimal amount = ParamUtils.getBigDecimal(expenseMap.get(Expense.AMOUNT));
                    amountTotal = amountTotal.add(amount);
                    HSSFRow row = sheet.createRow(i + 1);
                    for (int j = 0; j < columns.length; j++) {
                        HSSFCell cell = row.createCell(j);
                        cell.setCellValue(new HSSFRichTextString(ParamUtils.getString(expenseMap.get(columns[j]))));
                        if (columns[j].equals("amountDisplay")) {
                            cell.setCellStyle(numberCellStyle);
                        }
                    }
                }
                HSSFRow totalRow = sheet.createRow(size + 2);
                totalRow.createCell(3).setCellValue(new HSSFRichTextString("总计"));
                HSSFCell totalCell = totalRow.createCell(4);
                totalCell.setCellStyle(numberCellStyle);
                totalCell.setCellValue(new HSSFRichTextString(DigestUtils.formatDecimal(amountTotal)));
            }
            String fileName = "支出列表" + requestMap.get("timeStart") + "至" + requestMap.get("timeEnd");
            response.setContentType("application/msexcel");
            response.setHeader("Content-disposition", "attachment;filename="
                    + new String((fileName + ".xls").getBytes("gb2312"), "iso-8859-1"));
            excelModel.write(out);
            out.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return null;
    }

    private List transferList(List list) {
        List resultList = new ArrayList();
        for (Object obj : list) {
            try {
                Map resultMap = BeanUtils.toMap(obj);
                resultList.add(resultMap);
            } catch (Exception e) {
                log.error("catch exception ", e);
            }
        }
        return resultList;
    }


    @UrlMapping(value = "/incomeListExcel")
    public Map<String, Object> incomeList(@RequestMap Map<String, Object> requestMap, @Response HttpServletResponse response)
            throws Exception {
        OutputStream out = response.getOutputStream();
        try {
            List<String> ordering = new ArrayList<String>();
            ordering.add("i.income_date ASC");
            requestMap.put("ordering", ordering);
            List<Object> incomeWrapList = incomeProxy.queryForList(requestMap);
            GroupListMap groupListMap = new GroupListMap();
            for (Object o : incomeWrapList) {
                IncomeWrap incomeWrap = (IncomeWrap) o;
                Date incomeDate = incomeWrap.getIncomeDate();
                groupListMap.put(DateUtils.formatDate(incomeDate, DateMonthComparator.DATE_FORMAT), o);
            }
            List<String> sheetNames = groupListMap.getKeys();
            Collections.sort(sheetNames, new DateMonthComparator());
            ExcelModel excelModel = new ExcelModel();
            excelModel.init();
            excelModel.setSheetNames(sheetNames);
            excelModel.setColumnTitle(new String[]{"收入日期", "分类", "标题", "描述", "金额"});
            excelModel.setColumnWidth(new int[]{4000, 4000, 8000, 12000, 3000});
            excelModel.processHead();
            HSSFCellStyle numberCellStyle = this.getNumberStyle(excelModel.getWorkbook());
            HSSFWorkbook workbook = excelModel.getWorkbook();
            for (String sheetName : sheetNames) {
                HSSFSheet sheet = workbook.getSheet(sheetName);
                List list = groupListMap.get(sheetName);
                List incomeList = this.transferList(list);
                String[] columns = {"incomeDateDisplay", "categoryDisplay", "title", "description", "amountDisplay"};
                BigDecimal amountTotal = new BigDecimal(0);
                int size = incomeList.size();
                for (int i = 0; i < size; i++) {
                    Map<String, Object> incomeMap = (Map<String, Object>) incomeList.get(i);
                    BigDecimal amount = (BigDecimal) incomeMap.get(Income.AMOUNT);
                    amountTotal = amountTotal.add(amount);
                    HSSFRow row = sheet.createRow(i + 1);
                    for (int j = 0; j < columns.length; j++) {
                        HSSFCell cell = row.createCell(j);
                        cell.setCellValue(new HSSFRichTextString(ParamUtils.getString(incomeMap.get(columns[j]))));
                        if (columns[j].equals("amountDisplay")) {
                            cell.setCellStyle(numberCellStyle);
                        }
                    }
                }
                HSSFRow totalRow = sheet.createRow(size + 2);
                totalRow.createCell(3).setCellValue(new HSSFRichTextString("总计"));
                HSSFCell totalCell = totalRow.createCell(4);
                totalCell.setCellStyle(numberCellStyle);
                totalCell.setCellValue(new HSSFRichTextString(DigestUtils.formatDecimal(amountTotal)));
            }
            String fileName = "收入列表" + requestMap.get("timeStart") + "至" + requestMap.get("timeEnd");
            response.setContentType("application/msexcel");
            response.setHeader("Content-disposition", "attachment;filename="
                    + new String((fileName + ".xls").getBytes("gb2312"), "iso-8859-1"));
            excelModel.write(out);
            out.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return null;
    }

    @UrlMapping(value = "/accountStatisticsListExcel")
    public Map<String, Object> accountStatisticsList(@RequestMap Map<String, Object> requestMap, @Response HttpServletResponse response)
            throws Exception {
        OutputStream out = response.getOutputStream();
        try {
            List<String> ordering = new ArrayList<String>();
            ordering.add("ass.statistics_date ASC");
            requestMap.put("ordering", ordering);
            List<Object> accountStatisticsWrapList = accountStatisticsProxy.queryForList(requestMap);
            GroupListMap groupListMap = new GroupListMap();
            for (Object o : accountStatisticsWrapList) {
                AccountStatisticsWrap accountStatisticsWrap = (AccountStatisticsWrap) o;
                Date statisticsDate = accountStatisticsWrap.getStatisticsDate();
                groupListMap.put(DateUtils.formatDate(statisticsDate, DateYearComparator.DATE_FORMAT), o);
            }
            List<String> sheetNames = groupListMap.getKeys();
            Collections.sort(sheetNames, new DateYearComparator());
            ExcelModel excelModel = new ExcelModel();
            excelModel.init();
            excelModel.setSheetNames(sheetNames);
            excelModel.setColumnTitle(new String[]{"统计日期", "收入总和", "支出总和", "日盈利", "余额"});
            excelModel.setColumnWidth(new int[]{4000, 4000, 4000, 4000, 4000});
            excelModel.processHead();
            HSSFCellStyle numberCellStyle = this.getNumberStyle(excelModel.getWorkbook());
            HSSFWorkbook workbook = excelModel.getWorkbook();
            for (String sheetName : sheetNames) {
                HSSFSheet sheet = workbook.getSheet(sheetName);
                List list = groupListMap.get(sheetName);
                Collections.sort(list, new AccountStatisticsComparator());
                List accountStatisticsList = this.transferList(list);
                String[] columns = {"statisticsDateDisplay", "incomeTotalDisplay", "expenseTotalDisplay", "dayProfitDisplay", "balanceDisplay"};
                BigDecimal incomeTotalCount = new BigDecimal(0);
                BigDecimal expenseTotalCount = new BigDecimal(0);
                int size = accountStatisticsList.size();
                int rowNumber = 1;
                for (int i = 0; i < size; i++) {
                    Map<String, Object> accountStatisticsMap = (Map<String, Object>) accountStatisticsList.get(i);
                    BigDecimal incomeTotal = ParamUtils.getBigDecimal(accountStatisticsMap.get(AccountStatistics.INCOME_TOTAL));
                    BigDecimal expenseTotal = ParamUtils.getBigDecimal(accountStatisticsMap.get(AccountStatistics.EXPENSE_TOTAL));
                    incomeTotalCount = incomeTotalCount.add(incomeTotal);
                    expenseTotalCount = expenseTotalCount.add(expenseTotal);
                    HSSFRow row = sheet.createRow(rowNumber);
                    rowNumber++;
                    for (int j = 0; j < columns.length; j++) {
                        HSSFCell cell = row.createCell(j);
                        cell.setCellValue(new HSSFRichTextString(ParamUtils.getString(accountStatisticsMap.get(columns[j]))));
                        if (!columns[j].equals("statisticsDateDisplay")) {
                            cell.setCellStyle(numberCellStyle);
                        }
                    }
                }
                if (list != null && list.size() > 0) {
                    rowNumber++;
                    AccountStatisticsWrap wrapMapStart = (AccountStatisticsWrap) list.get(0);
                    AccountStatisticsWrap wrapMapEnd = (AccountStatisticsWrap) list.get(list.size() - 1);
                    List incomeAmountSumList = accountStatisticsProxy.computeIncomeCategoryAmountSum(wrapMapStart.getStatisticsDate(),
                            wrapMapEnd.getStatisticsDate());
                    for (Object obj : incomeAmountSumList) {
                        Map<String, Object> result = (Map<String, Object>) obj;
                        HSSFRow amountSumRow = sheet.createRow(rowNumber);
                        rowNumber++;
                        amountSumRow.createCell(0).setCellValue(new HSSFRichTextString(ParamUtils.getString(result.get("categoryName"))));
                        HSSFCell amountSumCell = amountSumRow.createCell(1);
                        amountSumCell.setCellStyle(numberCellStyle);
                        amountSumCell.setCellValue(ParamUtils.getString(result.get("result")));
                    }
                    HSSFRow incomeRow = sheet.createRow(rowNumber);
                    incomeRow.createCell(0).setCellValue(new HSSFRichTextString("收入总和统计"));
                    HSSFCell totalCell1 = incomeRow.createCell(1);
                    totalCell1.setCellStyle(numberCellStyle);
                    totalCell1.setCellValue(new HSSFRichTextString(DigestUtils.formatDecimal(incomeTotalCount)));
                    rowNumber += 2;
                    //------------------------------------------------------//
                    List expenseAmountSumList = accountStatisticsProxy.computeExpenseCategoryAmountSum(wrapMapStart.getStatisticsDate(),
                            wrapMapEnd.getStatisticsDate());
                    for (Object obj : expenseAmountSumList) {
                        Map<String, Object> result = (Map<String, Object>) obj;
                        HSSFRow amountSumRow = sheet.createRow(rowNumber);
                        rowNumber++;
                        amountSumRow.createCell(0).setCellValue(new HSSFRichTextString(ParamUtils.getString(result.get("categoryName"))));
                        HSSFCell amountSumCell = amountSumRow.createCell(1);
                        amountSumCell.setCellStyle(numberCellStyle);
                        amountSumCell.setCellValue(ParamUtils.getString(result.get("result")));
                    }
                    HSSFRow expenseRow = sheet.createRow(rowNumber);
                    expenseRow.createCell(0).setCellValue(new HSSFRichTextString("支出总和统计"));
                    HSSFCell totalCell2 = expenseRow.createCell(1);
                    totalCell2.setCellStyle(numberCellStyle);
                    totalCell2.setCellValue(new HSSFRichTextString(DigestUtils.formatDecimal(expenseTotalCount)));
                }
            }
            String fileName = "账户统计列表" + requestMap.get("timeStart") + "至" + requestMap.get("timeEnd");
            response.setContentType("application/msexcel");
            response.setHeader("Content-disposition", "attachment;filename="
                    + new String((fileName + ".xls").getBytes("gb2312"), "iso-8859-1"));
            excelModel.write(out);
            out.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return null;
    }

    private HSSFCellStyle getNumberStyle(HSSFWorkbook workbook) {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 左右居中
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);// 上下居中
        cellStyle.setWrapText(true);
        return cellStyle;
    }

    public void setExpenseProxy(ExpenseProxy expenseProxy) {
        this.expenseProxy = expenseProxy;
    }

    public void setIncomeProxy(IncomeProxy incomeProxy) {
        this.incomeProxy = incomeProxy;
    }

    public void setAccountStatisticsProxy(AccountStatisticsProxy accountStatisticsProxy) {
        this.accountStatisticsProxy = accountStatisticsProxy;
    }
}
