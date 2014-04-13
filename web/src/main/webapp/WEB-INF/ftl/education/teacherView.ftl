<@ExistError/>
<div class="fm-div">
    <table style="width:450px;" class="table-view-default">
        <tr>
            <td width="15%" align="right">
                <label>姓名</label>
            </td>
            <td width="35%">
            ${teacher.name!}
            </td>
            <td width="15%" align="right">
                <label>状态</label>
            </td>
            <td width="35%">
            ${getResName("common","teacherStatus",teacher.status!)}
            </td>
        </tr>
        <tr>
            <td align="right">
                <label>性别</label>
            </td>
            <td>
            ${getResName("common","userSex",teacher.sex!)}
            </td>
            <td align="right">
                <label>教师等级</label>
            </td>
            <td>
            ${getResName("company","teacherLevel",teacher.level!)}
            </td>
        </tr>
        <tr>
            <td align="right">
                <label>学历</label>
            </td>
            <td>
            ${getResName("common","education",teacher.education!)}
            </td>
            <td align="right">
                <label>毕业院校</label>
            </td>
            <td>
            ${teacher.graduateSchool!}
            </td>

        </tr>
        <tr>
            <td align="right">
                <label>专业</label>
            </td>
            <td>
            ${teacher.major!}
            </td>
            <td align="right">
                <label>工作经验</label>
            </td>
            <td>
            ${getResName("common","experience",teacher.experience!)}
            </td>
        </tr>
        <tr>
            <td align="right">
                <label>QQ</label>
            </td>
            <td>
            ${teacher.qq!}
            </td>
            <td align="right">
                <label>电子邮件</label>
            </td>
            <td>
            ${teacher.email!}
            </td>
        </tr>
        <tr>
            <td align="right">
                <label>固定电话</label>
            </td>
            <td>
            ${teacher.telephone!}
            </td>
            <td align="right">
                <label>手机号码</label>
            </td>
            <td>
            ${teacher.mobile!}
            </td>
        </tr>
        <tr>
            <td align="right">
                <label>科目</label>
            </td>
            <td colspan="3">
            ${teacher.subjectCn!}
            </td>
        </tr>
        <tr>
            <td align="right">
                <label>地址</label>
            </td>
            <td colspan="3">
            ${teacher.address!}
            </td>
        </tr>
        <tr>
            <td align="right" valign="middle">
                <label>整体描述</label>
            </td>
            <td colspan="3">
            ${teacher.descriptionHTML!}
            </td>
        </tr>
    </table>
    <div style="text-align:center;margin-top:5px;">
    <@Button label="关 闭" iconCls="icon-close">
        athene.window.get('${number}').close();
    </@Button>
    </div>
</div>