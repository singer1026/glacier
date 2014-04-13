<div style="width:100%;">
    <div id="teacher-condition-${number}" class="search-frame" style="width:100%;">
    <@Hidden binding="status" defaultValue="in"/>
        <table style="width:100%;">
            <tr>
            <@TextField layoutCls="sm-item" labelCls="sm-label" label="教师姓名" binding="name"/>
            <@ComboBox layoutCls="sm-item" labelCls="sm-label" label="科目" binding="subjectId" list=subjectList
            listKey="name" listValue="id"/>
            </tr>
        </table>
    </div>
    <div class="search-button" style="background-color: #ebf2fa;">
    <@Button label="查 询" iconCls="icon-search">
        page.gridSearch('teacher-condition-${number}','teacher-list-${number}');
    </@Button>
    <@Button label="清空教师列表" iconCls="icon-clear">
        athene.grid.get('teacher-list-${number}').clearSelectRows();
    </@Button>
    </div>
    <div id="teacher-list-${number}"/>
    <div id="teacher-name-list-${number}" style="border-top: 1px solid #a6c3eb;"/>
    <div class="fm-error ml5 mt3 mb3" id="teacher-name-list-${number}-error">
        <div class="btn-icon icon-error fl"></div>
        <div class="fm-error-content fl"></div>
        <div class="clear"/>
    </div>
    <div class="search-button  mt3" style="background-color: #ebf2fa;">
    <@Button label="确认选择" iconCls="icon-confirm">
        page.lessonTeacherSelect({number:'${number}',openerNumber:'${openerNumber}'})
    </@Button>
    <@Button label="关 闭"  iconCls="icon-close">
        athene.window.get('${number}').close();
    </@Button>
    </div>
</div>
<script type="text/javascript">
    athene.listfield.create({
        id:"teacher-name-list-${number}",
        binding:"${number}",
        title:"已选择教师列表",
        listKey:"teacherName",
        listValue:"teacherId",
        formatter:function(i, item) {
            var html = [];
            html.push(i < 9 ? "0" + (i + 1) : i + 1);
            html.push("." + item.teacherName);
            return html.join("");
        },
        onRemoveItem:function(item) {
            var transfer = {
                id:item.teacherId,
                name:item.teacherName
            };
            athene.grid.get("teacher-list-${number}").removeSelectRow(transfer);
        }
    });
    athene.grid.create({
        id:"teacher-list-${number}",
        pagination:true,
        url:"teacherQuery",
        binding:"${number}",
        pageSize:10,
        valueField:"id",
        selectModel:"multiSelect",
        defaultMap:{
            ordering:['CONVERT(u.name USING GB2312)']
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
                    var items = value,len = items.length,html = [];
                    for (var i = 0; i < len; i++) {
                        var item = items[i];
                        html.push(item.name || "");
                        (i + 1) < len && html.push("&nbsp;");
                    }
                    return html.join("");
                }
            }
        ],
        onQuery:function(d) {
            $I("teacher-name-list-${number}-error").hide();
        },
        onInit:function() {
            var items = athene.listfield.get("teacher-name-list-${openerNumber}").getItems();
            var itemsTransfer = [],len = items.length;
            for (var i = 0; i < len; i++) {
                var item = items[i];
                var transfer = {
                    id:item.teacherId,
                    name:item.teacherName
                };
                itemsTransfer.push(transfer);
            }
            this.setSelectRows(itemsTransfer);
            athene.listfield.get("teacher-name-list-${number}").setItems(items);
        },
        onAddSelectRow:function(item) {
            var transfer = {
                teacherId:item.id,
                teacherName:item.name
            };
            athene.listfield.get("teacher-name-list-${number}").addItem(transfer);
        },
        onRemoveSelectRow:function(item) {
            var transfer = {
                teacherId:item.id,
                teacherName:item.name
            };
            athene.listfield.get("teacher-name-list-${number}").removeItem(transfer);
        },
        onClearSelectRows:function() {
            athene.listfield.get("teacher-name-list-${number}").clearItems();
        }
    });
    $I("teacher-condition-${number}").doFocus();
</script>
