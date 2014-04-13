<@ExistError/>
<div class="fm-div">
    <table id="student-edit-${number}" style="width:550px;margin-left:0px;" cellpadding="0" cellspacing="0">
        <tr>
        <@TextField label="登录名" binding="student.loginId" require="true" existReadOnly="true"
        explain="例如名字[吕秀云],格式为[xiuyun.luxy],一旦注册不能更改"/>
        <@TextField label="姓名" binding="student.name" require="true"/>
        </tr>
        <tr>
        <@ComboBox label="性别" binding="student.sex" require="true" list=sexList listKey="name" listValue="value"/>
        <@TextField label="所在学校" binding="student.school"/>
        </tr>
        <tr>
        <@TextField label="电子邮件" binding="student.email"/>
        <@TextField label="QQ" binding="student.qq"/>

        </tr>
        <tr>
        <@TextField label="固定电话" binding="student.telephone"/>
        <@TextField label="手机号码" binding="student.mobile" require="true"/>
        </tr>
        <tr>
        <@DateField label="入校时间" binding="student.gmtEnter" onFocus="WdatePicker();" readOnly="true" require="true"/>
        <@DateField label="离校时间" binding="student.gmtQuit" onFocus="WdatePicker();" readOnly="true"
        explain="[新增][更新]操作不需要填写<br/>[离校]操作才会验证此日期"/>
        </tr>
        <tr>
            <td colspan="2">
            <@TextField td="false" label="地址" binding="student.address" style="width:438px;"/>
            </td>
        </tr>
        <tr>
            <td colspan="2">
            <@LabelField td="false" label="科目" require="true" name="subjectList">
                <div id="subject-list-${number}"/>
            </@LabelField>
            </td>
        </tr>
        <tr>
            <td colspan="2">
            <@TextArea td="false" label="整体描述" binding="student.description" style="height:100px;width:441px;"/>
            <@Hidden binding="student.id"/>
            </td>
        </tr>
        <tr>
            <td colspan="2" align="center" valign="top">
                <div>
                <#if student??>
                <@Button label="保 存" iconCls="icon-save">
                    athene.form.get('student-edit-${number}').update();
                </@Button>
                    <#if student.status??&&student.status=="in">
                    <@Button label="离 校" iconCls="icon-leave">
                        athene.form.get('student-edit-${number}').handler('quitUrl','messageQuit','onQuit');
                    </@Button>
                        <#elseif student.status??&&student.status=="out">
                        <@Button label="返 校" iconCls="icon-reset">
                            athene.form.get('student-edit-${number}').handler('enterUrl','messageEnter','onEnter');
                        </@Button>
                    </#if>
                <@Button label="删 除" iconCls="icon-delete">
                    athene.form.get('student-edit-${number}').remove();
                </@Button>
                    <#else >
                    <@Button label="保 存" iconCls="icon-save">
                        athene.form.get('student-edit-${number}').save();
                    </@Button>
                </#if>
                <@Button label="关 闭" iconCls="icon-close">
                    athene.window.get('${number}').close();
                </@Button>
                </div>
            </td>
        </tr>
    </table>
</div>
<script type="text/javascript">
    $I("student-edit-${number}").doFocus();
    athene.checkgroup.create({
        id:"subject-list-${number}",
        name:"subjectList",
        items:<@Json src=subjectList/>,
    <#if student??>
        selectItems:<@Json src=student.subjectList/>,
    </#if>
        binding:"${number}",
        itemWidth:90,
        limit:6,
        listKey:"name",
        listValue:"id"
    });
    athene.form.create({
        id:"student-edit-${number}",
        binding:"${number}",
        saveUrl:"studentSave",
        updateUrl:"studentUpdate",
        removeUrl:"studentDelete",
        enterUrl:"studentEnter",
        quitUrl:"studentQuit",
        dialogWidth:400,
        messageSave:function(confirmRow, row) {
            return "<div class='form-confirm-label'>请您确认要新增的学生[" + (row.name || "") + "]</div>" + confirmRow;
        },
        messageUpdate:function(confirmRow, row) {
            return "<div class='form-confirm-label'>请您确认要更新的学生[" + (row.name || "") + "]</div>" + confirmRow;
        },
        messageRemove:function(confirmRow, row) {
            return"<div class='form-confirm-label'>请您确认要删除的学生[" + (row.name || "") + "]</div>" + confirmRow;
        },
        messageEnter:function(confirmRow, row) {
            return "<div class='form-confirm-label'>请您确认要返校的学生[" + (row.name || "") + "]</div>" + confirmRow;
        },
        messageQuit:function(confirmRow, row) {
            return "<div class='form-confirm-label'>请您确认要离校的学生[" + (row.name || "") + "]</div>" + confirmRow;
        },
        onSuccess:function(d) {
            var grid = athene.grid.get("student-list-${openerNumber}");
            grid && (grid.submit = true);
        },
        onSave:function(row) {
            var win = athene.window.get("${number}");
            win.floatMessage = "新增学生[" + row.name + "]成功！";
            win.close();
        },
        onUpdate:function(row) {
            var win = athene.window.get("${number}");
            win.floatMessage = "更新学生[" + row.name + "]成功！";
            win.close();
        },
        onRemove:function(row) {
            var win = athene.window.get("${number}");
            win.floatMessage = "删除学生[" + row.name + "]成功！";
            win.close();
        },
        onEnter:function(row) {
            var win = athene.window.get("${number}");
            win.floatMessage = "学生[" + row.name + "]返校成功！";
            win.close();
        },
        onQuit:function(row) {
            var win = athene.window.get("${number}");
            win.floatMessage = "学生[" + row.name + "]离校成功！";
            win.close();
        },
        items:[
            {id:"id-${number}",type:"Hidden"},
            {id:"login-id-${number}",type:"TextField",label:"登录名"},
            {id:"name-${number}",type:"TextField",label:"姓名"},
            {id:"sex-${number}",type:"ComboBox",label:"性别"},
            {id:"qq-${number}",type:"TextField",label:"QQ"},
            {id:"email-${number}",type:"TextField",label:"电子邮件"},
            {id:"telephone-${number}",type:"TextField",label:"固定电话"},
            {id:"mobile-${number}",type:"TextField",label:"手机号码"},
            {id:"school-${number}",type:"TextField",label:"所在学校"},
            {id:"gmt-enter-${number}",type:"TextField",label:"入校时间"},
            {id:"gmt-quit-${number}",type:"TextField",label:"离校时间"},
            {id:"address-${number}",type:"TextField",label:"地址"},
            {id:"subject-list-${number}",type:"CheckGroup",label:"科目"},
            {id:"description-${number}",type:"TextArea"}
        ]
    });
</script>