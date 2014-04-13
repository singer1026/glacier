<@ExistError/>
<div class="fm-div">
    <div id="task-edit-${number}" style="width:400px;margin-left:0px;">
    <@TextField td="false" label="标题" binding="task.title" require="true" style="width:247px;"/>
    <@DateField label="提醒日期" binding="task.notifyDate" onFocus="WdatePicker();"
    readOnly="true" require="true" style="width:247px;"/>
    <@TextArea td="false" label="摘要" binding="task.description" style="height:100px;width:250px;"/>
    <#if task??>
    <@LabelField label="状态">
        <span class="fm-readonly">${task.statusDisplay!}</span>
    </@LabelField>
        <#if task.status="finish">
        <@LabelField label="处理人">
            <span class="fm-readonly">${task.disposerDisplay!}</span>
        </@LabelField>
        <@LabelField label="处理时间">
            <span class="fm-readonly">${task.disposeTimeDisplay!}</span>
        </@LabelField>
        <@LabelField label="处理意见">
            <span class="fm-readonly">${task.disposeOpinion?default("无")}</span>
        </@LabelField>
            <#else >
            <@TextArea td="false" label="处理意见" binding="task.disposeOpinion" style="height:100px;width:250px;"/>
        </#if>
    </#if>
    <@Hidden binding="task.id"/>
        <div style="text-align:center;">
        <#if task??>
            <#if task.status="active">
            <@Button label="完 成" baseRole="base_task_approve" cls="btn-blue" iconCls="icon-finish">
                athene.form.get('task-edit-${number}').handler('approveUrl','messageApprove','onApprove');
            </@Button>
            </#if>
        <@Button label="保 存" baseRole="base_task_update" iconCls="icon-save">
            athene.form.get('task-edit-${number}').update();
        </@Button>
        <@Button label="删 除" baseRole="base_task_delete" iconCls="icon-delete">
            athene.form.get('task-edit-${number}').remove();
        </@Button>
            <#else >
            <@Button label="保 存" baseRole="base_task_add" iconCls="icon-save">
                athene.form.get('task-edit-${number}').save();
            </@Button>
        </#if>
        <@Button label="关 闭" iconCls="icon-close">
            athene.window.get('${number}').close();
        </@Button>
        </div>
    </div>
</div>
<script type="text/javascript">
    $I("task-edit-${number}").doFocus();
    athene.form.create({
        id:"task-edit-${number}",
        binding:"${number}",
        saveUrl:"taskSave",
        updateUrl:"taskUpdate",
        removeUrl:"taskDelete",
        approveUrl:"taskApprove",
        dialogWidth:400,
        messageSave:function(confirmRow, row) {
            return "<div class='form-confirm-label'>请您确认要新增的任务[" + (row.title || "") + "]</div>" + confirmRow;
        },
        messageUpdate:function(confirmRow, row) {
            return "<div class='form-confirm-label'>请您确认要更新的任务[" + (row.title || "") + "]</div>" + confirmRow;
        },
        messageRemove:function(confirmRow, row) {
            return "<div class='form-confirm-label'>请您确认要删除的任务[" + (row.title || "") + "]</div>" + confirmRow;
        },
        messageApprove:function(confirmRow, row) {
            return "<div class='form-confirm-label'>请您确认要完成的任务[" + (row.title || "") + "]</div>" + confirmRow;
        },
        onSuccess:function(d) {
            var grid = athene.grid.get("task-list-${openerNumber}");
            grid && (grid.submit = true);
        },
        onSave:function(row) {
            var win = athene.window.get("${number}");
            win.floatMessage = "新增任务[" + row.title + "]成功！";
            win.close();
        },
        onUpdate:function(row) {
            var win = athene.window.get("${number}");
            win.floatMessage = "更新任务[" + row.title + "]成功！";
            win.close();
        },
        onRemove:function(row) {
            var win = athene.window.get("${number}");
            win.floatMessage = "删除任务[" + row.title + "]成功！";
            win.close();
        },
        onApprove:function(row) {
            var win = athene.window.get("${number}");
            win.floatMessage = "完成任务[" + row.title + "]成功！";
            win.close();
        },
        items:[
            {id:"id-${number}",type:"Hidden"},
            {id:"title-${number}",type:"TextField",label:"标题"},
            {id:"notify-date-${number}",type:"TextField",label:"最早提醒日期"},
            {id:"description-${number}",type:"TextArea"},
            {id:"dispose-opinion-${number}",type:"TextArea",label:"处理意见"}
        ]
    });
</script>
