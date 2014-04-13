<div class="win-inner">
    <div class="search-frame">
        <div id="expense-condition-${number}">
        <@DateField layoutCls="sm-item fl" labelCls="sm-label" label="开始时间" binding="timeStart"
        onFocus="WdatePicker();" readOnly="true" td="false"/>
        <@DateField layoutCls="sm-item fl" labelCls="sm-label" label="结束时间" binding="timeEnd"
        onFocus="WdatePicker();" readOnly="true" td="false"/>
        <@ComboBox layoutCls="sm-item fl" labelCls="sm-label" label="支出类目" binding="category" list=expenseCategoryList
        listKey="name" listValue="value" td="false"/>
        <@TextField layoutCls="sm-item fl" labelCls="sm-label" label="标题" binding="title" cls="fm-text-field" td="false"/>
            <div class="clear"/>
        </div>
    </div>
    <div class="search-button">
    <@Button label="查 询" baseRole="base_expense_list" iconCls="icon-search">
        page.gridSearch('expense-condition-${number}','expense-list-${number}');
    </@Button>
    <@Button label="新 增" baseRole="base_expense_add" iconCls="icon-add">
        page.expenseAdd({openerNumber:'${number}'},this);
    </@Button>
    <@Button label="导出Excel" baseRole="base_expense_list_excel" iconCls="icon-excel">
        page.expenseListExcel('expense-condition-${number}');
    </@Button>
    <@Button label="刷新今日统计" baseRole="base_today_account_statistics_simple_view" iconCls="icon-refresh">
        page.querySimpleTodayAccountStatistics('${number}');
    </@Button>
    </div>
    <div id="simple-account-statistics-today-${number}" class="account-statistics-view"/>
    <div id="expense-list-${number}"/>
</div>
<script type="text/javascript">
    athene.grid.create({
        id:"expense-list-${number}",
        binding:"${number}",
        pagination:true,
        url:"expenseQuery",
        pageSize:10,
        defaultMap:{
            ordering:['e.expend_date','e.id']
        },
        onRender:function() {
            var win = athene.window.get('${number}');
            win && win.onSetSize && win.onSetSize.call(win, win.width, win.height);
        },
        colModels:[
        <#if hasBaseRole("base_expense_update")>
            {
                width:27,
                resizeable:false,
                formatter:function(value, row) {
                    var rtnString = "<span class='cp btn-icon icon-edit' title='编辑' onclick=\"page.expenseEdit({id:"
                            + row.id + ",title:'" + row.title + "',openerNumber:'${number}'},this);\"/>";
                    return rtnString;
                }
            },
        </#if>
            {
                title:"标题",
                width:150,
                key:"title"
            },
            {
                title:"金额",
                key:"amountDisplay",
                formatter:function(value, row) {
                    return "<div style='text-align:right;color:red;'>" + value + "</div>";
                },
                width:100
            },
            {
                title:"支出日期",
                key:"expendDateDisplay",
                width:100
            },
            {
                title:"类目",
                key:"categoryDisplay",
                width:100
            },
            {
                title:"描述",
                key:"description",
                width:250
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
    $I("expense-condition-${number}").doFocus();
</script>
