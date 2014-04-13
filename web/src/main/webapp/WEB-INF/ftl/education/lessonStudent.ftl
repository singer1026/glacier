<div style="width:100%;">
    <div id="student-condition-${number}" class="search-frame" style="width:100%;">
    <@Hidden binding="status" defaultValue="in"/>
        <table style="width:100%;">
            <tr>
            <@TextField layoutCls="sm-item" labelCls="sm-label" label="学生姓名" binding="name"/>
            <@ComboBox layoutCls="sm-item" labelCls="sm-label" label="科目" binding="subjectId" list=subjectList
            listKey="name" listValue="id"/>
            </tr>
        </table>
    </div>
    <div class="search-button" style="background-color: #ebf2fa;">
    <@Button label="查 询" iconCls="icon-search">
        page.gridSearch('student-condition-${number}','student-list-${number}');
    </@Button>
    <@Button label="清空学生列表" iconCls="icon-clear">
        athene.grid.get('student-list-${number}').clearSelectRows();
    </@Button>
    </div>
    <div id="student-list-${number}"/>
    <div id="student-name-list-${number}" class="mb3"/>
    <div class="fm-error ml5 mt3 mb3" id="student-name-list-${number}-error">
        <div class="btn-icon icon-error fl"></div>
        <div class="fm-error-content fl"></div>
        <div class="clear"/>
    </div>
    <div class="search-button  mt3" style="background-color: #ebf2fa;">
    <@Button label="确认选择" iconCls="icon-confirm">
        page.lessonStudentSelect({number:'${number}',openerNumber:'${openerNumber}'})
    </@Button>
    <@Button label="关 闭" iconCls="icon-close">
        athene.window.get('${number}').close();
    </@Button>
    </div>
</div>
<script type="text/javascript">
    athene.listfield.create({
        id:"student-name-list-${number}",
        binding:"${number}",
        title:"已选择学生列表",
        listKey:"studentName",
        listValue:"studentId",
        formatter:function(i, item) {
            var html = [];
            html.push(i < 9 ? "0" + (i + 1) : i + 1);
            html.push("." + item.studentName);
            return html.join("");
        },
        onRemoveItem:function(item) {
            var transfer = {
                id:item.studentId,
                name:item.studentName
            };
            athene.grid.get("student-list-${number}").removeSelectRow(transfer);
        }
    });
    athene.grid.create({
        id:"student-list-${number}",
        pagination:true,
        url:"studentQuery",
        binding:"${number}",
        pageSize:10,
        selectModel:"multiSelect",
        valueField:"id",
        onQuery:function(d) {
            $I("student-name-list-${number}-error").hide();
        },
        onInit:function() {
            var items = athene.listfield.get("student-name-list-${openerNumber}").getItems();
            var itemsTransfer = [],len = items.length;
            for (var i = 0; i < len; i++) {
                var item = items[i];
                var transfer = {
                    id:item.studentId,
                    name:item.studentName
                };
                itemsTransfer.push(transfer);
            }
            this.setSelectRows(itemsTransfer);
            athene.listfield.get("student-name-list-${number}").setItems(items);
        },
        onAddSelectRow:function(item) {
            var transfer = {
                studentId:item.id,
                studentName:item.name
            };
            athene.listfield.get("student-name-list-${number}").addItem(transfer);
        },
        onRemoveSelectRow:function(item) {
            var transfer = {
                studentId:item.id,
                studentName:item.name
            };
            athene.listfield.get("student-name-list-${number}").removeItem(transfer);
        },
        onClearSelectRows:function() {
            athene.listfield.get("student-name-list-${number}").clearItems();
        },
        colModels:[
            {
                title:"姓名",
                key:"name",
                width:70
            },
            {
                title:"性别",
                key:"sexDisplay",
                width:50
            },
            {
                title:"科目",
                key:"subjectList",
                formatter:function(value, row) {
                    var items = value,len = items.length,html = [];
                    for (var i = 0; i < len; i++) {
                        var item = items[i];
                        html.push(item.name || "");
                        (i + 1) < len && html.push("&nbsp;");
                    }
                    return html.join("");
                },
                width:140
            },
            {
                title:"学校",
                width:140,
                key:"school"
            }
        ]
    });
    $I("student-condition-${number}").doFocus();
</script>
