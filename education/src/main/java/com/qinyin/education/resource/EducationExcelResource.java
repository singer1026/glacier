/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-6-10
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.resource;

import com.qinyin.athene.annotation.Args;
import com.qinyin.athene.annotation.RequestMap;
import com.qinyin.athene.annotation.Response;
import com.qinyin.athene.annotation.UrlMapping;
import com.qinyin.athene.model.BaseModel;
import com.qinyin.athene.util.AtheneUtils;
import com.qinyin.athene.util.BeanUtils;
import com.qinyin.athene.util.DateUtils;
import com.qinyin.athene.util.ParamUtils;
import com.qinyin.education.comparator.StudentExcelComparator;
import com.qinyin.education.model.LessonStudent;
import com.qinyin.education.model.Subject;
import com.qinyin.education.model.SubjectRelation;
import com.qinyin.education.proxy.LessonScheduleProxy;
import com.qinyin.education.proxy.StudentProxy;
import com.qinyin.education.proxy.TeacherProxy;
import com.qinyin.education.wrap.TeacherWrap;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class EducationExcelResource {
    public static Logger log = LoggerFactory.getLogger(EducationExcelResource.class);
    private StudentProxy studentProxy;
    private TeacherProxy teacherProxy;
    private LessonScheduleProxy lessonScheduleProxy;

    @UrlMapping(value = "/studentListExcel")
    public Map<String, Object> studentList(@RequestMap Map<String, Object> requestMap, @Response HttpServletResponse response)
            throws Exception {
        OutputStream out = response.getOutputStream();
        try {
            requestMap.put(SubjectRelation.SUBJECT_ID, ParamUtils.getInteger(requestMap.get(SubjectRelation.SUBJECT_ID)));
            List<Object> studentList = studentProxy.queryForList(requestMap);
            List resultList = this.transferStudentList(studentList);
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
            String[] columnHeads = {"姓名", "状态", "性别", "入校时间", "科目", "学校", "手机号码", "固定电话", "QQ", "地址"};
            String[] columns = {"name", "statusDisplay", "sexDisplay", "gmtEnterDisplay", "subjectName", "school", "mobile", "telephone", "qq", "address"};
            short[] columnsWidth = {3000, 2000, 1500, 3000, 7000, 6000, 4500, 4500, 4000, 11000};
            //设置列宽
            for (int i = 0; i < columnsWidth.length; i++) {
                sheet.setColumnWidth((short) i, columnsWidth[i]);
            }
            //设置列样式
            HSSFCellStyle sheetStyle = workbook.createCellStyle();
            for (int i = 0; i <= columnHeads.length; i++) {
                sheet.setDefaultColumnStyle((short) i, sheetStyle);
            }
            //设置列头字体
            HSSFFont columnHeadFont = workbook.createFont();
            columnHeadFont.setFontName("宋体");
            columnHeadFont.setFontHeightInPoints((short) 10);
            columnHeadFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            //设置列头样式
            HSSFCellStyle columnHeadStyle = workbook.createCellStyle();
            columnHeadStyle.setFont(columnHeadFont);
            columnHeadStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
            columnHeadStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
            columnHeadStyle.setLocked(true);
            columnHeadStyle.setWrapText(true);
            //设置普通单元格字体
            HSSFFont font = workbook.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 10);
            //设置普通单元格样式
            HSSFCellStyle style = workbook.createCellStyle();
            style.setFont(font);
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 左右居中
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);// 上下居中
            style.setWrapText(true);
            HSSFRow headRow = sheet.createRow(0);
            for (int i = 0; i < columnHeads.length; i++) {
                HSSFCell cell = headRow.createCell(i);
                cell.setCellValue(new HSSFRichTextString(columnHeads[i]));
                cell.setCellStyle(columnHeadStyle);
            }
            Collections.sort(resultList, new StudentExcelComparator());
            for (int i = 0; i < resultList.size(); i++) {
                Map<String, Object> studentMap = (Map<String, Object>) resultList.get(i);
                HSSFRow row = sheet.createRow(i + 1);
                for (int j = 0; j < columns.length; j++) {
                    HSSFCell cell = row.createCell(j);
                    cell.setCellValue(new HSSFRichTextString(ParamUtils.getString(studentMap.get(columns[j]))));
                    cell.setCellStyle(style);
                }
            }
            response.setContentType("application/msexcel");
            response.setHeader("Content-disposition", "attachment;filename="
                    + new String(("学生列表" + DateUtils.formatDate(new Date()) + ".xls").getBytes("gb2312"), "iso-8859-1"));
            workbook.write(out);
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

    private List transferStudentList(List<Object> studentList) {
        List resultList = new ArrayList();
        for (Object obj : studentList) {
            try {
                Map resultMap = BeanUtils.toMap(obj);
                List subjectList = ParamUtils.getList(resultMap.get("subjectList"));
                StringBuilder sb = new StringBuilder();
                if (studentList != null && subjectList.size() > 0) {
                    int len = subjectList.size();
                    for (int i = 0; i < len; i++) {
                        Subject subject = (Subject) subjectList.get(i);
                        sb.append(subject.getName());
                        if ((i + 1) < len) {
                            sb.append(" ");
                        }
                    }
                }
                resultMap.put("subjectName", sb.toString());
                resultList.add(resultMap);
            } catch (Exception e) {
                log.error("catch exception ", e);
            }
        }
        return resultList;
    }

    @UrlMapping(value = "/teacherScheduleExcel")
    public Map<String, Object> teacherSchedule(@Args Map<String, Object> paramMap,
                                               @RequestMap Map<String, Object> requestMap,
                                               @Response HttpServletResponse response) throws Exception {
        InputStream template = null;
        OutputStream out = response.getOutputStream();
        try {
            Integer id = ParamUtils.getIdInteger(requestMap.get(BaseModel.ID));
            if (id == null) {
                throw new RuntimeException("teacher is null");
            }
            String time = DateUtils.formatDate(new Date());
            TeacherWrap teacherWrap = teacherProxy.queryForWrap(id);
            String teacherName = teacherWrap.getName();
            Map<String, Object> teacherMap = lessonScheduleProxy.queryTeacherScheduleByTeacherId(id);
            ServletContext servletContext = AtheneUtils.getServletContext();
            template = servletContext.getResourceAsStream("/excel/老师上课表模板.xls");
            HSSFWorkbook workbook = new HSSFWorkbook(template);
            workbook.setSheetName(0, teacherName + "上课表");
            HSSFSheet sheet = workbook.getSheetAt(0);
            int[] columnWidth = new int[]{1100, 4700, 4700, 4700, 4700, 4700, 4700, 4700};
            for (int i = 0; i < columnWidth.length; i++) {
                sheet.setColumnWidth(i, columnWidth[i]);
            }
            //处理老师名字
            HSSFCell tnCell = sheet.getRow(0).getCell(0);
            String oldString = tnCell.getRichStringCellValue().getString();
            tnCell.setCellValue(new HSSFRichTextString(oldString.replace("${teacherName}", teacherName + "上课表 " + time)));
            //处理功课
            List AMList = ParamUtils.getList(teacherMap.get("AMList"));
            List PMList = ParamUtils.getList(teacherMap.get("PMList"));
            int startRow = 2, startColumn = 1, endColumn = startColumn + 7;
            for (int i = startColumn; i < endColumn; i++) {
                HSSFCell AMCell = sheet.getRow(startRow).getCell(i);
                String AMString = generateSingle((List) AMList.get(i - 1));
                if (StringUtils.isNotBlank(AMString)) {
                    AMCell.setCellValue(new HSSFRichTextString(AMString));
                }
                HSSFCell PMCell = sheet.getRow(startRow + 1).getCell(i);
                String PMString = generateSingle((List) PMList.get(i - 1));
                if (StringUtils.isNotBlank(PMString)) {
                    PMCell.setCellValue(new HSSFRichTextString(PMString));
                }
            }
            response.setContentType("application/msexcel");
            response.setHeader("Content-disposition", "attachment;filename="
                    + new String((teacherName + "上课表" + time + ".xls").getBytes("gb2312"), "iso-8859-1"));
            workbook.write(out);
            out.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            if (template != null) {
                template.close();
            }
            if (out != null) {
                out.close();
            }
        }
        return null;
    }

    private String generateSingle(List list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i != 0) {
                sb.append("\r\n");
            }
            Map<String, Object> map = (Map<String, Object>) list.get(i);
            sb.append(map.get("subjectName")).append("[").append(map.get("type")).append("]");
            String startTime = ParamUtils.getString(map.get("startTimeDisplay"));
            String endTime = ParamUtils.getString(map.get("endTimeDisplay"));
            String timeLength = ParamUtils.getString(map.get("timeLength"));
            sb.append("\r\n").append(startTime).append("-").append(endTime).append(" ").append(timeLength).append("分钟");
            List studentList = (List) map.get("lessonStudentList");
            if (studentList != null && studentList.size() > 0) {
                for (Object obj : studentList) {
                    LessonStudent lessonStudent = (LessonStudent) obj;
                    sb.append("\r\n").append(lessonStudent.getStudentName());
                }
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }

    public void setLessonScheduleProxy(LessonScheduleProxy lessonScheduleProxy) {
        this.lessonScheduleProxy = lessonScheduleProxy;
    }

    public void setStudentProxy(StudentProxy studentProxy) {
        this.studentProxy = studentProxy;
    }

    public void setTeacherProxy(TeacherProxy teacherProxy) {
        this.teacherProxy = teacherProxy;
    }
}
