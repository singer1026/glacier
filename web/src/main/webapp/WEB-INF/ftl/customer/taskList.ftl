<div class="win-inner">
    <div class="search-frame">
        <div id="task-condition-${number}">
        <@DateField layoutCls="sm-item fl" labelCls="sm-label" label="开始提醒" binding="timeStart"
        onFocus="WdatePicker();" readOnly="true" td="false"/>
        <@DateField layoutCls="sm-item fl" labelCls="sm-label" label="结束提醒" binding="timeEnd"
        onFocus="WdatePicker();" readOnly="true" td="false"/>
        <@ComboBox layoutCls="sm-item fl" labelCls="sm-label" label="状态" binding="status" list=taskStatusList
        listKey="name" listValue="value" td="false"/>
            <div class="clear"/>
        </div>
    </div>
    <div class="search-button">
    <@Button label="查 询" baseRole="base_task_list" iconCls="icon-search">
        page.gridSearch('task-condition-${number}','task-list-${number}');
    </@Button>
    <@Button label="新 增" baseRole="base_task_add" iconCls="icon-add">
        page.taskAdd({openerNumber:'${number}'},this);
    </@Button>
    </div>
    <div id="task-list-${number}"/>
</div>
<script type="text/javascript">
    athene.grid.create({
        id:"task-list-${number}",
        binding:"${number}",
        pagination:true,
        url:"taskQuery",
        pageSize:10,
        defaultMap:{
            ordering:['t.notify_date DESC','t.id DESC']
        },
        onRender:function() {
            var win = athene.window.get('${number}');
            win && win.onSetSize && win.onSetSize.call(win, win.width, win.height);
        },
        colModels:[
        <#if hasBaseRole("base_task_update")>
            {
                width:27,
                resizeable:false,
                formatter:function(value, row) {
                    var rtnString = "<span class='cp btn-icon icon-edit' title='编辑' onclick=\"page.taskEdit({id:"
                            + row.id + ",title:'" + row.title + "',openerNumber:'${number}'},this);\"/>";
                    return rtnString;
                }
            },
        </#if>
            {
                title:"标题",
                width:250,
                key:"title"
            },
            {
                title:"状态",
                width:60,
                key:"statusDisplay"
            },
            {
                title:"提醒日期",
                width:100,
                key:"notifyDateDisplay"
            },
            {
                title:"处理时间",
                key:"disposeTimeDisplay",
                width:130
            },
            {
                title:"处理人",
                width:100,
                key:"disposerDisplay"
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
    $I("task-condition-${number}").doFocus();
</script>
