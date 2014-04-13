<div style="width:96%;margin:0 auto;">
<#if error??>
${error}
    <#else>
        <table width="100%" style="margin-top:10px;" class="table-view-default">
            <tr>
                <td width="15%" align="right">
                    <label>教师姓名</label>
                </td>
                <td width="35%">
                ${lesson.teacherName!}
                </td>
                <td width="15%" align="right">
                    <label>学生姓名</label>
                </td>
                <td width="35%">
                ${lesson.studentName!}
                </td>
            </tr>

        </table>
        <div style="text-align:center;margin-top:5px;">
        <@Button label="关 闭" iconCls="icon-close">
            athene.window.get('${number}').close();
        </@Button>
        </div>
</#if>
</div>