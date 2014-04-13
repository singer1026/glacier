/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-5-1
 * @email zhaolie43@gmail.com
 */
package com.qinyin.finance.service;

import com.qinyin.athene.util.DateUtils;
import com.qinyin.finance.model.Expense;
import com.qinyin.finance.model.Income;
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

public class IncomeImportTest extends TestCase {
    public static Logger log = LoggerFactory.getLogger(IncomeImportTest.class);
    private String[] files = {"applicationContext.xml"};
    private ClassPathXmlApplicationContext ac = null;
    private IncomeService service;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ac = new ClassPathXmlApplicationContext(files);
        service = (IncomeService) ac.getBean("incomeService");
    }

    public void testDao() {
        List<Income> list = new ArrayList<Income>();
        try {
            File file = new File("D:\\2010年收入.xls");
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
            HSSFSheet sheet = workbook.getSheet("9月");
            int rows = sheet.getPhysicalNumberOfRows();//行数
            System.out.println("rows=" + rows);
            Date now = new Date();
            for (int r = 1; r < rows; r++) {
                HSSFRow row = sheet.getRow(r);
                Date incomeDate = DateUtils.parseDate(row.getCell(0).getStringCellValue(), "yyyy.MM.dd");
                String catagory = row.getCell(1).getStringCellValue();
                String title = row.getCell(2).getStringCellValue();
                String description = null;
                HSSFCell cc = row.getCell(3);
                if (cc != null) {
                    description = cc.getStringCellValue();
                }
                BigDecimal amount = new BigDecimal(row.getCell(4).getNumericCellValue());
                if (incomeDate == null || StringUtils.isBlank(catagory) ||
                        StringUtils.isBlank(title) || amount == null || amount.intValue() == 0) {
                    throw new Exception();
                }
                Income income = new Income();
//                income.setGmtCreate(now);
                income.setCreator("system");
//                income.setGmtModify(now);
                income.setModifier("system");
                income.setIsDeleted("n");
                income.setVersion(1);
                income.setCompanyId(1);
                income.setCategory(catagory);
                income.setTitle(title);
                income.setDescription(description);
//                income.setIncomeDate(incomeDate);
                income.setAmount(amount);
                list.add(income);
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
