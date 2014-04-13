/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-7-4
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.util;

import com.qinyin.athene.exception.HackerException;
import com.qinyin.athene.login.PrivilegeInfo;
import com.qinyin.athene.singleton.BeanFactoryHolder;

import java.util.regex.Pattern;

public class DefenseUtils {
    public static final String UNLAWFUL_CHAR = "存在非法字符";
    public static final String HAS_QUOTES = "存在单引号或者双引号";

    public static boolean isUnlawfulChar(String string) {
        Pattern space = Pattern.compile("[\\s]"); //空格
        Pattern n = Pattern.compile("[\\n]"); //换行符
        Pattern r = Pattern.compile("[\\r]"); //回车符
        string = space.matcher(string).replaceAll("");
        string = n.matcher(string).replaceAll("");
        string = r.matcher(string).replaceAll("");
        Pattern script = Pattern.compile("<script[^>]*?>.*?</script>");
        if (script.matcher(string).find()) return true;
        Pattern html = Pattern.compile("<.+?>", Pattern.DOTALL);
        if (html.matcher(string).find()) return true;
        return false;
    }

    public static boolean hasQuotes(String string) {
        Pattern quotes = Pattern.compile("'|\"");
        return quotes.matcher(string).find();
    }

    public static void main(String[] args) {
        boolean a = DefenseUtils.hasQuotes("45'f\"df");
        System.out.println(a);
    }

    public static void checkCompany(Object object) {
        Integer companyId = ParamUtils.getIdInteger(object);
        if (companyId == null) throw new HackerException();
        PrivilegeInfo pvgInfo = (PrivilegeInfo) BeanFactoryHolder.getBean("pvgInfo");
        if (companyId.compareTo(pvgInfo.getCompanyId()) != 0) {
            throw new HackerException();
        }
    }

//    public static void main(String[] args) {
//        Pattern p = Pattern.compile("<.+?>", Pattern.DOTALL); //去HTML
//        Pattern p1 = Pattern.compile("<javascript[^>]*?>.*?</javascript>");//去javascript
//        Pattern p2 = Pattern.compile("[\\s]"); //去空格
//        Pattern p3 = Pattern.compile("<script[^>]*?>.*?</script>"); //去JS
//        String meng = "<a href='www.liuzm.com'>关注java,,Flex,<script>alert('df');</script>数据库,AJAX,js,WEB,WINDOWS7,网站应用,互联网的文章评论和想法杂谈的博客</a>";
//        meng = p3.matcher(meng).replaceAll(""); //在这要先去JS,再去html代码,不然js理的代码就去掉了
//        meng = p.matcher(meng).replaceAll("");
//        meng = p1.matcher(meng).replaceAll("");
//        meng = p2.matcher(meng).replaceAll("");
//        System.out.println(meng);
//    }


}
