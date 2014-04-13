<@ExistError/>
<div class="fm-div">
    <div id="expense-edit-${number}" style="width:300px;margin-left:0px;">
    <@TextField td="false" label="标题" binding="expense.title" require="true"/>
    <#if hasBaseRole("base_expense_full_edit")>
    <@DateField label="支出日期" binding="expense.expendDate" onFocus="WdatePicker();" readOnly="true" require="true"/>
    <@TextField td="false" label="金额" binding="expense.amount" require="true"/>
        <#else >
            <#if !expense?? || expense.isToday>
            <@TextField td="false" label="金额" binding="expense.amount" require="true"/>
                <#else >
                <@LabelField td="false" label="金额">
                    <span class="fm-readonly cr">${expense.amountDisplay}</span>

                    <div class="clear"/>
                </@LabelField>
                    <span class="hide">
                    <@TextField td="false" binding="expense.amount" error="false"/>
                    </span>
            </#if>
    </#if>
    <@ComboBox td="false" label="类目" binding="expense.category" require="true" list=expenseCategoryList listKey="name" listValue="value"/>
    <@TextArea td="false" label="整体描述" binding="expense.description" style="height:100px;width:166px;"/>
    <@Hidden binding="expense.id"/>
        <div style="text-align:center;">
        <#if expense??>
        <@Button label="保 存" baseRole="base_expense_update" iconCls="icon-save">
            athene.form.get('expense-edit-${number}').update();
        </@Button>
            <#if hasBaseRole("base_expense_full_edit") || expense.isToday>
            <@Button label="删 除" baseRole="base_expense_delete" iconCls="icon-delete">
                athene.form.get('expense-edit-${number}').remove();
            </@Button>
            </#if>
            <#else >
            <@Button label="保 存" baseRole="base_expense_add" iconCls="icon-save">
                athene.form.get('expense-edit-${number}').save();
            </@Button>
        </#if>
        <@Button label="关 闭" iconCls="icon-close">
            athene.window.get('${number}').close();
        </@Button>
        </div>
    </div>
</div>
<script type="text/javascript">
    $I("expense-edit-${number}").doFocus();
    athene.form.create({
        id:"expense-edit-${number}",
        binding:"${number}",
        saveUrl:"expenseSave",
        updateUrl:"expenseUpdate",
        removeUrl:"expenseDelete",
        dialogWidth:400,
        messageSave:function(confirmRow, row) {
            return "<div class='form-confirm-label'>请您确认要新增的支出[" + (row.title || "") + "]</div>" + confirmRow;
        },
        messageUpdate:function(confirmRow, row) {
            return "<div class='form-confirm-label'>请您确认要更新的支出[" + (row.title || "") + "]</div>" + confirmRow;
        },
        messageRemove:function(confirmRow, row) {
            return "<div class='form-confirm-label'>请您确认要删除的支出[" + (row.title || "") + "]</div>" + confirmRow;
        },
        onSuccess:function(d) {
            var grid = athene.grid.get("expense-list-${openerNumber}");
            grid && (grid.submit = true);
        },
        onSave:function(row) {
            var win = athene.window.get("${number}");
            win.floatMessage = "新增支出[" + row.title + "]成功！";
            win.close();
        },
        onUpdate:function(row) {
            var win = athene.window.get("${number}");
            win.floatMessage = "更新支出[" + row.title + "]成功！";
            win.close();
        },
        onRemove:function(row) {
            var win = athene.window.get("${number}");
            win.floatMessage = "删除支出[" + row.title + "]成功！";
            win.close();
        },
        items:[
            {id:"id-${number}",type:"Hidden"},
            {id:"title-${number}",type:"TextField",label:"标题"},
            {id:"expend-date-${number}",type:"TextField",label:"支出日期"},
            {id:"amount-${number}",type:"TextField",label:"金额"},
            {id:"category-${number}",type:"ComboBox",label:"类目"},
            {id:"description-${number}",type:"TextArea"}
        ]
    });
</script>
