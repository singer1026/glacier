<div class='win-inner'>
    <div class="search-frame">
        <div id="student-condition-${number}">
        <@TextField layoutCls="sm-item fl" labelCls="sm-label" label="学生姓名" binding="name" td="false"/>
        <@ComboBox layoutCls="sm-item fl" labelCls="sm-label" label="状态" binding="status" list=studentStatusList
        listKey="name" listValue="value" defaultValue="in" td="false"/>
        <@ComboBox layoutCls="sm-item fl" labelCls="sm-label" label="科目" binding="subjectId" list=subjectList
        listKey="name" listValue="id" td="false"/>
        <@ComboBox layoutCls="sm-item fl" labelCls="sm-label" label="课程" binding="hasLesson" list=hasLessonList
        listKey="name" listValue="value" td="false"/>
            <div class="clear"/>
        </div>
    </div>
    <div class="search-button">
    <@Button label="查 询" baseRole="base_student_list" iconCls="icon-search">
        page.gridSearch('student-condition-${number}','student-list-${number}');
    </@Button>
    <@Button label="新 增" baseRole="base_student_add" iconCls="icon-add">
        page.studentAdd({openerNumber:'${number}'},this);
    </@Button>
    <@Button label="导出Excel" baseRole="base_student_list_excel" iconCls="icon-excel">
        page.studentListExcel('student-condition-${number}');
    </@Button>
    </div>
    <div id="student-list-${number}"/>
</div>
<script type="text/javascript">
    athene.grid.create({
        id:"student-list-${number}",
        pagination:true,
        binding:"${number}",
        url:"studentQuery",
        pageSize:10,
        defaultMap:{
            ordering:['CONVERT(u.name USING GB2312)']
        },
        onRender:function() {
            var win = athene.window.get('${number}');
            win && win.onSetSize && win.onSetSize.call(win, win.width, win.height);
        },
        colModels:[
            {
                title:"",
                width:27,
                key:"id",
                resizeable:false,
                formatter:function(value, row) {
                    var rtnString = "";
                <#if hasBaseRole("base_student_update")>
                    rtnString = "<span class='cp btn-icon icon-edit' title='编辑' onclick=\"page.studentEdit({id:"
                            + row.id + ",title:'" + row.name + "',openerNumber:'${number}'},this);\"/>";
                    <#elseif hasBaseRole("base_student_view")>
                        rtnString = "<span class='cp btn-icon icon-view' title='查看' onclick=\"page.studentView({id:"
                                + row.id + ",title:'" + row.name + "',openerNumber:'${number}'},this);\"/>";
                </#if>
                    return rtnString;
                }
            },
            {
                title:"姓名",
                key:"name",
                width:70
            },
            {
                title:"状态",
                key:"statusDisplay",
                width:50
            },
            {
                title:"性别",
                key:"sexDisplay",
                width:50
            },
            {
                title:"入校时间",
                key:"gmtEnterDisplay",
                width:100
            },
            {
                title:"科目",
                key:"subjectList",
                width:140,
                formatter:function(value, row) {
                    if (value) {
                        var items = value,len = items.length,html = [];
                        for (var i = 0; i < len; i++) {
                            var item = items[i];
                            html.push(item.name || "");
                            (i + 1) < len && html.push("&nbsp;");
                        }
                        return html.join("");
                    } else {
                        return null;
                    }
                }
            },
            {
                title:"学校",
                key:"school",
                width:150
            },
            {
                title:"手机号码",
                width:100,
                key:"mobile"
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
    $I("student-condition-${number}").doFocus();
</script>
