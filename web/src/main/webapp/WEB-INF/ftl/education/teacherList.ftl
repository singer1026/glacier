<div class="win-inner">
    <div class="search-frame">
        <div id="teacher-condition-${number}">
        <@TextField layoutCls="sm-item fl" labelCls="sm-label" label="教师姓名" binding="name" td="false"/>
        <@ComboBox layoutCls="sm-item fl" labelCls="sm-label" label="状态" binding="status" list=teacherStatusList
        listKey="name" listValue="value" defaultValue="in" td="false"/>
        <@ComboBox layoutCls="sm-item fl" labelCls="sm-label" label="科目" binding="subjectId" list=subjectList
        listKey="name" listValue="id" td="false"/>
            <div class="clear"/>
        </div>
    </div>
    <div class="search-button">
    <@Button label="查 询" baseRole="base_teacher_list" iconCls="icon-search">
        page.gridSearch('teacher-condition-${number}','teacher-list-${number}');
    </@Button>
    <@Button label="新 增" baseRole="base_teacher_add" iconCls="icon-add">
        page.teacherAdd({openerNumber:'${number}'},this);
    </@Button>
    </div>
    <div id="teacher-list-${number}"/>
</div>
<script type="text/javascript">
    athene.grid.create({
        id:"teacher-list-${number}",
        binding:"${number}",
        pagination:true,
        url:"teacherQuery",
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
                <#if hasBaseRole("base_teacher_update")>
                    rtnString = "<span class='cp btn-icon icon-edit' title='编辑' onclick=\"page.teacherEdit({id:"
                            + row.id + ",title:'" + row.name + "',openerNumber:'${number}'},this);\"/>";
                    <#elseif hasBaseRole("base_teacher_view")>
                        rtnString = "<span class='cp btn-icon icon-view' title='查看' onclick=\"page.teacherView({id:"
                                + row.id + ",title:'" + row.name + "',openerNumber:'${number}'},this);\"/>";
                </#if>
                    return rtnString;
                }
            },
            {
                title:"姓名",
                width:70,
                key:"name",
                formatter:function(value, row) {
                <#if hasBaseRole("base_teacher_schedule_view")>
                    var rtnString = "<a href='javascript:void(0);' onclick=\"athene.page.teacherScheduleView({title:'" + value + "功课表',id:" + row.id + "},this)\">" + value + "</a>";
                    return rtnString;
                    <#else>
                        return value;
                </#if>
                }
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
                title:"教师等级",
                key:"levelDisplay",
                width:80
            },
            {
                title:"工作经验",
                key:"experienceDisplay",
                width:80
            },
            {
                title:"学历",
                key:"educationDisplay",
                width:70
            },

            {
                title:"毕业院校",
                key:"graduateSchool",
                width:180
            },
            {
                title:"专业",
                key:"major",
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
                title:"手机号码",
                key:"mobile",
                width:100
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
    $I("teacher-condition-${number}").doFocus();
</script>
