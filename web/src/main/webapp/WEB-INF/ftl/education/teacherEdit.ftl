<@ExistError/>
<div class="fm-div">
    <table id="teacher-edit-${number}" style="width:550px;margin-left:0px;" cellpadding="0" cellspacing="0">
        <tr>
        <@TextField label="登录名" binding="teacher.loginId" require="true" existReadOnly="true"
        explain="例如名字[吕秀云],格式为[xiuyun.luxy],一旦注册不能更改"/>
        <@TextField label="姓名" binding="teacher.name" require="true"/>
        </tr>
        <tr>
        <@ComboBox label="性别" binding="teacher.sex" require="true" list=sexList listKey="name" listValue="value"/>
        <@ComboBox label="教师等级" binding="teacher.level" require="true" list=teacherLevelList listKey="name" listValue="value"/>
        </tr>
        <tr>
        <@ComboBox label="学历" binding="teacher.education" require="true" list=educationList listKey="name" listValue="value"/>
        <@TextField label="毕业院校" binding="teacher.graduateSchool" require="true"/>
        </tr>
        <tr>
        <@TextField label="专业" binding="teacher.major" require="true"/>
        <@ComboBox label="工作经验" binding="teacher.experience" require="true" list=experienceList listKey="name" listValue="value"/>
        </tr>
        <tr>
        <@TextField label="QQ" binding="teacher.qq"/>
        <@TextField label="电子邮件" binding="teacher.email"/>
        </tr>
        <tr>
        <@TextField label="固定电话" binding="teacher.telephone"/>
        <@TextField label="手机号码" binding="teacher.mobile" require="true"/>
        </tr>
        <tr>
            <td colspan="2">
            <@TextField td="false" label="地址" binding="teacher.address" style="width:438px;"/>
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
            <@TextArea td="false" label="整体描述" binding="teacher.description" style="height:80px;width:441px;"/>
            <@Hidden binding="teacher.id"/>
            </td>
        </tr>
        <tr>
            <td colspan="2" align="center" valign="top">
            <#if teacher??>
            <@Button label="保 存" iconCls="icon-save">
                athene.form.get('teacher-edit-${number}').update();
            </@Button>
                <#if teacher.status??&&teacher.status!="out">
                <@Button label="离 职" iconCls="icon-leave">
                    athene.form.get('teacher-edit-${number}').handler('quitUrl','messageQuit','onQuit');
                </@Button>
                </#if>
            <@Button label="删 除" iconCls="icon-delete">
                athene.form.get('teacher-edit-${number}').remove();
            </@Button>
                <#else >
                <@Button label="保 存" iconCls="icon-save">
                    athene.form.get('teacher-edit-${number}').save();
                </@Button>
            </#if>
            <@Button label="关 闭" iconCls="icon-close">
                athene.window.get('${number}').close();
            </@Button>
            </td>
        </tr>
    </table>
</div>
<script type="text/javascript">
    $I("teacher-edit-${number}").doFocus();
    athene.checkgroup.create({
        id:"subject-list-${number}",
        name:"subjectList",
        items:<@Json src=subjectList/>,
    <#if teacher??>
        selectItems:<@Json src=teacher.subjectList/>,
    </#if>
        binding:"${number}",
        itemWidth:90,
        limit:6,
        listKey:"name",
        listValue:"id"
    });
    athene.form.create({
        id:"teacher-edit-${number}",
        binding:"${number}",
        saveUrl:"teacherSave",
        updateUrl:"teacherUpdate",
        removeUrl:"teacherDelete",
        quitUrl:"teacherQuit",
        dialogWidth:400,
        messageSave:function(confirmRow, row) {
            return "<div class='form-confirm-label'>请您确认要新增的老师[" + (row.name || "") + "]</div>" + confirmRow;
        },
        messageUpdate:function(confirmRow, row) {
            return "<div class='form-confirm-label'>请您确认要更新的老师[" + (row.name || "") + "]</div>" + confirmRow;
        },
        messageRemove:function(confirmRow, row) {
            return "<div class='form-confirm-label'>请您确认要删除的老师[" + (row.name || "") + "]</div>" + confirmRow;
        },
        messageQuit:function(confirmRow, row) {
            return "<div class='form-confirm-label'>请您确认要离职的老师[" + (row.name || "") + "]</div>" + confirmRow;
        },
        onSuccess:function(d) {
            var grid = athene.grid.get("teacher-list-${openerNumber}");
            grid && (grid.submit = true);
        },
        onSave:function(row) {
            var win = athene.window.get("${number}");
            win.floatMessage = "新增教师[" + row.name + "]成功！";
            win.close();
        },
        onUpdate:function(row) {
            var win = athene.window.get("${number}");
            win.floatMessage = "更新教师[" + row.name + "]成功！";
            win.close();
        },
        onRemove:function(row) {
            var win = athene.window.get("${number}");
            win.floatMessage = "删除教师[" + row.name + "]成功！";
            win.close();
        },
        onQuit:function(row) {
            var win = athene.window.get("${number}");
            win.floatMessage = "教师[" + row.name + "]离职成功！";
            win.close();
        },
        items:[
            {id:"id-${number}",type:"Hidden"},
            {id:"login-id-${number}",type:"TextField",label:"登录名"},
            {id:"name-${number}",type:"TextField",label:"姓名"},
            {id:"sex-${number}",type:"ComboBox",label:"性别"},
            {id:"level-${number}",type:"ComboBox",label:"教师等级"},
            {id:"education-${number}",type:"ComboBox",label:"学历"},
            {id:"graduate-school-${number}",type:"TextField",label:"毕业院校"},
            {id:"major-${number}",type:"TextField",label:"专业"},
            {id:"experience-${number}",type:"ComboBox",label:"工作经验"},
            {id:"qq-${number}",type:"TextField",label:"QQ"},
            {id:"email-${number}",type:"TextField",label:"电子邮件"},
            {id:"telephone-${number}",type:"TextField",label:"固定电话"},
            {id:"mobile-${number}",type:"TextField",label:"手机号码"},
            {id:"address-${number}",type:"TextField",label:"地址"},
            {id:"subject-list-${number}",type:"CheckGroup",label:"科目"},
            {id:"description-${number}",type:"TextArea"}
        ]
    });
</script>
