<@ExistError/>
<div class="fm-div">
    <div id="income-edit-${number}" style="width:300px;margin-left:0px;">
    <@TextField td="false" label="标题" binding="income.title" require="true"/>
    <#if hasBaseRole("base_income_full_edit")>
    <@DateField label="收入日期" binding="income.incomeDate" onFocus="WdatePicker();" readOnly="true" require="true"/>
    <@TextField td="false" label="金额" binding="income.amount" require="true"/>
        <#else >
            <#if !income?? || income.isToday>
            <@TextField td="false" label="金额" binding="income.amount" require="true"/>
                <#else >
                <@LabelField td="false" label="金额">
                    <span class="fm-readonly cr">${income.amountDisplay}</span>

                    <div class="clear"/>
                </@LabelField>
                    <span class="hide">
                    <@TextField td="false" binding="income.amount" error="false"/>
                    </span>
            </#if>
    </#if>
    <@ComboBox td="false" label="类目" binding="income.category" require="true" list=incomeCategoryList listKey="name" listValue="value"/>
    <@TextArea td="false" label="整体描述" binding="income.description" style="height:100px;width:166px;"/>
    <@Hidden binding="income.id"/>
        <div style="text-align:center;">
        <#if income??>
        <@Button label="保 存" baseRole="base_income_update" iconCls="icon-save">
            athene.form.get('income-edit-${number}').update();
        </@Button>
            <#if hasBaseRole("base_income_full_edit") || income.isToday>
            <@Button label="删 除" baseRole="base_income_delete" iconCls="icon-delete">
                athene.form.get('income-edit-${number}').remove();
            </@Button>
            </#if>
            <#else >
            <@Button label="保 存" baseRole="base_income_add" iconCls="icon-save">
                athene.form.get('income-edit-${number}').save();
            </@Button>
        </#if>
        <@Button label="关 闭" iconCls="icon-close">
            athene.window.get('${number}').close();
        </@Button>
        </div>
    </div>
</div>
<script type="text/javascript">
    $I("income-edit-${number}").doFocus();
    athene.form.create({
        id:"income-edit-${number}",
        binding:"${number}",
        saveUrl:"incomeSave",
        updateUrl:"incomeUpdate",
        removeUrl:"incomeDelete",
        dialogWidth:400,
        messageSave:function(confirmRow, row) {
            return "<div class='form-confirm-label'>请您确认要新增的收入[" + (row.title || "") + "]</div>" + confirmRow;
        },
        messageUpdate:function(confirmRow, row) {
            return "<div class='form-confirm-label'>请您确认要更新的收入[" + (row.title || "") + "]</div>" + confirmRow;
        },
        messageRemove:function(confirmRow, row) {
            return "<div class='form-confirm-label'>请您确认要删除的收入[" + (row.title || "") + "]</div>" + confirmRow;
        },
        onSuccess:function(d) {
            var grid = athene.grid.get("income-list-${openerNumber}");
            grid && (grid.submit = true);
        },
        onSave:function(row) {
            var win = athene.window.get("${number}");
            win.floatMessage = "新增收入[" + row.title + "]成功！";
            win.close();
        },
        onUpdate:function(row) {
            var win = athene.window.get("${number}");
            win.floatMessage = "更新收入[" + row.title + "]成功！";
            win.close();
        },
        onRemove:function(row) {
            var win = athene.window.get("${number}");
            win.floatMessage = "删除收入[" + row.title + "]成功！";
            win.close();
        },
        items:[
            {id:"id-${number}",type:"Hidden"},
            {id:"title-${number}",type:"TextField",label:"标题"},
            {id:"income-date-${number}",type:"TextField",label:"收入日期"},
            {id:"amount-${number}",type:"TextField",label:"金额"},
            {id:"category-${number}",type:"ComboBox",label:"类目"},
            {id:"description-${number}",type:"TextArea"}
        ]
    });
</script>
