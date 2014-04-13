<div class="win-inner">
    <div class="search-frame">
        <div id="account-statistics-condition-${number}" style="width:550px;">
        <@DateField layoutCls="sm-item fl" labelCls="sm-label" label="开始时间" binding="timeStart"
        onFocus="WdatePicker();" readOnly="true" td="false"/>
        <@DateField layoutCls="sm-item fl" labelCls="sm-label" label="结束时间" binding="timeEnd"
        onFocus="WdatePicker();" readOnly="true" td="false"/>
            <div class="clear"/>
        </div>
    </div>
    <div class="search-button">
    <@Button label="查 询" baseRole="base_account_statistics_list" iconCls="icon-search">
        page.gridSearch('account-statistics-condition-${number}','account-statistics-list-${number}');
    </@Button>
    <@Button label="刷新今日统计" baseRole="base_today_account_statistics_view" iconCls="icon-refresh">
        page.queryTodayAccountStatistics('${number}');
    </@Button>
    <@Button label="导出Excel" baseRole="base_account_statistics_list_excel" iconCls="icon-excel">
        page.accountStatisticsListExcel('account-statistics-condition-${number}');
    </@Button>
    </div>
    <div id="account-statistics-today-${number}" class="account-statistics-view"/>
    <div id="account-statistics-list-${number}"/>
</div>
<script type="text/javascript">
    athene.grid.create({
        id:"account-statistics-list-${number}",
        binding:"${number}",
        hasRowNumber:true,
        pagination:true,
        url:"accountStatisticsQuery",
        pageSize:10,
        defaultMap:{
            ordering:['ass.statistics_date']
        },
        onRender:function() {
            var win = athene.window.get('${number}');
            win && win.onSetSize && win.onSetSize.call(win, win.width, win.height);
        },
        colModels:[
            {
                title:"统计日期",
                width:100,
                key:"statisticsDateDisplay"
            },
            {
                title:"收入总和",
                width:100,
                formatter:function(value, row) {
                    return "<div style='text-align:right;'>" + value + "</div>";
                },
                key:"incomeTotalDisplay"
            },
            {
                title:"支出总和",
                key:"expenseTotalDisplay",
                formatter:function(value, row) {
                    return "<div style='text-align:right;'>" + value + "</div>";
                },
                width:100
            },
            {
                title:"日盈利",
                key:"dayProfitDisplay",
                formatter:function(value) {
                    var s;
                    if (value.startsWith("-")) {
                        s = "<div style='text-align:right;color:green;'>" + value + "</div>";
                    } else if (value === "0.0") {
                        s = "<div style='text-align:right;'>" + value + "</div>";
                    } else {
                        s = "<div style='text-align:right;color:red;'>" + value + "</div>";
                    }
                    return s;
                },
                width:100
            },
            {
                title:"余额",
                style:"text-align:right;",
                key:"balanceDisplay",
                formatter:function(value) {
                    var s;
                    if (value.startsWith("-")) {
                        s = "<div style='text-align:right;color:green;'>" + value + "</div>";
                    } else if (value === "0.0") {
                        s = "<div style='text-align:right;'>" + value + "</div>";
                    } else {
                        s = "<div style='text-align:right;color:red;'>" + value + "</div>";
                    }
                    return s;
                },
                width:100
            }
        ]
    });
    $I("account-statistics-condition-${number}").doFocus();
</script>
