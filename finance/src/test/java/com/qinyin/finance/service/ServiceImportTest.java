/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-5-1
 * @email zhaolie43@gmail.com
 */
package com.qinyin.finance.service;

import com.qinyin.athene.util.DateUtils;
import com.qinyin.finance.model.Expense;
import junit.framework.TestCase;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceImportTest extends TestCase {
    public static Logger log = LoggerFactory.getLogger(ServiceImportTest.class);
    private String[] files = {"applicationContext.xml"};
    private ClassPathXmlApplicationContext ac = null;
    private ExpenseService service;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ac = new ClassPathXmlApplicationContext(files);
        service = (ExpenseService) ac.getBean("expenseService");
    }

    public void testDao() {
        List<Expense> list = new ArrayList<Expense>();
        try {
            File file = new File("D:\\2010年开支.xls");
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
            HSSFSheet sheet = workbook.getSheet("9月");
            int rows = sheet.getPhysicalNumberOfRows();//行数
            System.out.println("rows=" + rows);
            Date now = new Date();
            for (int r = 1; r < rows; r++) {
                HSSFRow row = sheet.getRow(r);
                Date expendDate = DateUtils.parseDate(row.getCell(0).getStringCellValue(), "yyyy.MM.dd");
                String catagory = row.getCell(1).getStringCellValue();
                String title = row.getCell(2).getStringCellValue();
                String description = null;
                HSSFCell cc = row.getCell(3);
                if (cc != null) {
                    description = cc.getStringCellValue();
                }
                BigDecimal amount = new BigDecimal(row.getCell(4).getNumericCellValue());
                if (expendDate == null || StringUtils.isBlank(catagory) ||
                        StringUtils.isBlank(title) ||
                        amount == null || amount.intValue() == 0) {
                    throw new Exception();
                }
                Expense expense = new Expense();
//                expense.setGmtCreate(now);
                expense.setCreator("system");
//                expense.setGmtModify(now);
                expense.setModifier("system");
                expense.setIsDeleted("n");
                expense.setVersion(1);
                expense.setCompanyId(1);
                expense.setCategory(catagory);
                expense.setTitle(title);
                expense.setDescription(description);
//                expense.setExpendDate(expendDate);
                expense.setAmount(amount);
                list.add(expense);
            }
//            service.addList(list);
        } catch (Exception e) {
            log.error("", e);
            System.exit(1);
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
