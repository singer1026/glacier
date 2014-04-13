<@ExistError/>
<div style="width: 100%;">
    <div id="lesson-edit-${number}">
        <div id="lp-list-${number}"/>
        <div class="fm-error ml5 mt3 mb3" id="lp-list-${number}-error">
            <div class="btn-icon icon-error fl"></div>
            <div class="fm-error-content fl"></div>
            <div class="clear"/>
        </div>
        <div id="teacher-name-list-${number}" class="mt3 mb3"/>
        <div class="fm-error ml5 mt3 mb3" id="teacher-name-list-${number}-error">
            <div class="btn-icon icon-error fl"></div>
            <div class="fm-error-content fl"></div>
            <div class="clear"/>
        </div>
        <div id="student-name-list-${number}" class="mt3 mb3"/>
        <div class="fm-error ml5 mt3 mb3" id="student-name-list-${number}-error">
            <div class="btn-icon icon-error fl"></div>
            <div class="fm-error-content fl"></div>
            <div class="clear"/>
        </div>
        <div class="search-button mt3" style="background-color: #ebf2fa;">
        <@Button label="新增课程安排" iconCls="icon-add">
            page.lessonPlanAdd({openerNumber:'${number}'},this);
        </@Button>
        <@Button label="选择教师" iconCls="icon-select">
            page.lessonTeacher({openerNumber:'${number}'},this);
        </@Button>
        <@Button label="清空教师列表" iconCls="icon-clear">
            athene.listfield.get('teacher-name-list-${number}').clearItems();
        </@Button>
        <@Button label="选择学生" iconCls="icon-select">
            page.lessonStudent({openerNumber:'${number}'},this);
        </@Button>
        <@Button label="清空学生列表" iconCls="icon-clear">
            athene.listfield.get('student-name-list-${number}').clearItems();
        </@Button>
        </div>
        <div style="margin-top:5px;">
            <div style="margin-left:-28px;">
            <@ComboBox td="false" label="安排科目" binding="lesson.subjectId" require="true" list=subjectList listKey="name" listValue="id"/>
            <@TextArea td="false" label="备注" binding="lesson.remark" style="height:80px;"/>
            <@Hidden binding="lesson.id"/>
            </div>
            <div class="search-button" style="background-color: #ebf2fa;">
            <#if lesson??>
            <@Button label="保 存" iconCls="icon-save">
                athene.form.get('lesson-edit-${number}').update();
            </@Button>
            <@Button label="删 除" iconCls="icon-delete">
                athene.form.get('lesson-edit-${number}').remove();
            </@Button>
                <#else >
                <@Button label="保 存" iconCls="icon-save">
                    athene.form.get('lesson-edit-${number}').save();
                </@Button>
            </#if>
            <@Button label="关 闭" iconCls="icon-close">
                athene.window.get('${number}').close();
            </@Button>
                <label id="lesson-edit-error-${number}" class="ml3 cr"></label>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $I("lesson-edit-${number}").doFocus();
    athene.listfield.create({
        id:"student-name-list-${number}",
        name:"lessonStudent",
    <#if lesson??>
        items:<@Json src=lesson.lessonStudentList/>,
    </#if>
        binding:"${number}",
        title:"学生列表",
        listKey:"studentName",
        listValue:"studentId",
        formatter:function(i, item) {
            var html = [];
            html.push(i < 9 ? "0" + (i + 1) : i + 1);
            html.push("." + item.studentName);
            return html.join("");
        },
        onSetItems:function(items) {
            if (items) {
                if (items.length == 1) {
                    $I("lesson-type-${number}").html("小课");
                } else if (items.length > 1) {
                    $I("lesson-type-${number}").html("大课");
                } else {
                    $I("lesson-type-${number}").html("数据异常");
                }
            } else {
                alert("数据异常,数组为null");
            }
        }
    });
    athene.listfield.create({
        id:"teacher-name-list-${number}",
        name:"lessonTeacher",
        binding:"${number}",
    <#if lesson??>
        items:<@Json src=lesson.lessonTeacherList/>,
    </#if>
        title:"教师列表",
        listKey:"teacherName",
        listValue:"teacherId",
        formatter:function(i, item) {
            var arr = [];
            arr.push(i < 9 ? "0" + (i + 1) : i + 1);
            arr.push("." + item.teacherName);
            return arr.join("");
        }
    });
    athene.form.create({
        id:"lesson-edit-${number}",
        binding:"${number}",
        saveUrl:"lessonSave",
        updateUrl:"lessonUpdate",
        removeUrl:"lessonDelete",
        dialogWidth:400,
        messageUpdate:function(confirmRow, row) {
            return "<div class='form-confirm-label'>请您确认要更新的课程</div>" + confirmRow;
        },
        messageRemove:function(confirmRow, row) {
            return "<div class='form-confirm-label'>请您确认要删除的课程</div>" + confirmRow;
        },
        messageSave:function(confirmRow, row) {
            return "<div class='form-confirm-label'>请您确认要新增的课程</div>" + confirmRow;
        },
        onSuccess:function(d) {
            var grid = athene.grid.get("lesson-list-${openerNumber}");
            grid && (grid.submit = true);
        },
        onSave:function() {
            var win = athene.window.get("${number}");
            win.floatMessage = "新增课程成功！";
            win.close();
        },
        onUpdate:function() {
            var win = athene.window.get("${number}");
            win.floatMessage = "更新课程成功！";
            win.close();
        },
        onRemove:function() {
            var win = athene.window.get("${number}");
            win.floatMessage = "删除课程成功！";
            win.close();
        },
        items:[
            {id:"id-${number}",type:"Hidden"},
            {id:"lp-list-${number}",type:"Grid",label:"课程安排列表"},
            {id:"teacher-name-list-${number}",type:"ListField",label:"教师列表"},
            {id:"student-name-list-${number}",type:"ListField",label:"学生列表"},
            {id:"subject-id-${number}",type:"ComboBox",label:"安排科目"},
            {id:"remark-${number}",type:"TextArea"}
        ]
    });
    athene.grid.create({
        id:"lp-list-${number}",
        name:"lessonPlan",
    <#if lesson??>
        items:<@Json src=lesson.lessonPlanList/>,
    </#if>
        binding:"${number}",
        colModels:[
            {
                title:"操作",
                width:50,
                resizeable:false,
                formatter:function(value, row, i) {
                    return "<span class='cp btn-icon icon-edit' title='编辑'"
                            + " onclick=\"page.lessonPlanEdit({gridId:'lp-list-${number}',index:" + i + ",openerNumber:'${number}'},this);\"/>"
                            + "<span style='margin-left: 2px;' class='cp btn-icon icon-delete' title='删除'"
                            + " onclick=\"athene.grid.get('lp-list-${number}').removeRow(" + i + ");\"/>";
                }
            },
            {
                title:"课程安排",
                formatter:function(value, row) {
                    var html = [];
                    html.push(row.dayOfWeekDisplay || "");
                    html.push("&nbsp;")
                    html.push(row.startTimeDisplay || "");
                    html.push("-")
                    html.push(row.endTimeDisplay || "");
                    html.push("&nbsp;")
                    html.push(row.timeLength || "");
                    html.push("分钟");
                    return html.join("");
                },
                width:250
            },
            {
                title:"教室",
                key:"classroomName",
                width:100
            },
            {
                title:"频率",
                key:"frequencyDisplay",
                width:100
            }
        ]
    });
</script>
