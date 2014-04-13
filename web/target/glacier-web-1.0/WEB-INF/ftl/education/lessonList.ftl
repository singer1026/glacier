<div class="win-inner">
    <div class="search-frame">
        <div id="lp-condition-${number}">
        <@TextField layoutCls="sm-item fl" labelCls="sm-label" label="教师姓名" binding="teacherName" td="false"/>
        <@TextField layoutCls="sm-item fl" labelCls="sm-label" label="学生姓名" binding="studentName" td="false"/>
        <@ComboBox layoutCls="sm-item fl" labelCls="sm-label" label="周期" binding="dayOfWeek" list=dayOfWeekList
        listKey="name" listValue="value" td="false"/>
        <@ComboBox layoutCls="sm-item fl" labelCls="sm-label" label="科目" binding="subjectId" list=subjectList
        listKey="name" listValue="id" td="false"/>
            <div class="clear"/>
        </div>
    </div>
    <div class="search-button">
    <@Button label="查 询" baseRole="base_lesson_list" iconCls="icon-search">
        page.gridSearch('lp-condition-${number}','lesson-list-${number}');
    </@Button>
    <@Button label="新 增" baseRole="base_lesson_add" iconCls="icon-add">
        page.lessonAdd({openerNumber:'${number}'},this);
    </@Button>
    <@Button label="课程统计分布" baseRole="base_lesson_day_statistics" iconCls="icon-statistics">
        page.lessonDayStatistics(this);
    </@Button>
    </div>
    <div id="lesson-list-${number}"/>
</div>
<script type="text/javascript">
    athene.grid.create({
        id:"lesson-list-${number}",
        pagination:true,
        url:"lessonQuery",
        pageSize:10,
        binding:"${number}",
        defaultMap:{
            ordering:['l.gmt_modify DESC']
        },
        onRender:function() {
            var win = athene.window.get('${number}');
            win && win.onSetSize && win.onSetSize.call(win, win.width, win.height);
        },
        colModels:[
        <#if hasBaseRole("base_lesson_update")>
            {
                title:"",
                width:27,
                key:"id",
                resizeable:false,
                formatter:function(value, row, i) {
                    var rtnString = "<span class='cp btn-icon icon-edit' title='编辑' onclick=\"page.lessonEdit({id:"
                            + row.id + ",openerNumber:'${number}'},this);\"/>";
                    return rtnString;
                }
            },
        </#if>
            {
                title:"教师",
                key:"lessonTeacherList",
                formatter:function(value, row) {
                    var items = value,len = items.length,html = [];
                    for (var i = 0; i < len; i++) {
                        var item = items[i];
                        html.push(item.teacherName || "");
                        (i + 1) < len && html.push("&nbsp;");
                    }
                    return html.join("");
                },
                width:120
            },
            {
                title:"学生",
                key:"lessonStudentList",
                formatter:function(value, row) {
                    var items = value,len = items.length,html = [];
                    for (var i = 0; i < len; i++) {
                        var item = items[i];
                        html.push(item.studentName || "");
                        (i + 1) < len && html.push("&nbsp;");
                    }
                    return html.join("");
                },
                width:120
            },
            {
                title:"科目",
                key:"subjectName",
                width:100
            },
            {
                title:"课程安排",
                key:"lessonPlanList",
                width:250,
                formatter:function(value, row) {
                    var items = value,len = items.length,html = [];
                    for (var i = 0; i < len; i++) {
                        var item = items[i];
                        html.push(item.dayOfWeekDisplay || "");
                        html.push("&nbsp;")
                        html.push(item.startTimeDisplay || "");
                        html.push("-")
                        html.push(item.endTimeDisplay || "");
                        html.push("&nbsp;")
                        html.push(item.timeLength || "");
                        html.push("分钟");
                        (i + 1) < len && html.push("<br/>");
                    }
                    return html.join("");
                }
            },
            {
                title:"创建时间",
                key:"gmtCreateDisplay",
                width:130
            },
            {
                title:"创建人",
                key:"creatorDisplay",
                width:70
            },
            {
                title:"修改时间",
                key:"gmtModifyDisplay",
                width:130
            } ,
            {
                title:"修改人",
                key:"modifierDisplay",
                width:70
            }
        ]
    });
    $I("lp-condition-${number}").doFocus();
</script>







