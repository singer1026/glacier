<@ExistError/>
<div class="fm-div">
    <table style="width:450px;" class="table-view-default">
        <tr>
            <td width="15%" align="right">
                <label>姓名</label>
            </td>
            <td width="35%">
            ${student.name!}
            </td>
            <td width="15%" align="right">
                <label>状态</label>
            </td>
            <td width="35%">
            ${getResName("common","studentStatus",student.status!)}
            </td>
        </tr>
        <tr>
            <td align="right">
                <label>性别</label>
            </td>
            <td>
            ${getResName("common","userSex",student.sex!)}
            </td>
            <td align="right">
                <label>学校</label>
            </td>
            <td>
            ${student.school!}
            </td>
        </tr>
        <tr>
            <td align="right">
                <label>电子邮件</label>
            </td>
            <td>
            ${student.email!}
            </td>
            <td align="right">
                <label>QQ</label>
            </td>
            <td>
            ${student.qq!}
            </td>
        </tr>
        <tr>
            <td align="right">
                <label>固定电话</label>
            </td>
            <td>
            ${student.telephone!}
            </td>
            <td align="right">
                <label>手机号码</label>
            </td>
            <td>
            ${student.mobile!}
            </td>
        </tr>
        <tr>
            <td align="right">
                <label>入校时间</label>
            </td>
            <td>
            <#if student.gmtEnter??>
            ${student.gmtEnter?string("yyyy-MM-dd")}
                </#if>
            </td>
            <td align="right">
                <label>离校时间</label>
            </td>
            <td>
            <#if student.gmtQuit??>
            ${student.gmtQuit?string("yyyy-MM-dd")}
                </#if>
            </td>
        </tr>
        <tr>
            <td align="right">
                <label>科目</label></td>
            <td colspan="3">
            ${student.subjectCn!}
            </td>
        </tr>
        <tr>
            <td align="right">
                <label>地址</label>
            </td>
            <td colspan="3">
            ${student.address!}
            </td>
        </tr>
        <tr>
            <td align="right">
                <label>整体描述</label></td>
            <td colspan="3">
            ${student.description!}
            </td>
        </tr>
    </table>
    <div style="text-align:center;margin-top:5px;">
    <@Button label="关 闭" iconCls="icon-close">
        athene.window.get('${number}').close();
    </@Button>
    </div>
</div>